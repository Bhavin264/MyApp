package com.fittrack.Utility;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GPSTracker implements LocationListener {

    public static final int ADDRESS = 0;
    public static final int CITY = 1;
    public static final int COUNTRY = 2;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 60 * 1000; // 60 Second
    public static boolean Is_SHOWING_GPS_SETTINGS = false;
    private final Context context;
    // Declaring a Location Manager
    protected LocationManager locationManager;
    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    // flag for passive status
    boolean isPassveEnabled = false;
    // flag for Location status
    boolean canGetLocation = false;
    // flag for Location permission
    boolean isHavePermission = false;
    double latitude = 0; // latitude
    double longitude = 0; // longitude
    private Location mlocation; // location

    public GPSTracker(Context context) {
        this.context = context;
        getLocation();
    }

    public static GPSTracker getInstance(Context context) {
        return new GPSTracker(context);
    }

    /**
     * save listener and get location
     *
     * @return
     */
    public Location getLocation() {
        try {

            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            // getting passive status
            isPassveEnabled = locationManager
                    .isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
// && !isPassveEnabled
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                this.canGetLocation = false;
            } else {
                this.canGetLocation = true;
                // First get location from Passive Provider
                if (isPassveEnabled) {
                    setListenerAndGetLocation(LocationManager.PASSIVE_PROVIDER);
                }
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    if (mlocation == null) {
                        setListenerAndGetLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (mlocation == null) {
                        setListenerAndGetLocation(LocationManager.GPS_PROVIDER);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mlocation;
    }

    /**
     * Function to check is permission enabled
     *
     * @return boolean
     */
    public boolean isHavePermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            isHavePermission = true;
        } else {
            isHavePermission = false;
        }
        return this.isHavePermission;
    }

    /**
     * set Listener And Get Location
     *
     * @param provider
     * @return
     */
    public Location setListenerAndGetLocation(String provider) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            isHavePermission = true;
        } else {
            isHavePermission = false;
            return null;
        }

        locationManager.requestLocationUpdates(
                provider,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

        if (locationManager != null) {
            mlocation = locationManager
                    .getLastKnownLocation(provider);
            if (mlocation != null) {
                latitude = mlocation.getLatitude();
                longitude = mlocation.getLongitude();
            }
        }
        Log.d("provider", " : " + provider);
        Log.d("mlocation", " : " + mlocation);
        return mlocation;
    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     */

    public void stopUsingGPS() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                isHavePermission = true;
            } else {
                isHavePermission = false;
                return;
            }
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (mlocation != null) {
            latitude = mlocation.getLatitude();
        }
        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (mlocation != null) {
            longitude = mlocation.getLongitude();
        }
        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     */
//    public void showSettingsAlert(final Context context) {
//        if (Is_SHOWING_GPS_SETTINGS) {
//            // if aleart is already showing then return otherwise show settings dialog
//            return;
//        }
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//        alertDialog.setCancelable(false);
//        // Setting Dialog Title
//        alertDialog.setTitle("");
//        // Setting Dialog Message
//        alertDialog.setMessage(context.getString(R.string.gpsisnotenabled));
//        // On pressing Settings button
//        alertDialog.setPositiveButton(context.getString(R.string.settings),
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        Intent intent = new Intent(
//                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        context.startActivity(intent);
//                        Is_SHOWING_GPS_SETTINGS = false;
//
//                    }
//                });
//        // on pressing cancel button
//        alertDialog.setNegativeButton(context.getString(R.string.text_cancel),
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                        Is_SHOWING_GPS_SETTINGS = false;
//                    }
//                });
//        // Showing Alert Message
//        alertDialog.show();
//        Is_SHOWING_GPS_SETTINGS = true;
//    }


    @Override
    public void onLocationChanged(Location location) {
        this.mlocation = location;
        Log.e("onLocationChanged", " : " + location);
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.e("onProviderEnabled", "" + provider);
        getLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    /**
     * get Current Address
     *
     * @param Address_Type
     * @return
     */
    public String getCurrentAddress(int Address_Type) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        getLocation();

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            // addresses = geocoder.getFromLocation(52.3167, 5.5500, 1);
            Log.e("full address", " : " + addresses);
            String address = addresses.get(0).getAddressLine(Address_Type);
            return address;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public String getCity() {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        getLocation();

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            // addresses = geocoder.getFromLocation(52.3167, 5.5500, 1);
            Log.e("", "full address : " + addresses);
            if (addresses.size() > 0)
                return addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public String getCity(double lat, double longi) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, longi, 1);
            // addresses = geocoder.getFromLocation(52.3167, 5.5500, 1);
            Log.e("", "full address : " + addresses);
            if (addresses.size() > 0)
                return addresses.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public LatLng getLatLong(String address) {
        try {

            Geocoder gc = new Geocoder(context);
            List<Address> addresses = gc.getFromLocationName(address, 1);

            // List<LatLng> ll = new ArrayList<LatLng>(addresses.size());
            for (Address a : addresses) {
                if (a.hasLatitude() && a.hasLongitude()) {
                    // ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
                    return new LatLng(a.getLatitude(), a.getLongitude());
                }
            }
        } catch (IOException e) {
            // handle the exception
        }
        return null;
    }
}
