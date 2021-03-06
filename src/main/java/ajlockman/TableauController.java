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

    /**
     * Handle when the mouse is pressed on the column.
     * @param me The triggered mouse event.
     */
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
            System.err.println ("TableauController::mousePressed(): " +
                    "Unexpectedly encountered a Dragging Object during a " +
                    "Mouse press.");
            return;
        }

        c.setActiveDraggingObject (cardView, me);

        c.setDragSource (col);

        col.redraw();
    }

    /**
     * Handle when the mouse is released on the column.
     * @param me The triggered mouse event.
     */
    public void mouseReleased(MouseEvent me)
    {
        Container c = theGame.getContainer();

        Widget draggingWidget = c.getActiveDraggingObject();
        if (draggingWidget == Container.getNothingBeingDragged()) {
            System.err.println ("TableauController::mouseReleased() " +
                    "unexpectedly found nothing being dragged.");
            c.releaseDraggingObject();
            return;
        }

        Widget fromWidget = c.getDragSource();
        if (fromWidget == null) {
            System.err.println ("TableauController::mouseReleased(): " +
                    "somehow no dragSource in container.");
            c.releaseDraggingObject();
            return;
        }

        //If widget is from the tableau, handle move like this.
        if (fromWidget.getName().contains("Tableau"))
        {
            CardView cardview = (CardView) draggingWidget;
            Card theCard = (Card) cardview.getModelElement();

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
        } else if (fromWidget.getName().contains("Reserve")) //Else if it's from the reserve...
        {
            ColumnView colView = (ColumnView) draggingWidget;
            Column theCol = (Column) colView.getModelElement();
            Card theCard = theCol.peek(); //Have to extract card from dragging column

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
