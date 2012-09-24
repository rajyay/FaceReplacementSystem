/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.helpers;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author Dell
 */
public class MatrixAndImage {

    public static int[][] imageToBinaryMatrix(BufferedImage image) {
        int binaryMatrix[][];
        int w = image.getWidth();
        int h = image.getHeight();
        binaryMatrix = new int[w][h];
        for (int x = 0; x < w; x++) {
            binaryMatrix[x] = new int[h];
            for (int y = 0; y < h; y++) {
                Color c = new Color(image.getRGB(x, y), true);
                if (c.getAlpha() < 255) {
                    continue;
                }
                binaryMatrix[x][y] = 1;
            }
        }
        return binaryMatrix;
    }

    //All the pixels marked "0" are made transparent
    public static BufferedImage matrixToImage(BufferedImage image, int[][] matrix) {
        BufferedImage binaryImage = DeepCopier.getBufferedImage(image, BufferedImage.TYPE_INT_ARGB);
        Color c;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (matrix[x][y] <= 0) {//If the pixel value is less than or equal to 0 then make it transparent
                    c = new Color(image.getRGB(x, y), true);
                    binaryImage.setRGB(x, y, ColorModelConverter.getTransparentColor(c).getRGB());
                } 
            }
        }
        return binaryImage;
    }

    //All the matrix cells valued 1 or more are highlighted with the color
    public static BufferedImage getHighlightedImage(BufferedImage image, int[][] matrix, Color c) {
        BufferedImage highlightedImg = DeepCopier.getBufferedImage(image, image.getType());
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (matrix[x][y] >= 1) {
                    highlightedImg.setRGB(x, y, c.getRGB());
                }
            }
        }
        return highlightedImg;
    }
}