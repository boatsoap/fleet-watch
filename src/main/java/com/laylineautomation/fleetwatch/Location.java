package com.laylineautomation.fleetwatch;

import android.app.Fragment;
import android.location.LocationListener;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Taylor on 12/10/2014.
 */
public class Location implements LocationListener {
    static Communicator comm;

    @Override
    public void onLocationChanged(android.location.Location location) {
        JSONObject obj = new JSONObject();
        try {
            //obj.put("username", userInfo.get("username"));
            //obj.put("userId", userInfo.getInt("id"));
            obj.put("lat", location.getLatitude());
            obj.put("lng", location.getLongitude());
            obj.put("sog", location.getSpeed());
            obj.put("cog", location.getBearing());
            obj.put("timestamp", location.getTime());
            obj.put("acc", location.getAccuracy());

            comm.newLocation(obj);

            //socket.sEmit("position", obj);


            //lat.setText(obj.get("lat").toString());
            //lng.setText(obj.get("lng").toString());
            //sog.setText(obj.get("sog").toString());
            //cog.setText(obj.get("cog").toString());

        } catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void setCommunicator(Communicator communicator){
        Location.comm = communicator;
    }

    public interface Communicator{
        public void newLocation(JSONObject obj) throws JSONException;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
