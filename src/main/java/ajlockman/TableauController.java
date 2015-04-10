package ajlockman;

import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.model.Move;
import ks.common.view.CardView;
import ks.common.view.ColumnView;
import ks.common.view.Container;
import ks.common.view.Widget;

import java.awt.event.MouseEvent;

public class TableauController extends SolitaireReleasedAdapter
{
    protected ColumnView col;
    protected Deck d;

    /**
     * Handles removing cards between tableau columns.
     *
     * @param theGame game under play.
     */
    public TableauController(Nestor theGame, ColumnView column, Deck deck)
    {
        super(theGame);
        this.col = column;
        this.d = deck;
    }

    public void mousePressed(MouseEvent me)
    {
        Container c = theGame.getContainer();

        /** Return if there is no card to be chosen. */
        Column wastePile = (Column) col.getModelElement();
        if (wastePile.count() == 0)
        {
            c.releaseDraggingObject();
            return;
        }

        CardView cardView = col.getCardViewForTopCard(me);

        if (cardView == null)
        {
            c.releaseDraggingObject();
            return;
        }

        Widget w = c.getActiveDraggingObject();
        if (w != Container.getNothingBeingDragged())
        {
            System.err.println ("WastePileController::mousePressed(): Unexpectedly encountered a Dragging Object during a Mouse press.");
            return;
        }

        c.setActiveDraggingObject (cardView, me);

        c.setDragSource (col);

        col.redraw();
    }

    public void mouseReleased(MouseEvent me)
    {
        Container c = theGame.getContainer();

        Widget draggingWidget = c.getActiveDraggingObject();
        if (draggingWidget == Container.getNothingBeingDragged()) {
            System.err.println ("FoundationController::mouseReleased() unexpectedly found nothing being dragged.");
            c.releaseDraggingObject();
            return;
        }

        Widget fromWidget = c.getDragSource();
        if (fromWidget == null) {
            System.err.println ("FoundationController::mouseReleased(): somehow no dragSource in container.");
            c.releaseDraggingObject();
            return;
        }

        Column dst = (Column)col.getModelElement();
        Column src = (Column)fromWidget.getModelElement();

        CardView cardview = (CardView)draggingWidget;
        Card theCard = (Card)cardview.getModelElement();

        Move tableauMove = new TableauMove(src, dst, theCard, d);
        if (tableauMove.doMove(theGame))
        {
            theGame.pushMove(tableauMove);
        }
        else
        {
            fromWidget.returnWidget(draggingWidget);
        }

        c.releaseDraggingObject();
        c.repaint();
    }
}
