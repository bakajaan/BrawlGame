package brawlgame;

import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 * ゲームクライアント
 *
 * @author bakaj
 */
public final class BrawlGame {

    //<editor-fold defaultstate="collapsed" desc="メンバ">
    /**
     * 現在のパネルの名前
     */
    private String panelName;
    /**
     * メインフレーム
     */
    private final JFrame mainF;
    /**
     * メニュークラス
     */
    private MainMenuPanel Menu;
    /**
     * ゲームクラス
     */
    private GamePanel Game;
//</editor-fold>

    /**
     * メインメソッド
     *
     * @param args
     */
    public static void main(String args[]) {
        BrawlGame BG = new BrawlGame();
        BG.mainRoop();
    }

    /**
     * コンストラクタ
     * フレームの作成、初期パネルとしてメニュー画面に繊維
     */
    public BrawlGame() {
        //フレームの作成
        mainF = new JFrame();
        mainF.setBounds(0, 0, 980, 660);
        mainF.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainF.setLayout(null);
        mainF.setResizable(false);
        mainF.setVisible(true);

        //メニュー画面から開始
        panelName = "menu";
        panelChange(panelName);
    }

    /**
     * ループ
     */
    public void mainRoop() {
        while (true) {
            long oldTime = System.currentTimeMillis();//描画前時間の取得
            String kekka = "";
            //選択中パネルの描画
            switch (panelName) {
                case "menu":
                    kekka = Menu.draw();
                    break;
                case "game":
                    kekka = Game.draw();
                    break;
            }
            //戻り値がパネル名だったときパネルの変更
            switch (kekka) {
                case "menu":
                    panelChange(kekka);
                    panelName = kekka;
                    break;
                case "game":
                    panelChange(kekka);
                    panelName = kekka;
                    break;
                default:
                    break;
            }
            long newTime = System.currentTimeMillis();//描画後時間の取得
            //フレームレートを安定させるためスリープさせる
            long sleepTime = 16 - (newTime - oldTime);
            if (newTime - oldTime > 16) {
                System.out.println("処理が重くなっています");
            }
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    System.err.println(e);
                }
            }
        }
    }

    /**
     * パネル変更
     *
     * @param panel　変更先パネル名
     */
    private void panelChange(String panel) {
        //パネルのインスタンスを生成
        switch (panel) {
            case "menu":
                Menu = new MainMenuPanel(mainF);
                break;
            case "game":
                Game = new GamePanel(mainF);
                break;
        }
    }
}
