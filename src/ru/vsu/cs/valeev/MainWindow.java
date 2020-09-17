package ru.vsu.cs.valeev;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private DrawPanel dp;
    private JButton buttonBr;
    private JButton buttonDDA;
    private JButton buttonWu;

    public MainWindow() throws HeadlessException {
        this.dp = new DrawPanel();
        this.add(dp);
    }
}
