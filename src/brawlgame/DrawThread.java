/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
        }
    }
}
