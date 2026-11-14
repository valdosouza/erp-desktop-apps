import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // The Flutter Gradle Plugin must be applied after the Android and Kotlin Gradle plugins.
    id("dev.flutter.flutter-gradle-plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

// Chave de upload do Play: fica no cofre fora do repositório
// (D:/Vault/.segredos, ou a pasta apontada por SETES_SEGREDOS).
// O key.properties de lá traz storeFile com caminho absoluto.
val segredosDir = System.getenv("SETES_SEGREDOS") ?: "D:/Vault/.segredos"
val keystorePropertiesFile = file("$segredosDir/android/budget_autocenter/key.properties")
val keystoreProperties = Properties().apply {
    if (keystorePropertiesFile.exists()) {
        keystorePropertiesFile.inputStream().use { load(it) }
    }
}

android {
    namespace = "com.setes.budget_autocenter"
    compileSdk = 36
    ndkVersion = "28.2.13676358"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    defaultConfig {
        // Definitivo na Play Store: não pode mudar depois do primeiro envio.
        applicationId = "com.setes.budgetautocenterapp"
        // You can update the following values to match your application needs.
        // For more information, see: https://flutter.dev/to/review-gradle-config.
        minSdk = 24
        targetSdk = 36
        // Uses the version code from pubspec.yaml. When using split APKs, 1000 * ABI_VERSION
        // is added automatically by Flutter. (https://developer.android.com/studio/build/configure-apk-splits#configure-APK-versions)
        // You can force using the value of versionCode by specifying the `-P force-version-code-ignoring-abi=true`
        // flag during build.
        versionCode = flutter.versionCode
        versionName = flutter.versionName
    }

    signingConfigs {
        if (keystorePropertiesFile.exists()) {
            create("release") {
                storeFile = file(keystoreProperties.getProperty("storeFile"))
                storePassword = keystoreProperties.getProperty("storePassword")
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        release {
            // Sem o cofre não há config de release e o build falha (abaixo):
            // nunca sai um release assinado com a chave debug.
            signingConfig = signingConfigs.findByName("release")
        }
    }
}

gradle.taskGraph.whenReady {
    val buildsRelease = allTasks.any { it.name.contains("Release") }
    if (buildsRelease && !keystorePropertiesFile.exists()) {
        throw GradleException(
            "Release precisa da chave de upload em ${keystorePropertiesFile.path} " +
                "(cofre .segredos; defina SETES_SEGREDOS se estiver em outra pasta)."
        )
    }
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

flutter {
    source = "../.."
}
