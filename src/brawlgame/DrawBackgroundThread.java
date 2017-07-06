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
            int AT = GP.AT;
            int BT = GP.BT;
            //処理速度を上げる為、位置情報が違うときのみアップデート
            if (GP.back.getLocation().x != GP.stageX
                    || GP.back.getLocation().y != GP.stageY) {
                GP.back.setLocation(GP.stageX, GP.stageY);
            }
            long newTime = System.currentTimeMillis();//描画後時間の取得
            //フレームレートを安定させるためスリープさせる
            long sleepTime = 16 - (newTime - oldTime);
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    System.err.println(e);
                }
            }
            if (newTime - oldTime > 16) {
                System.out.println("BackThreadが重くなっています");
            }
        }
    }
}
