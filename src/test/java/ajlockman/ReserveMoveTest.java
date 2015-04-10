package ajlockman;

import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.model.Deck;
import ks.launcher.Main;

public class ReserveMoveTest extends TestCase {
    Nestor nestor;
    GameWindow gw;

    @Override
    protected void setUp()
    {
        nestor = new Nestor();
        gw = Main.generateWindow(nestor, Deck.OrderByRank);
        gw.setVisible(true);
    }

    @Override
    protected void tearDown()
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
}
