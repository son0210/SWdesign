import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardMatchingEasy extends JFrame {
    private JPanel cardPanel;
    private JPanel topPanel;  // 타이머와 점수를 담을 패널
    private JLabel timerLabel;  // 타이머 표시 레이블
    private JLabel scoreLabel;  // 점수 표시 레이블
    private List<Card> cards;
    private Card selectedCard = null;
    private int pairsFound = 0;

    public CardMatchingEasy() {
        setTitle("엎어라 뒤집어라");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(768, 1024);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        topPanel = new JPanel(new FlowLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        timerLabel = new JLabel("Timer: 0");
        scoreLabel = new JLabel("Score: 0");
        topPanel.add(timerLabel);
        topPanel.add(scoreLabel);
        topPanel.setBackground(new Color(125, 159, 104));
        add(topPanel, BorderLayout.NORTH); // 상단에 타이머와 점수 패널 추가

        cardPanel = new JPanel(new GridLayout(4, 4, 10, 10));
        cards = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            String imagePath = "easy/easy" + (i+1) + ".jpg";
            ImageIcon icon = new ImageIcon(imagePath);

            for (int j = 0; j < 2; j++) {
                Card card = new Card(icon);
                cards.add(card);
            }
        }

        Collections.shuffle(cards);

        for (Card card : cards) {
            card.addActionListener(new CardClickListener());
            cardPanel.add(card);
        }

        add(cardPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public class Card extends JButton {
        private ImageIcon icon;
        private Color defaultBackgroundColor = new Color(237, 227, 206);
        private boolean isFaceUp = false;

        public Card(ImageIcon icon) {
            this.icon = icon;
            setPreferredSize(new Dimension(150, 200)); // 버튼의 크기를 설정
            setBackground(defaultBackgroundColor);
        }

        public void showCard() {
            isFaceUp = true;
            setIcon(scaleIcon(icon, getPreferredSize())); // 이미지 아이콘 크기를 버튼의 크기에 맞게 조절
            setBackground(new Color(237, 227, 206)); // 배경색을 원하는 색으로 설정
        }

        public void hideCard() {
            isFaceUp = false;
            setIcon(null);
            setBackground(defaultBackgroundColor);
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
                    if (selectedCard.icon.equals(clickedCard.icon)) {
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
        SwingUtilities.invokeLater(() -> new CardMatchingEasy());
    }
}
