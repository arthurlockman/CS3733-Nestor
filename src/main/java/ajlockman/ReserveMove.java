package ajlockman;

import ks.common.games.Solitaire;
import ks.common.model.*;

/**
 * Move a card either from the tableau to the reserve, or from
 * the reserve to the tableau.
 */
public class ReserveMove extends Move
{
    protected BuildablePile source;
    protected Column dest;
    protected Card theCard;
    protected Deck deck;
    protected int direction; //1 if from reserve to tableau, 0 otherwise

    /**
     * ReserveMove constructor. Moves card from pile to column.
     * @param src The source pile. Card will be moved from this pile.
     * @param dst The destination column. Card will be moved to this column.
     * @param card The card to be moved.
     * @param d The deck that is associated with the game.
     */
    public ReserveMove(BuildablePile src, Column dst, Card card, Deck d)
    {
        this.source = src;
        this.dest = dst;
        this.theCard = card;
        this.deck = d;
        this.direction = 1;
    }

    /**
     * ReserveMove constructor. Moves card from column to pile.
     * @param src The source column. Card will be moved from this column.
     * @param dst The destination pile. Card will be moved to this pile.
     * @param card The card to be moved.
     * @param d The deck that is associated with the game.
     */
    public ReserveMove(Column src, BuildablePile dst, Card card, Deck d)
    {
        this.source = dst;
        this.dest = src;
        this.theCard = card;
        this.deck = d;
        this.direction = 0;
    }

    /**
     * Perform the move.
     * @param game The solitaire game to perform the move on.
     * @return Whether or not the move was successful.
     */
    @Override
    public boolean doMove(Solitaire game)
    {
        if (!valid(game)) return false;
        if (direction == 1) //1 if from reserve to tableau, 0 otherwise
        {
            deck.add(theCard);
            deck.add(dest.get());
            game.updateScore(+2);
        } else {
            deck.add(theCard);
            deck.add(source.get());
            game.updateScore(+2);
        }
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
        if (direction == 1) //1 if from reserve to tableau, 0 otherwise
        {
            if (deck.count() == 0) return false;
            dest.add(deck.get());
            source.add(deck.get());
            game.updateScore(-2);
        } else {
            if (deck.count() == 0) return false;
            source.add(deck.get());
            dest.add(deck.get());
            game.updateScore(-2);
        }
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
        if (direction == 1) //1 if from reserve to tableau, 0 otherwise
        {
            return !dest.empty() && theCard.sameRank(dest.peek());
        } else {
            return !source.empty() && theCard.sameRank(source.peek());
        }
    }
}
