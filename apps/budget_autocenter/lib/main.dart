import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_crashlytics/firebase_crashlytics.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_modular/flutter_modular.dart';
import 'app.dart';
import 'src/app_module.dart';

Future<void> main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await _initFirebase();
  runApp(ModularApp(module: AppModule(), child: const MyApp()));
}

/// Firebase/Crashlytics só no Android: a config vem do google-services.json
/// (plugin do Gradle). Na web não há config, então fica de fora. Uma falha
/// aqui nunca impede o app de abrir.
Future<void> _initFirebase() async {
  if (kIsWeb) return;
  try {
    await Firebase.initializeApp();
    final crashlytics = FirebaseCrashlytics.instance;
    await crashlytics.setCrashlyticsCollectionEnabled(!kDebugMode);
    FlutterError.onError = crashlytics.recordFlutterFatalError;
    PlatformDispatcher.instance.onError = (error, stack) {
      crashlytics.recordError(error, stack, fatal: true);
      return true;
    };
  } catch (e, s) {
    debugPrint('Firebase não inicializado: $e\n$s');
  }
}
