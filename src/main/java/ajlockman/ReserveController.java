package ajlockman;

import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.model.*;
import ks.common.view.*;

import java.awt.event.MouseEvent;

public class ReserveController extends SolitaireReleasedAdapter
{
    protected BuildablePileView pile;
    protected Deck d;
    /**
     * Handle mouse events on the reserve pile.
     *
     * @param theGame game under play.
     */
    public ReserveController(Solitaire theGame, BuildablePileView pile, Deck deck)
    {
        super(theGame);
        this.pile = pile;
        this.d = deck;
    }

    /**
     * Handle when the mouse is pressed on the reserve column.
     * @param me The triggered mouse event.
     */
    public void mousePressed(MouseEvent me)
    {
        Container c = theGame.getContainer();

        /** Return if there is no card to be chosen. */
        BuildablePile sourcePile = (BuildablePile) pile.getModelElement();
        if (sourcePile.count() == 0)
        {
            c.releaseDraggingObject();
            return;
        }

        //If the top card is not flipped, flip the top card.
        if (sourcePile.getNumFaceUp() == 0) {
            Move m = new FlipCardMove (sourcePile);
            if (m.doMove(theGame)) {
                theGame.pushMove (m);
                theGame.refreshWidgets();
            } else {
                // error in flip card. Not sure what to do
                System.err.println ("ReserveController::mousePressed(). " +
                        "Unexpected failure in flip card.");
            }
            return;
        }

        //Otherwise hand off card to the container for drag.
        ColumnView colView = pile.getColumnView(me);

        if (colView == null) {
            return;
        }

        Column col = (Column) colView.getModelElement();
        if (col == null) {
            System.err.println("ReserveController::mousePressed(): " +
                    "Unexpectedly encountered a ColumnView with no Column.");
            return; // sanity check, but should never happen.
        }

        Widget w = c.getActiveDraggingObject();
        if (w != Container.getNothingBeingDragged())
        {
            System.err.println ("ReserveController::mousePressed(): " +
                    "Unexpectedly encountered a Dragging Object during a " +
                    "Mouse press.");
            return;
        }

        c.setActiveDraggingObject (colView, me);

        c.setDragSource (pile);

        pile.redraw();
    }

    /**
     * Handle when the mouse is released on the reserve.
     * @param me The triggered mouse event.
     */
    public void mouseReleased(MouseEvent me)
    {
        Container c = theGame.getContainer();

        Widget draggingWidget = c.getActiveDraggingObject();
        if (draggingWidget == Container.getNothingBeingDragged()) {
            System.err.println ("ReserveController::mouseReleased() " +
                    "unexpectedly found nothing being dragged.");
            c.releaseDraggingObject();
            return;
        }

        Widget fromWidget = c.getDragSource();
        if (fromWidget == null) {
            System.err.println ("ReserveController::mouseReleased(): " +
                    "somehow no dragSource in container.");
            c.releaseDraggingObject();
            return;
        }

        //If widget is not from the reserve, do move.
        if (!fromWidget.getName().contains("Reserve"))
        {
            CardView cardview = (CardView) draggingWidget;
            Card theCard = (Card) cardview.getModelElement();

            BuildablePile dst = (BuildablePile) pile.getModelElement();
            Column src = (Column) fromWidget.getModelElement();

            Move reserveMove = new ReserveMove(src, dst, theCard, d);
            if (reserveMove.doMove(theGame))
            {
                theGame.pushMove(reserveMove);
            } else
            {
                fromWidget.returnWidget(draggingWidget);
            }
        } else
        {
            fromWidget.returnWidget(draggingWidget);
        }

        c.releaseDraggingObject();
        c.repaint();
    }
}
