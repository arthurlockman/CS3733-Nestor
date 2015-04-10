package ajlockman;

import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.model.*;
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
        Column sourceCol = (Column) col.getModelElement();
        if (sourceCol.count() == 0)
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
            System.err.println ("WastePileController::mousePressed(): " +
                    "Unexpectedly encountered a Dragging Object during a " +
                    "Mouse press.");
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
            System.err.println ("FoundationController::mouseReleased() " +
                    "unexpectedly found nothing being dragged.");
            c.releaseDraggingObject();
            return;
        }

        Widget fromWidget = c.getDragSource();
        if (fromWidget == null) {
            System.err.println ("FoundationController::mouseReleased(): " +
                    "somehow no dragSource in container.");
            c.releaseDraggingObject();
            return;
        }

        System.out.println(fromWidget.getName());

        CardView cardview = (CardView) draggingWidget;
        Card theCard = (Card) cardview.getModelElement();

        if (fromWidget.getName().contains("Tableau"))
        {
            Column dst = (Column) col.getModelElement();
            Column src = (Column) fromWidget.getModelElement();

            Move tableauMove = new TableauMove(src, dst, theCard, d);
            if (tableauMove.doMove(theGame))
            {
                theGame.pushMove(tableauMove);
            } else
            {
                fromWidget.returnWidget(draggingWidget);
            }
        } else if (fromWidget.getName().contains("Reserve"))
        {
            Column dst = (Column) col.getModelElement();
            BuildablePile src = (BuildablePile) fromWidget.getModelElement();

            Move reserveMove = new ReserveMove(src, dst, theCard, d);
            if (reserveMove.doMove(theGame))
            {
                theGame.pushMove(reserveMove);
            } else
            {
                fromWidget.returnWidget(draggingWidget);
            }
        }

        c.releaseDraggingObject();
        c.repaint();

    }
}
