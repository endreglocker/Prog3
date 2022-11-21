package menu;

import javax.swing.*;
import java.awt.*;

public class Menu {
    JFrame f = new JFrame("Asteroids");
    final int WINDOW_WIDTH = 900;
    final int WINDOW_HEIGHT = 600;

    public void menu_init() {
        f.setVisible(true);
        f.setLayout(null);
        f.setResizable(false);
        f.getContentPane().setBackground(new Color(0x333333));
        //f.setSize(WINDOW_WIDTH, WINDOW_HEIGHT); // either this, or setPreferredSize() with pack()
        f.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
        f.add(menu_panel());
        f.pack();
    }

    JPanel menu_panel() {
        JPanel p = new JPanel();
        final int PANEL_WIDTH = 150;
        final int PANEL_HEIGHT = 300;

        int x = WINDOW_WIDTH / 2 - PANEL_WIDTH / 2;
        int y = WINDOW_HEIGHT / 2 - PANEL_HEIGHT / 2;

        p.setBounds(x, y, PANEL_WIDTH, PANEL_HEIGHT);

        p.setLayout(new GridLayout(4, 1));

        setButtons(p);
        return p;
    }

    void setButtons (JPanel p){
        JButton e = new JButton("Exit");
        JButton s = new JButton("ScoreBoard");
        JButton n = new JButton("New Game");
        JButton c = new JButton("Continue");


        p.add(c);
        p.add(n);
        p.add(s);
        p.add(e);

        e.addActionListener(exit -> f.dispose());
        s.addActionListener(exit -> {f.dispose();  new ScoreBoard(WINDOW_HEIGHT, WINDOW_WIDTH);});

    }
}
