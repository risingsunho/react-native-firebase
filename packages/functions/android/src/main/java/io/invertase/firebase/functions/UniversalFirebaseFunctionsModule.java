package io.invertase.firebase.functions;

import android.content.Context;
import com.facebook.react.bridge.ReadableMap;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableReference;
import com.google.android.gms.tasks.Continuation;
import com.google.firebase.functions.HttpsCallableResult;
import androidx.annotation.NonNull;

import io.invertase.firebase.common.UniversalFirebaseModule;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("WeakerAccess")
public class UniversalFirebaseFunctionsModule extends UniversalFirebaseModule {
public static final String DATA_KEY = "data";
public static final String CODE_KEY = "code";
public static final String MSG_KEY = "message";
public static final String DETAILS_KEY = "details";

UniversalFirebaseFunctionsModule(Context context, String serviceName) {
super(context, serviceName);
}

Task<Object> httpsCallable(
String appName,
String region,
String origin,
String name,
Object data,
ReadableMap options
) {
FirebaseApp firebaseApp = FirebaseApp.getInstance(appName);
FirebaseFunctions functionsInstance = FirebaseFunctions.getInstance(firebaseApp, region);

      HttpsCallableReference httpReference = functionsInstance.getHttpsCallable(name);

      if (options.hasKey("timeout")) {
        httpReference.setTimeout((long) options.getInt("timeout"), TimeUnit.SECONDS);
      }

      if (origin != null) {
        functionsInstance.useFunctionsEmulator(origin);
      }

    Task<Object> task = httpReference.call(data).continueWith(new Continuation<HttpsCallableResult, Object>() {
        @Override
        public Object then(@NonNull Task<HttpsCallableResult> task) throws Exception {
          Object result = task.getResult().getData();
          return result;
        }
    });
    return task;

}

}
