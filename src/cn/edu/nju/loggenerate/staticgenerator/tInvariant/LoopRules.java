package cn.edu.nju.loggenerate.staticgenerator.tInvariant;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.loggenerate.staticgenerator.model.ProcessModel;

public class LoopRules {

	private ProcessModel partloopModel;//循环结构，去除了主路径上的活动，用来做P不变量分解的
	private ProcessModel mainModel;//主路径结构，去除了循环上的活动，用来做P不变量分解的
	private String[] insertBetween;//插入循环的活动开始和结束，是一个2个元素的数组
	private int loopLevel;//第几层循环，有了subRules，这个概念就没什么用了
	private String singlePlaceName;//就是单个库所的循环，和主路径只有一个相交库所，这样的话，mainModel是null，不需要对mainModel做P不变量分解
	
	private List<LoopRules> subRules;//依附在这个model下的loop，是一个树形结构
	private ProcessModel oriModel;//原始的model，由这个model拆分为partloopModel和mainModel
	private ProcessModel newLoopModel;//使用新的循环生成的model，之前走弯路了...
	
	public LoopRules(){
		
	}
	
	public LoopRules(ProcessModel oriModel, ProcessModel partloopModel, ProcessModel mainModel, String[] insertBetween, int loopLevel ){
		this.oriModel = oriModel;
		this.partloopModel = partloopModel;
		this.mainModel = mainModel;
		this.insertBetween = insertBetween;
		this.loopLevel = loopLevel;
	}
	
	public LoopRules(ProcessModel oriModel, ProcessModel newLoopModel, String[] insertBetween, int loopLevel ){
		this.oriModel = oriModel;
		this.newLoopModel = newLoopModel;
		this.insertBetween = insertBetween;
		this.loopLevel = loopLevel;
	}
	
	public LoopRules(ProcessModel partloopModel, ProcessModel newLoopModel , String singlePlaceName, String[] insertBetween, int loopLevel ){
		this.oriModel = partloopModel;
		this.partloopModel = partloopModel;
		this.newLoopModel = newLoopModel;
		this.singlePlaceName = singlePlaceName;
		this.insertBetween = insertBetween;
		this.loopLevel = loopLevel;
	}
	
	public LoopRules(ProcessModel partloopModel, String singlePlaceName, String[] insertBetween, int loopLevel ){
		this.oriModel = partloopModel;
		this.partloopModel = partloopModel;
		this.singlePlaceName = singlePlaceName;
		this.insertBetween = insertBetween;
		this.loopLevel = loopLevel;
	}
	
	public void addSubRules(LoopRules loopRules){
		if( null == subRules ){
			this.subRules = new ArrayList<LoopRules>(); 
		}
		subRules.add(loopRules);
	}
	
	public ProcessModel getPartloopModel() {
		return partloopModel;
	}
	public void setPartloopModel(ProcessModel partloopModel) {
		this.partloopModel = partloopModel;
	}
	public ProcessModel getMainModel() {
		return mainModel;
	}
	public void setMainModel(ProcessModel mainModel) {
		this.mainModel = mainModel;
	}
	public int getLoopLevel() {
		return loopLevel;
	}
	public void setLoopLevel(int loopLevel) {
		this.loopLevel = loopLevel;
	}
	public String[] getInsertBetween() {
		return insertBetween;
	}
	public void setInsertBetween(String[] insertBetween) {
		this.insertBetween = insertBetween;
	}

	public String getSinglePlaceName() {
		return singlePlaceName;
	}

	public void setSinglePlaceName(String singlePlaceName) {
		this.singlePlaceName = singlePlaceName;
	}

	public ProcessModel getOriModel() {
		return oriModel;
	}

	public void setOriModel(ProcessModel oriModel) {
		this.oriModel = oriModel;
	}

	public List<LoopRules> getSubRules() {
		return subRules;
	}

	public void setSubRules(List<LoopRules> subRules) {
		this.subRules = subRules;
	}

	public ProcessModel getNewLoopModel() {
		return newLoopModel;
	}

	public void setNewLoopModel(ProcessModel newLoopModel) {
		this.newLoopModel = newLoopModel;
	}

}
