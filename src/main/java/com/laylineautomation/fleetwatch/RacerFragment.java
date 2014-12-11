package com.laylineautomation.fleetwatch;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class RacerFragment extends Fragment {


    TextView lat;
    TextView lng;
    TextView sog;
    TextView cog;

    // TODO: Rename and change types of parameters
    private String username;

    public static RacerFragment newInstance(JSONObject obj) {
        RacerFragment racerFragment = new RacerFragment();

        Bundle args = new Bundle();
        args.putString("username", "taylor");
        racerFragment.setArguments(args);

        return racerFragment;
    }

    public RacerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            username = getArguments().getString("username");
        }
    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        lat = (TextView) this.getView().findViewById(R.id.dispLat);
        lng = (TextView) this.getView().findViewById(R.id.dispLng);
        sog = (TextView) this.getView().findViewById(R.id.dispSog);
        cog = (TextView) this.getView().findViewById(R.id.dispCog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_racer, container, false);
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //set listener here
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //listener = null;
    }


    public void setLabels(JSONObject obj) throws JSONException {
        lat.setText(obj.get("lat").toString());
        lng.setText(obj.get("lng").toString());
        sog.setText(obj.get("sog").toString());
        cog.setText(obj.get("cog").toString());
    }

}
