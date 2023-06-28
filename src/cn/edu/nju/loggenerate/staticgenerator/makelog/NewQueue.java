package cn.edu.nju.loggenerate.staticgenerator.makelog;

import java.util.LinkedList;
import java.util.List;

public class NewQueue{

	public LinkedList<List<String>> tracesQueue;
	private LinkedList<List<String>> tracesQueueCopy;
	private LinkedList<Integer> nextNumQueue;
	private LinkedList<Integer> nextNumQueueCopy;
	private Integer nextNum;

	public NewQueue(){
		this.tracesQueue = new LinkedList<List<String>>();
		this.tracesQueueCopy = new LinkedList<List<String>>();
		this.nextNumQueue = new LinkedList<Integer>();
		this.nextNumQueueCopy = new LinkedList<Integer>();
		this.nextNum = 0;
	}
	
	public void reStart(){
		this.tracesQueue = (LinkedList<List<String>>) this.tracesQueueCopy.clone();
		this.nextNumQueue = (LinkedList<Integer>) this.nextNumQueueCopy.clone();
	}

	public List<String> pollList() {
		List<String> nextList = this.tracesQueue.poll();
		Integer nextNumTmp = this.nextNumQueue.poll();
		
		this.nextNum = nextNumTmp;
		return nextList;
	}
	
	public int pollNextNum(){
		return this.nextNum;
	}
	
	public void offer(List<String> list, int nextNum){
		this.tracesQueue.offer(list);
		this.tracesQueueCopy.offer(list);
		this.nextNumQueue.offer(nextNum);
		this.nextNumQueueCopy.offer(nextNum);
	}
	
	public void close(){
		
	}
	
	public LinkedList<List<String>> closeWithoutDelete(){
		return this.tracesQueueCopy;
	}
	
}
