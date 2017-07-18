package brawlgame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.ImageIcon;

/**
 *
 * @author C0116309
 */
public class GameMap {

    public final int TILE_SIZE = 32;
    private int[][] map;
    private int row;
    private int col;
    private final int width;
    private final int height;
    private ImageIcon iconb;
    private ImageIcon icon;

    GamePanel GP;
    /**
     * コンストラクタ
     *
     * @param filename
     * @param panel
     */
    public GameMap(String filename, GamePanel panel) {
        GP = panel;
        load(filename);
        width = TILE_SIZE * col;
        height = TILE_SIZE * row;
        loadImage();
        panel.getGameP().setSize(iconb.getIconWidth(), iconb.getIconHeight());
        if (panel.getMe().getMode() == 'a') {
            panel.getMe().getZahyou().x = 950;
        } else {
            panel.getMe().getZahyou().x = 1350;
        }
    }

    public void reset(int mapno) {
        System.out.println(mapno);
        switch (mapno) {
            case 0:
                if (GP.getMe().getMode() == 'a') {
                    GP.getMe().getZahyou().x = 950;
                    GP.getMe().getZahyou().y = 380;
                } else {
                    GP.getMe().getZahyou().x = 1350;
                    GP.getMe().getZahyou().y = 380;
                }
                break;
            case 1:
                if (GP.getMe().getMode() == 'a') {
                    GP.getMe().getZahyou().x = 950;
                    GP.getMe().getZahyou().y = 380;
                } else {
                    GP.getMe().getZahyou().x = 1350;
                    GP.getMe().getZahyou().y = 380;
                }
                break;
            case 2:
                if (GP.getMe().getMode() == 'a') {
                    GP.getMe().getZahyou().x = 950;
                    GP.getMe().getZahyou().y = 380;
                } else {
                    GP.getMe().getZahyou().x = 1350;
                    GP.getMe().getZahyou().y = 380;
                }
                break;
            case 3:
                if (GP.getMe().getMode() == 'a') {
                    GP.getMe().getZahyou().x = 950;
                    GP.getMe().getZahyou().y = 380;
                } else {
                    GP.getMe().getZahyou().x = 1350;
                    GP.getMe().getZahyou().y = 380;
                }
                break;
            case 4:
                if (GP.getMe().getMode() == 'a') {
                    GP.getMe().getZahyou().x = 950;
                    GP.getMe().getZahyou().y = 380;
                } else {
                    GP.getMe().getZahyou().x = 1350;
                    GP.getMe().getZahyou().y = 380;
                }
                break;
            case 5:
                if (GP.getMe().getMode() == 'a') {
                    GP.getMe().getZahyou().x = 950;
                    GP.getMe().getZahyou().y = 380;
                } else {
                    GP.getMe().getZahyou().x = 1350;
                    GP.getMe().getZahyou().y = 380;
                }
                break;
        }

    }

    /**
     * 画像を読み込む
     */
    private void loadImage() {
        iconb = new ImageIcon("./src/img/back2.jpg");
        //ImageIcon iconb = new ImageIcon(new ImageIcon("./src/img/back2.jpg").getImage().getScaledInstance(2640, 1920, Image.SCALE_DEFAULT));
        icon = new ImageIcon(new ImageIcon(
                "./src/img/block.gif").
                getImage().getScaledInstance(TILE_SIZE, TILE_SIZE,
                        Image.SCALE_DEFAULT));
    }

    /**
     * ピクセル単位をタイル単位に変更する
     *
     * @param pixels ピクセル単位
     * @return タイル単位
     */
    public int pixelsToTiles(double pixels) {
        return (int) Math.floor(pixels / TILE_SIZE);
    }

    public int tilePixel(int tiles) {
        return tiles * TILE_SIZE;
    }

    /**
     * 描画
     *
     * @param g グラフィック
     */
    public void drow(Graphics g) {
        g.drawImage(iconb.getImage(), 0, 0, null);
        for (int i = 0; i < height / TILE_SIZE; i++) {
            for (int j = 0; j < width / TILE_SIZE; j++) {
                switch (map[i][j]) {
                    case 1://足場
                        g.drawImage(icon.getImage(), tilePixel(j), tilePixel(i), null);
                        break;
                }
            }
        }
    }

    /**
     * ファイル読み込み
     *
     * @param filename
     */
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
            System.err.println(ex);
        }

    }

    /**
     * 衝突判定
     *
     * @param player
     * @param newX
     * @param newY
     * @return
     */
    public Point getTileCollision(GameChara player, int newX, int newY) {
        int fromX = Math.min(player.getZahyou().x, newX);
        int fromY = Math.min(player.getZahyou().y, newY);
        int toX = Math.max(player.getZahyou().x, newX);
        int toY = Math.max(player.getZahyou().y, newY);

        int fromTileX = pixelsToTiles(fromX);
        int fromTileY = pixelsToTiles(fromY);
        int toTileX = pixelsToTiles(toX + 100 - 1);
        int toTileY = pixelsToTiles(toY + 100 - 1);

        // 衝突しているか調べる
        for (int x = fromTileX; x <= toTileX; x++) {
            for (int y = fromTileY; y <= toTileY; y++) {
                // 画面外は衝突
                if (x < 0 || x >= col) {
                    return new Point(x, y);
                }
                if (y < 0 || y >= row) {
                    return new Point(x, y);
                }
                // ブロックがあったら衝突
                if (map[y][x] == 1) {
                    return new Point(x, y);
                }
            }
        }

        return null;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    
}
