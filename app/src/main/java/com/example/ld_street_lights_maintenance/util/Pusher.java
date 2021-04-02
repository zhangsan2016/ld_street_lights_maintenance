package com.example.ld_street_lights_maintenance.util;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;

import java.util.List;
import java.util.UUID;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class Pusher {
    // 服务 uuid 和特征 uuid
    private static String serviceUuid = "0000ffa0-0000-1000-8000-00805f9b34fb";
    private static String characteristicUuidA = "0000ffa1-0000-1000-8000-00805f9b34fb";
    private static String characteristicUuidB = "0000ffa2-0000-1000-8000-00805f9b34fb";

    private static byte head = -18;
    private static byte tail = -17;


    public static Pusher getInstance() {
        return Pusher.BleManagerHolder.sBleManager;
    }

    private static class BleManagerHolder {
        private static final Pusher sBleManager = new Pusher();
    }


    public static void writeOrder(String order, BleWriteCallback callback) throws Exception {
        // 去空格
        byte[] data = HexUtil.hexStringToBytes(order.replace(" ", ""));

        List<BleDevice> bleDevices = BleManager.getInstance().getAllConnectedDevice();
        if (bleDevices.size() > 0) {


            BluetoothGatt mBluetoothGatt = BleManager.getInstance().getBluetoothGatt(bleDevices.get(0));
            // 获取服务
            BluetoothGattService service = mBluetoothGatt.getService(UUID.fromString(serviceUuid));
            // 获取特征 获取一个描述符 84:C2:E4:03:02:04    0000ffa1-0000-1000-8000-00805f9b34fb
            BluetoothGattCharacteristic gattCharacteristicA1 = service.getCharacteristic(UUID.fromString(characteristicUuidA));
            BluetoothGattCharacteristic gattCharacteristicA2 = service.getCharacteristic(UUID.fromString(characteristicUuidB));

            BleManager.getInstance().write(
                    bleDevices.get(0),
                    gattCharacteristicA2.getService().getUuid().toString(),
                    gattCharacteristicA2.getUuid().toString(),
                    data,
                    callback);
        } else {
            throw new Exception("请先连接蓝牙设备");
        }
    }


    /**
     * 拼接指令
     *
     * @throws Exception
     */
    public static void writeSpliceOrder(String order, final BleWriteCallback callback) throws Exception {


        final List<BleDevice> bleDevices = BleManager.getInstance().getAllConnectedDevice();
        if (bleDevices.size() > 0) {
            // 去空格
            final byte[] data = HexUtil.hexStringToBytes(order.replace(" ", ""));

            // 长度大于20需要分包
            if (data.length < 20) {
                BluetoothGatt mBluetoothGatt = BleManager.getInstance().getBluetoothGatt(bleDevices.get(0));
                // 获取服务
                BluetoothGattService service = mBluetoothGatt.getService(UUID.fromString(serviceUuid));
                // 获取特征 获取一个描述符 84:C2:E4:03:02:04    0000ffa1-0000-1000-8000-00805f9b34fb
                BluetoothGattCharacteristic gattCharacteristicA1 = service.getCharacteristic(UUID.fromString(characteristicUuidA));
                final BluetoothGattCharacteristic gattCharacteristicA2 = service.getCharacteristic(UUID.fromString(characteristicUuidB));

                BleManager.getInstance().write(
                        bleDevices.get(0),
                        gattCharacteristicA2.getService().getUuid().toString(),
                        gattCharacteristicA2.getUuid().toString(),
                        new byte[]{01,01},
                        new BleWriteCallback() {

                            @Override
                            public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                                BleManager.getInstance().write(
                                        bleDevices.get(0),
                                        gattCharacteristicA2.getService().getUuid().toString(),
                                        gattCharacteristicA2.getUuid().toString(),
                                        data,
                                        callback);
                            }

                            @Override
                            public void onWriteFailure(final BleException exception) {
                                try {
                                    throw  new Exception(exception.toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                ;
                            }
                        });
            }{
                // 分包
            }

        } else {
            throw new Exception("请先连接蓝牙设备");
        }
    }


}
