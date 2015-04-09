package ajlockman;
import ks.common.games.Solitaire;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.model.Move;


public class RemoveCardTableauMove extends Move
{
    Column source1, source2;
    Deck deck;

    public RemoveCardTableauMove(Column c1, Column c2, Deck d)
    {
        source1 = c1;
        source2 = c2;
        deck = d;
    }

    @Override
    public boolean doMove(Solitaire game)
    {
        if (!valid(game)) { return false; }

        Card c = source1.get();
        deck.add(c);
        c = source2.get();
        deck.add(c);
        return true;
    }

    @Override
    public boolean undo(Solitaire game)
    {
        Card c = deck.get();
        source2.add(c);
        c = deck.get();
        source1.add(c);
        return true;
    }

    @Override
    public boolean valid(Solitaire game)
    {
        return !source1.empty() && !source2.empty() &&
                source1.peek().sameRank(source2.peek());
    }
}
