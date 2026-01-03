# ReYAMF Receiver Fix (LSPosed)

Fixes a crash in **reYAMF** on Android 14+ caused by calling
`unregisterReceiver()` on an already unregistered `BroadcastReceiver`.

## Problem

reYAMF crashes apps like:
- Tasker
- PopupWidget
- Other short-lived activity apps

Error:

## Cause

`AppWindow.onDestroy()` in reYAMF unregisters a BroadcastReceiver
without checking registration state.  
Android 14+ enforces this strictly and crashes the caller.

## Solution

This LSPosed module hooks:
'com.mja.reyamf.xposed.ui.window.AppWindow .onDestroy()' 
and safely guards `unregisterReceiver()` to prevent crashes.

## Requirements

- Android 13+
- LSPosed (tested on LSPosed IT)
- reYAMF

## Installation

1. Build APK from source
2. Install APK
3. Enable module in LSPosed
4. Scope it to `com.mja.reyamf`
5. Reboot

## Notes

- No APK patching
- Survives reYAMF updates
- No performance impact
