package brawlgame;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
     * スタートラベル
     */
    private final JLabel start;
    /**
     * 終了ラベル
     */
    private final JLabel end;
    /**
     * 選択項目
     */
    private int selectMenu = 0;
    /**
     * パネルを変更するかどうか
     */
    private boolean changePanel = false;
//</editor-fold>

    /**
     * コンストラクタ
     * パネルの作成、ラベルやリスナーの追加
     *
     * @param mainF
     */
    @SuppressWarnings("deprecation")
    public MainMenuPanel(JFrame mainF) {

        //パネルの作成
        JPanel menuP = new JPanel();
        menuP.setBounds(0, 0, 1024, 768);
        menuP.setLayout(null);
        mainF.add(menuP);
        menuP.setVisible(true);
        menuP.show();

        //ラベルの作成
        start = new JLabel("start");
        start.setBounds(0, 0, 100, 50);
        start.setForeground(Color.BLACK);
        menuP.add(start);
        end = new JLabel("end");
        end.setBounds(0, 50, 100, 50);
        end.setForeground(Color.GRAY);
        menuP.add(end);

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
        //選択項目によってラベルの色を変更
        switch (selectMenu) {
            case 0:
                start.setForeground(Color.BLACK);
                end.setForeground(Color.GRAY);
                break;
            case 1:
                start.setForeground(Color.GRAY);
                end.setForeground(Color.BLACK);
                break;
        }
        //フラグがたっていたらgameを戻す
        if (changePanel == true) {
            return "game";
        }
        return "";
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
