/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package brawlgame;

import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author C0116309
 */
public class GameMap {

    public static final int TILE_SIZE = 32;

    private int[][] map;
    private int row;
    private int col;
    private int width;
    private int height;

    private Image blockImage;
    private Image background;

    public GameMap(String filename) {
        load(filename);
        width = TILE_SIZE * col;
        height = TILE_SIZE * row;
        loadImage();
    }

    /**
     * 画像を読み込む
     */
    private void loadImage() {
        ImageIcon iconb = new ImageIcon(new ImageIcon("./src/img/paper.png").getImage().getScaledInstance(2400, 3304, Image.SCALE_DEFAULT));
        background = iconb.getImage();
        ImageIcon icon = new ImageIcon(new ImageIcon(
                "./src/img/block.gif").
                getImage().getScaledInstance(TILE_SIZE, TILE_SIZE,
                        Image.SCALE_DEFAULT));
        blockImage = icon.getImage();
    }

    public static int tilePixel(int tiles) {
        return tiles * TILE_SIZE;
    }

    /**
     *
     * @param g グラフィック
     */
    public void drow(Graphics g) {

        g.drawImage(background, 0, 0, null);
        for (int i = 0; i < height / TILE_SIZE; i++) {
            for (int j = 0; j < width / TILE_SIZE; j++) {
                switch (map[i][j]) {
                    case 1://足場
                        g.drawImage(blockImage, tilePixel(j), tilePixel(i), null);
                        break;
                }
            }
        }
    }

    private void load(String filename) {
        try {
            File file = new File("./src/map/" + filename);
            BufferedReader br2 = new BufferedReader(new FileReader(file));

            String line;
            //行の読み込み
            line = br2.readLine();
            row = Integer.parseInt(line);
            //列の読み込み
            line = br2.readLine();
            col = Integer.parseInt(line);
            //マップロード
            map = new int[row][col];
            for (int i = 0; i < row; i++) {
                line = br2.readLine();
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
