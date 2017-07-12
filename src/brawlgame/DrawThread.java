package brawlgame;

import java.awt.Graphics;

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
