package Ingame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardMatchingEasy extends JFrame {
    private JPanel cardPanel;
    private JPanel topPanel;  
    private JLabel timerLabel;  
    private JLabel scoreLabel;  
    private List<Card> cards;
    private Card selectedCard = null;
    private int pairsFound = 0;
    private static final String initialImagePath = "cardlogo.jpg";
    private static final int initialImageWidth = 150;
    private static final int initialImageHeight = 200;

    public CardMatchingEasy() {
        setTitle("엎어라 뒤집어라");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 900);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        topPanel = new JPanel(new FlowLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        timerLabel = new JLabel("Timer: 0");
        scoreLabel = new JLabel("Score: 0");
        topPanel.add(timerLabel);
        topPanel.add(scoreLabel);
        topPanel.setBackground(new Color(125, 159, 104));
        add(topPanel, BorderLayout.NORTH);


        cardPanel = new JPanel(new GridLayout(4, 4, 10, 10));
        cards = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            String imagePath = "easy/easy" + (i + 1) + ".jpg";
            ImageIcon icon = new ImageIcon(imagePath);

            for (int j = 0; j < 2; j++) {
                Card card = new Card(icon);
                card.addMouseListener(new CardClickListener());
                cards.add(card);
            }
        }

        Collections.shuffle(cards);

        for (Card card : cards) {
            cardPanel.add(card);
        }

        initializeCardImages();

        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.add(cardPanel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        add(mainPanel);

        setVisible(true);
    }

    private void initializeCardImages() {
        ImageIcon defaultIcon = new ImageIcon(initialImagePath);
        Image img = defaultIcon.getImage().getScaledInstance(initialImageWidth, initialImageHeight, Image.SCALE_SMOOTH);
        defaultIcon = new ImageIcon(img);
        
        for (Card card : cards) {
            card.setIcon(defaultIcon);
        }
    }

    public class Card extends JLabel {
        private ImageIcon icon;
        private boolean isFaceUp = false;

        public Card(ImageIcon icon) {
            this.icon = icon;
            setPreferredSize(new Dimension(initialImageWidth, initialImageHeight));
            setIcon(icon);
        }

        public void showCard() {
            isFaceUp = true;
            setIcon(scaleIcon(icon, getPreferredSize()));
        }

        public boolean isFaceUp() {
            return isFaceUp;
        }
        
        private ImageIcon scaleIcon(ImageIcon originalIcon, Dimension size) {
            Image image = originalIcon.getImage();
            Image scaledImage = image.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
    }

    private class CardClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            Card clickedCard = (Card) e.getSource();

            if (!clickedCard.isFaceUp) {
                if (selectedCard == null) {
                    clickedCard.showCard();
                    selectedCard = clickedCard;
                } else {
                    clickedCard.showCard();
                    if (selectedCard.icon.equals(clickedCard.icon)) {
                        pairsFound++;
                        if (pairsFound == 8) {
                            JOptionPane.showMessageDialog(null, "You win!");
                        }
                        selectedCard.setEnabled(false);
                        clickedCard.setEnabled(false);
                        selectedCard = null;
                    } else {
                    	Timer timer = new Timer(50, new ActionListener() {
                    	    @Override
                    	    public void actionPerformed(ActionEvent e) {
                    	        if (selectedCard != null) {
                    	            selectedCard.isFaceUp = false;
                    	            selectedCard.setIcon(scaleIcon(new ImageIcon(initialImagePath), selectedCard.getPreferredSize()));
                    	        }
                    	        if (clickedCard != null) {
                    	            clickedCard.isFaceUp = false;
                    	            clickedCard.setIcon(scaleIcon(new ImageIcon(initialImagePath), clickedCard.getPreferredSize()));
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
        private ImageIcon scaleIcon(ImageIcon originalIcon, Dimension size) {
            Image image = originalIcon.getImage();
            Image scaledImage = image.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CardMatchingEasy());
    }
}
