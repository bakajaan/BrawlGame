package brawlgame;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * ゲームの内部処理
 * ゲームパネル、描画、サーバ処理をもつ
 *
 * @author bakaj
 */
public final class GamePanel {

    //<editor-fold defaultstate="collapsed" desc="メンバ">
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
     * 背景用ラベル
     * 背景をアイコンとして格納
     */
    JLabel back;
    /**
     * 自分キャラクター右向き用ラベル
     * それぞれの動きのアイコンを格納
     */
    JLabel AcharR[];
    /**
     * 敵キャラクター右向き用ラベル
     * それぞれの動きのアイコンを格納
     */
    JLabel BcharR[];
    /**
     * 自分キャラクター左向き用ラベル
     * それぞれの動きのアイコンを格納
     */
    JLabel AcharL[];
    /**
     * 敵キャラクター左向き用ラベル
     * それぞれの動きのアイコンを格納
     */
    JLabel BcharL[];
    /**
     * キー入力用キーリスナー
     * 終了処理でフレームから削除
     */
    KeyListener kl;
    /**
     * 画面サイズ変更用コンポーネント
     */
    ComponentListener cl;
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
     * カメラ移動用背景Y座標
     */
    int stageY = 0;
    /**
     * 直立カウント
     * 立っている時のアニメーションに利用
     */
    int standCount = 0;
    /**
     * 徒歩カウント
     * この値を利用して通常時の動きを実現する
     */
    int walkCount = 0;
    /**
     * 死亡カウント
     * 死亡して一定時間するとリスポーンする
     */
    int deathCount = 0;
    /**
     * キャラクターのサイズ
     */
    int setCharaSize = 100;
    /**
     * 自分がどちらのチームかの判定
     * 先にサーバーに入った方がaで後がb
     */
    char mode;
    /**
     * 攻撃中チーム
     * 初期はaチームから攻撃開始
     */
    char turnMode = 'a';
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
     * ジャンプキー押下判定
     */
    boolean Junpkey = false;
    /**
     * 着地しているかの判定
     */
    boolean setti = true;
    /**
     * パネルを変更していいかどうかの判定
     */
    boolean changePanel = false;
    /**
     * 描画許可のフラグ
     * 座標の処理中はそれぞれの座標に一時的なずれが生じるので描画しない
     */
    boolean drawEnable = false;
    /**
     * 左上にテストで表示するラベル
     */
    JLabel onlyDebug;
//</editor-fold>

    /**
     * ゲームパネル
     * ゲームの内部処理をもつ
     *
     * @param mainF　パネル追加先フレーム
     */
    public GamePanel(JFrame mainF) {
        //フレームの所持
        SmainF = mainF;

        //それぞれスレッドのインスタンス生成
        SThread = new ServerAccessThread(this);
        DThread = new DrawThread(this);

        //パネルの作成
        gameP = new JPanel();
        gameP.setBounds(0, 0, 2400, 3304);
        gameP.setLayout(null);
        gameP.setVisible(true);
        gameP.show();
        gameP.setBackground(Color.WHITE);
        gameP.setBorder(new BevelBorder(BevelBorder.RAISED));
        mainF.add(gameP);

        //試験用ラベルの作成
        onlyDebug = new JLabel();
        onlyDebug.setBounds(0, 0, 1000, 16);
        gameP.add(onlyDebug);

        //画像の読み込み
        loadImage();

        //キーリスナーの追加
        kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

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
                    case "G":
                        Junpkey = true;
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
                        break;
                    case "D":
                        Dkey = false;
                        walkCount = 0;
                        break;
                    case "G":
                        Junpkey = false;
                        break;
                    case "F":
                        Attkey = false;
                        break;
                    case "Escape":
                        break;
                }
            }
        };
        mainF.addKeyListener(kl);

        //コンポーネントリスナーの追加
        cl = new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (e.getComponent().getWidth() / 4
                        > e.getComponent().getHeight() / 3) {
                    //横長の時縦の大きさのみ変更
                    gameP.setSize((e.getComponent().getHeight() / 3) * 4,
                            e.getComponent().getHeight());
                } else {
                    //縦長の時横の大きさのみ変更
                    gameP.setSize(e.getComponent().getWidth(),
                            (e.getComponent().getWidth() / 4) * 3);
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        };
        mainF.addComponentListener(cl);

        //自分のチームを取得
        mode = SThread.getMode();
        if (mode == 'N') {
            end();
            return;
        }

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
        drawEnable = false;
        myUpdate();//自分のアップデート
        drawEnable = true;
        onlyDebug.setText("mode=" + mode + " AX=" + AX + " AY=" + AY
                + " AT=" + AT + "turnMode=" + turnMode
                + " JunpPlace=" + junpPlace);

        //フラグがたっていたらmenuを戻す
        if (changePanel == true) {
            return "menu";
        }
        return "";
    }

    /**
     * キャラクター座標の処理
     * 座標を変更させる
     */
    private void myUpdate() {
        //死亡中の処理
        if (AT == 8 && deathCount < 60) {
            //死亡モーション中は1秒間そのまま
            deathCount++;
            return;
        } else if (deathCount == 60) {
            //死亡して１秒たったら座標とモーションタイプをリセット
            deathCount = 0;
            AT = 0;
            if (mode == 'a') {
                AX = 200;
            } else {
                AX = 200;
            }
            return;
        } else if (AX + setCharaSize > BX && AX < BX + setCharaSize && BT == 3) {
            //相手と重なっていて相手が攻撃モーション中の時死亡させる
            AT = 8;
            if (mode == 'a') {
                turnMode = 'b';
            } else {
                turnMode = 'a';
            }
            return;
        }
        if (BT == 8) {
            turnMode = mode;
        }

        //キーによって移動
        if (Junpkey && setti && !Attkey) {
            junpPlace = AY;
            setti = false;
            gra -= 32;
        }
        if (Akey && !Dkey && !Attkey) {
            AH = 2;
            walkCount++;
            AX -= 10;
        }
        if (Dkey && !Akey && !Attkey) {
            AH = 1;
            walkCount++;
            AX += 10;
        }

        //着地していない時は重力を座標に影響させる
        if (setti == false) {
            gra += 2;
            AY += gra;
            //自分の座標がジャンプ地点より低くなったら着地状態に変更
            if (AY > junpPlace) {
                AT = 0;
                gra = 0;
                AY = junpPlace;
                setti = true;
            }
        } else if (!Akey && !Dkey) {
            if (AX > BX) {
                AH = 2;
            } else {
                AH = 1;
            }
        }

        //表示タイプの変更
        if (setti == false) {
            AT = 7;
        } else if (Attkey) {
            AT = 6;
        } else if (walkCount > 0 && AT >= 0 && AT <= 5) {
            switch ((walkCount / 5) % 6) {
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
                    AT = 3;
                    break;
                case 4:
                    AT = 2;
                    break;
                case 5:
                    AT = 1;
                    break;
            }
        } else {
            standCount++;
            switch ((standCount / 5) % 2) {
                case 0:
                    AT = 4;
                    break;
                case 1:
                    AT = 5;
                    break;
            }
        }
    }

    /**
     * 画像を読み込む
     */
    public void loadImage() {
        //イメージの読み込み
        ImageIcon charactarR[] = new ImageIcon[9];
        for (int i = 0; i < 9; i++) {
            charactarR[i] = new ImageIcon(new ImageIcon(
                    "./src/img/" + (i + 1) + ".png").
                    getImage().getScaledInstance(setCharaSize, setCharaSize,
                            Image.SCALE_DEFAULT));
        }
        ImageIcon charactarL[] = new ImageIcon[9];
        for (int i = 0; i < 9; i++) {
            charactarL[i] = new ImageIcon(new ImageIcon(
                    "./src/img/" + (i + 1 + 9) + ".png").
                    getImage().getScaledInstance(setCharaSize, setCharaSize,
                            Image.SCALE_DEFAULT));
        }
        ImageIcon backI = new ImageIcon();
        backI = new ImageIcon(new ImageIcon(
                "./src/img/paper.png").
                getImage().getScaledInstance(2400, 3304,
                        Image.SCALE_DEFAULT));

        //自分用キャララベル作成
        AcharR = new JLabel[9];
        for (int i = 0; i < 9; i++) {
            AcharR[i] = new JLabel(charactarR[i]);
        }
        for (int i = 0; i < 9; i++) {
            gameP.add(AcharR[i]);
            AcharR[i].hide();
            AcharR[i].setBounds(AX, AY, setCharaSize, setCharaSize);
        }
        AcharL = new JLabel[9];
        for (int i = 0; i < 9; i++) {
            AcharL[i] = new JLabel(charactarL[i]);
        }
        for (int i = 0; i < 9; i++) {
            gameP.add(AcharL[i]);
            AcharL[i].hide();
            AcharL[i].setBounds(AX, AY, setCharaSize, setCharaSize);
        }

        //敵用キャララベル作成
        BcharR = new JLabel[9];
        for (int i = 0; i < 9; i++) {
            BcharR[i] = new JLabel(charactarR[i]);
        }
        for (int i = 0; i < 9; i++) {
            gameP.add(BcharR[i]);
            BcharR[i].hide();
            BcharR[i].setBounds(BX, BY, setCharaSize, setCharaSize);
        }
        BcharL = new JLabel[9];
        for (int i = 0; i < 9; i++) {
            BcharL[i] = new JLabel(charactarL[i]);
        }
        for (int i = 0; i < 9; i++) {
            gameP.add(BcharL[i]);
            BcharL[i].hide();
            BcharL[i].setBounds(BX, BY, setCharaSize, setCharaSize);
        }

        //背景用ラベルの作成
        back = new JLabel(backI);
        back.setBounds(0, 0, 2400, 3304);
        gameP.add(back);
    }

    /**
     * 終了処理
     */
    private void end() {
        if (mode != 'N') {
            SThread.disconect();
        }
        DThread.stop();
        gameP.hide();
        SmainF.remove(gameP);
        SmainF.removeComponentListener(cl);
        SmainF.removeKeyListener(kl);
        changePanel = true;
    }
}
