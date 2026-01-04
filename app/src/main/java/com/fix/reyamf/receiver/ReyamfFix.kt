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

    XposedHelpers.findAndHookMethod(
        Context::class.java,
        "unregisterReceiver",
        BroadcastReceiver::class.java,
        object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                try {
                    // let Android do its thing
                } catch (_: IllegalArgumentException) {
                    // swallow crash
                    param.result = null
                }
            }
        }
    )
}
