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
            //パネルの移動
            if (GP.isDrawEnable()) {
                int move = 0;
                if (GP.getTurnMode() == GP.getMe().getMode()) {
                    if (GP.getMe().getZahyou().x > GP.getTeki().getZahyou().x) {
                        if (GP.getMe().getZahyou().x - GP.getTeki().getZahyou().x < 980) {
                            move = GP.getGameP().getX() + (GP.getTeki().getZahyou().x + (GP.getMe().getZahyou().x - GP.getTeki().getZahyou().x) / 2) - 490 + GP.getCharSize() / 2;
                            if (move > 32) {
                                move = 32;
                            }
                            if (move < -32) {
                                move = -32;
                            }
                        } else {
                            move = GP.getGameP().getX() + GP.getMe().getZahyou().x - 200 + GP.getCharSize() / 2;
                            if (move > 32) {
                                move = 32;
                            }
                            if (move < -32) {
                                move = -32;
                            }
                        }
                    } else {
                        if (GP.getTeki().getZahyou().x - GP.getMe().getZahyou().x < 980) {
                            move = GP.getGameP().getX() + (GP.getMe().getZahyou().x + (GP.getTeki().getZahyou().x - GP.getMe().getZahyou().x) / 2) - 490 + GP.getCharSize() / 2;
                            if (move > 32) {
                                move = 32;
                            }
                            if (move < -32) {
                                move = -32;
                            }
                        } else {
                            move = GP.getGameP().getX() + GP.getMe().getZahyou().x - 200 + GP.getCharSize() / 2;
                            if (move > 32) {
                                move = 32;
                            }
                            if (move < -32) {
                                move = -32;
                            }
                        }
                    }
                } else {
                    if (GP.getMe().getZahyou().x > GP.getTeki().getZahyou().x) {
                        if (GP.getMe().getZahyou().x - GP.getTeki().getZahyou().x < 980) {
                            move = GP.getGameP().getX() + (GP.getTeki().getZahyou().x + (GP.getMe().getZahyou().x - GP.getTeki().getZahyou().x) / 2) - 490 + GP.getCharSize() / 2;
                            if (move > 32) {
                                move = 32;
                            }
                            if (move < -32) {
                                move = -32;
                            }
                        } else {
                            move = GP.getGameP().getX() + GP.getTeki().getZahyou().x - 200 + GP.getCharSize() / 2;
                            if (move > 32) {
                                move = 32;
                            }
                            if (move < -32) {
                                move = -32;
                            }
                        }
                    } else {
                        if (GP.getTeki().getZahyou().x - GP.getMe().getZahyou().x < 980) {
                            move = GP.getGameP().getX() + (GP.getMe().getZahyou().x + (GP.getTeki().getZahyou().x - GP.getMe().getZahyou().x) / 2) - 490 + GP.getCharSize() / 2;
                            if (move > 32) {
                                move = 32;
                            }
                            if (move < -32) {
                                move = -32;
                            }
                        } else {
                            move = GP.getGameP().getX() + GP.getTeki().getZahyou().x - 200 + GP.getCharSize() / 2;
                            if (move > 32) {
                                move = 32;
                            }
                            if (move < -32) {
                                move = -32;
                            }
                        }
                    }
                }
                if (GP.getGameP().getX() - move > 0) {
                    GP.getGameP().setLocation(0, 0);
                } else if (GP.getGameP().getX() - move + GP.getMap().getWidth() < 980) {
                    GP.getGameP().setLocation(GP.getGameP().getX(), 0);
                } else {
                    GP.getGameP().setLocation(GP.getGameP().getX() - move, 0);
                }
                GP.getGameP().repaint();
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
