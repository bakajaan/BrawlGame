/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brawlgame;

import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author bakaj
 */
public class GamePanel {

    /*ImageIcon back = new ImageIcon(new ImageIcon("C:\\\\Users\\\\bakaj\\\\Documents\\\\NetBeansProjects\\\\BrawlGame\\\\src\\\\img\\\\paper.png").
            getImage().getScaledInstance(1200, 1652, Image.SCALE_DEFAULT));*/
    JFrame SmainF;
    JPanel gameP;
    JLabel Achar[];
    JLabel Bchar[];
    JLabel bg;
    KeyAdapter ka;
    Socket sock;
    PrintWriter out;
    BufferedReader in;
    //自宅Wi-Fi
    //String server = "192.168.3.17";
    //ポケットWi-Fi
    //String server = "192.168.179.3";
    //オフライン
    String server = "localhost";
    int junpPlace = 0;
    int gra = 0;
    int AX = 200;
    int AY = 300;
    int BX;
    int BY;
    int AT = 0;
    int BT;
    int AH = 0;
    int BH;
    int afterBX = 0;
    int walkCount = 0;
    int deathCount = 0;
    int charHeight;
    int charWitdh;
    char mode;
    boolean Wkey = false;
    boolean Akey = false;
    boolean Skey = false;
    boolean Dkey = false;
    boolean SPACEkey = false;
    boolean setti = true;
    boolean kougekiFlag = false;
    boolean changePanel = false;

    JLabel onlyDebug;

    public GamePanel(JFrame mainF) {
        //フレームの所持
        SmainF = mainF;

        //パネルの作成
        gameP = new JPanel( /*//背景の描画
            @Override
            public void paint(Graphics G) {
                G.drawImage(haikei, 0, 0, this);
                super.paint(G);
            }*/);
        gameP.setBounds(0, 0, 1000, 600);
        gameP.setLayout(null);
        mainF.add(gameP);
        gameP.setVisible(true);
        gameP.show();

        //画像の読み込み
        loadImage();

        //試験用ラベルの作成
        onlyDebug = new JLabel();
        onlyDebug.setBounds(0, 0, 1000, 16);
        gameP.add(onlyDebug);

        //フレームの読み込み
        ka = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyText(e.getKeyCode())) {
                    case "W":
                        Wkey = true;
                        break;
                    case "S":
                        Skey = true;
                        break;
                    case "A":
                        Akey = true;
                        break;
                    case "D":
                        Dkey = true;
                        break;
                    case "スペース":
                        SPACEkey = true;
                        break;
                    case "Esc":
                        end();
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyText(e.getKeyCode())) {
                    case "W":
                        Wkey = false;
                        break;
                    case "S":
                        Skey = false;
                        break;
                    case "A":
                        Akey = false;
                        walkCount = 0;
                        AH = 0;
                        break;
                    case "D":
                        Dkey = false;
                        walkCount = 0;
                        AH = 0;
                        break;
                    case "スペース":
                        SPACEkey = false;
                        kougekiFlag = false;
                        break;
                    case "Escape":
                        break;
                }
            }
        };
        mainF.addKeyListener(ka);

        //サーバーに接続
        try {
            InetAddress addr = InetAddress.getByName(server);
            sock = new Socket(addr, 7788);
            System.out.println("Connected");
            out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            mode = in.readLine().charAt(0);
        } catch (UnknownHostException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }

        //一応再描画
        gameP.repaint();
        mainF.setVisible(true);
    }

    public String draw() {
        myUpdate(); //自分のアップデート
        server();   //サーバーと通信
        charactar();//キャラクターの描画

        onlyDebug.setText("mode=" + mode + " AX=" + AX + " AY=" + AY
                + " AT=" + AT + " JunpPlace=" + junpPlace);

        //フラグがたっていたらmenuを戻す
        if (changePanel == true) {
            return "menu";
        }
        return "";
    }

    public void loadImage() {
        //画像読み込み
        ImageIcon char1 = new ImageIcon(new ImageIcon("./src/img/1.png").
                getImage().getScaledInstance(120, 120, Image.SCALE_DEFAULT));
        ImageIcon char2 = new ImageIcon(new ImageIcon("./src/img/2.png").
                getImage().getScaledInstance(120, 120, Image.SCALE_DEFAULT));
        ImageIcon char3 = new ImageIcon(new ImageIcon("./src/img/3.png").
                getImage().getScaledInstance(120, 120, Image.SCALE_DEFAULT));
        ImageIcon char4 = new ImageIcon(new ImageIcon("./src/img/4.png").
                getImage().getScaledInstance(120, 120, Image.SCALE_DEFAULT));
        ImageIcon char5 = new ImageIcon(new ImageIcon("./src/img/5.png").
                getImage().getScaledInstance(120, 120, Image.SCALE_DEFAULT));
        ImageIcon char6 = new ImageIcon(new ImageIcon("./src/img/6.png").
                getImage().getScaledInstance(120, 120, Image.SCALE_DEFAULT));
        /*ImageIcon back = new ImageIcon(new ImageIcon("C:\\\\Users\\\\bakaj\\\\Documents\\\\NetBeansProjects\\\\BrawlGame\\\\src\\\\img\\\\paper.png").
                getImage().getScaledInstance(1200, 1652, Image.SCALE_DEFAULT));
         */
        charHeight = char1.getIconHeight();
        charWitdh = char1.getIconWidth();

        //自分用キャララベル作成
        Achar = new JLabel[6];
        for (int i = 0; i < 6; i++) {
            Achar[i] = new JLabel();
        }
        Achar[0].setIcon(char1);
        Achar[1].setIcon(char2);
        Achar[2].setIcon(char3);
        Achar[3].setIcon(char4);
        Achar[4].setIcon(char5);
        Achar[5].setIcon(char6);
        gameP.add(Achar[0]);
        for (int i = 0; i < 6; i++) {
            gameP.add(Achar[i]);
            Achar[i].hide();
            Achar[i].setBounds(AX, AY, char1.getIconWidth(), char1.getIconHeight());
        }

        //敵用キャララベル作成
        Bchar = new JLabel[6];
        for (int i = 0; i < 6; i++) {
            Bchar[i] = new JLabel();
        }
        Bchar[0].setIcon(char1);
        Bchar[1].setIcon(char2);
        Bchar[2].setIcon(char3);
        Bchar[3].setIcon(char4);
        Bchar[4].setIcon(char5);
        Bchar[5].setIcon(char6);
        gameP.add(Bchar[0]);
        for (int i = 0; i < 6; i++) {
            gameP.add(Bchar[i]);
            Bchar[i].hide();
            Bchar[i].setBounds(BX, BY, char1.getIconWidth(), char1.getIconHeight());
        }
    }

    private void myUpdate() {
        if (Wkey) {
            if (setti) {
                junpPlace = AY;
                setti = false;
                gra += 32;
            }
        }
        if (Skey) {
            if (setti == false) {
                gra -= 6;
            }
        }
        if (Akey) {
            AH = 2;
            walkCount++;
            AX -= 16;
        }
        if (Dkey) {
            AH = 1;
            walkCount++;
            AX += 16;
        }
        if (SPACEkey) {
            if (kougekiFlag == false) {
                kougekiFlag = true;
            }
        }
        if (setti == false) {
            //ジャンプ中の処理
            /*if (AH == 1) {
                //右を向いていたら
                if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                    AX += 2;
                } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                    AX += 16;
                } else {
                    AX += 8;
                }
            } else if (AH == 2) {
                //左を向いていたら
                if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                    AX -= 16;
                } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                    AX -= 2;
                } else {
                    AX -= 8;
                }
            } else {
                if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                    AH = 2;
                } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                    AH = 1;
                }
            }*/
            gra -= 2;
            AY = AY -= gra;
            if (AY > junpPlace) {
                AT = 0;
                gra = 0;
                AY = junpPlace;
                setti = true;
            }
        }
        //表示タイプの変更
        if (AT == 5 && deathCount < 60) {
            //死亡モーション中は	1秒間そのまま
            deathCount++;
        } else if (deathCount == 60) {
            //死亡して１秒たったら座標とモーションタイプをリセット
            deathCount = 0;
            AT = 0;
            if (mode == 'a') {
                AX = 200;
            } else {
                AX = 200;
            }
        } else if (AX + 120 > BX && AX < BX + 120 && BT == 3) {
            //相手と重なっていて相手が攻撃モーション中の時死亡させる
            AT = 5;
        } else if (setti == false) {
            AT = 4;
        } else if (kougekiFlag == true) {
            AT = 3;
        } else if (walkCount > 0 && AT >= 0 && AT <= 2) {
            switch ((walkCount / 5) % 4) {
                case 0:
                    AT = 0;
                    break;
                case 1:
                    AT = 1;
                    break;
                case 2:
                    AT = 2;
                    break;
                case 3:
                    AT = 1;
                    break;
            }
        } else {
            AT = 0;
        }
    }

    private void server() {
        try {
            //自分モーションタイプ送信
            out.println("T" + AT);
            out.flush();
            //敵モーションタイプ受信
            BT = Integer.parseInt(in.readLine());

            //自分方向送信
            out.println("H" + AH);
            out.flush();
            //敵方向受信
            BH = Integer.parseInt(in.readLine());

            //自分X座標送信
            out.println("X" + AX);
            out.flush();
            //敵X座標受信
            BX = Integer.parseInt(in.readLine());
            afterBX = BX;

            //自分Y座標送信
            out.println("Y" + AY);
            out.flush();
            //敵Y座標受信
            BY = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            System.err.println(e);
        } catch (NumberFormatException e) {
            System.err.println(e);
        }
    }

    private void charactar() {
        //処理速度を上げる為、位置情報が違うときのみアップデート
        if (Achar[AT].getLocation().x != AX
                || Achar[AT].getLocation().y != AY) {
            Achar[AT].setLocation(AX, AY);
        }
        if (Bchar[BT].getLocation().x != BX
                || Bchar[BT].getLocation().y != BY) {
            Bchar[BT].setLocation(BX, BY);
        }
        //対象の画像が表示されていない時は他を隠して対象を表示
        if (Achar[AT].isVisible() == false) {
            for (int i = 0; i < 6; i++) {
                if (Achar[i].isVisible()) {
                    Achar[i].hide();
                }
            }
            Achar[AT].show();
        }
        if (Bchar[BT].isVisible() == false) {
            for (int i = 0; i < 6; i++) {
                if (Bchar[i].isVisible()) {
                    Bchar[i].hide();
                }
            }
            Bchar[BT].show();
        }
    }

    private void end() {
        gameP.hide();
        SmainF.remove(gameP);
        SmainF.removeKeyListener(ka);
        changePanel = true;
    }
}
