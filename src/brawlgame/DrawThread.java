package brawlgame;

/**
 *
 * @author bakaj
 */
public class DrawThread extends Thread {

    GamePanel GP;

    public DrawThread(GamePanel gameP) {
        GP = gameP;
    }

    @Override
    public void run() {
        while (true) {
            long oldTime = System.currentTimeMillis();//描画前時間の取得
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
            //処理速度を上げる為、位置情報が違うときのみアップデート
            if (GP.Achar[GP.AT].getLocation().x != GP.AX
                    || GP.Achar[GP.AT].getLocation().y != GP.AY) {
                GP.Achar[GP.AT].setLocation(GP.AX, GP.AY);
            }
            if (GP.Bchar[GP.BT].getLocation().x != GP.BX
                    || GP.Bchar[GP.BT].getLocation().y != GP.BY) {
                GP.Bchar[GP.BT].setLocation(GP.BX, GP.BY);
            }
            //対象の画像が表示されていない時は他を隠して対象を表示
            if (GP.Achar[GP.AT].isVisible() == false) {
                for (int i = 0; i < 6; i++) {
                    if (GP.Achar[i].isVisible()) {
                        GP.Achar[i].hide();
                    }
                }
                GP.Achar[GP.AT].show();
            }
            if (GP.Bchar[GP.BT].isVisible() == false) {
                for (int i = 0; i < 6; i++) {
                    if (GP.Bchar[i].isVisible()) {
                        GP.Bchar[i].hide();
                    }
                }
                GP.Bchar[GP.BT].show();
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
            if (newTime - oldTime > 16) {
                System.out.println("DrawThreadが重くなっています");
            }
        }
    }
}
