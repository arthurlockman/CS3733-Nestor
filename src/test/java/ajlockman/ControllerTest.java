package ajlockman;

import ks.client.gamefactory.GameWindow;
import ks.common.model.Card;
import ks.common.model.Deck;
import ks.launcher.Main;

import java.awt.event.MouseEvent;

public class ControllerTest extends KSTestCase
{
    Nestor nestor;
    GameWindow gw;

    @Override
    public void setUp()
    {
        nestor = new Nestor();
        gw = Main.generateWindow(nestor, Deck.OrderBySuit);
        gw.setVisible(true);

        gw.getGameContainer().forceNextHand();
    }

    @Override
    public void tearDown()
    {
        gw.dispose();
    }

    public void testReserveToTableauMove()
    {
        MouseEvent pr = createPressed(nestor, nestor.reserveView, 20, 50);
        nestor.reserveView.getMouseManager().handleMouseEvent(pr);

        MouseEvent rel = createReleased(nestor, nestor.tableauViews[5], 0, 0);
        nestor.tableauViews[5].getMouseManager().handleMouseEvent(rel);

        assertFalse(nestor.reserve.faceUp());
        assertEquals(new Card(Card.KING, Card.DIAMONDS), nestor.tableau[5].peek());

        nestor.reserveView.getMouseManager().handleMouseEvent(pr);
        assertTrue(nestor.reserve.faceUp());
    }

    public void testTableauToReserveMove()
    {
        MouseEvent pr = createPressed(nestor, nestor.tableauViews[5], 20, 200);
        nestor.tableauViews[5].getMouseManager().handleMouseEvent(pr);

        MouseEvent rel = createReleased(nestor, nestor.reserveView, 0, 0);
        nestor.reserveView.getMouseManager().handleMouseEvent(rel);

        assertFalse(nestor.reserve.faceUp());
        assertEquals(new Card(Card.KING, Card.DIAMONDS), nestor.tableau[5].peek());

        nestor.reserveView.getMouseManager().handleMouseEvent(pr);
        assertTrue(nestor.reserve.faceUp());
    }

    public void testBrokenTableauToReserveMove()
    {
        MouseEvent pr = createPressed(nestor, nestor.tableauViews[5], 0, 0);
        nestor.tableauViews[5].getMouseManager().handleMouseEvent(pr);

        try
        {
            MouseEvent rel = createReleased(nestor, nestor.reserveView, 0, 0);
            nestor.reserveView.getMouseManager().handleMouseEvent(rel);
        } catch (Error e)
        {
            if (!e.getMessage().contains("nothing being dragged"))
                fail("Found dragging object when there shouldn't have been.");
        }
    }

    public void testBrokenReserveToTableauMove()
    {
        MouseEvent pr = createPressed(nestor, nestor.reserveView, 0, 0);
        nestor.reserveView.getMouseManager().handleMouseEvent(pr);

        try
        {
            MouseEvent rel = createReleased(nestor, nestor.tableauViews[5], 0, 0);
            nestor.tableauViews[5].getMouseManager().handleMouseEvent(rel);
        } catch (Error e)
        {
            if (!e.getMessage().contains("nothing being dragged"))
                fail("Found dragging object when there shouldn't have been.");
        }
    }

    public void testTableauToTableauMove()
    {
        MouseEvent pr = createPressed(nestor, nestor.tableauViews[0], 20, 200);
        nestor.tableauViews[0].getMouseManager().handleMouseEvent(pr);

        MouseEvent rel = createReleased(nestor, nestor.tableauViews[1], 0, 0);
        nestor.tableauViews[1].getMouseManager().handleMouseEvent(rel);

        assertEquals(new Card(Card.KING, Card.SPADES), nestor.tableau[0].peek());
        assertEquals(new Card(Card.SIX, Card.SPADES), nestor.tableau[1].peek());
    }

    public void testBrokenTableauToTableauMove()
    {
        MouseEvent pr = createPressed(nestor, nestor.tableauViews[0], 0, 0);
        nestor.tableauViews[0].getMouseManager().handleMouseEvent(pr);

        try
        {
            MouseEvent rel = createReleased(nestor, nestor.tableauViews[1], 0, 0);
            nestor.tableauViews[1].getMouseManager().handleMouseEvent(rel);
        } catch (Error e)
        {
            if (!e.getMessage().contains("nothing being dragged"))
                fail("Found dragging object when there shouldn't have been.");
        }
    }
}
