package ajlockman;
import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.model.Deck;
import ks.launcher.Main;

public class RemoveCardTableauMoveTest extends TestCase
{
    public void testSimpleMoveValidity()
    {
        Nestor nestor = new Nestor();
        GameWindow gw = Main.generateWindow(nestor, Deck.OrderByRank);
        gw.setVisible(true);

        RemoveCardTableauMove rctm =
                new RemoveCardTableauMove(nestor.tableau[0],
                        nestor.tableau[1], nestor.deck);
        assertEquals(true, rctm.valid(nestor));
    }

    public void testInvalidMoveValidity()
    {
        Nestor nestor = new Nestor();
        GameWindow gw = Main.generateWindow(nestor, Deck.OrderByRank);
        gw.setVisible(true);

        RemoveCardTableauMove rctm =
                new RemoveCardTableauMove(nestor.tableau[0],
                        nestor.tableau[4], nestor.deck);
        assertEquals(false, rctm.valid(nestor));
    }

    public void testValidMove()
    {
        Nestor nestor = new Nestor();
        GameWindow gw = Main.generateWindow(nestor, Deck.OrderByRank);
        gw.setVisible(true);

        int c1count = nestor.tableau[0].count();
        int c2count = nestor.tableau[1].count();
        RemoveCardTableauMove rctm =
                new RemoveCardTableauMove(nestor.tableau[0],
                        nestor.tableau[1], nestor.deck);
        assertEquals(true, rctm.doMove(nestor));
        assertEquals(c1count - 1, nestor.tableau[0].count());
        assertEquals(c2count - 1, nestor.tableau[1].count());
        assertEquals(2, nestor.getScore().getValue());
    }

    public void testInvalidMove()
    {
        Nestor nestor = new Nestor();
        GameWindow gw = Main.generateWindow(nestor, Deck.OrderByRank);
        gw.setVisible(true);

        int c1count = nestor.tableau[0].count();
        int c2count = nestor.tableau[4].count();
        RemoveCardTableauMove rctm =
                new RemoveCardTableauMove(nestor.tableau[0],
                        nestor.tableau[4], nestor.deck);
        assertEquals(false, rctm.doMove(nestor));
        assertEquals(c1count, nestor.tableau[0].count());
        assertEquals(c2count, nestor.tableau[4].count());
        assertEquals(0, nestor.getScore().getValue());
    }
}
