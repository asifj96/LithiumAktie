buildscript {

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
        jcenter()

    }
    dependencies {
        classpath(Classpath.gradle)
        classpath(Classpath.gradlePlugin)
        classpath(Classpath.googleServices)
        classpath(Classpath.firebaseCrashlytics)
        classpath(Classpath.navigationGradlePlugin)
        classpath(Classpath.firebasePerformance)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        classpath ("gradle.plugin.com.onesignal:onesignal-gradle-plugin:[0.14.0, 0.99.99]")
    }
}
allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        jcenter()

    }
}
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
