package ajlockman;

import ks.common.games.Solitaire;
import ks.common.model.*;

public class ReserveMove extends Move
{
    protected BuildablePile source;
    protected Column dest;
    protected Card theCard;
    protected Deck deck;
    protected int direction; //1 if from reserve to tableau, 0 otherwise

    public ReserveMove(BuildablePile src, Column dst, Card card, Deck d)
    {
        this.source = src;
        this.dest = dst;
        this.theCard = card;
        this.deck = d;
        this.direction = 1;
    }

    public ReserveMove(Column src, BuildablePile dst, Card card, Deck d)
    {
        this.source = dst;
        this.dest = src;
        this.theCard = card;
        this.deck = d;
        this.direction = 0;
    }

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
