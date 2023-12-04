package Game;

public class Score {
    public int cardScore;
    public int timerScore;
    public int finalscore;
    
    /**
     * Score 클래스의 생성자입니다.
     */
    public Score() {
        this.cardScore = 0;
    }
    
    /**
     * 점수를 증가시킵니다.
     *
     * @param points 증가시킬 점수
     */
    public void increaseScore(int points) {
        this.cardScore += points;
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
     * 타이머 점수를 저장합니다.
     *
     * @param timerScore 타이머 점수
     */
    public void TimerScore(int timerScore) {
        this.timerScore = timerScore;
    }
    
    /**
     * 총 점수를 반환합니다.
     *
     * @return 총 점수
     */
    public int totalScore() {
        return timerScore + cardScore;
    }
}
