package brawlgame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * メインメニュークラス
 *
 * @author bakaj
 */
public class MainMenuPanel {

    //<editor-fold defaultstate="collapsed" desc="メンバ">
    /**
     * 選択項目
     */
    private int selectMenu = 0;
    private int backCount = 0;
    /**
     * パネルを変更するかどうか
     */
    private boolean changePanel = false;
    private ImageIcon title;
    private ImageIcon menu1;
    private ImageIcon menu2;
    private ImageIcon back[];
    private JPanel menuP;
//</editor-fold>

    /**
     * コンストラクタ
     * パネルの作成、ラベルやリスナーの追加
     *
     * @param mainF
     */
    @SuppressWarnings("deprecation")
    public MainMenuPanel(JFrame mainF) {

        loadImage();

        //パネルの作成
        menuP = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(title.getImage(), 0, 0, null);
                switch (selectMenu) {
                    case 0:
                        g.drawImage(menu1.getImage(), 0, 0, null);
                        break;
                    case 1:
                        g.drawImage(menu2.getImage(), 0, 0, null);
                        break;
                }
                g.drawImage(back[backCount / 10 % 4].getImage(), 0, 0, null);
            }
        };
        menuP.setBounds(0, 0, 1024, 768);
        menuP.setLayout(null);
        mainF.add(menuP);
        menuP.setVisible(true);
        menuP.show();

        //キーリスナーの追加
        KeyListener ka = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyText(e.getKeyCode())) {
                    case "上":
                        selectMenu = 0;
                        break;
                    case "下":
                        selectMenu = 1;
                        break;
                    case "Enter":
                        switch (selectMenu) {
                            case 0:
                                end(mainF, menuP, this);
                                break;
                            case 1:
                                System.exit(0);
                        }
                        break;
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        };
        mainF.addKeyListener(ka);

        //再描画
        menuP.repaint();
        mainF.setVisible(true);
    }

    /**
     * ループされる描画メソッド
     *
     * @return　遷移先パネル名
     */
    public String draw() {
        backCount++;
        menuP.repaint();
        //フラグがたっていたらgameを戻す
        if (changePanel == true) {
            return "game";
        }
        return "";
    }

    private void loadImage() {
        title = new ImageIcon("./src/img/m1.png");
        menu1 = new ImageIcon("./src/img/m2.png");
        menu2 = new ImageIcon("./src/img/m3.png");
        back = new ImageIcon[4];
        for (int i = 0; i < 4; i++) {
            back[i] = new ImageIcon("./src/img/mb" + (i + 1) + ".png");
        }
    }

    /**
     * 終了処理
     */
    @SuppressWarnings("deprecation")
    private void end(JFrame mainF, JPanel menuP, KeyListener kl) {
        menuP.hide();
        mainF.remove(menuP);
        mainF.removeKeyListener(kl);
        changePanel = true;
    }
}
