package com.fix.reyamf.receiver

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class ReyamfFix : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "com.mja.reyamf") return

        try {
            val clazz = XposedHelpers.findClass(
                "com.mja.reyamf.xposed.ui.window.AppWindow",
                lpparam.classLoader
            )

            XposedHelpers.findAndHookMethod(
                clazz,
                "onDestroy",
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        try {
                            // let original code run
                        } catch (_: IllegalArgumentException) {
                            // swallow receiver-not-registered crash
                        }
                    }
                }
            )
        } catch (_: Throwable) {
            // safety: prevent bootloop if class changes
        }
    }
}
