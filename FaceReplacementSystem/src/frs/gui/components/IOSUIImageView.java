/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.gui.components;

import frs.helpers.DeepCopier;
import hu.droidzone.iosui.IOSUIView;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 *
 * @author Dell
 */
public class IOSUIImageView extends IOSUIView {

    protected BufferedImage image;
    protected int w, h;
    protected int imageW, imageH;
    protected int newImageW, newImageH;
    float aspectRatio;

    public IOSUIImageView(String colSpec, String rowSpec) {
        super(colSpec, rowSpec);

    }

    public IOSUIImageView(int col, int row) {
        super(col, row);
    }

    public void setImage(BufferedImage image) {
        this.image = DeepCopier.getBufferedImage(image, image.getType());
        imageW = image.getWidth();
        imageH = image.getHeight();
        aspectRatio = (float) imageW / imageH;
        adjustImageSize();
        //repaint();
    }

    //Resize Image according to the aspect ratio
    public void adjustImageSize() {
        w = (int) this.getPreferredSize().getWidth();
        h = (int) this.getPreferredSize().getHeight();
        newImageW = w;
        newImageH = (int) (newImageW / aspectRatio);
        if (newImageH > h) {
            newImageH = h;
            newImageW = (int) (aspectRatio * newImageH);
        }
    }

    //The image which is drawn has different width from the actual image
    //The coordinates should be taken with respect to actual image, not with the drawn image
    public Point toActualImagePoint(Point point) {
        int x, y;
        x = (int) (image.getWidth() * (float) point.x / newImageW);
        y = (int) (image.getHeight() * (float) point.y / newImageH);
        return new Point(x, y);
    }

    //This function converts a coordinate into the coordinate which would be drawn into this View
    public Point toDrawnImagePoint(Point point) {
        int x, y;
        x = (int) (point.x * (float) newImageW / image.getWidth());
        y = (int) (point.y * (float) newImageH / image.getHeight());
        return new Point(x, y);
    }

//    public void setWH(int width,int height){
//        this.w=width;
//        this.h=height;
//    }
//    @Override
//    public Dimension getPreferredSize(){
//        return new Dimension(w,h);
//   }
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, (int) this.getPreferredSize().getWidth(), (int) this.getPreferredSize().getHeight());
        if (image != null) {
            g.drawImage(image, 0, 0, newImageW, newImageH, null);
        }
    }
}
