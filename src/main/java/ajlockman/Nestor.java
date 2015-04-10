package ajlockman;
import ks.client.gamefactory.GameWindow;
import ks.common.controller.SolitaireMouseMotionAdapter;
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

public class Nestor extends Solitaire
{
    protected Deck deck;
    protected BuildablePile reserve;
    protected Column[] tableau = new Column[8];
    protected BuildablePileView reserveView;
    protected ColumnView[] tableauViews = new ColumnView[8];
    protected IntegerView scoreView, numLeftView;

    @Override
    public String getName()
    {
        return "ajlockman-Nestor";
    }

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

    /**
     * Deck of cards be dealt into the tableau columns such that
     * each column contains no cards of the same rank. The
     * remaining cards are placed into a buildable pile.
     */
    @Override
    public void initialize()
    {
        initializeModel(getSeed());
        initializeView();
        initializeController();

        updateScore(0);
    }

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
    }

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

        //Handle dealing cards
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

    public static void main (String []args)
    {
        GameWindow gw = Main.generateWindow(new Nestor(), Deck.OrderByRank);
        gw.setVisible(true);
    }
}

