package com.example.onlinechatting.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientTCPConnector {
    // Cloud server ip address : 182.92.224.230
    private static ClientTCPConnector instance = new ClientTCPConnector("10.0.2.2", 8888);

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter output;
    private String hostname;
    private String ip;
    private int port;

    private ClientTCPConnector(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public static ClientTCPConnector getInstance() {
        if (instance == null)
            instance = new ClientTCPConnector("10.0.2.2", 8888);
        return instance;
    }

    /**
     * 建立连接，获取输入输出流
     */
    public void connect() {
        try {
            socket = new Socket(hostname, port);
            ip = socket.getLocalAddress().toString();
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream());
            System.out.println(reader + "|" + output);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向服务器发送请求数据
     */
    public void sendData(String message) {
        output.println(message);
        output.flush();
    }

    /**
     * 接受服务器的返回数据
     */
    public String receiveData() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 一次请求的封装
     */
    public String request(String message) {
        sendData(message);
        return receiveData();
    }

    public void close() {
        try {
            if (output != null)
                output.close();
            if (reader != null)
                reader.close();
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
