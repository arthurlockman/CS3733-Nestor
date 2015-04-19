package ajlockman;

import ks.common.games.Solitaire;
import ks.common.model.BuildablePile;
import ks.common.model.Move;

/**
 * Flip the top card on the reserve.
 */
public class FlipCardMove extends Move
{
    protected BuildablePile source;

    /**
     * FlipCardMove constructor.
     * @param src The source pile on which to flip the card/
     */
    public FlipCardMove(BuildablePile src)
    {
        this.source = src;
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
        source.flipCard();
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
        source.flipCard();
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
        return !source.faceUp();
    }
}
