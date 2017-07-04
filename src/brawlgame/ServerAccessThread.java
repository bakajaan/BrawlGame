/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brawlgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author bakaj
 */
public class ServerAccessThread extends Thread {

    /**
     * サーバー接続用ソケット
     */
    Socket sock;
    /**
     * サーバー送信用
     */
    PrintWriter out;
    /**
     * サーバー受信用
     */
    BufferedReader in;
    /**
     * サーバーアドレス
     */
    //自宅Wi-Fi
    //String server = "192.168.3.17";
    //ポケットWi-Fi
    //String server = "192.168.179.3";
    //オフライン
    String server = "localhost";
    GamePanel GP = null;

    public ServerAccessThread(GamePanel gameP) {
        GP = gameP;
        //サーバーに接続
        try {
            InetAddress addr = InetAddress.getByName(server);
            sock = new Socket(addr, 7788);
            System.out.println("Connected");
            out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public char getMode() {
        char mode = 0;
        try {
            mode = in.readLine().charAt(0);
        } catch (UnknownHostException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }
        return mode;
    }

    @Override
    public void run() {
        while (true) {
            try {
                //自分モーションタイプ送信
                out.println("T" + GP.AT);
                out.flush();
                //敵モーションタイプ受信
                GP.BT = Integer.parseInt(in.readLine());

                //自分方向送信
                out.println("H" + GP.AH);
                out.flush();
                //敵方向受信
                GP.BH = Integer.parseInt(in.readLine());

                //自分X座標送信
                out.println("X" + GP.AX);
                out.flush();
                //敵X座標受信
                GP.BX = Integer.parseInt(in.readLine());
                GP.afterBX = GP.BX;

                //自分Y座標送信
                out.println("Y" + GP.AY);
                out.flush();
                //敵Y座標受信
                GP.BY = Integer.parseInt(in.readLine());
            } catch (IOException | NumberFormatException e) {
                System.err.println(e);
            }
        }
    }
}
