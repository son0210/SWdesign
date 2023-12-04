package Game;

/** 게임의 점수를 계산하여 나타냅니다. */
public class Score {
	/** 카드에서 얻은 점수 */
    public int cardScore;
    /** 타이머에서 얻은 점수 */
    public int timerScore;
    /** 최종 총 점수 */
    public int finalscore;
    
    /** 초기 카드 점수와 타이머 점수가 0인 Score객체를 생성 */
    public Score() {
        this.cardScore = 0;
        this.timerScore = 0;
    }
    
    /**
     * 지정된 포인트만큼 카드 점수를 증가시킵니다.
     * @param points 카드 점수를 증가시킬 포인트
     */
    public void increaseScore(int points) {
        this.cardScore += points;
    }

    /**
     * 현재 카드 점수를 가져옵니다.
     * @return 현재 카드 점수
     */
    public int getScore() {
        return cardScore;
    }
    
    /**
     * 타이머 점수를 저장합니다.
     * @param timerScore 남은 타이머 점수
     */
    public void TimerScore(int timerScore) {
        this.timerScore = timerScore * 10;
    }
    
    /**
     * 총 점수를 반환합니다.
     * @return 총 점수
     */
    public int totalScore() {
        return timerScore + cardScore;
    }
}
