package ajlockman;
import ks.client.gamefactory.GameWindow;
import ks.common.controller.SolitaireMouseMotionAdapter;
import ks.common.controller.SolitaireReleasedAdapter;
import ks.common.games.Solitaire;
import ks.common.games.SolitaireUndoAdapter;
import ks.common.model.BuildablePile;
import ks.common.model.Card;
import ks.common.model.Column;
import ks.common.model.Deck;
import ks.common.view.BuildablePileView;
import ks.common.view.CardImages;
import ks.common.view.ColumnView;
import ks.common.view.IntegerView;
import ks.launcher.Main;

/**
 * An implementation of the solitaire game Nestor. The objective
 * of the game is to remove cards of matching ranks. Once
 * all matched pairs have been removed, then the game is won.
 *
 * @author Arthur Lockman - WPI CS 3733
 */
public class Nestor extends Solitaire
{
    protected Deck deck;
    protected BuildablePile reserve;
    protected Column[] tableau = new Column[8];
    protected BuildablePileView reserveView;
    protected ColumnView[] tableauViews = new ColumnView[8];
    protected IntegerView scoreView, numLeftView;

    /**
     * Return the name of the game.
     * @return A string, the game's name.
     */
    @Override
    public String getName()
    {
        return "ajlockman-Nestor";
    }

    /**
     * Determines if the game has been won.
     * @return True if the game is won, false otherwise.
     */
    @Override
    public boolean hasWon()
    {
        int empty = 0;
        for (int i = 0; i < 8; i++)
        {
            if (tableau[i].empty()) empty++;
        }
        if (reserve.empty()) empty++;
        return (empty == 9);
    }

    @Override
    public void initialize()
    {
        initializeModel(getSeed());
        initializeView();
        initializeController();

        updateScore(0);
    }

    /**
     * Initialize the controllers. This creates the controllers for each
     * visual entity used in the game.
     */
    void initializeController()
    {
        for (int i = 0; i < 8; i++)
        {
            tableauViews[i].setMouseAdapter(new TableauController(this, tableauViews[i], deck));
            tableauViews[i].setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
            tableauViews[i].setUndoAdapter(new SolitaireUndoAdapter(this));
        }
        reserveView.setMouseAdapter(new ReserveController(this, reserveView, deck));
        reserveView.setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
        reserveView.setUndoAdapter(new SolitaireUndoAdapter(this));

        scoreView.setMouseAdapter(new SolitaireReleasedAdapter(this));
        numLeftView.setMouseAdapter(new SolitaireReleasedAdapter(this));
        scoreView.setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
        numLeftView.setMouseMotionAdapter(new SolitaireMouseMotionAdapter(this));
    }

    /**
     * Deck of cards be dealt into the tableau columns such that
     * each column contains no cards of the same rank. The
     * remaining cards are placed into a buildable pile.
     *
     * @param seed The seed to create the deck with.
     */
    void initializeModel(int seed)
    {
        deck = new Deck("deck");
        deck.create(seed);
        model.addElement(deck);

        for (int i = 0; i < 8; i++)
        {
            tableau[i] = new Column("Tableau " + i);
            model.addElement(tableau[i]);
        }

        reserve = new BuildablePile("Reserve");
        model.addElement(reserve);

        updateNumberCardsLeft(52);

        //Deal cards onto the stacks. This will deal cards in such
        //a way that no two cards of the same rank are left in the
        //same column. In the event that a card cannot be dealt
        //onto any column due to rank, it adds it to the reserve
        //instead of the tableau, leading to a tableau of 5 cards
        //instead of the usual 4.
        System.out.println("Dealing cards...");
        int deals = 0;
        while (deck.count() > 4)
        {
            deals++;
            if (deals > 48) { break; }
            for (int i = 0; i < 8; i++)
            {
                if (!(tableau[i].count() >= 6)) {
                    boolean matched = false;
                    Card c = deck.get();
                    for (int j = 0; j < tableau[i].count(); j++) {
                        if (tableau[i].peek(j).sameRank(c)) {
                            matched |= true;
                        }
                    }
                    if (!matched) {
                        tableau[i].add(c);
                        updateNumberCardsLeft(-1);
                    } else {
                        //Move card to bottom of deck.
                        Deck tmp = new Deck();
                        tmp.removeAll();
                        tmp.push(deck);
                        deck.removeAll();
                        deck.add(c);
                        deck.push(tmp);
                    }
                }
            }
        }
        while(deck.count() > 0)
        {
            reserve.add(deck.get());
            reserve.flipCard();
            updateNumberCardsLeft(-1);
        }
        reserve.flipCard();
        System.out.println("Done.");
    }

    /**
     * Initialize the visual entities. This method creates
     * and places all visual entities on the screen and adds
     * them to the visual container.
     */
    void initializeView()
    {
        CardImages ci = getCardImages();

        //Initialize column views.
        for (int i = 0; i < 8; i++)
        {
            tableauViews[i] = new ColumnView(tableau[i]);
            tableauViews[i].setBounds(20 * (i + 1) + (i * ci.getWidth()),
                    30, ci.getWidth(),
                    (int)(ci.getHeight() * 0.23 * 7) + ci.getHeight());
            tableauViews[i].setName("TableauView" + i);
            container.addWidget(tableauViews[i]);
        }

        //Initialize reserve view.
        reserveView = new BuildablePileView(reserve);
        reserveView.setBounds(20, 30 + tableauViews[0].getHeight(),
                ci.getWidth(), (int)(ci.getHeight() * 1.5));
        reserveView.setName("ReserveView");
        container.addWidget(reserveView);

        //Initialize score view
        scoreView = new IntegerView(score);
        scoreView.setFontSize(28);
        scoreView.setBounds(40 + ci.getWidth(),
                30 + tableauViews[0].getHeight(),
                100, 60);
        scoreView.setName("ScoreView");
        container.addWidget(scoreView);

        //Initialize cards left view
        numLeftView = new IntegerView(numLeft);
        numLeftView.setFontSize(28);
        numLeftView.setBounds(140 + ci.getWidth(),
                30 + tableauViews[0].getHeight(),
                100, 60);
        numLeftView.setName("NumLeftView");
        container.addWidget(numLeftView);
    }

    /**
     * Main method for Nestor.
     * @param args command line arguments.
     */
    public static void main (String []args)
    {
        GameWindow gw = Main.generateWindow(new Nestor(), Deck.OrderByRank);
        gw.setVisible(true);
    }
}

