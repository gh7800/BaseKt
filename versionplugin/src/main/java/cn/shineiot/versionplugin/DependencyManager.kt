package cn.shineiot.versionplugin

/**
 * 配置和 build相关的
 */
object BuildVersion {
    const val compileSdkVersion = 31
    const val minSdkVersion = 21
    const val targetSdkVersion = 31
    const val versionCode = 1
    const val versionName = "1.0.2"
}

/**
 * 项目相关配置
 */
object BuildConfig {
    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.20"

    //AndroidX
    const val core_ktx = "androidx.core:core-ktx:1.6.0" //1.7.1 minSDK最小33 有bug
    const val appcompat = "androidx.appcompat:appcompat:1.3.1" //1.6.1 minSDK最小33 有bug
    const val material = "com.google.android.material:material:1.3.0"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    const val recyclerview = "androidx.recyclerview:recyclerview:1.2.0"
    const val cardView = "androidx.cardview:cardview:1.0.0"
    const val multidex = "androidx.multidex:multidex:2.0.1"
    const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"

    const val material_dialogs_core = "com.afollestad.material-dialogs:core:3.3.0"
    const val material_dialogs_input = "com.afollestad.material-dialogs:input:3.3.0"
    const val material_dialogs_lifecycle = "com.afollestad.material-dialogs:lifecycle:3.2.1"
    //const val splashScreen = "androidx.core:core-splashscreen:1.0.0"

    const val lifecycle_viewModel_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1"
    const val lifecycle_common = "androidx.lifecycle:lifecycle-common-java8:2.2.0"
    const val kotlinx_coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.1"

    const val retrofit = "com.squareup.retrofit2:retrofit:2.9.0"
    const val converter_gson = "com.squareup.retrofit2:converter-gson:2.9.0"
    const val okhttp = "com.squareup.okhttp3:okhttp:4.3.1"
    const val logging_interceptor = "com.squareup.okhttp3:logging-interceptor:3.12.1"

    const val eventbus = "org.greenrobot:eventbus:3.2.0"
    const val eventbus_annotation_processor = "org.greenrobot:eventbus-annotation-processor:3.2.0"

    const val glide = "com.github.bumptech.glide:glide:4.9.0"
    const val glide_compiler = "com.github.bumptech.glide:compiler:4.9.0"

    const val MNProgressHUD = "com.github.maning0303:MNProgressHUD:V2.0.0X"//加载中的loading

    const val agentWebCore = "com.github.Justson.AgentWeb:agentweb-core:4.1.9-androidx"//webView
    const val AndroidAutoSize = "com.github.JessYanCoding:AndroidAutoSize:1.2.1"//屏幕适配
    const val permissionX = "com.guolindev.permissionx:permissionx:1.6.1"//权限申请

    const val leakcanary = "com.squareup.leakcanary:leakcanary-android:2.8.1"//内存泄漏
    const val crashReport = "com.tencent.bugly:crashreport:4.1.9"//异常信息收集
    const val photoView = "com.github.chrisbanes:PhotoView:2.0.0"//photoView
    const val androidFilePicker = "me.rosuh:AndroidFilePicker:0.7.0"//文件管理

    const val tbsSdk = "com.tencent.tbs:tbssdk:44165" //腾讯浏览服务
    const val xPopup = "com.github.li-xiaojun:XPopup:2.7.6"//popupWindow
    const val mmkv = "com.tencent:mmkv:1.2.12"//mmkv

    const val room = "androidx.room:room-runtime:2.4.2"//room数据库
    const val room_compiler_kapt =  "androidx.room:room-compiler:2.4.2"
    const val room_ktx =  "androidx.room:room-ktx:2.4.2"
    const val mpAndroidChart = "com.github.PhilJay:MPAndroidChart:v3.1.0"//图表

    //jcenter() 未迁移的
    const val matisse = "com.zhihu.android:matisse:0.5.3-beta3"//相册
    const val zxing_library = "cn.yipianfengye.android:zxing-library:2.2"//扫一扫
}
