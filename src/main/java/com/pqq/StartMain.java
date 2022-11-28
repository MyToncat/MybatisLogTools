package com.pqq;

import com.pqq.gui.MainUi;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import org.jb2011.lnf.beautyeye.ch3_button.BEButtonUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;

public class StartMain {
    public static void main(String[] args) {
        try
        {

            UIManager.put("RootPane.setupButtonVisible",false);
            //设置本属性将改变窗口边框样式定义
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencyAppleLike;
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();

            UIManager.put("ToolBar.isPaintPlainBackground", Boolean.TRUE);
            Border bd = new org.jb2011.lnf.beautyeye.ch8_toolbar.BEToolBarUI.ToolBarBorder(
                    UIManager.getColor("ToolBar.shadow")     //Floatable时触点的颜色
                    , UIManager.getColor("ToolBar.highlight")//Floatable时触点的阴影颜色
                    , new Insets(6, 0, 11, 0));              //border的默认insets
            UIManager.put("ToolBar.border",new BorderUIResource(bd));



            UIManager.put("TabbedPane.tabAreaInsets"
                    , new javax.swing.plaf.InsetsUIResource(3,3,2,20));
        }
        catch(Exception e)
        {
            //TODO exception
        }

        MainUi mainUi = new MainUi();
    }
}
