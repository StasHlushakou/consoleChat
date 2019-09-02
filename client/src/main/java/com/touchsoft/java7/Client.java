package com.touchsoft.java7;

import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Client {

    public static String ipAddr = "localhost";
    public static int port = 4044;


    public static void main(String[] args) {
        new ClientSomthing(ipAddr, port);
    }

}
//----------------------------------------------------------------------------------------------------------------

/*
        try {
            try (Socket clientSocket = new Socket(ipAddr, port);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))){



                System.out.println("Вы что-то хотели сказать? Введите это здесь:");


                while (true) {
                    String word = reader.readLine(); // ждём пока клиент что-нибудь  не напишет в консоль
                    out.write(word + "\n"); // отправляем сообщение на сервер
                    out.flush();
                }


            }
        } catch (IOException e) {
            System.err.println(e);
        }
*/
