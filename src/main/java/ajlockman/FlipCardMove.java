package ajlockman;

import ks.common.games.Solitaire;
import ks.common.model.BuildablePile;
import ks.common.model.Move;

public class FlipCardMove extends Move
{
    protected BuildablePile source;
    public FlipCardMove(BuildablePile src)
    {
        this.source = src;
    }

    @Override
    public boolean doMove(Solitaire game)
    {
        if (!valid(game)) { return false; }
        source.flipCard();
        return true;
    }

    @Override
    public boolean undo(Solitaire game)
    {
        source.flipCard();
        return true;
    }

    @Override
    public boolean valid(Solitaire game)
    {
        return !source.faceUp();
    }
}
