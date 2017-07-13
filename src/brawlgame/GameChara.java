package brawlgame;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author bakaj
 */
public class GameChara {

    private final GamePanel GP;
    private ImageIcon charactarR[];
    private ImageIcon charactarL[];

    public GameChara(GamePanel gameP) {
        GP = gameP;
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

    /**
     *
     * @param g グラフィック
     */
    @SuppressWarnings("deprecation")
    public void drow(Graphics g) {
        switch (GP.getAH()) {
            case 1:
                g.drawImage(charactarR[GP.getAT()].getImage(), GP.getAX(), GP.getAY(), null);
                break;
            case 2:
                g.drawImage(charactarL[GP.getAT()].getImage(), GP.getAX(), GP.getAY(), null);
                break;
        }
        switch (GP.getBH()) {
            case 1:
                g.drawImage(charactarR[GP.getBT()].getImage(), GP.getBX(), GP.getBY(), null);
                break;
            case 2:
                g.drawImage(charactarL[GP.getBT()].getImage(), GP.getBX(), GP.getBY(), null);
                break;
        }
    }
}
