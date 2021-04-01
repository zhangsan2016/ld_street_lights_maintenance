package com.example.ld_street_lights_maintenance.util;

import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;

import java.util.List;

public class Pusher {
    private static byte head = -16;


    public static void pushMessage() throws Exception {

        // disconnectAllDevice 关闭所有链接设备
        List<BleDevice> bleDevices = BleManager.getInstance().getAllConnectedDevice();
        if (bleDevices.size() > 0) {
            // 发送信心
            BleDevice bleDevice = bleDevices.get(0);

        } else {
            throw new Exception("请先连接蓝牙设备");
        }


    }


    /**
     *  凭借头尾
     * @throws Exception
     */
    public static void pushSpliceMessage() throws Exception {

        // disconnectAllDevice 关闭所有链接设备
        List<BleDevice> bleDevices = BleManager.getInstance().getAllConnectedDevice();
        if (bleDevices.size() > 0) {
            // 发送信心
            BleDevice bleDevice = bleDevices.get(0);

        } else {
            throw new Exception("请先连接蓝牙设备");
        }


    }




}
