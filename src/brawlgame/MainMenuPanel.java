package brawlgame;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
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
    private ImageIcon title[];
    private ImageIcon menu[];
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
//                if ((int) (Math.random() * 10) == 2) {
//                    g.drawImage(title[1].getImage(), 0, 0, null);
//                }else if ((int) (Math.random() * 10) == 3) {
//                    g.drawImage(title[2].getImage(), 0, 0, null);
//                }
                g.drawImage(title[0].getImage(), 0, 0, null);
                g.drawImage(menu[selectMenu].getImage(), 0, 0, null);
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
                        if (selectMenu > 0) {
                            selectMenu--;
                        }
                        break;
                    case "下":
                        if (selectMenu < 2) {
                            selectMenu++;
                        }
                        break;
                    case "Enter":
                        switch (selectMenu) {
                            case 0:
                                end(mainF, menuP, this);
                                break;
                            case 1:
                                try {
                                    File file = new File("./src/html/system.html");
                                    String cmd = file.getAbsolutePath();
                                    Runtime.getRuntime().exec("cmd /c start " + cmd);
                                } catch (IOException ev) {
                                    System.err.println(ev);
                                }
                                break;
                            case 2:
                                System.exit(0);
                                break;
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
        title = new ImageIcon[4];
        for (int i = 0; i < 4; i++) {
            title[i] = new ImageIcon("./src/img/mt" + (i + 1) + ".png");
        }
        menu = new ImageIcon[3];
        for (int i = 0; i < 3; i++) {
            menu[i] = new ImageIcon("./src/img/m" + (i + 1) + ".png");
        }
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
