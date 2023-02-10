package com.pqq;

import com.formdev.flatlaf.FlatLightLaf;
import com.pqq.gui.MainUi;

import javax.swing.*;

public class StartMain {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        try {
            UIManager.setLookAndFeel( new FlatLightLaf());
            UIManager.put( "Button.arc", 999 );
            UIManager.put( "Component.arc", 999 );
            UIManager.put( "ProgressBar.arc", 999 );
            UIManager.put( "TextComponent.arc", 999 );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        MainUi mainUi = new MainUi();
    }
}
