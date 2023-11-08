package Ingame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardMatchingGame extends JFrame {
    private JPanel cardPanel;
    private List<Card> cards;
    private Card selectedCard = null;
    private int pairsFound = 0;

    public CardMatchingGame() {
        setTitle("Card Matching Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new BorderLayout());

        cardPanel = new JPanel(new GridLayout(4, 4));
        cards = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            cards.add(new Card(i));
            cards.add(new Card(i));
        }

        Collections.shuffle(cards);

        for (Card card : cards) {
            card.addActionListener(new CardClickListener());
            cardPanel.add(card);
        }

        add(cardPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private class Card extends JButton {
        private int value;
        private boolean isFaceUp = false;

        public Card(int value) {
            this.value = value;
            setPreferredSize(new Dimension(80, 80));
        }

        public void showCard() {
            isFaceUp = true;
            setText(String.valueOf(value));
        }

        public void hideCard() {
            isFaceUp = false;
            setText("");
        }

        public boolean isFaceUp() {
            return isFaceUp;
        }
    }

    private class CardClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Card clickedCard = (Card) e.getSource();

            if (!clickedCard.isFaceUp) {
                if (selectedCard == null) {
                    clickedCard.showCard();
                    selectedCard = clickedCard;
                } else {
                    clickedCard.showCard();
                    if (selectedCard.value == clickedCard.value) {
                        pairsFound++;
                        if (pairsFound == 8) {
                            JOptionPane.showMessageDialog(null, "You win!");
                        }
                        selectedCard.setEnabled(false);
                        clickedCard.setEnabled(false);
                        selectedCard = null;
                    } else {
                        Timer timer = new Timer(400, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (selectedCard != null) {
                                    selectedCard.hideCard();
                                }
                                if (clickedCard != null) {
                                    clickedCard.hideCard();
                                }
                                selectedCard = null;
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CardMatchingGame());
    }
}
