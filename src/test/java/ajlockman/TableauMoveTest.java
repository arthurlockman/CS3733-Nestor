package ajlockman;
import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.launcher.Main;

public class TableauMoveTest extends TestCase
{
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

    public void testSimpleMoveValidity()
    {
        Card theCard = nestor.tableau[0].get();
        TableauMove rctm =
                new TableauMove(nestor.tableau[0],
                        nestor.tableau[1], theCard, nestor.deck);
        assertEquals(true, rctm.valid(nestor));
    }

    public void testInvalidMoveValidity()
    {
        Card theCard = nestor.tableau[0].get();
        TableauMove rctm =
                new TableauMove(nestor.tableau[0],
                        nestor.tableau[4], theCard, nestor.deck);
        assertEquals(false, rctm.valid(nestor));
    }

    public void testValidMove()
    {
        int c1count = nestor.tableau[0].count();
        int c2count = nestor.tableau[1].count();
        Card theCard = nestor.tableau[0].get();
        TableauMove rctm =
                new TableauMove(nestor.tableau[0],
                        nestor.tableau[1], theCard, nestor.deck);
        assertEquals(true, rctm.doMove(nestor));
        assertEquals(c1count - 1, nestor.tableau[0].count());
        assertEquals(c2count - 1, nestor.tableau[1].count());
        assertEquals(2, nestor.getScore().getValue());
    }

    public void testValidMoveUndo()
    {
        int c1count = nestor.tableau[0].count();
        int c2count = nestor.tableau[1].count();
        Card theCard = nestor.tableau[0].get();
        TableauMove rctm =
                new TableauMove(nestor.tableau[0],
                        nestor.tableau[1], theCard, nestor.deck);
        assertEquals(true, rctm.doMove(nestor));
        assertEquals(c1count - 1, nestor.tableau[0].count());
        assertEquals(c2count - 1, nestor.tableau[1].count());
        assertEquals(2, nestor.getScore().getValue());

        assertEquals(true, rctm.undo(nestor));
        assertEquals(c1count, nestor.tableau[0].count());
        assertEquals(c2count, nestor.tableau[1].count());
    }

    public void testInvalidMove()
    {
        int c1count = nestor.tableau[0].count();
        int c2count = nestor.tableau[4].count();
        Card theCard = nestor.tableau[0].get();
        TableauMove rctm =
                new TableauMove(nestor.tableau[0],
                        nestor.tableau[4], theCard, nestor.deck);
        assertEquals(false, rctm.doMove(nestor));
        nestor.tableau[0].add(theCard);
        assertEquals(c1count, nestor.tableau[0].count());
        assertEquals(c2count, nestor.tableau[4].count());
        assertEquals(0, nestor.getScore().getValue());
    }
}
