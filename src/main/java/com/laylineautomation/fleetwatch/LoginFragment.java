package com.laylineautomation.fleetwatch;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class LoginFragment extends Fragment {

    EditText username;
    Button login;

    SharedPreferences sharedPref;
    static Communicator comm;

    public LoginFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        username = (EditText) this.getView().findViewById(R.id.username);

        username.setText(sharedPref.getString("username", ""));

        login = (Button) this.getView().findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                if(!user.equals("")){
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("username", user);
                    editor.commit();

                    comm.login(user);
                } else {
                    Toast.makeText(getActivity(), "We need a name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setCommunicator(Communicator communicator){
        LoginFragment.comm = communicator;
    }
    public interface Communicator {
        public void login(String username);
    }

}
