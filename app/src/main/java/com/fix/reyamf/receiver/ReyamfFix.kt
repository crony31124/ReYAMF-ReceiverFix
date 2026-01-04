package com.fix.reyamf.receiver

import android.content.BroadcastReceiver
import android.content.ContextWrapper
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class ReyamfFix : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        // Only hook reYAMF
        if (lpparam.packageName != "com.mja.reyamf") return

        try {
            XposedHelpers.findAndHookMethod(
                ContextWrapper::class.java,
                "unregisterReceiver",
                BroadcastReceiver::class.java,
                object : XC_MethodHook() {

                    override fun beforeHookedMethod(param: MethodHookParam) {
                        try {
                            // Check call stack to ensure this comes from AppWindow
                            val calledFromAppWindow = Throwable().stackTrace.any {
                                it.className == "com.mja.reyamf.xposed.ui.window.AppWindow"
                            }

                            if (calledFromAppWindow) {
                                // Swallow double-unregister crash from reYAMF
                                param.result = null
                            }
                        } catch (_: Throwable) {
                            // Fail-safe: never crash system
                        }
                    }
                }
            )
        } catch (_: Throwable) {
            // Absolute safety net: prevent bootloop if API changes
        }
    }
}
