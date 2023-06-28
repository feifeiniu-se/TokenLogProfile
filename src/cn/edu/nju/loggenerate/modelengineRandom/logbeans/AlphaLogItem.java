package cn.edu.nju.loggenerate.modelengineRandom.logbeans;

import java.util.Calendar;

//import cn.edu.nju.loggenerate.modelengineRandom.flowbeans.NewToken;


public class AlphaLogItem {

	private static String ProcessInstance = "		<ProcessInstance id=\"";

	public static String AuditTrailEntry = "AuditTrailEntry";
	public static String WorkflowModelElement = "WorkflowModelElement";
	public static String EventType = "				<EventType>complete</EventType>";
	public static String Timestamp = "Timestamp";
	public static String time_pre = "2013-04-28T";
	public static String time_post = ".887+01:00";
	public static String Originator = "				<Originator>UNDEFINED</Originator>";

//	public static String getProcessInstance(NewToken token) {
//		String item = ProcessInstance + token.getCaseID() + "\">\r\n";
//		Calendar rightNow = Calendar.getInstance();
//		for (Transition t : token.getTransitions()) {
//			item = item + "			<" + AuditTrailEntry + ">\r\n";
//			item = item + "				<" + WorkflowModelElement + ">" + t.getName() + "</" + WorkflowModelElement + ">\r\n";
//			item = item + EventType + "\r\n";
//			item = item + "				<" + Timestamp + ">" + makeTime(rightNow) + time_post + "</" + Timestamp + ">\r\n";
//			item = item + Originator + "\r\n";
//			item = item + "			</" + AuditTrailEntry + ">\r\n";
//			rightNow.add(Calendar.MINUTE, 1);
//		}
//		item = item + "		</ProcessInstance>\r\n";
//		return item;
//	}
	
	public static String makeTime(Calendar rightNow){
		int year = rightNow.get(Calendar.YEAR);
		int month = rightNow.get(Calendar.MONTH);
		int date = rightNow.get(Calendar.DATE);
		int hour = rightNow.get(Calendar.HOUR);
		int minite = rightNow.get(Calendar.MINUTE);
		int second = rightNow.get(Calendar.SECOND);
		
		return year + "-0" + month + "-" + date + "T" + hour + ":" + minite + ":" + second;
	}

	public static String getProcessInstance(String trace) {
		String[] traceContent = trace.split(":");
		String[] tasks = traceContent[1].split(",");
		Calendar rightNow = Calendar.getInstance();
		String item = ProcessInstance + traceContent[0] + "\">\r\n";
		for (String taskName : tasks) {
			item = item + "			<" + AuditTrailEntry + ">\r\n";
			item = item + "				<" + WorkflowModelElement + ">" + taskName + "</" + WorkflowModelElement + ">\r\n";
			item = item + EventType + "\r\n";
			item = item + "				<" + Timestamp + ">" + makeTime(rightNow) + time_post + "</" + Timestamp + ">\r\n";
			item = item + Originator + "\r\n";
			item = item + "			</" + AuditTrailEntry + ">\r\n";
			rightNow.add(Calendar.MINUTE, 1);
		}
		item = item + "		</ProcessInstance>\r\n";
		return item;
	}
	
	public static String getProcessInstanceXES(String trace) {
		String[] traceContent = trace.split(":");
		String[] tasks = traceContent[1].split(",");
		Calendar rightNow = Calendar.getInstance();
		String item = "	<trace>\r\n" + "		<string key=\"concept:name\" value=\"Order: " + traceContent[0]+ "\"/>\r\n";
		for (String taskName : tasks) {
			item = item + "		<event>\r\n";
			item = item + "			<string key=\"org:group\" value=\"Purchase\"/>\r\n";
			item = item + "			<string key=\"lifecycle:transition\" value=\"complete\"/>\r\n";
			item = item + "			<date key=\"time:timestamp\" value=\"" + makeTime(rightNow) + "\"/>\r\n";
			item = item + "			<string key=\"concept:name\" value=\"" + taskName + "\"/>\r\n";
			item = item + "		</event>\r\n";
			rightNow.add(Calendar.MINUTE, 5);
		}
		item = item + "	</trace>\r\n";
		return item;
	}
	
}
