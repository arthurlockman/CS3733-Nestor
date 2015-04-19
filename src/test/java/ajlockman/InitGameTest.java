package ajlockman;

import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.model.Deck;
import ks.launcher.Main;

public class InitGameTest extends TestCase
{
    /**
     * Test the game initialization method, to make sure that
     * the correct number of cards is in each tableau column
     * and the reserve when a game is created.
     */
    public void testGameInit()
    {
        Nestor nestor = new Nestor();
        GameWindow gw = Main.generateWindow(nestor, Deck.OrderByRank);
        gw.setVisible(true);

        assertEquals("ajlockman-Nestor", nestor.getName());
        assertEquals(0, nestor.getNumLeft().getValue());
        assertEquals(0, nestor.getScore().getValue());
        for (int i = 0; i < 8; i++)
            assertEquals(6, nestor.tableau[i].count());
        assertEquals(4, nestor.reserve.count());
        assertEquals(false, nestor.hasWon());
    }
}
