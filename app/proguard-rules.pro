# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile

# --- Room / DB Rules ---
# Room uses code generation so usually it's fine, but let's be safe.
-keep class com.zincstate.manifest.core.database.entity.** { *; }

# --- Coroutines ---
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# --- WorkManager ---
-keep class androidx.work.Worker { *; }
-keep class androidx.work.CoroutineWorker { *; }

# --- Compose ---
# Keep all Compose functions to avoid crashes (though R8 usually handles this)
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

# Keep our UI models intact if we use reflection (not used right now, but safe)
-keep class com.zincstate.manifest.feature.**.**UiModel { *; }
-keep class com.zincstate.manifest.feature.**.**UiItem { *; }
-keep class com.zincstate.manifest.feature.**.**UiState { *; }

# --- Hilt ---
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }