package water.ustc.db.score;

public class ScoreBean {
	
	private ScoreDAO scoreDAO;
	
	private String subject;
	private String userId;
	private Integer score;
	private Boolean pass;
	
	public ScoreBean() {
		// TODO Auto-generated constructor stub
		System.out.println("This is constructor ScoreBean().");
	}
	
	public ScoreBean(String subject, String userId, Integer score, Boolean pass) {
		// TODO Auto-generated constructor stub
		this.subject = subject;
		this.userId = userId;
		this.score = score;
		this.pass = pass;
	}

	public ScoreDAO getScoreDAO() {
		return scoreDAO;
	}

	public String getSubject() {
		return subject;
	}

	public String getUserId() {
		return userId;
	}

	public Integer getScore() {
		return score;
	}

	public Boolean getPass() {
		return pass;
	}

	public void setScoreDAO(ScoreDAO scoreDAO) {
		this.scoreDAO = scoreDAO;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public void setPass(Boolean pass) {
		this.pass = pass;
	}

}
