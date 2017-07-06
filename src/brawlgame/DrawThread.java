package brawlgame;
//test02
//test04
//test03
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
            int AT = GP.AT;
            int BT = GP.BT;
            long oldTime = System.currentTimeMillis();//描画前時間の取得
            //処理速度を上げる為、位置情報が違うときのみアップデート
            if (GP.back.getLocation().x != GP.stageX
                    || GP.back.getLocation().y != GP.stageY) {
                GP.back.setLocation(GP.stageX, GP.stageY);
            }
            if (GP.Achar[AT].getLocation().x != GP.AX + GP.stageX
                    || GP.Achar[AT].getLocation().y != GP.AY) {
                GP.Achar[AT].setLocation(GP.AX + GP.stageX, GP.AY);
            }
            if (GP.Bchar[BT].getLocation().x != GP.BX
                    || GP.Bchar[BT].getLocation().y != GP.BY) {
                GP.Bchar[BT].setLocation(GP.BX, GP.BY);
            }
            //対象の画像が表示されていない時は他を隠して対象を表示
            if (GP.Achar[AT].isVisible() == false) {
                for (int i = 0; i < 6; i++) {
                    if (GP.Achar[i].isVisible()) {
                        GP.Achar[i].hide();
                    }
                }
                GP.Achar[AT].show();
            }
            if (GP.Bchar[BT].isVisible() == false) {
                for (int i = 0; i < 6; i++) {
                    if (GP.Bchar[i].isVisible()) {
                        GP.Bchar[i].hide();
                    }
                }
                GP.Bchar[BT].show();
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
                System.out.println("DrawThreadが重くなっています");
            }
        }
    }
}
