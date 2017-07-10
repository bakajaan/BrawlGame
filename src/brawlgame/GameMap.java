/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brawlgame;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author C0116309
 */
public class GameMap {

    private int[][] map;
    private int row;
    private int col;
    private int width;
    private int height;

    public GameMap(String filename) {
        load(filename);
    }

    public void drow(Graphics g) {

    }

    private void load(String filename) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("./src/map/" + filename)));

            String line;
            //行の読み込み
            line = br.readLine();
            row = Integer.parseInt(line);
            //列の読み込み    
            line = br.readLine();
            col = Integer.parseInt(line);
            //マップロード
            map = new int[row][col];
            for (int i = 0; i < row; i++) {
                line = br.readLine();
                for (int j = 0; j < col; j++) {
                    map[i][j] = Integer.parseInt(line.charAt(j) + "");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * マップの高さを所得する
     *
     * @return マップの高さを返す
     */
    private int getHeight() {
        return height;
    }

    /**
     * マップの横幅を所得する
     *
     * @return マップの横幅を返す
     */
    private int getWidth() {
        return width;
    }
}
