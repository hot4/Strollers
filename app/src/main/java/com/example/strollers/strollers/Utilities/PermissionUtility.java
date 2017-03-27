package com.example.strollers.strollers.Utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class PermissionUtility {

    public static final String LOCATIONPERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;

    private static boolean useRunTimePermissions() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    private static boolean hasPermission(Activity activity, String permission) {
        if (useRunTimePermissions()) {
            return ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        if (useRunTimePermissions()) {
            ActivityCompat.requestPermissions(activity, permissions, requestCode);
        }
    }

    public static boolean shouldShowRational(Activity activity, String permission) {
        if (useRunTimePermissions()) {
            return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
        }
        return false;
    }

    public static void showRational(final Activity activity, RelativeLayout activityLayout) {
        // Provide an additional rationale to the user if the permission was not granted
        // and the user would benefit from additional context for the use of the permission.
        // Display a SnackBar with a button to request the missing permission.
        Snackbar snackbar = Snackbar.make(activityLayout, "Position access is required to display routes based on current location.",
                Snackbar.LENGTH_INDEFINITE);

        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(3);

        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open application setting
                goToAppSettings(activity);
            }
        }).show();
    }

    private static boolean shouldAskForPermission(Activity activity, String permission) {
        if (useRunTimePermissions()) {
            return !hasPermission(activity, permission) &&
                    (!hasAskedForPermission(activity, permission) ||
                            !shouldShowRational(activity, permission));
        }
        return false;
    }

    public static ArrayList<String> shouldAskForPermissions(Activity activity, String[] permissions) {
        ArrayList<String> requestPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (shouldAskForPermission(activity, permission)) {
                requestPermissions.add(permission);
            }
        }
        return requestPermissions;
    }

    public static void goToAppSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", activity.getPackageName(), null));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    private static boolean hasAskedForPermission(Activity activity, String permission) {
        return PreferenceManager
                .getDefaultSharedPreferences(activity)
                .getBoolean(permission, false);
    }
}
