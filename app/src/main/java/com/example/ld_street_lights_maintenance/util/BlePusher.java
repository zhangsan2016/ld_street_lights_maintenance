package com.example.ld_street_lights_maintenance.util;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Build;
import android.util.Log;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.example.ld_street_lights_maintenance.crc.CopyOfcheckCRC;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BlePusher {
    // 服务 uuid 和特征 uuid
    private static String serviceUuid = "0000ffa0-0000-1000-8000-00805f9b34fb";
    private static String characteristicUuidA = "0000ffa1-0000-1000-8000-00805f9b34fb";
    private static String characteristicUuidB = "0000ffa2-0000-1000-8000-00805f9b34fb";

    private static byte head = -18;
    private static byte tail = -17;


    public static BlePusher getInstance() {
        return BlePusher.BleManagerHolder.sBleManager;
    }

    private static class BleManagerHolder {
        private static final BlePusher sBleManager = new BlePusher();
    }


    /**
     * 写入指令
     *
     * @param order
     * @param callback
     * @throws Exception
     */
    public static void writeOrder(String order, final BleWriteCallback callback) throws Exception {
        // 去空格
        final byte[] data = HexUtil.hexStringToBytes(order.replace(" ", ""));

        final List<BleDevice> bleDevices = BleManager.getInstance().getAllConnectedDevice();
        if (bleDevices.size() > 0) {

            BluetoothGatt mBluetoothGatt = BleManager.getInstance().getBluetoothGatt(bleDevices.get(0));
            // 获取服务
            BluetoothGattService service = mBluetoothGatt.getService(UUID.fromString(serviceUuid));
            // 获取特征 获取一个描述符 84:C2:E4:03:02:04    0000ffa1-0000-1000-8000-00805f9b34fb
            final BluetoothGattCharacteristic gattCharacteristicA1 = service.getCharacteristic(UUID.fromString(characteristicUuidA));
            BluetoothGattCharacteristic gattCharacteristicA2 = service.getCharacteristic(UUID.fromString(characteristicUuidB));

            BleManager.getInstance().write(
                    bleDevices.get(0),
                    gattCharacteristicA2.getService().getUuid().toString(),
                    gattCharacteristicA2.getUuid().toString(),
                    new byte[]{01, 01},
                    new BleWriteCallback() {
                        @Override
                        public void onWriteSuccess(int current, int total, byte[] justWrite) {

                            BleManager.getInstance().write(
                                    bleDevices.get(0),
                                    gattCharacteristicA1.getService().getUuid().toString(),
                                    gattCharacteristicA1.getUuid().toString(),
                                    data,
                                    callback);

                        }

                        @Override
                        public void onWriteFailure(BleException exception) {
                            try {
                                throw new Exception("蓝牙写入失败，请尝试靠近一点~");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            throw new Exception("请先连接蓝牙设备");
        }
    }


    /**
     * 拼接指令
     * 协议组成：帧头 - 功能码 - 数据长度 - 数据 - crc - 帧尾部
     *
     * @param funCode  功能码
     * @param data     数据
     * @param callback 会掉
     * @throws Exception
     */
    public static void writeSpliceOrder(byte[] funCode, final byte[] data, final BleWriteCallback callback) throws Exception {

        // 协议拼接
        final byte[] spliceData = spliceOder(funCode, data);
        Log.e("xxx", ">>>>>>>>>>>>>>>>>>> 发送的数据 " + Arrays.toString(spliceData));

        final List<BleDevice> bleDevices = BleManager.getInstance().getAllConnectedDevice();
        if (bleDevices.size() > 0) {

            BluetoothGatt mBluetoothGatt = BleManager.getInstance().getBluetoothGatt(bleDevices.get(0));
            // 获取服务
            BluetoothGattService service = mBluetoothGatt.getService(UUID.fromString(serviceUuid));
            // 获取特征 获取一个描述符 84:C2:E4:03:02:04    0000ffa1-0000-1000-8000-00805f9b34fb
            final BluetoothGattCharacteristic gattCharacteristicA1 = service.getCharacteristic(UUID.fromString(characteristicUuidA));
            final BluetoothGattCharacteristic gattCharacteristicA2 = service.getCharacteristic(UUID.fromString(characteristicUuidB));

            // 长度大于20需要分包
            if (spliceData.length < 20) {
                BleManager.getInstance().write(
                        bleDevices.get(0),
                        gattCharacteristicA2.getService().getUuid().toString(),
                        gattCharacteristicA2.getUuid().toString(),
                        new byte[]{01, 01},
                        new BleWriteCallback() {

                            @Override
                            public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                                BleManager.getInstance().write(
                                        bleDevices.get(0),
                                        gattCharacteristicA1.getService().getUuid().toString(),
                                        gattCharacteristicA1.getUuid().toString(),
                                        spliceData,
                                        callback);
                            }

                            @Override
                            public void onWriteFailure(final BleException exception) {
                                try {
                                    throw new Exception(exception.toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
            } else {

                // 分包
                Object[] objdata = BytesUtil.splitAry(spliceData, 20);
                int bytesLeng = objdata.length;

                for (int i = 0; i < bytesLeng; i++) {
                    byte[] bytedata = (byte[]) objdata[i];


                    // 用于同步线程
                    final CountDownLatch latch = new CountDownLatch(1);

                    BleManager.getInstance().write(
                            bleDevices.get(0),
                            gattCharacteristicA2.getService().getUuid().toString(),
                            gattCharacteristicA2.getUuid().toString(),
                            new byte[]{(byte) bytesLeng, (byte) (i + 1)},
                            new BleWriteCallback() {

                                @Override
                                public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                                    Log.e("xx", ">>>>>>>>>>>>>>> 最后一个" + current + "  " +  total + "  " + Arrays.toString(justWrite));

                                    try {
                                        Thread.sleep(200);
                                        BleManager.getInstance().write(
                                                bleDevices.get(0),
                                                gattCharacteristicA1.getService().getUuid().toString(),
                                                gattCharacteristicA1.getUuid().toString(),
                                                spliceData,
                                                callback);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onWriteFailure(final BleException exception) {
                                    try {
                                        throw new Exception(exception.toString());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                }

            }

        } else {
            throw new Exception("请先连接蓝牙设备");
        }
    }

    /**
     *  分包发送
     * @param mTotalNum 总包数
     * @param mCount  当前包数
     * @param mData   数据
     * @param callback  回调
     */
    private void splitWrite(int mTotalNum,int mCount,byte[] mData, BleWriteCallback callback) {


    }


    private static byte[] spliceOder(byte[] funCode, byte[] data) {

        //  协议组成：帧头 - 功能码 - 数据长度 - 数据 - crc - 帧尾部
        byte[] cBytes = BytesUtil.concat(new byte[]{head}, funCode);
        // 判断数据是否为空,为空不携带数据
        if (data != null) {
            // 帧头 - 功能码 - 数据长度
            byte[] leng = BytesUtil.intBytesHL(data.length, 2);
            cBytes = BytesUtil.concat(cBytes, leng);
            // 帧头 - 功能码 - 数据长度 - 数据
            cBytes = BytesUtil.concat(cBytes, data);
        } else {
            // 帧头 - 功能码 - 数据长度
            byte[] leng = new byte[2];
            cBytes = BytesUtil.concat(cBytes, leng);
        }
        // 帧头 - 功能码 - 数据长度 - 数据 - crc
        cBytes = CopyOfcheckCRC.crc(cBytes);
        // 帧头 - 功能码 - 数据长度 - 数据 - crc - 帧尾部
        cBytes = BytesUtil.concat(cBytes, new byte[]{tail});

        return cBytes;

    }


    public static void readSpliceOrder(final BleReadCallback callback) {

        final List<BleDevice> bleDevices = BleManager.getInstance().getAllConnectedDevice();

        if (bleDevices.size() > 0) {

            // 获取服务
            BluetoothGatt mBluetoothGatt = BleManager.getInstance().getBluetoothGatt(bleDevices.get(0));
            BluetoothGattService service = mBluetoothGatt.getService(UUID.fromString(serviceUuid));
            final BluetoothGattCharacteristic gattCharacteristicA1 = service.getCharacteristic(UUID.fromString(characteristicUuidA));
            BluetoothGattCharacteristic gattCharacteristicA2 = service.getCharacteristic(UUID.fromString(characteristicUuidB));



            /*BleManager.getInstance().read(
                    bleDevices.get(0),
                    gattCharacteristicA1.getService().getUuid().toString(),
                    gattCharacteristicA1.getUuid().toString(),
                    callback);*/

            // 先获数据长度服务，判断读取次数
            BleManager.getInstance().read(
                    bleDevices.get(0),
                    gattCharacteristicA2.getService().getUuid().toString(),
                    gattCharacteristicA2.getUuid().toString(),
                    new BleReadCallback() {
                        @Override
                        public void onReadSuccess(byte[] data) {

                            Log.e("xxx", ">>>>>>>>>>>>>>>>>>> read data = " + Arrays.toString(data));

                            if (data[1] == 1) {
                                BleManager.getInstance().read(
                                        bleDevices.get(0),
                                        gattCharacteristicA1.getService().getUuid().toString(),
                                        gattCharacteristicA1.getUuid().toString(),
                                        callback);
                            } else {
                                // 分包获取
                            }

                        }

                        @Override
                        public void onReadFailure(BleException exception) {
                            try {
                                throw new Exception(exception.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });


        }


    }


}
