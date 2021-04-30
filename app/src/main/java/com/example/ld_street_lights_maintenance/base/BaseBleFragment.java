package com.example.ld_street_lights_maintenance.base;


import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ld_street_lights_maintenance.R;
import com.ldgd.ld_nfc_ndef_module.base.BaseNfcActivity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseBleFragment extends BaseFragment {
    private static String BACKGROUND_LOCATION_PERMISSION = "android.permission.ACCESS_BACKGROUND_LOCATION";
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private boolean isNeedCheck = true;
    private boolean needCheckBackLocation = false;
    protected String[] needPermissions = new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.READ_PHONE_STATE"};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT > 28 && getActivity().getApplicationContext().getApplicationInfo().targetSdkVersion > 28) {
            this.needPermissions = new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.READ_PHONE_STATE", BACKGROUND_LOCATION_PERMISSION};
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.mNfcAdapter = NfcAdapter.getDefaultAdapter(getContext());
        this.mPendingIntent = PendingIntent.getActivity(getContext(), 0, new Intent(getContext(), getActivity().getClass()), 0);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (this.mNfcAdapter != null) {
            this.mNfcAdapter.enableForegroundDispatch(getActivity(), this.mPendingIntent, (IntentFilter[])null, (String[][])null);
        }

        if (Build.VERSION.SDK_INT >= 23 && getActivity().getApplicationInfo().targetSdkVersion >= 23 && this.isNeedCheck) {
            this.checkPermissions(this.needPermissions);
        }
    }

    private void checkPermissions(String... permissions) {
        try {
            if (Build.VERSION.SDK_INT >= 23 && getActivity().getApplicationInfo().targetSdkVersion >= 23) {
                List<String> needRequestPermissonList = this.findDeniedPermissions(permissions);
                if (null != needRequestPermissonList && needRequestPermissonList.size() > 0) {
                    String[] array = (String[])needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]);
                    Method method = this.getClass().getMethod("requestPermissions", String[].class, Integer.TYPE);
                    method.invoke(this, array, 0);
                }
            }
        } catch (Throwable var5) {
        }
    }

    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList();
        if (Build.VERSION.SDK_INT >= 23 && getContext().getApplicationInfo().targetSdkVersion >= 23) {
            try {
                String[] var3 = permissions;
                int var4 = permissions.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    String perm = var3[var5];
                    Method checkSelfMethod = this.getClass().getMethod("checkSelfPermission", String.class);
                    Method shouldShowRequestPermissionRationaleMethod = this.getClass().getMethod("shouldShowRequestPermissionRationale", String.class);
                    if (((Integer)checkSelfMethod.invoke(this, perm) != 0 || (Boolean)shouldShowRequestPermissionRationaleMethod.invoke(this, perm)) && (this.needCheckBackLocation || !BACKGROUND_LOCATION_PERMISSION.equals(perm))) {
                        needRequestPermissonList.add(perm);
                    }
                }
            } catch (Throwable var9) {
            }
        }

        return needRequestPermissonList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] paramArrayOfInt) {
        super.onRequestPermissionsResult(requestCode, permissions, paramArrayOfInt);
        if (requestCode == 0 && !this.verifyPermissions(paramArrayOfInt)) {
            this.showMissingPermissionDialog();
            this.isNeedCheck = false;
        }
    }

    private boolean verifyPermissions(int[] grantResults) {
        int[] var2 = grantResults;
        int var3 = grantResults.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            int result = var2[var4];
            if (result != 0) {
                return false;
            }
        }

        return true;
    }

    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.notifyTitle);
        builder.setMessage(R.string.notifyMsg);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
             //   getActivity().this.finish();
            }
        });
        builder.setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
              //  BaseNfcActivity.this.startAppSettings();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}
