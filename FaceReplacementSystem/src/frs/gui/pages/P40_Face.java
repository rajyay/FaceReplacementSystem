/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package frs.gui.pages;

import frs.gui.components.IOSUICheckBox;
import frs.gui.components.IOSUIImageView;
import frs.gui.components.IOSUIRadioButton;
import frs.gui.components.ResizableRectangleView;
import frs.helpers.GeometricTransformation;
import frs.helpers.SnakeClass;
import frs.helpers.SnakeInitializer;
import frs.main.RFApplication;
import hu.droidzone.iosui.IOSUIButton;
import hu.droidzone.iosui.IOSUILabel;
import hu.droidzone.iosui.IOSUITextField;
import hu.droidzone.iosui.IOSUIView;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;

/**
 *
 * @author Robik Shrestha
 */
public class P40_Face extends RFPage {

    protected ResizableRectangleView rrv = new ResizableRectangleView("500px", "400px");
    protected IOSUIView pnlView;
    protected IOSUIView optionView;
    protected IOSUIRadioButton skinRB, gaRB, snakeRB;
    protected IOSUILabel skinLbl, gaLbl, snakeLbl;
    protected ButtonGroup btnGroup;
    //components for snakeView
    protected IOSUIView snakeView;
    protected IOSUILabel snakeThresholdLbl, snakeIterationLbl;
    protected IOSUITextField snakeThresholdTF, snakeIterationTF;
    //components for curveView
    protected IOSUIView curveView;
    protected IOSUILabel curveLbl;
    protected IOSUIView checkBoxView;
    protected IOSUICheckBox chinCB, leftCurveCB, rightCurveCB;
    protected IOSUILabel chinLbl, leftCurveLbl, rightCurveLbl;
    //Buttons
    protected IOSUIButton extractFaceBtn, resetBtn;
    public static int SOURCE = 1, TARGET = 2;
    protected int identity = SOURCE;

    public P40_Face(final RFApplication app, int identity) {
        super(app, "Face Extraction");
        this.identity = identity;
        initComponents();
    }

    public void initComponents() {
        mainView = new IOSUIView("500px,10px,240px", "400px");
        if (this.identity == SOURCE) {
            initSrc();
        } else {
            initTar();
        }

        mainView.addXY(rrv, 1, 1, "f,f");
        mainView.addXY(rrv, 1, 1);
        pnlView = new IOSUIView("240px", "165px,10px,140px,10px,30px,10px,30px");
        initOptionView();
        initCurveView();
        initBtnView();
        mainView.addXY(pnlView, 3, 1);
        addXY(mainView, 1, 1);
    }

    public void initSrc() {
        frs.rotateSource();
        //frs.drawFPOnRotatedImage();
        rrv.setImage(frs.getRotatedSrcImg());
        rrv.initializeRectangle(GeometricTransformation.getRectangleUsingFP(frs.getRotatedSrcFP()));
    }

    public void initTar() {
        rrv.setImage(frs.getTargetImage());
        rrv.initializeRectangle(GeometricTransformation.getRectangleUsingFP(frs.getTarFP()));
    }

    public void initOptionView() {
        optionView = new IOSUIView("20px,10px,210px", "30px,3px,30px,2px,30px,65px,5px");

        skinRB = new IOSUIRadioButton();
        skinRB.getRadio().setSelected(true);
        gaRB = new IOSUIRadioButton();
        snakeRB = new IOSUIRadioButton();
        //Add all radio buttons to a group
        btnGroup = new ButtonGroup();
        btnGroup.add(skinRB.getRadio());
        btnGroup.add(gaRB.getRadio());
        btnGroup.add(snakeRB.getRadio());
        //Add the buttons to the view
        optionView.addXY(skinRB, 1, 1);
        //optionView.addXY(gaRB, 1, 3);
        optionView.addXY(snakeRB, 1, 5);
        initRBLbl();
        initSnakeView();
        pnlView.addXY(optionView, 1, 1);
    }

    public void initRBLbl() {
        //initialize labels
        skinLbl = new IOSUILabel("Skin Color Thresholds");
        skinLbl.setForeground(Color.white);
        gaLbl = new IOSUILabel("Genetic Algorithm");
        gaLbl.setForeground(Color.white);
        snakeLbl = new IOSUILabel("Snake Algorithm");
        snakeLbl.setForeground(Color.white);
        optionView.addXY(skinLbl, 3, 1);
        //optionView.addXY(gaLbl, 3, 3);
        optionView.addXY(snakeLbl, 3, 5);
    }

    public void initSnakeView() {
        snakeView = new IOSUIView("180px,10px,50px", "30px,5px,30px");
        snakeThresholdLbl = new IOSUILabel("Threshold");
        snakeThresholdLbl.setForeground(Color.white);
        snakeIterationLbl = new IOSUILabel("Iterations");
        snakeIterationLbl.setForeground(Color.white);
        IOSUIView snakeThresholdBack = new IOSUIView("180px", "30px");
        snakeThresholdBack.setBackground(Color.white);
        snakeThresholdTF = new IOSUITextField(false);
        snakeThresholdTF.setText("3");
        snakeThresholdBack.addXY(snakeThresholdTF, 1, 1);
        snakeIterationTF = new IOSUITextField(false);
        snakeIterationTF.setText("5");
        IOSUIView snakeIterationBack = new IOSUIView("180px", "30px");
        snakeIterationBack.setBackground(Color.white);
        snakeIterationBack.addXY(snakeIterationTF, 1, 1);
        snakeView.addXY(snakeThresholdLbl, 1, 1);
        snakeView.addXY(snakeIterationLbl, 1, 3);
        snakeView.addXY(snakeThresholdBack, 3, 1);
        snakeView.addXY(snakeIterationBack, 3, 3);
        optionView.addXYW(snakeView, 1, 6, 3);
    }

    public void initCurveView() {
        curveView = new IOSUIView("240px", "30px,5px,100px");
        curveLbl = new IOSUILabel("Apply curve bounds");
        curveLbl.setForeground(Color.white);
        curveView.addXY(curveLbl, 1, 1);
        initCheckBoxView();
        //pnlView.addXY(curveView, 1, 3);
    }

    public void initCheckBoxView() {
        checkBoxView = new IOSUIView("60px,5px,175px", "30px,5px,30px,5px,30px");
        chinCB = new IOSUICheckBox();
        chinCB.getCheckBox().setSelected(true);
        leftCurveCB = new IOSUICheckBox();
        leftCurveCB.getCheckBox().setSelected(true);
        rightCurveCB = new IOSUICheckBox();
        rightCurveCB.getCheckBox().setSelected(true);
        chinLbl = new IOSUILabel("Chin Curve");
        chinLbl.setForeground(Color.white);
        leftCurveLbl = new IOSUILabel("Left Curve");
        leftCurveLbl.setForeground(Color.white);
        rightCurveLbl = new IOSUILabel("Right Curve");
        rightCurveLbl.setForeground(Color.white);
        checkBoxView.addXY(chinCB, 1, 1);
        checkBoxView.addXY(leftCurveCB, 1, 3);
        checkBoxView.addXY(rightCurveCB, 1, 5);
        checkBoxView.addXY(chinLbl, 3, 1);
        checkBoxView.addXY(leftCurveLbl, 3, 3);
        checkBoxView.addXY(rightCurveLbl, 3, 5);
        curveView.addXY(checkBoxView, 1, 3);
    }

    public void initBtnView() {
        initExtractFaceBtn();
        initResetBtn();

        pnlView.addXY(extractFaceBtn, 1, 5);
        //pnlView.addXY(resetBtn, 1, 7);
    }

    public void initExtractFaceBtn() {
        extractFaceBtn = new IOSUIButton(new AbstractAction("Extract Face") {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (chinCB.getCheckBox().isSelected()) {
                    frs.setFlagForCurve(true);
                } else {
                    frs.setFlagForCurve(false);
                }
                setRect();
                if (skinRB.getRadio().isSelected()) {
                    findFaceUsingThresholds();
                } else if (snakeRB.getRadio().isSelected()) {//add the code of snake here

                    findFaceUsingSnake();
                }
                if (identity == SOURCE) {
                    frs.removeSrcHairFromFace();
                    frs.findSourceBoundaryFilledImage();
                    rrv.setImage(frs.getSourceBoundaryFilledImage());
                } else {
                    frs.removeTarHairFromFace();
                    rrv.setImage(frs.getTargetSkinImage());
                }
            }
        });

    }

    public void initResetBtn() {
        resetBtn = new IOSUIButton(new AbstractAction("Reset") {

            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
    }

    @Override
    public void goNext() {
        if (identity == SOURCE) {
            pc.navigateTo(new P20_FP(app, P20_FP.TARGET));
        } else {
            frs.warp(3);
            pc.navigateTo(new P80_Replace(app));
        }
    }

    //calls the functions of frs to apply skin color thresholds to extract the face region
    public void findFaceUsingThresholds() {
        if (this.identity == SOURCE) {
            frs.detectSourceSkin();
            //frs.shrinkSource();
            //frs.growSource();
            frs.findSourceCurves();
            frs.useSrcCurves();
            frs.findSourceBoundaryFilledMatrix();
        } else {
            frs.detectTargetSkin();
        }
    }

    public void findFaceUsingSnake() {
        if (identity == SOURCE) {
            int threshold = Integer.parseInt(snakeThresholdTF.getText());
            int iteration = Integer.parseInt(snakeIterationTF.getText());
            frs.applySnakeToSrc(threshold, iteration);
            frs.findSourceCurves();
            frs.useSrcCurves();
            frs.findSourceBoundaryFilledMatrix();
        }
        //frs.findSourceBoundaryFilledImage();
    }

    public void setRect() {
        Rectangle r = rrv.getRectangle();
        Rectangle resized = new Rectangle();
        int stopx = r.x + r.width;
        int stopy = r.y + r.height;
        Point a = rrv.toActualImagePoint(new Point(r.x, r.y));
        Point b = rrv.toActualImagePoint(new Point(stopx, stopy));
        resized.x = a.x;
        resized.y = a.y;
        resized.width = b.x - a.x;
        resized.height = b.y - a.y;
        if (this.identity == SOURCE) {
            setSrcRect(resized);
        } else {
            setTarRect(resized);
        }

    }

    public void setSrcRect(Rectangle resized) {
        frs.setSourceFaceRectangle(resized);
        frs.findSourceRectImage();
        frs.findSourceRectFP();
    }

    public void setTarRect(Rectangle resized) {
        frs.setTarFaceRect(resized);
        frs.findTargetRectImage();
        frs.findTargetRectFP();
    }
}
