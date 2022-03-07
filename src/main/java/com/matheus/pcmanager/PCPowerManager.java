package com.matheus.pcmanager;

import java.io.IOException;

import com.homeauto.devices.DevicesManager;

import org.apache.commons.lang3.SystemUtils;

import lombok.Getter;

public final class PCPowerManager {

    @Getter
    private static DevicesManager devicesManager;

    public static void main(String[] args) {
        devicesManager = new DevicesManager();
        System.out.println("[INFO] Starting power manager watcher");
    }

    public static boolean shutdown(int time) throws IOException {
        String shutdownCommand = null, t = time == 0 ? "now" : String.valueOf(time);

        if (SystemUtils.IS_OS_AIX) shutdownCommand = "shutdown -Fh " + t;
        else if (SystemUtils.IS_OS_FREE_BSD || SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_NET_BSD || SystemUtils.IS_OS_OPEN_BSD || SystemUtils.IS_OS_UNIX)
            shutdownCommand = "shutdown -h " + t;
        else if (SystemUtils.IS_OS_HP_UX) shutdownCommand = "shutdown -hy " + t;
        else if (SystemUtils.IS_OS_IRIX) shutdownCommand = "shutdown -y -g " + t;
        else if (SystemUtils.IS_OS_SOLARIS || SystemUtils.IS_OS_SUN_OS) shutdownCommand = "shutdown -y -i5 -g" + t;
        else if (SystemUtils.IS_OS_WINDOWS) shutdownCommand = "shutdown.exe /s /t " + t;
        else return false;

        Runtime.getRuntime().exec(shutdownCommand);
        return true;
    }

}
