package ajlockman;

import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.launcher.Main;

public class ReserveMoveTest extends TestCase {
    Nestor nestor;
    GameWindow gw;

    /**
     * Set up the window before each test.
     */
    @Override
    protected void setUp()
    {
        nestor = new Nestor();
        gw = Main.generateWindow(nestor, Deck.OrderBySuit);
        gw.setVisible(true);

        gw.getGameContainer().forceNextHand();
    }

    /**
     * Destroy the window after each successful test.
     */
    @Override
    protected void tearDown()
    {
        gw.dispose();
    }

    /**
     * Test a move that moves cards from the reserve to the
     * tableau. This move should be valid.
     */
    public void testReserveToTableauMove()
    {
        int count1 = nestor.reserve.count() - 1;
        int count2 = nestor.tableau[5].count() - 1;
        Card theCard = nestor.reserve.get();
        ReserveMove rm = new ReserveMove(nestor.reserve,
                nestor.tableau[5], theCard, nestor.deck);
        assertEquals(true, rm.valid(nestor));
        assertEquals(true, rm.doMove(nestor));
        assertEquals(count1, nestor.reserve.count());
        assertEquals(count2, nestor.tableau[5].count());
    }

    /**
     * Test a move that moves cards from the tableau to the
     * reserve. This move should be valid.
     */
    public void testTableauToReserveMove()
    {
        int count1 = nestor.reserve.count() - 1;
        int count2 = nestor.tableau[5].count() - 1;
        Card theCard = nestor.tableau[5].get();
        ReserveMove rm = new ReserveMove(nestor.tableau[5],
                nestor.reserve, theCard, nestor.deck);
        assertEquals(true, rm.valid(nestor));
        assertEquals(true, rm.doMove(nestor));
        assertEquals(count1, nestor.reserve.count());
        assertEquals(count2, nestor.tableau[5].count());
    }

    /**
     * Test the undo functionality of a move from the
     * tableau to the reserve.
     */
    public void testTableauToReserveMoveUndo()
    {
        int count1 = nestor.reserve.count() - 1;
        int count2 = nestor.tableau[5].count() - 1;
        Card theCard = nestor.tableau[5].get();
        ReserveMove rm = new ReserveMove(nestor.tableau[5],
                nestor.reserve, theCard, nestor.deck);
        assertEquals(true, rm.valid(nestor));
        assertEquals(true, rm.doMove(nestor));
        assertEquals(count1, nestor.reserve.count());
        assertEquals(count2, nestor.tableau[5].count());

        assertEquals(true, rm.undo(nestor));
        assertEquals(count1 + 1, nestor.reserve.count());
        assertEquals(count2 + 1, nestor.tableau[5].count());
    }
    /**
     * Test the undo functionality of a move from the
     * reserve to the tableau.
     */
    public void testReserveToTableauMoveUndo()
    {
        int count1 = nestor.reserve.count() - 1;
        int count2 = nestor.tableau[5].count() - 1;
        Card theCard = nestor.reserve.get();
        ReserveMove rm = new ReserveMove(nestor.reserve,
                nestor.tableau[5], theCard, nestor.deck);
        assertEquals(true, rm.valid(nestor));
        assertEquals(true, rm.doMove(nestor));
        assertEquals(count1, nestor.reserve.count());
        assertEquals(count2, nestor.tableau[5].count());

        assertEquals(true, rm.undo(nestor));
        assertEquals(count1 + 1, nestor.reserve.count());
        assertEquals(count2 + 1, nestor.tableau[5].count());
    }

    /**
     * Test to make sure that an invalid move fails.
     */
    public void testInvalidMove()
    {
        int count1 = nestor.reserve.count();
        int count2 = nestor.tableau[6].count();
        Card theCard = nestor.tableau[6].get();
        ReserveMove rm = new ReserveMove(nestor.tableau[6],
                nestor.reserve, theCard, nestor.deck);
        assertEquals(false, rm.valid(nestor));
        assertEquals(false, rm.doMove(nestor));
        assertEquals(count1, nestor.reserve.count());
        assertEquals(count2, nestor.tableau[5].count());
    }

    /**
     * Tests the flip card move, flipping the top card
     * of the reserve buildablePile.
     */
    public void testFlipCardMove()
    {
        Card theCard = nestor.tableau[5].get();
        ReserveMove rm = new ReserveMove(nestor.tableau[5],
                nestor.reserve, theCard, nestor.deck);
        assertEquals(true, rm.valid(nestor));
        assertEquals(true, rm.doMove(nestor));
        assertEquals(false, nestor.reserve.faceUp());
        FlipCardMove fm = new FlipCardMove(nestor.reserve);
        assertEquals(true, fm.valid(nestor));
        assertEquals(true, fm.doMove(nestor));
        assertEquals(true, nestor.reserve.faceUp());

        FlipCardMove fm2 = new FlipCardMove(nestor.reserve);
        assertEquals(false, fm2.valid(nestor));
        assertEquals(false, fm2.doMove(nestor));
    }

    /**
     * Tests the undo functionality of the flip card move.
     */
    public void testFlipCardMoveUndo()
    {
        Card theCard = nestor.tableau[5].get();
        ReserveMove rm = new ReserveMove(nestor.tableau[5],
                nestor.reserve, theCard, nestor.deck);
        assertEquals(true, rm.valid(nestor));
        assertEquals(true, rm.doMove(nestor));
        assertEquals(false, nestor.reserve.faceUp());
        FlipCardMove fm = new FlipCardMove(nestor.reserve);
        assertEquals(true, fm.valid(nestor));
        assertEquals(true, fm.doMove(nestor));
        assertEquals(true, nestor.reserve.faceUp());

        assertEquals(true, fm.undo(nestor));
        assertEquals(false, nestor.reserve.faceUp());
    }
}
