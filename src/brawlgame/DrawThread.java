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
                int AH = GP.AH;
                int BH = GP.BH;
                //パネルの移動
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
                //対象のキャラクター画像を描画
                switch (AH) {
                    case 1:
                        if (GP.AcharR[AT].getLocation().x != GP.AX
                                || GP.AcharR[AT].getLocation().y != GP.AY) {
                            GP.AcharR[AT].setLocation(GP.AX, GP.AY);
                        }
                        if (GP.AcharR[AT].isVisible() == false) {
                            for (int i = 0; i < 6; i++) {
                                if (GP.AcharR[i].isVisible()) {
                                    GP.AcharR[i].hide();
                                    break;
                                }
                                if (GP.AcharL[i].isVisible()) {
                                    GP.AcharL[i].hide();
                                    break;
                                }
                            }
                            GP.AcharR[AT].show();
                        }
                        break;
                    case 2:
                        if (GP.AcharL[AT].getLocation().x != GP.AX
                                || GP.AcharL[AT].getLocation().y != GP.AY) {
                            GP.AcharL[AT].setLocation(GP.AX, GP.AY);
                        }
                        if (GP.AcharL[AT].isVisible() == false) {
                            for (int i = 0; i < 6; i++) {
                                if (GP.AcharR[i].isVisible()) {
                                    GP.AcharR[i].hide();
                                    break;
                                }
                                if (GP.AcharL[i].isVisible()) {
                                    GP.AcharL[i].hide();
                                    break;
                                }
                            }
                            GP.AcharL[AT].show();
                        }
                        break;
                }
                switch (BH) {
                    case 1:
                        if (GP.BcharR[BT].getLocation().x != GP.BX
                                || GP.BcharR[BT].getLocation().y != GP.BY) {
                            GP.BcharR[BT].setLocation(GP.BX, GP.BY);
                        }
                        if (GP.BcharR[BT].isVisible() == false) {
                            for (int i = 0; i < 6; i++) {
                                if (GP.BcharR[i].isVisible()) {
                                    GP.BcharR[i].hide();
                                    break;
                                }
                                if (GP.BcharL[i].isVisible()) {
                                    GP.BcharL[i].hide();
                                    break;
                                }
                            }
                            GP.BcharR[BT].show();
                        }
                        break;
                    case 2:
                        if (GP.BcharL[BT].getLocation().x != GP.BX
                                || GP.BcharL[BT].getLocation().y != GP.BY) {
                            GP.BcharL[BT].setLocation(GP.BX, GP.BY);
                        }
                        if (GP.BcharL[BT].isVisible() == false) {
                            for (int i = 0; i < 6; i++) {
                                if (GP.BcharR[i].isVisible()) {
                                    GP.BcharR[i].hide();
                                    break;
                                }
                                if (GP.BcharL[i].isVisible()) {
                                    GP.BcharL[i].hide();
                                    break;
                                }
                            }
                            GP.BcharL[BT].show();
                        }
                        break;
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
