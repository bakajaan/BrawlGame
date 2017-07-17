package brawlgame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
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
     * 重力
     * 着地していない時座標から引かれる
     */
    private int gra = 2;
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
    private int WkeyCount = 0;
    /**
     * キャラクターのサイズ
     */
    private int charSize = 100;
    /**
     * キャラクターの画像の量
     */
    private int charType = 17;
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
     * パネルを変更していいかどうかの判定
     */
    private boolean changePanel = false;
    /**
     * 描画許可のフラグ
     * 座標の処理中はそれぞれの座標に一時的なずれが生じるので描画しない
     */
    private boolean drawEnable = false;
    private GameChara me;
    private GameChara teki;
    private List otherChara;
    private GameMap map;
//</editor-fold>

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

        //パネルの作成 
        gameP = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (map != null) {
                    map.drow(g);
                }
                if (me != null) {
                    me.drow(g);
                }
                if (teki != null) {
                    teki.drow(g);
                }
            }
        };
        gameP.setBounds(0, 0, 2400, 3304);
        gameP.setLayout(null);
        gameP.setVisible(true);
        gameP.show();
        gameP.setBackground(Color.WHITE);
        gameP.setBorder(new BevelBorder(BevelBorder.RAISED));
        mainF.add(gameP);

        //コンポーネントリスナーの追加
        ComponentListener cl = new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
//                if (e.getComponent().getWidth() / 4
//                        > e.getComponent().getHeight() / 3) {
//                    //横長の時縦の大きさのみ変更
//                    gameP.setSize((e.getComponent().getHeight() / 3) * 4,
//                            e.getComponent().getHeight());
//                } else {
//                    //縦長の時横の大きさのみ変更
//                    gameP.setSize(e.getComponent().getWidth(),
//                            (e.getComponent().getWidth() / 4) * 3);
//                }
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
                        end(mainF, this, cl, SThread, DThread);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyText(e.getKeyCode())) {
                    case "W":
                        Wkey = false;
                        WkeyCount = 0;
                        break;
                    case "S":
                        Skey = false;
                        break;
                    case "A":
                        Akey = false;
                        AkeyCount = 0;
                        break;
                    case "D":
                        Dkey = false;
                        DkeyCount = 0;
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
        char mode = SThread.getMode();
        if (mode == 'N') {
            end(mainF, kl, cl, SThread, DThread);
            return;
        }

        //マップ読み込み
        me = new GameChara(this, mode);
        teki = new GameChara(this, 'b');
        map = new GameMap("map02.dat", this);
        otherChara = new ArrayList();

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
        update();//キー操作
        me.update();//自分の座標計算
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

        if (me.getMode() == 'a') {
            if (teki.getZahyou().x == 0) {
                map = new GameMap("map01.dat", this);
            }
        } else {
            if (me.getZahyou().x == 0) {
                map = new GameMap("map01.dat", this);
            }
        }

        //死亡処理
        if (me.getZahyou().y + charSize > teki.getZahyou().y
                && me.getZahyou().y < teki.getZahyou().y + charSize
                && me.getZahyou().x + charSize / 2 > teki.getZahyou().x
                && me.getZahyou().x < teki.getZahyou().x + charSize
                && (teki.getType() == 6 || teki.getType() == 9 || teki.getType() == 12)
                && teki.getHead() == 1) {
            //相手と重なっていて相手が攻撃モーション中の時死亡させる
            me.setType(14);
            AttkeyCount = 0;
            if (me.getMode() == 'a') {
                turnMode = 'b';
            } else {
                turnMode = 'a';
            }
            return;
        } else if ((me.getZahyou().y + charSize > teki.getZahyou().y
                && me.getZahyou().y < teki.getZahyou().y + charSize
                && me.getZahyou().x + charSize > teki.getZahyou().x
                && me.getZahyou().x < teki.getZahyou().x + charSize / 2
                && (teki.getType() == 6 || teki.getType() == 9 || teki.getType() == 12)
                && teki.getHead() == 2)) {
            //相手と重なっていて相手が攻撃モーション中の時死亡させる
            me.setType(14);
            AttkeyCount = 0;
            if (me.getMode() == 'a') {
                turnMode = 'b';
            } else {
                turnMode = 'a';
            }
            return;
        } else if (me.getZahyou().y > 600 || teki.getZahyou().y > 600) {
            map = new GameMap("map01.dat", this);
        }
        if (teki.getType() == 14) {
            turnMode = me.getMode();
        }

        //キーによるカウント
        if (Akey) {
            if (AkeyCount < 10) {
                AkeyCount++;
            }
        }
        if (Dkey) {
            if (DkeyCount < 10) {
                DkeyCount++;
            }
        }
        if (Wkey) {
            WkeyCount++;
        }
        if (Attkey && AttkeyCount < 20) {
            AttkeyCount++;
        }
        if (!Attkey && AttkeyCount != 0 && AttkeyCount != 20) {
            AttkeyCount--;
            if (AttkeyCount >= 3) {
                AttkeyCount = 2;
            }
        }
        if (!Attkey && AttkeyCount == 20) {
            AttkeyCount = 0;
        }
    }

    /**
     * 終了処理
     */
    @SuppressWarnings("deprecation")
    private void end(JFrame mainF, KeyListener kl, ComponentListener cl,
            ServerAccessThread SThread, DrawThread DThread) {
        if (me.getMode() != 'N') {
            SThread.disconect();
        }
        DThread.stop();
        gameP.hide();
        mainF.remove(gameP);
        mainF.removeComponentListener(cl);
        mainF.removeKeyListener(kl);
        changePanel = true;
    }

    //<editor-fold defaultstate="collapsed" desc="取得、設定メソッド">
    /**
     * @return the gameP
     */
    public JPanel getGameP() {
        return gameP;
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

    /**
     * @return the AkeyCount
     */
    public int getAkeyCount() {
        return AkeyCount;
    }

    /**
     * @return the DkeyCount
     */
    public int getDkeyCount() {
        return DkeyCount;
    }

    /**
     * @return the AttkeyCount
     */
    public int getAttkeyCount() {
        return AttkeyCount;
    }

    /**
     * @return the Wkey
     */
    public boolean isWkey() {
        return Wkey;
    }

    /**
     * @return the Akey
     */
    public boolean isAkey() {
        return Akey;
    }

    /**
     * @return the Skey
     */
    public boolean isSkey() {
        return Skey;
    }

    /**
     * @return the Dkey
     */
    public boolean isDkey() {
        return Dkey;
    }

    /**
     * @return the Attkey
     */
    public boolean isAttkey() {
        return Attkey;
    }

    /**
     * @return the Junpkey
     */
    public boolean isJunpkey() {
        return Junpkey;
    }

    /**
     * @return the gra
     */
    public int getGra() {
        return gra;
    }

    /**
     * @param gra the gra to set
     */
    public void setGra(int gra) {
        this.gra = gra;
    }

    /**
     *
     * @return
     */
    public GameChara getMe() {
        return me;
    }

    /**
     *
     * @return
     */
    public GameChara getTeki() {
        return teki;
    }

    /**
     *
     */
    public void addOtherchara() {

    }

    /**
     * @return the WkeyCount
     */
    public int getWkeyCount() {
        return WkeyCount;
    }

    /**
     *
     * @return
     */
    public GameMap getMap() {
        return map;
    }
//</editor-fold>
}
