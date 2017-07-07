package brawlgame;

/**
 * キャラクター描画スレッドクラス
 *
 * @author bakaj
 */
public class DrawThread extends Thread {

    /**
     * ゲームパネルクラス
     */
    GamePanel GP;

    /**
     * コンストラクタ
     *
     * @param gameP ゲームクラス
     */
    public DrawThread(GamePanel gameP) {
        GP = gameP;
    }

    @Override
    public void run() {
        while (true) {
            long oldTime = System.currentTimeMillis();//描画前時間の取得
            if (GP.drawEnable == true) {
                int AT = GP.AT;
                int BT = GP.BT;
                //背景の描画
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
                //処理速度を上げる為、位置情報が違うときのみアップデート
                if (GP.Achar[AT].getLocation().x != GP.AX
                        || GP.Achar[AT].getLocation().y != GP.AY) {
                    GP.Achar[AT].setLocation(GP.AX, GP.AY);
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
                GP.drawEnable = false;
            }
            long newTime = System.currentTimeMillis();//描画後時間の取得
            //フレームレートを安定させるため1msスリープさせる
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                System.err.println(e);
            }
            if (newTime - oldTime > 16) {
                System.out.println("DrawThreadが重くなっています");
            }
        }
    }
}
