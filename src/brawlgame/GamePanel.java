package brawlgame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
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
     * ゲーム表示用パネル
     */
    private JPanel gameP;
    /**
     * 自分キャラクター右向き用ラベル
     * それぞれの動きのアイコンを格納
     */
    private JLabel AcharR[];
    /**
     * 敵キャラクター右向き用ラベル
     * それぞれの動きのアイコンを格納
     */
    private JLabel BcharR[];
    /**
     * 自分キャラクター左向き用ラベル
     * それぞれの動きのアイコンを格納
     */
    private JLabel AcharL[];
    /**
     * 敵キャラクター左向き用ラベル
     * それぞれの動きのアイコンを格納
     */
    private JLabel BcharL[];
    /**
     * ジャンプ時Y座標を格納
     * Y座標が重なったときジャンプを終了(仮)
     */
    private int junpPlace = 0;
    /**
     * 重力
     * 着地していない時座標から引かれる
     */
    private int gra = 0;
    /**
     * 自分X座標
     * 左上が(0.0)
     */
    private int AX = 200;
    /**
     * 自分Y座標
     * 左上が(0.0)
     */
    private int AY = 300;
    /**
     * 敵X座標
     * 左上が(0.0)
     */
    private int BX;
    /**
     * 敵Y座標
     * 左上が(0.0)
     */
    private int BY;
    /**
     * 自分の動きのタイプ
     */
    private int AT = 0;
    /**
     * 敵の動きのタイプ
     */
    private int BT;
    /**
     * 自分の向き
     */
    private int AH = 0;
    /**
     * 敵の向き
     */
    private int BH;
    /**
     * 直立カウント
     * 立っている時のアニメーションに利用
     */
    private int standCount = 0;
    /**
     * 徒歩カウント
     * この値を利用して通常時の動きを実現する
     */
    private int walkCount = 0;
    /**
     * 死亡カウント
     * 死亡して一定時間するとリスポーンする
     */
    private int deathCount = 0;
    /**
     * Aキー押下カウント
     */
    private int AkeyCount = 0;
    /**
     * Dキー押下カウント
     */
    private int DkeyCount = 0;
    /**
     * 攻撃時間カウント
     */
    private int AttkeyCount = 0;
    private int syagamiCount = 0;
    /**
     * キャラクターのサイズ
     */
    private int charSize = 100;
    /**
     * キャラクターの画像の量
     */
    private int charType = 17;
    private int sowdPosision = 0;
    /**
     * 自分がどちらのチームかの判定
     * 先にサーバーに入った方がaで後がb
     */
    private char mode;
    /**
     * 攻撃中チーム
     * 初期はaチームから攻撃開始
     */
    private char turnMode = 'a';
    /**
     * Wキー押下判定
     */
    private boolean Wkey = false;
    /**
     * Aキー押下判定
     */
    private boolean Akey = false;
    /**
     * Sキー押下判定
     */
    private boolean Skey = false;
    /**
     * Dキー押下判定
     */
    private boolean Dkey = false;
    /**
     * 攻撃キー押下判定
     */
    private boolean Attkey = false;
    /**
     * ジャンプキー押下判定
     */
    private boolean Junpkey = false;
    /**
     * 着地しているかの判定
     */
    private boolean setti = true;
    /**
     * パネルを変更していいかどうかの判定
     */
    private boolean changePanel = false;
    /**
     * 描画許可のフラグ
     * 座標の処理中はそれぞれの座標に一時的なずれが生じるので描画しない
     */
    private boolean drawEnable = false;
//</editor-fold>

    GameMap map;

    int vx = 0;
    int vy = 0;

    /**
     * ゲームパネル
     * ゲームの内部処理をもつ
     *
     * @param mainF　パネル追加先フレーム
     */
    @SuppressWarnings("deprecation")
    public GamePanel(JFrame mainF) {

        //それぞれスレッドのインスタンス生成
        ServerAccessThread SThread = new ServerAccessThread(this);
        DrawThread DThread = new DrawThread(this);

        //マップ読み込み
        map = new GameMap("map01.dat");
        GameChara chara = new GameChara(this);

        //パネルの作成
        gameP = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                map.drow(g);
                chara.drow(g);
            }
        };
        gameP.setBounds(0, 0, 2400, 3304);
        gameP.setLayout(null);
        gameP.setVisible(true);
        gameP.show();
        gameP.setBackground(Color.WHITE);
        gameP.setBorder(new BevelBorder(BevelBorder.RAISED));
        mainF.add(gameP);

        //画像の読み込み
        loadImage(gameP);

        //コンポーネントリスナーの追加
        ComponentListener cl = new ComponentListener() {
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

        //キーリスナーの追加
        KeyListener kl = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyText(e.getKeyCode())) {
                    case "W":
                        Wkey = true;
                        if (sowdPosision < 2) {
                            sowdPosision++;
                        }
                        break;
                    case "S":
                        Skey = true;
                        if (sowdPosision > 0) {
                            sowdPosision--;
                        }
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
                        end(mainF, this, cl, SThread, DThread);
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
                        syagamiCount = 0;
                        break;
                    case "A":
                        Akey = false;
                        AkeyCount = 0;
                        walkCount = 0;
                        break;
                    case "D":
                        Dkey = false;
                        DkeyCount = 0;
                        walkCount = 0;
                        break;
                    case "G":
                        Junpkey = false;
                        break;
                    case "F":
                        Attkey = false;
                        //AttkeyCount = 0;
                        break;
                    case "Escape":
                        break;
                }
            }
        };
        mainF.addKeyListener(kl);

        //自分のチームを取得
        mode = SThread.getMode();
        if (mode == 'N') {
            end(mainF, kl, cl, SThread, DThread);
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
        update();//自分のアップデート
        drawEnable = true;

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
    private void update() {
        //ぶつかった時
        //左右
        if (AH == 1) {
            vx = 1;
        } else if (AH == 2) {
            vx = -1;
        }
        int nextAX = AX + vx;
        Point tile = map.getTileCollision(this, nextAX, AY);
        if (tile == null) {
            //AX = nextAX;
        } else {
            //右へ進んでいる場合
            if (AH == 1) {
                AX = GameMap.tilePixel(tile.x) - charSize;
                //左に進んでいる場合
            } else if (AH == 2) {
                AX = GameMap.tilePixel(tile.x + 1);
            }
            vx = 0;
        }
        //上下
        vy = gra;
        int nextAY = AY + vy;
        tile = map.getTileCollision(this, AX, AY);
        if (tile == null) {

        } else {
            //下にある場合
            if (vy>0) {
                AY = GameMap.tilePixel(tile.y) - charSize;
                setti = true;
                //上にある場合
            } else if(vy<0){
                AY = GameMap.tilePixel(tile.y +1);
            }
            vy = 0;
        }
        //死亡処理
        if (AT == 14 && deathCount < 60) {
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
        } else if (AY + charSize > BY && AY < BY + charSize
                && AX + charSize / 2 > BX && AX < BX + charSize
                && (BT == 6 || BT == 9 || BT == 12)
                && BH == 1) {
            //相手と重なっていて相手が攻撃モーション中の時死亡させる
            AT = 14;
            AttkeyCount = 0;
            if (mode == 'a') {
                turnMode = 'b';
            } else {
                turnMode = 'a';
            }
            return;
        } else if (AY + charSize > BY && AY < BY + charSize
                && AX + charSize > BX && AX < BX + charSize / 2
                && (BT == 6 || BT == 9 || BT == 12)
                && BH == 2) {
            //相手と重なっていて相手が攻撃モーション中の時死亡させる
            AT = 14;
            AttkeyCount = 0;
            if (mode == 'a') {
                turnMode = 'b';
            } else {
                turnMode = 'a';
            }
            return;
        }
        if (BT == 14) {
            turnMode = mode;
        }

        //キーによる移動
        if (Junpkey && setti && !Attkey && !Skey) {
            junpPlace = AY;
            setti = false;
            gra -= 20;
        }
        if (Akey && !Dkey && !Attkey && syagamiCount < 20) {
            if (AkeyCount < 10) {
                AkeyCount++;
                AX -= 5;
            } else {
                AH = 2;
                walkCount++;
                AX -= 10;
            }
        }
        if (Dkey && !Akey && !Attkey && syagamiCount < 20) {
            if (DkeyCount < 10) {
                DkeyCount++;
                AX += 5;
            } else {
                AH = 1;
                walkCount++;
                AX += 10;
            }
        }
        if (Skey) {
            syagamiCount++;
        }
        if (Attkey && AttkeyCount < 20) {
            AttkeyCount++;
            if (AttkeyCount < 4 && setti) {
                switch (AH) {
                    case 1:
                        AX += 8;
                        break;
                    case 2:
                        AX -= 8;
                        break;
                }
            }
            if (AttkeyCount > 17) {
                switch (AH) {
                    case 1:
                        AX -= 8;
                        break;
                    case 2:
                        AX += 8;
                        break;
                }
            }
        }
        if (!Attkey && AttkeyCount != 0 && AttkeyCount != 20) {
            AttkeyCount--;
            if (AttkeyCount >= 3) {
                AttkeyCount = 2;
            }
            if (AttkeyCount < 3) {
                switch (AH) {
                    case 1:
                        AX -= 8;
                        break;
                    case 2:
                        AX += 8;
                        break;
                }
            }
        }
        if (!Attkey && AttkeyCount == 20) {
            AttkeyCount = 0;
        }

        //敵との反発移動
        if (AY + charSize > BY && AY < BY + charSize
                && AX + charSize > BX && AX < BX + charSize && Attkey
                && (BT == 6 || BT == 9 || BT == 12)) {
            switch (AH) {
                case 1:
                    AX -= 30;
                    break;
                case 2:
                    AX += 30;
                    break;
            }
        }

        //重力による移動
        if (setti == false) {
            gra += 1;
            AY += gra;
            if (Attkey) {
                switch (AH) {
                    case 1:
                        AX += 12;
                        break;
                    case 2:
                        AX -= 12;
                        break;
                }
            }
            //自分の座標がジャンプ地点より低くなったら着地状態に変更
            if (AY > junpPlace) {
                AT = 0;
                gra = 0;
                AY = junpPlace;
                setti = true;
            }
        }

        //座標の最終チェック
        if (AX < 0) {
            AX = 0;
        }

        //向いてる方向の変更
        if (setti && !Akey && !Dkey) {
            if (AX > BX) {
                AH = 2;
            } else {
                AH = 1;
            }
        }

        //表示タイプ変更
        if (setti == false && !Attkey) {
            AT = 13;
        } else if (setti == false && Attkey) {
            AT = 16;
        } else if (syagamiCount > 20) {
            AT = 15;
        } else if (Attkey && AttkeyCount < 20) {
            switch (sowdPosision) {
                case 0:
                    AT = 6;
                    break;
                case 1:
                    AT = 9;
                    break;
                case 2:
                    AT = 12;
                    break;
            }
        } else if (walkCount > 0) {
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
            switch (sowdPosision) {
                case 0:
                    switch ((standCount / 5) % 2) {
                        case 0:
                            AT = 4;
                            break;
                        case 1:
                            AT = 5;
                            break;
                    }
                    break;
                case 1:
                    switch ((standCount / 5) % 2) {
                        case 0:
                            AT = 7;
                            break;
                        case 1:
                            AT = 8;
                            break;
                    }
                    break;
                case 2:
                    switch ((standCount / 5) % 2) {
                        case 0:
                            AT = 10;
                            break;
                        case 1:
                            AT = 11;
                            break;
                    }
                    break;
            }
        }
    }

    /**
     * 画像を読み込む
     *
     * @param gameP
     */
    @SuppressWarnings("deprecation")
    public void loadImage(JPanel gameP) {

    }

    /**
     * 終了処理
     */
    @SuppressWarnings("deprecation")
    private void end(JFrame mainF, KeyListener kl, ComponentListener cl,
            ServerAccessThread SThread, DrawThread DThread) {
        if (mode != 'N') {
            SThread.disconect();
        }
        DThread.stop();
        gameP.hide();
        mainF.remove(gameP);
        mainF.removeComponentListener(cl);
        mainF.removeKeyListener(kl);
        changePanel = true;
    }

    /**
     * @return the gameP
     */
    public JPanel getGameP() {
        return gameP;
    }

    /**
     * @return the AcharR
     */
    public JLabel[] getAcharR() {
        return AcharR;
    }

    /**
     * @return the BcharR
     */
    public JLabel[] getBcharR() {
        return BcharR;
    }

    /**
     * @return the AcharL
     */
    public JLabel[] getAcharL() {
        return AcharL;
    }

    /**
     * @return the BcharL
     */
    public JLabel[] getBcharL() {
        return BcharL;
    }

    /**
     * @return the AX
     */
    public int getAX() {
        return AX;
    }

    /**
     * @return the AY
     */
    public int getAY() {
        return AY;
    }

    /**
     * @return the BX
     */
    public int getBX() {
        return BX;
    }

    /**
     * @return the BY
     */
    public int getBY() {
        return BY;
    }

    /**
     * @return the AT
     */
    public int getAT() {
        return AT;
    }

    /**
     * @return the BT
     */
    public int getBT() {
        return BT;
    }

    /**
     * @return the AH
     */
    public int getAH() {
        return AH;
    }

    /**
     * @return the BH
     */
    public int getBH() {
        return BH;
    }

    /**
     * @param BX the BX to set
     */
    public void setBX(int BX) {
        this.BX = BX;
    }

    /**
     * @param BY the BY to set
     */
    public void setBY(int BY) {
        this.BY = BY;
    }

    /**
     * @param BT the BT to set
     */
    public void setBT(int BT) {
        this.BT = BT;
    }

    /**
     * @param BH the BH to set
     */
    public void setBH(int BH) {
        this.BH = BH;
    }

    /**
     * @return the charSize
     */
    public int getCharSize() {
        return charSize;
    }

    /**
     * @return the charType
     */
    public int getCharType() {
        return charType;
    }

    /**
     * @return the mode
     */
    public char getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(char mode) {
        this.mode = mode;
    }

    /**
     * @return the turnMode
     */
    public char getTurnMode() {
        return turnMode;
    }

    /**
     * @param turnMode the turnMode to set
     */
    public void setTurnMode(char turnMode) {
        this.turnMode = turnMode;
    }

    /**
     * @return the drawEnable
     */
    public boolean isDrawEnable() {
        return drawEnable;
    }

    /**
     * @param drawEnable the drawEnable to set
     */
    public void setDrawEnable(boolean drawEnable) {
        this.drawEnable = drawEnable;
    }
}
