package menu;

import javax.swing.*;
import java.awt.*;

public class ScoreBoard {
    JPanel scores = new JPanel();
    JPanel back_panel = new JPanel();
    JFrame frame = new JFrame();
    int window_height;
    int window_width;

    public ScoreBoard(int h, int w) {
        window_height = h;
        window_width = w;

        scores();
    }

    public void scores() {
        frame.setVisible(true);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(window_width, window_height));
        frame.getContentPane().setBackground(new Color(0x333333));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        table_sclresheet();
        back_button();
    }

    public void table_sclresheet() {

        /* DATA INTO SCORES */

        final int SCORETABLE_WIDTH = 300;
        final int SCORETABLE_HEIGHT = 400;
        int x = window_width / 2 - SCORETABLE_WIDTH / 2;
        int y = 0;

        scores.setBounds(x, y, SCORETABLE_WIDTH, SCORETABLE_HEIGHT);
        scores.setBackground(Color.RED);
        frame.add(scores);
    }

    public void back_button() {
        JButton back = new JButton("Back");
        final int BUTTON_HEIGHT = 150;
        final int BUTTON_WIDTH = 150;
        int y_back = window_height - BUTTON_HEIGHT;
        int x_back = window_width / 2 - BUTTON_WIDTH / 2;

        back_panel.setLayout(new GridLayout(1, 1));
        back_panel.add(back);
        back_panel.setBounds(x_back, y_back, BUTTON_WIDTH, (BUTTON_HEIGHT / 2));
        frame.add(back_panel);

        back.addActionListener(b -> {
            frame.dispose();
            new Menu().menu_init();
        });
    }
}
