# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in [sdk]/tools/proguard/proguard-android.txt

# Keep AdMob classes
-keep class com.google.android.gms.ads.** { *; }
-dontwarn com.google.android.gms.ads.**

# Keep LibGDX classes
-keep class com.badlogic.gdx.** { *; }
-dontwarn com.badlogic.gdx.**

# Keep game classes
-keep class com.game.jumper.** { *; }