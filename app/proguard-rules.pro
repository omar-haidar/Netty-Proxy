# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
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
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# ===== قواعد Netty لإصدار Release =====

# 1. الحفاظ على جميع فئات Netty
-keep class io.netty.** { *; }
-keepnames class io.netty.** { *; }

# 2. تجاهل الفئات الخاصة بأنظمة التشغيل الأخرى (ليست على Android)
-dontwarn io.netty.channel.epoll.**
-dontwarn io.netty.channel.kqueue.**
-dontwarn io.netty.channel.unix.**
-dontwarn io.netty.handler.ssl.**
-dontwarn io.netty.incubator.**
-dontwarn io.netty.resolver.dns.macos.**

# 3. تجاهل أنظمة التسجيل غير المستخدمة
-dontwarn org.apache.log4j.**
-dontwarn org.apache.logging.log4j.**
-dontwarn org.slf4j.**
-dontwarn org.jctools.**
-dontwarn reactor.blockhound.**


# Keep all optional Netty dependencies
-dontwarn com.aayushatharva.brotli4j.**
-dontwarn com.barchart.udt.**
-dontwarn com.fasterxml.aalto.**
-dontwarn com.github.luben.zstd.**
-dontwarn com.google.protobuf.**
-dontwarn com.jcraft.jzlib.**
-dontwarn com.ning.compress.**
-dontwarn com.oracle.svm.**
-dontwarn com.sun.nio.sctp.**
-dontwarn gnu.io.**
-dontwarn javax.naming.**
-dontwarn javax.xml.stream.**
-dontwarn lzma.sdk.**
-dontwarn net.jpountz.lz4.**
-dontwarn net.jpountz.xxhash.**
-dontwarn org.jboss.marshalling.**

# 4. الحفاظ على ميزات الانعكاس
-keepattributes Signature, RuntimeVisibleAnnotations, AnnotationDefault

# 5. الحفاظ على خدمات Netty الداخلية
-keep class META-INF.services.** { *; }
-keep class META-INF.io.netty.** { *; }

# 6. تجنب إزالة فئات معينة تستخدم في الانعكاس
-keep class io.netty.util.internal.** { *; }
-keep class io.netty.handler.codec.** { *; }

