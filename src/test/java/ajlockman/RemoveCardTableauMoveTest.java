package ajlockman;
import junit.framework.TestCase;
import ks.client.gamefactory.GameWindow;
import ks.common.model.Deck;
import ks.launcher.Main;

public class RemoveCardTableauMoveTest extends TestCase
{
    public void testSimpleMove()
    {
        Nestor nestor = new Nestor();
        GameWindow gw = Main.generateWindow(new Nestor(), Deck.OrderByRank);

        RemoveCardTableauMove rctm =
                new RemoveCardTableauMove(nestor.tableau[0],
                        nestor.tableau[1], nestor.deck);
        assertEquals(true, rctm.valid(nestor));
    }
}
