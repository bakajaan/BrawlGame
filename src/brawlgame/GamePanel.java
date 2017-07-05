package brawlgame;

import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * ゲームの内部処理
 * ゲームパネル、描画、サーバ処理をもつ
 *
 * @author bakaj
 */
public final class GamePanel {

    /*ImageIcon back = new ImageIcon(new ImageIcon("C:\\\\Users\\\\bakaj\\\\Documents\\\\NetBeansProjects\\\\BrawlGame\\\\src\\\\img\\\\paper.png").
            getImage().getScaledInstance(1200, 1652, Image.SCALE_DEFAULT));*/
    /**
     * メインフレーム
     * キーリスナーとパネルの追加削除に利用
     */
    JFrame SmainF;
    /**
     * ゲーム表示用パネル
     */
    JPanel gameP;
    /**
     * 自分キャラクター用ラベル
     * それぞれの動きのアイコンを格納
     */
    JLabel Achar[];
    /**
     * 敵キャラクター用ラベル
     * それぞれの動きのアイコンを格納
     */
    JLabel Bchar[];
    /**
     * キーリスナー追加用キーアダプター
     * 終了処理でフレームから削除
     */
    KeyAdapter ka;
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
     * サーバーサクセス用のスレッド
     * サーバーとデータの送受信を行う。
     */
    ServerAccessThread SThread;
    /**
     * 描画用のスレッド
     * キャラクターの座標更新、描画を行う
     */
    DrawThread DThread;
    /**
     * サーバーアドレス
     */
    //自宅Wi-Fi
    //String server = "192.168.3.17";
    //ポケットWi-Fi
    //String server = "192.168.179.3";
    //オフライン
    String server = "localhost";
    /**
     * ジャンプ時Y座標を格納
     * Y座標が重なったときジャンプを終了(仮)
     */
    int junpPlace = 0;
    /**
     * 重力
     * 着地していない時座標から引かれる
     */
    int gra = 0;
    /**
     * 自分X座標
     * 左上が(0.0)
     */
    int AX = 200;
    /**
     * 自分Y座標
     * 左上が(0.0)
     */
    int AY = 300;
    /**
     * 敵X座標
     * 左上が(0.0)
     */
    int BX;
    /**
     * 敵Y座標
     * 左上が(0.0)
     */
    int BY;
    /**
     * 自分の動きのタイプ
     */
    int AT = 0;
    /**
     * 敵の動きのタイプ
     */
    int BT;
    /**
     * 自分の向き
     */
    int AH = 0;
    /**
     * 敵の向き
     */
    int BH;
    /**
     * 徒歩カウント
     * この値を利用して歩く動きを実現する
     */
    int walkCount = 0;
    /**
     * 死亡カウント
     * 死亡して一定時間するとリスポーンする
     */
    int deathCount = 0;
    /**
     * キャラクター画像の高さ
     */
    int charHeight;
    /**
     * キャラクター画像の幅
     */
    int charWitdh;
    /**
     * 自分がどちらのチームかの判定
     * 先にサーバーに入った方がaで後がb
     */
    char mode;
    /**
     * Wキー押下判定
     */
    boolean Wkey = false;
    /**
     * Aキー押下判定
     */
    boolean Akey = false;
    /**
     * Sキー押下判定
     */
    boolean Skey = false;
    /**
     * Dキー押下判定
     */
    boolean Dkey = false;
    /**
     * 攻撃キー押下判定
     */
    boolean Attkey = false;
    /**
     * 着地しているかの判定
     */
    boolean setti = true;
    /**
     * パネルを変更していいかどうかの判定
     */
    boolean changePanel = false;
    /**
     * 左上にテストで表示するラベル
     */
    JLabel onlyDebug;

    /**
     * ゲームパネル
     * ゲームの内部処理をもつ
     *
     * @param mainF
     */
    public GamePanel(JFrame mainF) {
        //フレームの所持
        SmainF = mainF;

        SThread = new ServerAccessThread(this);
        DThread = new DrawThread(this);

        //パネルの作成
        gameP = new JPanel( /*//背景の描画
            @Override
            public void paint(Graphics G) {
                G.drawImage(haikei, 0, 0, this);
                super.paint(G);
            }*/);
        gameP.setBounds(0, 0, 1024, 768);
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
                    case "F":
                        Attkey = true;
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
                    case "F":
                        Attkey = false;
                        break;
                    case "Escape":
                        break;
                }
            }
        };
        mainF.addKeyListener(ka);

        //自分のチームを取得
        mode = SThread.getMode();

        //それぞれのスレッドを開始
        SThread.start();
        DThread.start();

        //一応再描画
        gameP.repaint();
        mainF.setVisible(true);
    }

    /**
     * レンダリング処理
     *
     * @return 画面遷移先
     */
    public String draw() {
        myUpdate();//自分のアップデート
        
        onlyDebug.setText("mode=" + mode + " AX=" + AX + " AY=" + AY
                + " AT=" + AT + " JunpPlace=" + junpPlace);
        
        //フラグがたっていたらmenuを戻す
        if (changePanel == true) {
            return "menu";
        }
        return "";
    }

    /**
     * キャラクターの処理
     * 座標を変更させる
     */
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
                gra -= 4;
            }
        }
        if (Akey) {
            AH = 2;
            walkCount++;
            AX -= 8;
        }
        if (Dkey) {
            AH = 1;
            walkCount++;
            AX += 8;
        }
        if (setti == false) {
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
            //死亡モーション中は1秒間そのまま
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
        } else if (Attkey) {
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

    /**
     * 画像を読み込む
     */
    public void loadImage() {
        //画像読み込み
        ImageIcon char1 = new ImageIcon(new ImageIcon("./src/img/1.png").
                getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));
        ImageIcon char2 = new ImageIcon(new ImageIcon("./src/img/2.png").
                getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));
        ImageIcon char3 = new ImageIcon(new ImageIcon("./src/img/3.png").
                getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));
        ImageIcon char4 = new ImageIcon(new ImageIcon("./src/img/4.png").
                getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));
        ImageIcon char5 = new ImageIcon(new ImageIcon("./src/img/5.png").
                getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));
        ImageIcon char6 = new ImageIcon(new ImageIcon("./src/img/6.png").
                getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT));
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

    /**
     * 終了処理
     */
    private void end() {
        SThread.stop();
        gameP.hide();
        SmainF.remove(gameP);
        SmainF.removeKeyListener(ka);
        changePanel = true;
    }
}
