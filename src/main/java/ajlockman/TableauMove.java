package ajlockman;

import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.model.Move;

public class TableauMove extends Move
{
    protected Column source;
    protected Column dest;
    protected Card theCard;
    protected Deck deck;

    public TableauMove(Column src, Column dst, Card theCard, Deck d)
    {
        this.source = src;
        this.dest = dst;
        this.theCard = theCard;
        this.deck = d;
    }

    @Override
    public boolean doMove(Solitaire game)
    {
        if (!valid(game)) { return false; }
        deck.add(theCard);
        deck.add(dest.get());
        game.updateScore(+2);
        return true;
    }

    @Override
    public boolean undo(Solitaire game)
    {
        if (deck.count() == 0) return false;
        dest.add(deck.get());
        source.add(deck.get());
        game.updateScore(-2);
        return true;
    }

    @Override
    public boolean valid(Solitaire game)
    {
        return !dest.empty() && theCard.sameRank(dest.peek());
    }
}
