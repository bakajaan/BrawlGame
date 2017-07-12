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
    private final GamePanel GP;

    /**
     * コンストラクタ
     *
     * @param gameP ゲームクラス
     */
    public DrawThread(GamePanel gameP) {
        GP = gameP;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void run() {
        while (true) {
            long oldTime = System.currentTimeMillis();//描画前時間の取得
            if (GP.isDrawEnable() == true) {
                int AT = GP.getAT();
                int BT = GP.getBT();
                int AH = GP.getAH();
                int BH = GP.getBH();
                GP.getGameP().repaint();
                //パネルの移動
                if (GP.getTurnMode() == GP.getMode()) {
                    if (GP.getAX() > GP.getBX()) {
                        if (GP.getAX() - GP.getBX() < 980) {
                            int move = GP.getGameP().getX() + (GP.getBX() + (GP.getAX() - GP.getBX()) / 2) - 490 + GP.getCharSize() / 2;
                            if (move > 32) {
                                move = 32;
                            }
                            if (move < -32) {
                                move = -32;
                            }
                            GP.getGameP().setLocation(GP.getGameP().getX() - move, 0);
                        } else {
                            int move = GP.getGameP().getX() + GP.getAX() - 200 + GP.getCharSize() / 2;
                            if (move > 32) {
                                move = 32;
                            }
                            if (move < -32) {
                                move = -32;
                            }
                            GP.getGameP().setLocation(GP.getGameP().getX() - move, 0);
                        }
                    } else {
                        if (GP.getBX() - GP.getAX() < 980) {
                            int move = GP.getGameP().getX() + (GP.getAX() + (GP.getBX() - GP.getAX()) / 2) - 490 + GP.getCharSize() / 2;
                            if (move > 32) {
                                move = 32;
                            }
                            if (move < -32) {
                                move = -32;
                            }
                            GP.getGameP().setLocation(GP.getGameP().getX() - move, 0);
                        } else {
                            int move = GP.getGameP().getX() + GP.getAX() - 200 + GP.getCharSize() / 2;
                            if (move > 32) {
                                move = 32;
                            }
                            if (move < -32) {
                                move = -32;
                            }
                            GP.getGameP().setLocation(GP.getGameP().getX() - move, 0);
                        }
                    }
                } else {
                    if (GP.getAX() > GP.getBX()) {
                        if (GP.getAX() - GP.getBX() < 980) {
                            int move = GP.getGameP().getX() + (GP.getBX() + (GP.getAX() - GP.getBX()) / 2) - 490 + GP.getCharSize() / 2;
                            if (move > 32) {
                                move = 32;
                            }
                            if (move < -32) {
                                move = -32;
                            }
                            GP.getGameP().setLocation(GP.getGameP().getX() - move, 0);
                        } else {
                            int move = GP.getGameP().getX() + GP.getBX() - 200 + GP.getCharSize() / 2;
                            if (move > 32) {
                                move = 32;
                            }
                            if (move < -32) {
                                move = -32;
                            }
                            GP.getGameP().setLocation(GP.getGameP().getX() - move, 0);
                        }
                    } else {
                        if (GP.getBX() - GP.getAX() < 980) {
                            int move = GP.getGameP().getX() + (GP.getAX() + (GP.getBX() - GP.getAX()) / 2) - 490 + GP.getCharSize() / 2;
                            if (move > 32) {
                                move = 32;
                            }
                            if (move < -32) {
                                move = -32;
                            }
                            GP.getGameP().setLocation(GP.getGameP().getX() - move, 0);
                        } else {
                            int move = GP.getGameP().getX() + GP.getBX() - 200 + GP.getCharSize() / 2;
                            if (move > 32) {
                                move = 32;
                            }
                            if (move < -32) {
                                move = -32;
                            }
                            GP.getGameP().setLocation(GP.getGameP().getX() - move, 0);
                        }
                    }
                }
                //対象のキャラクター画像を描画
                switch (AH) {
                    case 1:
                        if (GP.getAcharR()[AT].getLocation().x != GP.getAX()
                                || GP.getAcharR()[AT].getLocation().y != GP.getAY()) {
                            GP.getAcharR()[AT].setLocation(GP.getAX(), GP.getAY());
                        }
                        if (GP.getAcharR()[AT].isVisible() == false) {
                            for (int i = 0; i < GP.getCharType(); i++) {
                                if (GP.getAcharR()[i].isVisible()) {
                                    GP.getAcharR()[i].hide();
                                    break;
                                }
                                if (GP.getAcharL()[i].isVisible()) {
                                    GP.getAcharL()[i].hide();
                                    break;
                                }
                            }
                            GP.getAcharR()[AT].show();
                        }
                        break;
                    case 2:
                        if (GP.getAcharL()[AT].getLocation().x != GP.getAX()
                                || GP.getAcharL()[AT].getLocation().y != GP.getAY()) {
                            GP.getAcharL()[AT].setLocation(GP.getAX(), GP.getAY());
                        }
                        if (GP.getAcharL()[AT].isVisible() == false) {
                            for (int i = 0; i < GP.getCharType(); i++) {
                                if (GP.getAcharR()[i].isVisible()) {
                                    GP.getAcharR()[i].hide();
                                    break;
                                }
                                if (GP.getAcharL()[i].isVisible()) {
                                    GP.getAcharL()[i].hide();
                                    break;
                                }
                            }
                            GP.getAcharL()[AT].show();
                        }
                        break;
                }
                switch (BH) {
                    case 1:
                        if (GP.getBcharR()[BT].getLocation().x != GP.getBX()
                                || GP.getBcharR()[BT].getLocation().y != GP.getBY()) {
                            GP.getBcharR()[BT].setLocation(GP.getBX(), GP.getBY());
                        }
                        if (GP.getBcharR()[BT].isVisible() == false) {
                            for (int i = 0; i < GP.getCharType(); i++) {
                                if (GP.getBcharR()[i].isVisible()) {
                                    GP.getBcharR()[i].hide();
                                    break;
                                }
                                if (GP.getBcharL()[i].isVisible()) {
                                    GP.getBcharL()[i].hide();
                                    break;
                                }
                            }
                            GP.getBcharR()[BT].show();
                        }
                        break;
                    case 2:
                        if (GP.getBcharL()[BT].getLocation().x != GP.getBX()
                                || GP.getBcharL()[BT].getLocation().y != GP.getBY()) {
                            GP.getBcharL()[BT].setLocation(GP.getBX(), GP.getBY());
                        }
                        if (GP.getBcharL()[BT].isVisible() == false) {
                            for (int i = 0; i < GP.getCharType(); i++) {
                                if (GP.getBcharR()[i].isVisible()) {
                                    GP.getBcharR()[i].hide();
                                    break;
                                }
                                if (GP.getBcharL()[i].isVisible()) {
                                    GP.getBcharL()[i].hide();
                                    break;
                                }
                            }
                            GP.getBcharL()[BT].show();
                        }
                        break;
                }
                GP.setDrawEnable(false);
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
