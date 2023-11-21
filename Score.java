// Score.java

package Ingame;

public class Score {
    private int cardScore;

    /**
     * Score 클래스의 생성자입니다.
     */
    public Score() {
        this.cardScore = 0;
    }

    /**
     * 현재 점수를 반환합니다.
     *
     * @return 현재 점수
     */
    public int getScore() {
        return cardScore;
    }

    /**
     * 점수를 증가시킵니다.
     *
     * @param points 증가시킬 점수
     */
    public void increaseScore(int points) {
        cardScore += points;
    }
}
