package ru.vsu.cs.valeev;


public class Main {

    public static void main(String[] args) {
        MainWindow mw = new MainWindow();
        mw.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        mw.setSize(800, 600);
        mw.setVisible(true);
        System.out.println(/*1.5 * Math.PI / */Math.atan2(0, 100) * 180 / Math.PI);
//        System.out.println();
    }
}
