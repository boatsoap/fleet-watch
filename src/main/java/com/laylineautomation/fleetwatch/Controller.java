package com.laylineautomation.fleetwatch;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;


public class Controller extends Activity implements SocketIO.Communicator,
            Location.Communicator, LoginFragment.Communicator{

    FragmentManager fragmentManager;
    LocationManager locationManager;

    RacerFragment racerFragment;
    SocketIO socketFragment;
    LoginFragment loginFragment;
    Location locationListener = new Location();

    private boolean transmit = false;
    private boolean loggedIn = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        fragmentManager = getFragmentManager();

        if(socketFragment == null) {
            socketFragment = new SocketIO();
        }

        fragmentManager.beginTransaction().add(socketFragment, "socket").commit();
        socketFragment.mConnect();

        loginFragment = (LoginFragment) fragmentManager.findFragmentByTag("login");
        if(!loggedIn){
            loginFragment = new LoginFragment();
            fragmentManager.beginTransaction().replace(R.id.mainContent, loginFragment, "racer").commit();
        } else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            racerFragment = (RacerFragment) fragmentManager.findFragmentByTag("racer");
            socketFragment = (SocketIO) fragmentManager.findFragmentByTag("socket");

            if (racerFragment == null) {
                racerFragment = new RacerFragment();
            }

            fragmentManager.beginTransaction().replace(R.id.mainContent, racerFragment, "racer").commit();

            locationListener.setCommunicator(this);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener, Looper.getMainLooper());
        }
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_controller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void srvData(JSONObject obj) throws JSONException {

    }

    @Override
    public void changeTrans() {
        if(transmit){
            transmit = false;
            locationManager.removeUpdates(locationListener);
        } else {
            transmit = true;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener, Looper.getMainLooper());
        }
    }

    @Override
    public void handshake() {

    }

    @Override
    public void newLocation(JSONObject obj) throws JSONException {
        racerFragment.setLabels(obj);
        socketFragment.sEmit("position", obj);
    }

    @Override
    public void login(String username) {
        JSONObject obj = null;
        try {
            obj.put("username", username);
            socketFragment.sEmitCb("login", obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
