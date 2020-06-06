const val kotlin = "1.2.71"
const val ROOM_VERSION = "2.2.5"
const val NAVIGATION_VERSION = "2.2.1"
const val KOIN_VERSION = "2.1.5"

object Kotlin {
    val kotlinStandardLibrary = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${kotlin}"
}

object Androidx {
    val core = "androidx.core:core-ktx:1.2.0"
    val appcompat = "androidx.appcompat:appcompat:1.1.0"
    val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.0-beta3"

    val navigation = "androidx.navigation:navigation-ui:$NAVIGATION_VERSION"
    val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:2.0.0"
    val navigationFragment = "androidx.navigation:navigation-fragment-ktx:$NAVIGATION_VERSION"
    val navigationUi = "androidx.navigation:navigation-ui-ktx:$NAVIGATION_VERSION"
    val legacySupport = "androidx.legacy:legacy-support-v4:1.0.0"
    val lifecycleViewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.0.0"
    val viewPager = "androidx.viewpager2:viewpager2:1.0.0"

    val room = "androidx.room:room-runtime:$ROOM_VERSION"
    val roomAnnotations = "androidx.room:room-compiler:$ROOM_VERSION"
    val roomKotlin = "androidx.room:room-ktx:$ROOM_VERSION"
    val sqlCipher = "net.zetetic:android-database-sqlcipher:4.3.0"

    val crypto = "androidx.security:security-crypto:1.0.0-alpha02"
}

object ThirdPartyLibs {
    val viewPagerDots = "com.romandanylyk:pageindicatorview:1.0.3"
    val permissions = "pub.devrel:easypermissions:3.0.0"
    val timber = "com.jakewharton.timber:timber:4.7.1"
    val timberTrees = "fr.bipi.treessence:treessence:0.3.2"
}

object Google {
    val analytics = "com.google.firebase:firebase-analytics:17.2.2"
    val crashlythics = "com.google.firebase:firebase-crashlytics:17.0.1"
}

object NetworkLibs {
    val gson = "com.google.code.gson:gson:2.8.5"
}

object Material {
    val main = "com.google.android.material:material:1.2.0-alpha06"
}