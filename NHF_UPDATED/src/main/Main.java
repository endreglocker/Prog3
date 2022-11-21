package main;

import game.*;
import menu.CharacterCreation;
import menu.Menu;

public class Main {
    public static void main(String[] args) {
        //new GameFrame().startGame();
        //new Menu().menu_init();

        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(CharacterCreation::createAndShowGUI);
    }
}