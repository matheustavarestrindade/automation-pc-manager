package com.matheus.pcmanager;

import java.io.IOException;

import com.homeauto.devices.implementation.DeviceImplementation;
import com.homeauto.devices.implementation.SolarMonitorV1;
import com.homeauto.devices.utilities.DeviceType;

public class PowerOffWatcher implements Runnable {

    private int UNSUCESSFULL_TRYES = 0;

    @Override
    public void run() {
        while (true) {
            boolean isDeviceFound = false;
            boolean isDataUpdated = false;
            for (DeviceImplementation device : PCPowerManager.getDevicesManager().getRegisteredDevices()) {
                if (device.getType() != DeviceType.SOLAR_MONITOR_V1) {
                    continue;
                }
                isDeviceFound = true;
                device.refreshDeviceData();
                if (!device.isDataLoaded()) {
                    continue;
                }

                SolarMonitorV1 sm = (SolarMonitorV1) device;
                float baterySOC = sm.getSolarMonitorData().getBatteryStateOfCharge();
                isDataUpdated = true;

                if (baterySOC > 30) {
                    continue;
                }
                try {
                    System.out.println("SHUTTING DOWN SYSTEM AS BATERRY CHARGE IS BELLOW 30%");
                    PCPowerManager.shutdown(10);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

            if (isDeviceFound && !isDataUpdated) {
                UNSUCESSFULL_TRYES += 1;
            }

            if (UNSUCESSFULL_TRYES > 12) { // one hour trying to comunnicate with the device. Shut down
                try {
                    System.out.println("SHUTTING DOWN SYSTEM AS COMUNNICATION IS UNREACHEABLE");
                    PCPowerManager.shutdown(10);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                Thread.sleep(1000 * 60 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
