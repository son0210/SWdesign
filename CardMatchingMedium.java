package Ingame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 난이도 중의 카드 매칭 게임을 나타냅니다.
 * JFrame을 확장하며 게임의 그래픽 사용자 인터페이스(GUI)를 포함합니다.
 */
public class CardMatchingMedium extends JFrame {
    private JPanel cardPanel;
    private JPanel topPanel;
    private JButton button;
    private JLabel scoreLabel;
    public Score score;
    public static Level1Timer level1timer;
    private List<Card> cards;
    private int pairsFound = 0;
    private Card selectedCard = null;
    private boolean isComparing = false;
    private static final String initialImagePath = "src/image/cardlogo.jpg";

    /**
     * CardMatchingEasy 클래스의 생성자입니다.
     * 게임 창을 초기화하고 사용자 인터페이스를 설정합니다.
     */
    public CardMatchingMedium() {
        setTitle("엎어라 뒤집어라");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1400, 1100));
        
        score = new Score();
        scoreLabel = new JLabel("Score: 0");
        
        topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(new Color(125, 159, 104));
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();

        // Add scoreLabel to the left side
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        topPanel.add(scoreLabel, gbc);

        // Add some horizontal space between scoreLabel and the button
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        topPanel.add(Box.createHorizontalStrut(10), gbc);

        // Add button to the right side
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        ImageIcon pauseIcon = new ImageIcon("src/image/pause.png");
        button = new JButton(pauseIcon);
        button.setPreferredSize(new Dimension(20, 20));
        button.setBackground(new Color(125, 159, 104));
        button.setBorderPainted(false);
        topPanel.add(button, gbc);

        // Add some vertical space between the button and the progress bar
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weighty = 1.0;
        topPanel.add(Box.createVerticalStrut(10), gbc);

        //타이머 추가
        Level1Timer level1timer = new Level1Timer();
        JProgressBar timerVisible = level1timer.getProgressBar();
        topPanel.add(timerVisible);
        
        // Add the JProgressBar to the center
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        topPanel.add(timerVisible, gbc);

        cardPanel = new JPanel(new GridLayout(5, 6, 10, 10));
        cardPanel.setBackground(new Color(237, 227, 206));
        cards = new ArrayList<>();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int cardWidth = (int) (screenSize.getWidth()* 0.08);
        int cardHeight = (int) (screenSize.getHeight()* 0.16);
        
        for (int i = 0; i < 15; i++) {
            String imagePath = "src/image/medium/food" + (i + 1) + ".jpg";
            ImageIcon cardIcon = new ImageIcon(imagePath);

            for (int j = 0; j < 2; j++) {
                Card card = new Card(cardIcon, cardWidth, cardHeight);
                card.addMouseListener(new CardClickListener());
                cards.add(card);
            }
        }

        Collections.shuffle(cards);
        initializeCardImages(cardWidth, cardHeight);
        
        for (Card card : cards) {
            cardPanel.add(card);
        }
        
        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.setBackground(new Color(237, 227, 206));
        centerPanel.add(cardPanel);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        setVisible(true);
    }
    
    /**
     * 게임의 모든 카드에 대한 이미지를 초기화합니다.
     * 각 카드의 기본 이미지 아이콘을 초기 이미지의 크기에 맞게 조절합니다.
     */
    private void initializeCardImages(int cardWidth, int cardHeight) {
        ImageIcon defaultIcon = new ImageIcon(initialImagePath);
        Image img = defaultIcon.getImage().getScaledInstance(cardWidth, cardHeight, Image.SCALE_SMOOTH);
        defaultIcon = new ImageIcon(img);

        for (Card card : cards) {
            card.setIcon(defaultIcon);
        }
    }
    /**
     * Card 클래스는 게임에서 사용되는 카드를 나타냅니다.
     * 각 카드에는 연관된 이미지 아이콘이 있으며 앞면이나 뒷면으로 나타낼 수 있습니다.
     */
    public class Card extends JLabel {
        private ImageIcon icon;
        private boolean isFaceUp = false;

        /**
         * Card 클래스의 생성자입니다.
         *
         * @param icon 카드와 연관된 이미지 아이콘입니다.
         */
        public Card(ImageIcon icon, int width, int height) {
            this.icon = icon;
            setPreferredSize(new Dimension(width, height));
            setIcon(scaleIcon(icon, getPreferredSize()));
        }

        /**
         * 카드의 앞면을 보여주기 위해 아이콘을 설정하고 카드를 앞면으로 표시합니다.
         */
        public void showCard() {
            isFaceUp = true;
            setIcon(scaleIcon(icon, getPreferredSize()));
        }

        /**
         * 카드가 앞면인지 확인합니다.
         *
         * @return 카드가 앞면이면 true, 그렇지 않으면 false를 반환합니다.
         */
        public boolean isFaceUp() {
            return isFaceUp;
        }
        
        /**
         * 이미지 아이콘을 지정된 크기로 조절하는 유틸리티 메서드입니다.
         *
         * @param originalIcon 조절할 원본 이미지 아이콘입니다.
         * @param size 조절된 이미지의 크기를 나타내는 Dimension 객체입니다.
         * @return 지정된 크기로 조절된 이미지 아이콘을 나타내는 ImageIcon 객체입니다.
         */
        public ImageIcon scaleIcon(ImageIcon originalIcon, Dimension size) {
            Image image = originalIcon.getImage();
            Image scaledImage = image.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
    }

    /**
     * CardClickListener 클래스는 카드에 대한 마우스 클릭 이벤트를 처리합니다.
     * MouseAdapter 인터페이스를 구현합니다.
     */
    private class CardClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            Card clickedCard = (Card) e.getSource();

            if (isComparing || clickedCard.isFaceUp()) {
                return; // 비교 중이거나 이미 앞면이면 클릭을 무시
            }

            clickedCard.showCard();

            if (selectedCard == null) {
                selectedCard = clickedCard;
            } else {
                isComparing = true;
                Timer cardTimer = new Timer(40, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (selectedCard.icon.equals(clickedCard.icon)) {
                            score.increaseScore(2);
                            scoreLabel.setText("Score: " + score.getScore());
                            pairsFound++;
                            if (pairsFound == 15) {
//                            	score.timerScore = level1timer.getTimerValue(); // 타이머 점수 저장해서 최종 점수에 더해져야함
                            	// 타이머 끝나면 게임 끝나거나 게임 끝나면 타이머 끝나야함
                            	JOptionPane.showMessageDialog(null, "You win");
                            }
                            selectedCard.setEnabled(false);
                            clickedCard.setEnabled(false);
                        } else {
                            selectedCard.isFaceUp = false;
                            selectedCard.setIcon(scaleIcon(new ImageIcon(initialImagePath), selectedCard.getPreferredSize()));

                            clickedCard.isFaceUp = false;
                            clickedCard.setIcon(scaleIcon(new ImageIcon(initialImagePath), clickedCard.getPreferredSize()));
                        }

                        selectedCard = null;
                        isComparing = false;
                    }
                });

                cardTimer.setRepeats(false);
                cardTimer.start();
            }
        }

    	
    	/**
    	 * 이미지 아이콘을 지정된 크기로 조절하는 유틸리티 메서드입니다.
    	 *
    	 * @param originalIcon 조절할 원본 이미지 아이콘입니다.
    	 * @param size 조절된 이미지의 크기를 나타내는 Dimension 객체입니다.
    	 * @return 지정된 크기로 조절된 이미지 아이콘을 나타내는 ImageIcon 객체입니다.
    	 */
        public ImageIcon scaleIcon(ImageIcon originalIcon, Dimension size) {
            Image image = originalIcon.getImage();
            Image scaledImage = image.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
    }

    /**
     * CardMatchingEasy 애플리케이션을 시작하는 메인 메서드입니다.
     *
     * @param args 명령행 인수 (사용되지 않음).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CardMatchingMedium());
    }
}
