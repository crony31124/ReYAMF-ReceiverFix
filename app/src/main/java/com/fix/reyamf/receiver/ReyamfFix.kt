package com.fix.reyamf.receiver

import android.content.BroadcastReceiver
import android.content.ContextWrapper
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class ReyamfFix : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "com.mja.reyamf") return

        try {
            XposedHelpers.findAndHookMethod(
                ContextWrapper::class.java,
                "unregisterReceiver",
                BroadcastReceiver::class.java,
                object : XC_MethodHook() {

                    override fun beforeHookedMethod(
                        param: XC_MethodHook.MethodHookParam
                    ) {
                        try {
                            // Let Android handle normally
                        } catch (_: IllegalArgumentException) {
                            // Receiver already unregistered → swallow crash
                            param.result = null
                        }
                    }
                }
            )
        } catch (_: Throwable) {
            // Safety net: prevent bootloop if API changes
        }
    }
}
