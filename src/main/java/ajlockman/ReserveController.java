package ajlockman;

import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.model.*;
import ks.common.view.BuildablePileView;
import ks.common.view.CardView;
import ks.common.view.Container;
import ks.common.view.Widget;

import java.awt.event.MouseEvent;

public class ReserveController extends SolitaireReleasedAdapter
{
    protected BuildablePileView pile;
    protected Deck d;
    /**
     * SolitaireReleasedAdapter constructor comment.
     *
     * @param theGame game under play.
     */
    public ReserveController(Solitaire theGame, BuildablePileView pile, Deck deck)
    {
        super(theGame);
        this.pile = pile;
        this.d = deck;
    }

    public void mousePressed(MouseEvent me)
    {

    }

    public void mouseReleased(MouseEvent me)
    {

    }
}
