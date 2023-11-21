package Ingame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 난이도 상의 카드 매칭 게임을 나타냅니다.
 * JFrame을 확장하며 게임의 그래픽 사용자 인터페이스(GUI)를 포함합니다.
 */
public class CardMatchingHard extends JFrame {
    private JPanel cardPanel;
    private JPanel topPanel;  
    private JLabel timerLabel;  
    private JLabel scoreLabel;  
    private List<Card> cards;
    private Card selectedCard = null;
    private int pairsFound = 0;
    private Score cardScore;
    private static final String initialImagePath = "cardlogo.jpg";
    private static final int initialImageWidth = 102;
    private static final int initialImageHeight = 136;

    /**
     * CardMatchingEasy 클래스의 생성자입니다.
     * 게임 창을 초기화하고 사용자 인터페이스를 설정합니다.
     */
    public CardMatchingHard() {
        setTitle("엎어라 뒤집어라");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        topPanel = new JPanel(new FlowLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        scoreLabel = new JLabel("Score: 0");
        topPanel.add(scoreLabel);
        topPanel.setBackground(new Color(125, 159, 104));
        add(topPanel, BorderLayout.NORTH);


        cardPanel = new JPanel(new GridLayout(6, 8, 10, 10));
        cardPanel.setBackground(new Color(237, 227, 206));
        cards = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            String imagePath = "hard/hard" + (i + 1) + ".jpg";
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
        pack(); // 내용에 딱맞게 화면 조정
        setLocationRelativeTo(null); //화면 센터로 고정
        setVisible(true);
    }
    
    /**
     * 게임의 모든 카드에 대한 이미지를 초기화합니다.
     * 각 카드의 기본 이미지 아이콘을 초기 이미지의 크기에 맞게 조절합니다.
     */
    private void initializeCardImages() {
        ImageIcon defaultIcon = new ImageIcon(initialImagePath);
        Image img = defaultIcon.getImage().getScaledInstance(initialImageWidth, initialImageHeight, Image.SCALE_SMOOTH);
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
        public Card(ImageIcon icon) {
            this.icon = icon;
            setPreferredSize(new Dimension(initialImageWidth, initialImageHeight));
            setIcon(icon);
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
        private ImageIcon scaleIcon(ImageIcon originalIcon, Dimension size) {
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
        /**
         * 카드에 대한 마우스 클릭 이벤트를 처리합니다.
         * 클릭한 카드가 뒷면인 경우 카드의 앞면을 보여줍니다.
         * 두 개의 카드가 선택된 경우 일치 여부를 확인합니다.
         *
         * @param e 마우스 이벤트입니다.
         */
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
                    	cardScore.increaseScore(2); // 점수 업데이트
                        scoreLabel.setText("Score: " + cardScore.getScore()); // 레이블 업데이트
                        pairsFound++;
                        if (pairsFound == 24) {
                            JOptionPane.showMessageDialog(null, "You win!");
                        }
                        selectedCard.setEnabled(false);
                        clickedCard.setEnabled(false);
                        selectedCard = null;
                    } else {
                    	Timer cardTimer = new Timer(50, new ActionListener() {
                    		/**
                    		 * Timer 이벤트에 대한 동작을 처리하는 ActionListener 구현체입니다.
                    		 * 선택된 카드가 존재하는 경우 해당 카드를 뒷면으로 뒤집고, 이미지를 초기 크기로 조절합니다.
                    		 * 선택된 카드가 없는 경우 아무 동작도 수행하지 않습니다.
                    		 *
                    		 * @param e Timer 이벤트입니다.
                    		 */
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
                    	cardTimer.setRepeats(false);
                        cardTimer.start();
                    }
                }
            }
        }
    	
    	/**
    	 * 이미지 아이콘을 지정된 크기로 조절하는 유틸리티 메서드입니다.
    	 *
    	 * @param originalIcon 조절할 원본 이미지 아이콘입니다.
    	 * @param size 조절된 이미지의 크기를 나타내는 Dimension 객체입니다.
    	 * @return 지정된 크기로 조절된 이미지 아이콘을 나타내는 ImageIcon 객체입니다.
    	 */
        private ImageIcon scaleIcon(ImageIcon originalIcon, Dimension size) {
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
        SwingUtilities.invokeLater(() -> new CardMatchingHard());
    }
}
