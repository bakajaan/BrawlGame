package brawlgame;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author bakaj
 */
public class MainMenuPanel {

    /**
     * メインフレーム
     */
    JFrame SmainF;
    /**
     * メニュー用パネル
     */
    JPanel menuP;
    /**
     * スタートラベル
     */
    JLabel start;
    /**
     * 終了ラベル
     */
    JLabel end;
    /**
     * 選択項目
     */
    int selectMenu = 0;
    /**
     * パネルを変更するかどうか
     */
    boolean changePanel = false;
    /**
     * リスナー用キーアダプター
     */
    KeyAdapter ka;

    /**
     *
     * @param mainF
     */
    public MainMenuPanel(JFrame mainF) {
        //フレームの所持
        SmainF = mainF;

        //パネルの作成
        menuP = new JPanel();
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
        ka = new KeyAdapter() {
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
                                end();
                                break;
                            case 1:
                                System.exit(0);
                        }
                        break;
                }
            }
        };
        mainF.addKeyListener(ka);

        //一応再描画
        menuP.repaint();
        mainF.setVisible(true);
    }

    /**
     *
     * @return
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
    private void end() {
        menuP.hide();
        SmainF.remove(menuP);
        SmainF.removeKeyListener(ka);
        changePanel = true;
    }
}
