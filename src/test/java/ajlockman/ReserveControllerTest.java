package ajlockman;

import ks.client.gamefactory.GameWindow;
import ks.common.model.Deck;
import ks.launcher.Main;

public class ReserveControllerTest extends KSTestCase
{
    Nestor nestor;
    GameWindow gw;

    @Override
    public void setUp()
    {
        nestor = new Nestor();
        gw = Main.generateWindow(nestor, Deck.OrderByRank);
        gw.setVisible(true);
    }

    @Override
    public void tearDown()
    {
        gw.dispose();
    }

    public void testReserveToTableauMove()
    {

    }

    public void testTableauToReserveMove()
    {

    }

    public void testFlipCardMove()
    {

    }

    public void testInvalidFlipCardMove()
    {

    }

}
