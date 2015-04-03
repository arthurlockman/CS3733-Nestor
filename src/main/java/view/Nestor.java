package view;

import ks.client.gamefactory.GameWindow;
import ks.common.games.Solitaire;
import ks.common.model.Deck;
import ks.common.model.Pile;
import ks.common.view.*;
import ks.launcher.Main;

public class Nestor extends Solitaire
{
    @Override
    public String getName()
    {
        return null;
    }

    @Override
    public boolean hasWon()
    {
        return false;
    }

    /**
     * Initialize the solitaire variation.
     */
    @Override
    public void initialize()
    {
        initializeModel(getSeed());
        initializeView();
        initializeController();
    }

    void initializeController()
    {
    }

    void initializeModel(int seed)
    {
    }

    void initializeView()
    {
    }

    public static void main (String []args)
    {
        GameWindow gw = Main.generateWindow(new Nestor(), 100);
        gw.setVisible(true);
    }
}

