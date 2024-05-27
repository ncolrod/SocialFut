plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "ncolrod.socialfutv3"
    compileSdk = 34

    defaultConfig {
        applicationId = "ncolrod.socialfutv3"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //voy a utlizar las implementaciones del navigation drawer
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.navigation:navigation-fragment:2.5.3")
    implementation ("androidx.navigation:navigation-ui:2.5.3")
    //implementamos retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    //implementamos lombok
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    compileOnly("org.projectlombok:lombok:1.18.32")
    // implementamos jackson
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.11.0")
    implementation("com.google.android.material:material:1.4.0")

}