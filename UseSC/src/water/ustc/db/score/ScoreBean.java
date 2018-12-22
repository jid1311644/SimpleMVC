package water.ustc.db.score;

public class ScoreBean {
	
	private String subject;
	private String userId;
	private Integer score;
	private Boolean pass;
	
	public ScoreBean(String subject, String userId, Integer score, Boolean pass) {
		// TODO Auto-generated constructor stub
		this.subject = subject;
		this.userId = userId;
		this.score = score;
		this.pass = pass;
	}

}
