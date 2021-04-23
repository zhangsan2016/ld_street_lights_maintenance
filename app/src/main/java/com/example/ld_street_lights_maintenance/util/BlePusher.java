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
import com.clj.fastble.exception.OtherException;
import com.clj.fastble.utils.HexUtil;
import com.example.ld_street_lights_maintenance.crc.CopyOfcheckCRC;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BlePusher {
    // 服务 uuid 和特征 uuid
    private static String serviceUuid = "0000ffa0-0000-1000-8000-00805f9b34fb";
    private static String characteristicUuidA = "0000ffa1-0000-1000-8000-00805f9b34fb";
    private static String characteristicUuidB = "0000ffa2-0000-1000-8000-00805f9b34fb";

    private static byte head = -18;
    private static byte tail = -17;

    // 分包读取到的数据
    private static byte[] mergeData = new byte[0];


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
                    splitWrite(bytesLeng, i + 1, bytedata, callback, gattCharacteristicA1, gattCharacteristicA2, bleDevices.get(0), latch);

                    //阻塞当前线程直到latch中数值为零才执行
                    latch.await();

                }

            }

        } else {
            throw new Exception("请先连接蓝牙设备");
        }
    }

    /**
     * 分包发送
     *
     * @param mTotalNum 总包数
     * @param mCount    当前包数
     * @param mData     数据
     * @param callback  回调
     * @param latch
     */
    private static void splitWrite(final int mTotalNum, final int mCount, final byte[] mData, final BleWriteCallback callback, final BluetoothGattCharacteristic a1, BluetoothGattCharacteristic a2, final BleDevice bleDevice, final CountDownLatch latch) {
        BleManager.getInstance().write(
                bleDevice,
                a2.getService().getUuid().toString(),
                a2.getUuid().toString(),
                new byte[]{(byte) mTotalNum, (byte) mCount},
                new BleWriteCallback() {

                    @Override
                    public void onWriteSuccess(final int current, final int total, final byte[] justWrite) {
                        Log.e("xx", ">>>>>>>>>>>>>>> 分包发送" + Arrays.toString(justWrite));
                        BleManager.getInstance().write(
                                bleDevice,
                                a1.getService().getUuid().toString(),
                                a1.getUuid().toString(),
                                mData,
                                new BleWriteCallback() {
                                    @Override
                                    public void onWriteSuccess(int current, int total, byte[] justWrite) {
                                        if (mTotalNum == mCount) {
                                            callback.onWriteSuccess(current, total, justWrite);
                                        }
                                        //让latch中的数值减一
                                        latch.countDown();
                                    }

                                    @Override
                                    public void onWriteFailure(BleException exception) {
                                        if (callback != null) {
                                            callback.onWriteFailure(new OtherException("写入异常 " + exception.getDescription()));
                                        }
                                    }
                                });

                    }

                    @Override
                    public void onWriteFailure(final BleException exception) {
                        try {
                            if (callback != null) {
                                callback.onWriteFailure(new OtherException("写入异常 " + exception.getDescription()));
                            }
                            throw new Exception(exception.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

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
            final BluetoothGattCharacteristic gattCharacteristicA2 = service.getCharacteristic(UUID.fromString(characteristicUuidB));



            /*BleManager.getInstance().read(
                    bleDevices.get(0),
                    gattCharacteristicA1.getService().getUuid().toString(),
                    gattCharacteristicA1.getUuid().toString(),
                    callback);*/

            // 先获数据长度服务，判断读取次数
            String uuid_service = gattCharacteristicA2.getService().getUuid().toString();
            String uuid_read = gattCharacteristicA2.getUuid().toString();
            BleManager.getInstance().read(
                    bleDevices.get(0),
                    uuid_service,
                    uuid_read,
                    new BleReadCallback() {
                        @Override
                        public void onReadSuccess(final byte[] data) {

                            Log.e("xxx", ">>>>>>>>>>>>>>>>>>> read data = " + Arrays.toString(data));

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (data[0] == 1) {
                                        BleManager.getInstance().read(
                                                bleDevices.get(0),
                                                gattCharacteristicA1.getService().getUuid().toString(),
                                                gattCharacteristicA1.getUuid().toString(),
                                                callback);
                                    } else {
                                        // 分包读取
                                        mergeData = new byte[0];
                                        int dataSize = data[0];
                                        for (int i = 0; i < dataSize; i++) {
                                            // 用于同步线程
                                            CountDownLatch latch = new CountDownLatch(1);
                                            splitRead(data[0], (i + 1), callback, gattCharacteristicA1, gattCharacteristicA2, bleDevices.get(0), latch);
                                            try {
                                                //阻塞当前线程直到latch中数值为零才执行
                                                latch.await(5, TimeUnit.SECONDS);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                                break;
                                            }
                                        }
                                    }
                                }
                            }).start();

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


    /**
     * 分包读取
     *
     * @param mTotalNum 总包数
     * @param mCount    当前包数
     * @param callback  回调
     * @param a1
     * @param a2
     * @param bleDevice
     * @param latch
     */
    private static void splitRead(final byte mTotalNum, final int mCount, final BleReadCallback callback, final BluetoothGattCharacteristic a1, final BluetoothGattCharacteristic a2, final BleDevice bleDevice, final CountDownLatch latch) {


        BleManager.getInstance().read(
                bleDevice,
                a1.getService().getUuid().toString(),
                a1.getUuid().toString(),
                new BleReadCallback() {
                    @Override
                    public void onReadSuccess(byte[] data) {
                        mergeData = BytesUtil.byteMergerAll(mergeData, data);

                        Log.e("xxx", ">>>>>>>>>>>>>>>>>>> splitRead read data = " + Arrays.toString(data));

                        if (mTotalNum == mCount) {
                            if (callback != null) {
                                callback.onReadSuccess(mergeData);
                            }
                        }
                        //让latch中的数值减一
                        latch.countDown();
                    }

                    @Override
                    public void onReadFailure(BleException exception) {
                        if (callback != null) {
                            callback.onReadFailure(new OtherException("读取异常 " + exception.getDescription()));
                        }
                    }
                });

      /*  BleManager.getInstance().write(
                bleDevice,
                a2.getService().getUuid().toString(),
                a2.getUuid().toString(),
                new byte[]{mTotalNum, (byte) mCount},
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(int current, int total, byte[] justWrite) {
                        BleManager.getInstance().read(
                                bleDevice,
                                a1.getService().getUuid().toString(),
                                a1.getUuid().toString(),
                                new BleReadCallback() {
                                    @Override
                                    public void onReadSuccess(byte[] data) {
                                        mergeData = BytesUtil.byteMergerAll(mergeData, data);
                                        if (mTotalNum == mCount) {
                                            if (callback != null) {
                                                callback.onReadSuccess(mergeData);
                                            }
                                        }
                                        //让latch中的数值减一
                                        latch.countDown();
                                    }

                                    @Override
                                    public void onReadFailure(BleException exception) {
                                        if (callback != null) {
                                            callback.onReadFailure(new OtherException("读取异常 " + exception.getDescription()));
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onWriteFailure(BleException exception) {
                        if (callback != null) {
                            callback.onReadFailure(new OtherException("写入异常 " + exception.getDescription()));
                        }
                    }
                });*/


    }

}

