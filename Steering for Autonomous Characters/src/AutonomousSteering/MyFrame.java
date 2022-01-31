package AutonomousSteering;

import javax.swing.*;

public class MyFrame extends JFrame{

    private static int frameX;
    private static int frameY;


    MyPanel panel;

    MyFrame(){

        panel = new MyPanel();
        this.addMouseListener(new MouseInput());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        frameX = this.getLocation().x;
        frameY = this.getLocation().y;

        Constants.INSETS_TOP = this.getInsets().top;
        Constants.INSETS_BOTTOM = this.getInsets().bottom;
        Constants.INSETS_SIDES = this.getInsets().right;

    }

    public static int getFrameX() {
        return frameX;
    }

    public static int getFrameY() {
        return frameY;
    }
}