plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "ncolrod.socialfut"
    compileSdk = 34

    defaultConfig {
        applicationId = "ncolrod.socialfut"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //implementamos retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //implementamos lombok
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    compileOnly("org.projectlombok:lombok:1.18.32")
    // implementamos jackson
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    implementation("com.squareup.retrofit2:converter-jackson:2.9.0")

}