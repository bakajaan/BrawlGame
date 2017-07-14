package brawlgame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import javax.swing.ImageIcon;

/**
 *
 * @author bakaj
 */
public class GameChara {

    private final GamePanel GP;
    private ImageIcon charactarR[];
    private ImageIcon charactarL[];
    private Point zahyou;
    private boolean setti = true;
    private int junpPower = 0;
    private Point junpPlace;
    private int syagamiCount = 0;
    private int standCount = 0;
    private int walkCount = 0;
    private int head = 1;
    private int type = 0;
    private int sowdPosision = 0;
    private int deathCount = 0;
    private int huriageCount = 0;
    private int AttCount = 0;
    private char mode;

    public GameChara(GamePanel gameP) {
        GP = gameP;
        zahyou = new Point(200, 300);
        junpPlace = new Point(200, 300);
        loadImage();
    }

    /**
     * 画像を読み込む
     */
    private void loadImage() {
        //イメージの読み込み
        charactarR = new ImageIcon[GP.getCharType()];
        for (int i = 0; i < GP.getCharType(); i++) {
            charactarR[i] = new ImageIcon(new ImageIcon(
                    "./src/img/" + "a" + (i + 1) + ".png").
                    getImage().getScaledInstance(GP.getCharSize(), GP.getCharSize(),
                            Image.SCALE_DEFAULT));
        }
        charactarL = new ImageIcon[GP.getCharType()];
        for (int i = 0; i < GP.getCharType(); i++) {
            charactarL[i] = new ImageIcon(new ImageIcon(
                    "./src/img/" + "b" + (i + 1) + ".png").
                    getImage().getScaledInstance(GP.getCharSize(), GP.getCharSize(),
                            Image.SCALE_DEFAULT));
        }
    }

    public void update() {
        Point move = new Point(0, 0);

        //死亡処理
        if (type == 14 && deathCount < 60) {
            //死亡モーション中は1秒間そのまま
            deathCount++;
            return;
        } else if (deathCount == 60) {
            //死亡して１秒たったら座標とモーションタイプをリセット
            deathCount = 0;
            type = 0;
            if (mode == 'a') {
                zahyou.x = 200;
            } else {
                zahyou.x = 200;
            }
            return;
        }
        //キーによる移動
        if (!GP.isAkey() && !GP.isDkey()) {
            walkCount = 0;
        }
        if (GP.isJunpkey() && setti && !GP.isAttkey() && !GP.isSkey()) {
            junpPlace.y = zahyou.y;
            setti = false;
            junpPower = 28;
        }
        if (GP.isAkey() && !GP.isDkey() && !GP.isAttkey() && syagamiCount < 20) {
            if (GP.getAkeyCount() < 10) {
                move.x -= 5;
            } else {
                head = 2;
                walkCount++;
                move.x -= 10;
            }
        }
        if (GP.isDkey() && !GP.isAkey() && !GP.isAttkey() && syagamiCount < 20) {
            if (GP.getDkeyCount() < 10) {
                move.x += 5;
            } else {
                head = 1;
                walkCount++;
                move.x += 10;
            }
        }
        if (GP.isSkey()) {
            if (syagamiCount == 0) {
                if (sowdPosision > 0) {
                    sowdPosision--;
                }
            }
            syagamiCount++;
        } else {
            syagamiCount = 0;
        }
        if (GP.isWkey()) {
            if (huriageCount == 0) {
                if (sowdPosision < 2) {
                    sowdPosision++;
                }
                huriageCount++;
            }
        } else {
            huriageCount = 0;
        }
        if (GP.isAttkey() && AttCount < 20) {
            if (AttCount < 4 && setti) {
                switch (head) {
                    case 1:
                        move.x += 8;
                        break;
                    case 2:
                        move.x -= 8;
                        break;
                }
            }
            if (AttCount > 15) {
                switch (head) {
                    case 1:
                        move.x -= 8;
                        break;
                    case 2:
                        move.x += 8;
                        break;
                }
            }
            AttCount++;
        }
        if (!GP.isAttkey() && AttCount != 0 && AttCount != 20) {
            if (AttCount > 10) {
                AttCount = 20 - AttCount;
            }
            if (AttCount == 0) {
            } else if (AttCount < 3) {
                switch (head) {
                    case 1:
                        move.x -= 8;
                        break;
                    case 2:
                        move.x += 8;
                        break;
                }
                AttCount--; 
            }
        }

        //重力による移動
        if (setti == true) {
            junpPower = 0;
        }
        junpPower -= GP.getGra();
        move.y -= junpPower;
        //System.out.println(setti);
        /*if (GP.isAttkey() && setti) {
            switch (head) {
                case 1:
                    move.x += 16;
                    break;
                case 2:
                    move.x -= 16;
                    break;
            }
        }*/
        //自分の座標がジャンプ地点より低くなったら着地状態に変更
        /*if (zahyou.y + move.y >= junpPlace.y) {
                type = 0;
                move.y = 0;
                junpPower = 0;
                zahyou.y = junpPlace.y;
                setti = true;
            }*/

        //ぶつかった時
        //左右
        int nextAX = zahyou.x + move.x;
        Point tile = GP.getMap().getTileCollision(this, nextAX, zahyou.y);
        if (tile == null) {
            zahyou.x += move.x;
        } else {
            //右へ進んでいる場合
            if (move.x > 0) {
                zahyou.x = GP.getMap().tilePixel(tile.x) - GP.getCharSize();
                //左に進んでいる場合
            } else if (move.x < 0) {
                zahyou.x = GP.getMap().tilePixel(tile.x + 1);
            }
        }
        //上下
        int nextAY = zahyou.y + move.y;
        tile = GP.getMap().getTileCollision(this, zahyou.x, nextAY);
        if (tile == null) {
            zahyou.y += move.y;
            setti = false;
        } else {
            //下にある場合
            if (move.y > 0) {
                zahyou.y = GP.getMap().tilePixel(tile.y) - GP.getCharSize();
                setti = true;
                //上にある場合
            } else if (move.y < 0) {
                zahyou.y = GP.getMap().tilePixel(tile.y + 1);
            }

        }

        //向いてる方向の変更
        if (setti && !GP.isAkey() && !GP.isDkey()) {
            if (zahyou.x > GP.getTeki().getZahyou().x) {
                head = 2;
            } else {
                head = 1;
            }
        }

        //表示タイプ変更
        if (setti == false && !GP.isAttkey()) {
            type = 13;
        } else if (setti == false && GP.isAttkey()) {
            type = 16;
        } else if (syagamiCount > 20) {
            type = 15;
        } else if (GP.isAttkey() && GP.getAttkeyCount() < 20) {
            switch (sowdPosision) {
                case 0:
                    type = 6;
                    break;
                case 1:
                    type = 9;
                    break;
                case 2:
                    type = 12;
                    break;
            }
        } else if (walkCount > 0) {
            switch ((walkCount / 5) % 6) {
                case 0:
                    type = 0;
                    break;
                case 1:
                    type = 1;
                    break;
                case 2:
                    type = 2;
                    break;
                case 3:
                    type = 3;
                    break;
                case 4:
                    type = 2;
                    break;
                case 5:
                    type = 1;
                    break;
            }
        } else {
            standCount++;
            switch (sowdPosision) {
                case 0:
                    switch ((standCount / 5) % 2) {
                        case 0:
                            type = 4;
                            break;
                        case 1:
                            type = 5;
                            break;
                    }
                    break;
                case 1:
                    switch ((standCount / 5) % 2) {
                        case 0:
                            type = 7;
                            break;
                        case 1:
                            type = 8;
                            break;
                    }
                    break;
                case 2:
                    switch ((standCount / 5) % 2) {
                        case 0:
                            type = 10;
                            break;
                        case 1:
                            type = 11;
                            break;
                    }
                    break;
            }
        }
    }

    /**
     *
     * @param g グラフィック
     */
    @SuppressWarnings("deprecation")
    public void drow(Graphics g) {
        switch (head) {
            case 1:
                g.drawImage(charactarR[type].getImage(), zahyou.x, zahyou.y, null);
                break;
            case 2:
                g.drawImage(charactarL[type].getImage(), zahyou.x, zahyou.y, null);
                break;
        }
    }

    public Point getZahyou() {
        return zahyou;
    }

    public void setZahyou(Point zahyou) {
        this.zahyou = zahyou;
    }

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

}
