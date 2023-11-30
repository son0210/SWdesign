package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 난이도 하의 카드 매칭 게임을 나타냅니다.
 * JFrame을 확장하며 게임의 그래픽 사용자 인터페이스(GUI)를 포함합니다.
 */
public class CardMatchingEasy extends JPanel {
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

    protected MainPage mainPage;
    private CardLayout cardLayout;
    private JPanel panel;
    private JPanel panelG;
	private String[] data;

    /**
     * CardMatchingEasy 클래스의 생성자입니다.
     * 게임 창을 초기화하고 사용자 인터페이스를 설정합니다.
     */
    public CardMatchingEasy(CardLayout layout, JPanel panel, MainPage mainPage, String[] data) {
        cardLayout = layout;
        this.panel = panel;
        this.mainPage = mainPage;

        setVisible(true);
        setLayout(cardLayout);

        panelG = new JPanel();
        panelG.setLayout(new GridBagLayout());

        score = new Score();
        scoreLabel = new JLabel("Score:0");
        topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
        topPanel.setBackground(Utility.maincolor);
        
        GridBagConstraints gbc = new GridBagConstraints();

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
        button.setBackground(Utility.maincolor);
        button.setBorderPainted(false);
        button.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                addpausePanel(cardLayout, panel, mainPage); }
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(Utility.pointcolor); }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(Utility.maincolor); } });
        topPanel.add(button, gbc);

        //타이머 추가
        Level1Timer level1timer = new Level1Timer();
        JProgressBar timerVisible = level1timer.getProgressBar();
        topPanel.add(timerVisible);

        // Add the JProgressBar to the center
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        topPanel.add(timerVisible, gbc);
        
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panelG.add(topPanel, gbc);

        cardPanel = new JPanel(new GridLayout(4, 4, 10, 10));
        cardPanel.setBackground(Utility.backcolor);
        cards = new ArrayList<>();

        Dimension parentFrame = Toolkit.getDefaultToolkit().getScreenSize();
        int cardWidth = (int) (parentFrame.getWidth()* 0.09);
        int cardHeight = (int) (parentFrame.getHeight()* 0.18);
        for (int i = 0; i < 8; i++) {
            String imagePath = "src/image/easy/easy" + (i + 1) + ".jpg";
            ImageIcon icon = new ImageIcon(imagePath);

            for (int j = 0; j < 2; j++) {
                Card card = new Card(icon, cardWidth, cardHeight);
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
        centerPanel.add(cardPanel);
        centerPanel.setBackground(Utility.backcolor);

        gbc.gridy = 1;
        panelG.add(centerPanel,gbc);

        setVisible(true);
        add(panelG,"EasyPanel");
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
            card.setPreferredSize(new Dimension(cardWidth, cardHeight));
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
                            if (pairsFound == 8) {
//                            	score.timerScore = level1timer.getTimerValue();
                                addfinishPanel(cardLayout, panel, mainPage);
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
        private ImageIcon scaleIcon(ImageIcon originalIcon, Dimension size) {
            Image image = originalIcon.getImage();
            Image scaledImage = image.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
    }

    private void addpausePanel(CardLayout layout, JPanel panel, MainPage mainPage) {
        cardLayout = layout;
        this.panel = panel;
        this.mainPage = mainPage;

        // 새로운 패널 생성
        PausePage pausePanel = new PausePage(layout, panel, mainPage);

        // 기존 패널에 새로운 패널 추가
        panelG.add(pausePanel.getPausePanel(),"pausePanel");

        CardLayout cardLayout = (CardLayout) panelG.getLayout();
        cardLayout.show(panelG, "pausePanel");
    }
    
    private void addfinishPanel(CardLayout layout, JPanel panel, MainPage mainPage) {

        cardLayout = layout;
        this.panel = panel;
        this.mainPage = mainPage;

        // 새로운 패널 생성
        FinishPage finishPanel = new FinishPage(layout, panel, mainPage);

        // 기존 패널에 새로운 패널 추가
        panel.add(finishPanel.getFinishPanel(), "finishPanel");

        // 카드를 새로운 패널로 전환
        cardLayout = (CardLayout) panel.getLayout();
        cardLayout.show(panel, "finishPanel");
    }
}