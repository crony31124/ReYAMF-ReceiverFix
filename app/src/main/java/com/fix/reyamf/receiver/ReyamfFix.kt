package com.fix.reyamf.receiver

import android.content.Context
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
                            val receiver =
                                XposedHelpers.getObjectField(
                                    param.thisObject,
                                    "broadcastReceiver"
                                ) as? BroadcastReceiver ?: return

                            val ctx = param.thisObject as? Context ?: return

                            ctx.unregisterReceiver(receiver)

                        } catch (_: IllegalArgumentException) {
                            // Receiver already unregistered → safe
                        }
                    }
                }
            )
        } catch (_: Throwable) {
            // Class/field not found or reYAMF updated → prevent bootloop
        }
    }
}
