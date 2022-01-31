package AutonomousSteering;

import java.awt.*;

import javax.swing.*;

public class MyFrame extends JFrame{

    MyPanel panel;

    MyFrame(){

        panel = new MyPanel();
        this.addMouseListener(new MouseInput());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }
}