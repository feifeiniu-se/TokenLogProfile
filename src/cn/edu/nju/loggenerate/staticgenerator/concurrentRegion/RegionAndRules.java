package cn.edu.nju.loggenerate.staticgenerator.concurrentRegion;

import java.util.List;

public class RegionAndRules {

	private String[] partRegion;
	private List<List<String>> irrelevantPaths;
	private List<Rule> eliminateRules;
	private List<Rule> inserRules;
	
	public RegionAndRules(String[] partRegion, List<List<String>> irrelevantPaths, List<Rule> eliminateRules, List<Rule> inserRules){
		this.setPartRegion(partRegion);
		this.irrelevantPaths = irrelevantPaths;
		this.eliminateRules = eliminateRules;
		this.inserRules = inserRules;

		int pathNum = irrelevantPaths.size();
		int tasks = 0;
		long fenmu = 1;
		for(List<String> path : irrelevantPaths){
			tasks = tasks + path.size();
			fenmu = fenmu * ConcurrentAndLoopStatistics.sum(path.size());
		}
		ConcurrentAndLoopStatistics.totalAveragePathLength = ConcurrentAndLoopStatistics.totalAveragePathLength + (float) tasks / pathNum;
		ConcurrentAndLoopStatistics.numberOfTraces = (ConcurrentAndLoopStatistics.sum(tasks) / fenmu) * ConcurrentAndLoopStatistics.numberOfTraces;
	}
	
	public List<List<String>> getIrrelevantPaths() {
		return irrelevantPaths;
	}
	public void setIrrelevantPaths(List<List<String>> irrelevantPaths) {
		this.irrelevantPaths = irrelevantPaths;
	}
	public List<Rule> getEliminateRules() {
		return eliminateRules;
	}
	public void setEliminateRules(List<Rule> eliminateRules) {
		this.eliminateRules = eliminateRules;
	}
	public List<Rule> getInserRules() {
		return inserRules;
	}
	public void setInserRules(List<Rule> inserRules) {
		this.inserRules = inserRules;
	}

	public String[] getPartRegion() {
		return partRegion;
	}

	public void setPartRegion(String[] partRegion) {
		this.partRegion = partRegion;
	}
}
