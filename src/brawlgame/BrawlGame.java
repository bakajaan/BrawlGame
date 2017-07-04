package brawlgame;
//test1
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 *
 * @author bakaj
 */
public final class BrawlGame {

    /**
     * 現在のパネルの名前
     */
    String panelName;
    /**
     * メインフレーム
     */
    JFrame mainF;
    /**
     * メニュークラス
     */
    MainMenuPanel Menu;
    /**
     * ゲームクラス
     */
    GamePanel Game;

    /**
     *
     * @param args
     */
    public static void main(String args[]) {
        BrawlGame BG = new BrawlGame();
        BG.mainRoop();
    }

    /**
     *
     */
    public BrawlGame() {
        //フレームの作成
        mainF = new JFrame();
        mainF.setBounds(0, 0, 1024, 768);
        mainF.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainF.setLayout(null);
        mainF.setVisible(true);

        //メニュー画面から開始
        panelName = "menu";
        panelChange(panelName);
    }

    /**
     *
     */
    public void mainRoop() {
        while (true) {
            String kekka = "";
            long oldTime = System.currentTimeMillis();//描画前時間の取得
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
            if (sleepTime < 0) {
                sleepTime = 0;
            } else {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    System.err.println(e);
                }
            }
        }
    }

    /**
     *
     * @param panel
     */
    public void panelChange(String panel) {
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
