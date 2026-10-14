# 青小农 ProGuard 规则
# Room / Compose 基本不需要额外规则，保留实体类以防万一
-keep class com.tiever.qxn.data.model.** { *; }
-dontwarn org.jetbrains.annotations.**
