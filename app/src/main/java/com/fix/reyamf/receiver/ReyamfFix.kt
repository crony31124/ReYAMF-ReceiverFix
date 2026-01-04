package com.fix.reyamf.receiver

import android.content.BroadcastReceiver
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
                            val receiver = XposedHelpers.getObjectField(
                                param.thisObject,
                                "broadcastReceiver"
                            )

                            if (receiver is BroadcastReceiver) {
                                XposedHelpers.setObjectField(
                                    param.thisObject,
                                    "broadcastReceiver",
                                    null
                                )
                            }
                        } catch (_: Throwable) {
                            // field missing / renamed → fail safe
                        }
                    }
                }
            )
        } catch (_: Throwable) {
            // class not found → fail safe, no bootloop
        }
    }
}
