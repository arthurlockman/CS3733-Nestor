package ajlockman;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.model.Move;

/**
 * Move a card from one tableau column to another.
 */
public class TableauMove extends Move
{
    protected Column source;
    protected Column dest;
    protected Card theCard;
    protected Deck deck;

    /**
     * TableauMove constructor.
     * @param src The source column. Card will be moved from this column.
     * @param dst The destination column. Card will be moved to this column.
     * @param theCard The card to be moved.
     * @param d The deck that is associated with the game.
     */
    public TableauMove(Column src, Column dst, Card theCard, Deck d)
    {
        this.source = src;
        this.dest = dst;
        this.theCard = theCard;
        this.deck = d;
    }

    /**
     * Perform the move.
     * @param game The solitaire game to perform the move on.
     * @return Whether or not the move was successful.
     */
    @Override
    public boolean doMove(Solitaire game)
    {
        if (!valid(game)) { return false; }
        deck.add(theCard);
        deck.add(dest.get());
        game.updateScore(+2);
        return true;
    }

    /**
     * Undo the move.
     * @param game The solitaire game to perform the move on.
     * @return Whether or not the move could be undone.
     */
    @Override
    public boolean undo(Solitaire game)
    {
        if (deck.count() == 0) return false;
        dest.add(deck.get());
        source.add(deck.get());
        game.updateScore(-2);
        return true;
    }

    /**
     * Get if the move is valid.
     * @param game The game to check validity for.
     * @return Whether or not the move is valid.
     */
    @Override
    public boolean valid(Solitaire game)
    {
        return !dest.empty() && theCard.sameRank(dest.peek());
    }
}
