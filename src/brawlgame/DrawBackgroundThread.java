package brawlgame;

/**
 *
 * @author bakaj
 */
public class DrawBackgroundThread extends Thread {

    /**
     * 値取得用ゲームクラス
     */
    GamePanel GP;

    /**
     * コンストラクタ
     *
     * @param gameP ゲームクラス
     */
    public DrawBackgroundThread(GamePanel gameP) {
        GP = gameP;
    }

    @Override
    public void run() {
        while (true) {
            long oldTime = System.currentTimeMillis();//描画前時間の取得
            if (GP.drawEnable == true) {
                if (GP.turnMode == GP.mode) {
                    if (GP.gameP.getLocation().x + GP.AX > 600) {
                        GP.gameP.setLocation(-GP.AX + 600, 0);
                    }
                    if (GP.gameP.getLocation().x + GP.AX < 200) {
                        GP.gameP.setLocation(-GP.AX + 200, 0);
                    }
                } else {
                    if (GP.gameP.getLocation().x + GP.BX > 600) {
                        GP.gameP.setLocation(-GP.BX + 600, 0);
                    }
                    if (GP.gameP.getLocation().x + GP.BX < 200) {
                        GP.gameP.setLocation(-GP.BX + 200, 0);
                    }
                }
            }
            long newTime = System.currentTimeMillis();//描画後時間の取得
            //フレームレートを安定させるため1msスリープさせる
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
            if (newTime - oldTime > 16) {
                System.out.println("BackThreadが重くなっています");
            }
        }
    }
}
