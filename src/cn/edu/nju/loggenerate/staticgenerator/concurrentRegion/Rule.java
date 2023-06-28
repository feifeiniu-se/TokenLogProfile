package cn.edu.nju.loggenerate.staticgenerator.concurrentRegion;

public class Rule {

	private String preTran;
	private String postTran;
	private String curTran;
	
	public Rule(){
	}
	
	public Rule(String preTran, String postTran){
		this.preTran = preTran;
		this.postTran = postTran;
	}
	
	public Rule(String preTran, String curTran, String postTran){
		this(preTran, postTran);
		this.curTran = curTran;
	}

	public String getPreTran() {
		return preTran;
	}

	public void setPreTran(String preTran) {
		this.preTran = preTran;
	}

	public String getPostTran() {
		return postTran;
	}

	public void setPostTran(String postTran) {
		this.postTran = postTran;
	}

	public String getCurTran() {
		return curTran;
	}

	public void setCurTran(String curTran) {
		this.curTran = curTran;
	}
	
	@Override
	public String toString() {
		return "preTran : "+preTran+" postTran : "+postTran+" curTran : "+curTran;
	}
}
