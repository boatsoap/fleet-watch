package com.laylineautomation.fleetwatch;

import android.app.Fragment;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class SocketIO extends Fragment{

    Socket sock = null;
    static Communicator comm;


    public void sEmit(String event, JSONObject obj){
        if(sock.connected()) {
            System.out.println(obj);
            sock.emit(event, obj);
        }
    }

    public void sEmitCb(String event, JSONObject obj){
        if(sock.connected()) {
            sock.emit(event, obj, new Ack() {
                @Override
                public void call(Object... args) {
                    JSONObject json = (JSONObject)args[0];
                    try {
                        comm.srvData(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void mDisconnect(JSONObject obj){
        this.sEmit("leave", obj);
        sock.disconnect();
    }

    public void mConnect(){
        sock.connect();
    }

    public SocketIO(){
        try {
            sock = IO.socket("http://50.247.124.73:44101");
            sock.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect");
                    //comm.handshake();
                }
            });
            sock.on(Socket.EVENT_DISCONNECT, new Emitter.Listener(){
                @Override
                public void call(Object... args){
                    System.out.println("disconnect");
                }
            });
            sock.on("disconnAll", new Emitter.Listener(){
                @Override
                public void call(Object... args){

                    comm.changeTrans();
                }
            });
            sock.on("transmit", new Emitter.Listener(){
                @Override
                public void call(Object... args){
                    comm.changeTrans();
                }
            });

        } catch(URISyntaxException e) {
            e.printStackTrace();
        }
    };

    public void setCommunicator(Communicator communicator){
        SocketIO.comm = communicator;
    }

    public interface Communicator {
        public void srvData(JSONObject obj) throws JSONException;
        public void changeTrans();
        public void handshake();
    }

}
