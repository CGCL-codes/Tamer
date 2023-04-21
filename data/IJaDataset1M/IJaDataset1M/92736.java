package com.track;

import java.io.BufferedInputStream;
import android.os.Bundle;
import android.app.*;
import android.util.Log;
import android.content.*;
import android.content.res.Resources;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.track.views.MapTileView;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;

/**
 * @author nbukauskas
 *
 */
public class mapview extends Activity {

    public Menu mMenu;

    private static final int sMapMenuResources[] = { R.menu.map_page_menu };

    public final int[] zoomLevels = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 };

    public static int currentZoomLevel = 11;

    public static double currentMapLat = -73.90022;

    public static double currentMapLon = 40.77482;

    public static double currentTargetLat = 40;

    public static double currentTargetLon = -73;

    public static boolean navOn = false;

    private LocationManager _mLocationManager;

    private LocationListener _mLocationListener;

    private MapTileView _map;

    private Boolean isNavigable = false;

    private Thread mMapLoadThread;

    private Runnable mMapLoadRunnable;

    private Thread mMapUIThread;

    private Runnable mMapUIRunnable;

    private Thread mMapMenuThread;

    private Resources res;

    private static MapTileView mView;

    private static LinearLayout container;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getResources();
        container = new LinearLayout(this);
        container.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        setContentView(container);
        mView = new MapTileView(this);
        container.addView(mView);
    }

    ;

    protected final void setUpLocationServices() {
        _mLocationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                onLocationUpdate(location);
            }

            public void onProviderDisabled(String provider) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        };
        if (navOn) {
            _mLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            _mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, _mLocationListener);
        } else {
            handleMapContentLoading();
            Log.w("PRESET  -- location -- >>>", ("Lat: " + currentMapLat + " Lon: " + currentMapLon));
        }
    }

    private final void handleMapContentLoading() {
        _map.drawMapTiles();
    }

    private final void onLocationUpdate(Location loc) {
        Log.w("NAV MANAGER -- location -- >>>", (loc.getLatitude() + ":" + loc.getLongitude()));
        currentMapLat = loc.getLatitude();
        currentMapLon = loc.getLongitude();
        _map.drawMapTiles();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(sMapMenuResources[0], menu);
        return true;
    }

    public void returnToMainSelector() {
        Intent intent = new Intent();
        intent.setClass(mapview.this, select.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.map_menu_back:
                returnToMainSelector();
                return true;
            case R.id.map_menu_zoomIn:
                currentZoomLevel += 1;
                _map.drawMapTiles();
                return true;
            case R.id.map_menu_zoomOut:
                currentZoomLevel -= 1;
                _map.drawMapTiles();
                return true;
            default:
                break;
        }
        return false;
    }

    protected final void drawNavUpdateToast() {
        Toast.makeText(this, "Lat:" + currentMapLat + " Lon: " + currentMapLon, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _mLocationManager.removeUpdates(_mLocationListener);
        _mLocationManager = null;
        _mLocationListener = null;
    }
}
