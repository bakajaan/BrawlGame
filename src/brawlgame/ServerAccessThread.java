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
 * サーバー通信クラス
 *
 * @author bakaj
 */
public class ServerAccessThread extends Thread {

    //<editor-fold defaultstate="collapsed" desc="メンバ">
    /**
     * サーバー接続用ソケット
     */
    private Socket sock;
    /**
     * サーバー送信用
     */
    private PrintWriter out;
    /**
     * サーバー受信用
     */
    private BufferedReader in;
    /**
     * ゲームパネル
     * 座標を取得するのに利用
     */
    private GamePanel GP = null;
//</editor-fold>

    /**
     * コンストラクタ
     * サーバーにアクセスしてルートを確立
     *
     * @param gameP　値取得用GamePanelクラス
     */
    public ServerAccessThread(GamePanel gameP) {
        /**
         * サーバーアドレス
         */
        //自宅Wi-Fi
        //String server = "192.168.3.17";
        //String server = "192.168.3.10";
        //ポケットWi-Fi
        //String server = "192.168.179.3";
        //オフライン
        String server = "localhost";
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

    /**
     * チームを取得
     *
     * @return チーム(a.b)
     */
    public char getMode() {
        if (in == null) {
            return 'N';
        }
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
        try {
            while (true) {
                long oldTime = System.currentTimeMillis();//通信前時間の取得
                String sendT = ""
                        + "T" + GP.getAT() + "t"
                        + "H" + GP.getAH() + "h"
                        + "X" + GP.getAX() + "x"
                        + "Y" + GP.getAY() + "y";
                out.println(sendT);
                out.flush();
                String receiveT = in.readLine();
                GP.setBT(Integer.parseInt(receiveT.substring(
                        receiveT.indexOf("T") + 1, receiveT.indexOf("t"))));
                GP.setBH(Integer.parseInt(receiveT.substring(
                        receiveT.indexOf("H") + 1, receiveT.indexOf("h"))));
                GP.setBX(Integer.parseInt(receiveT.substring(
                        receiveT.indexOf("X") + 1, receiveT.indexOf("x"))));
                GP.setBY(Integer.parseInt(receiveT.substring(
                        receiveT.indexOf("Y") + 1, receiveT.indexOf("y"))));
                long newTime = System.currentTimeMillis();//通信後時間の取得
                //描画の半分の速さでループするようにスリープさせる
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    System.err.println(e);
                }
                if (newTime - oldTime > 16) {
                    System.out.println("ServerThreadが重くなっています");
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println(e);
        }
    }

    /**
     * サーバー切断
     * サーバーにCとメッセージを送り切断を要求する
     */
    @SuppressWarnings("deprecation")
    public void disconect() {
        out.println("C");
        out.flush();
        this.stop();
    }
}
