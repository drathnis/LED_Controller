package com.example.led_controller.backup;

import android.util.Log;

import com.example.led_controller.MainActivity;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;


public class Bulb_Controller extends MainActivity {

    private String bulbIP;
    private final int bulbPort = 55443;
    private Socket mSocket;
    private boolean cmd_run = true;
    private BufferedOutputStream mBos;
    private BufferedReader mReader;
    private android.os.Handler handler;

    Bulb_Controller(String ip, android.os.Handler handle){

        handler = handle;
        bulbIP = ip;

        connect();
    }

    public void write(final String cmd) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (mBos != null && mSocket.isConnected()) {
                        try {
                            mBos.write(cmd.getBytes());
                            mBos.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.d("WRITE", "mBos = null or mSocket is closed");
                    }
                }
            }).start();
    }



    private void connect(){
        Log.d("CONNECT", "Trying To Connect");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cmd_run = true;
                    mSocket = new Socket(bulbIP, bulbPort);
                    mSocket.setKeepAlive(true);
                    mBos= new BufferedOutputStream(mSocket.getOutputStream());
                    mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                    //handler.sendEmptyMessage(MainActivity.CONNECT_SUCCESS);

                    while (cmd_run){
                        try {
                            String value = mReader.readLine();
                            Log.d("READER", "value = "+value);
                            if (value==null)break;
                        }catch (Exception e){
                            Log.e("Error", e.toString());
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //handler.sendEmptyMessage(MainActivity.CONNECT_FAILURE);
                }
            }
        }).start();
    }

    public void close(){
        try{
            cmd_run = false;
            if (mSocket!=null)
                mSocket.close();
        }catch (Exception e){
            Log.e("Error", e.toString());
        }

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            cmd_run = false;
            if (mSocket!=null)
                mSocket.close();
        }catch (Exception e){
            Log.e("Error", e.toString());
        }

    }



}
