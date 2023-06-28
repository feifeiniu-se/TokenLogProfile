package cn.edu.nju.loggenerate.modelengine.enginemain;

public class EngineCase {

	private static int engineCase;

	public static void initiallize() {
		engineCase = 0;
	}

	public static void caseGoOn() {
		EngineCase.engineCase++;
	}

	public static int getEngineCase() {
		return EngineCase.engineCase;
	}

}
