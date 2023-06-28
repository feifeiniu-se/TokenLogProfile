package cn.edu.nju.loggenerate.staticgenerator.concurrentRegion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.nju.loggenerate.staticgenerator.model.Place;
import cn.edu.nju.loggenerate.staticgenerator.model.ProcessModel;
import cn.edu.nju.loggenerate.staticgenerator.model.Transition;

/**
 * 计算局部最大并发区间
 * @author Administrator
 *
 */
public class ConcurrentRegion {

	private ProcessModel model;
	
	private List<String> mainPathTNames;
	private List<RegionAndRules> RegionAndRulesList;
	
	public ConcurrentRegion(ProcessModel model){
		this.model = model;
	}

	public void genConcurrentResions(List<ProcessModel> models){
		List<List<String>> actionsList = this.sortActions(models, model.getStartPlace().getName());
		List<String[]> partRegions = this.getConcurrentPartRegion(models, model.getStartPlace().getName());
		RegionAndRulesList = new ArrayList<RegionAndRules>();
		for( String[] partRegion : partRegions ){
			String begin = partRegion[0];
			String end = partRegion[1];
			List<List<String>> concurrentRegion = new ArrayList<List<String>>();
			for( List<String> actions : actionsList ){
				List<String> concurrentPath = new ArrayList<String>();
				int indexBegin = actions.indexOf(begin);
				int indexEnd = actions.indexOf(end);
				for( int i = indexBegin+1; i < indexEnd; i++ ){
					concurrentPath.add(actions.get(i));
				}
				if(concurrentPath.size() == 0) continue;
				//如果不存在，就是没有重复，就添加
				if( !isExistInConcurrentRegion( concurrentRegion, concurrentPath ) )
					concurrentRegion.add(concurrentPath);
			}
			List<List<String>> irrelevantPaths;
			List<List<String>> relevantPaths;
			if( concurrentRegion.size() == 1 ){
				irrelevantPaths = concurrentRegion;
				relevantPaths = new ArrayList<List<String>>();
			}else{
				FindLog findLog = new FindLog();
				findLog.findLongList(concurrentRegion);
				irrelevantPaths = findLog.getUnrelateList();
				relevantPaths = findLog.getRelateList();
			}
			
			RuleGen ruleGen = new RuleGen(irrelevantPaths, relevantPaths);
			List<Rule> eliminateRules = ruleGen.eliminateRules();
			List<Rule> insertRules = ruleGen.insertRules();
			RegionAndRules regionAndRules = new RegionAndRules(partRegion, irrelevantPaths, eliminateRules, insertRules);
			RegionAndRulesList.add(regionAndRules);
		}
	}
	
	
	private boolean isExistInConcurrentRegion(List<List<String>> concurrentRegion, List<String> concurrentPath ){
		//如果原来是空的，就是不存在
		if( concurrentRegion.size() == 0 ){
			return false;
		}
		for( List<String> existPath : concurrentRegion ){
			//个数不对，直接返回不存在
			if( existPath.size() != concurrentPath.size() )
				continue;
			
			int count = 0;
			for( int i = 0; i < concurrentPath.size(); i++ ){
				
				if( existPath.get(i).equals(concurrentPath.get(i)) ){
					count++;
				}
			}
			//存在重复
			if( existPath.size() == count ){
				return true;
			}				
		}
		return false;
	}
	
	
	private List<String[]> getConcurrentPartRegion(List<ProcessModel> models, String startPlaceName){
		this.mainPathTNames = this.getCommonTransition(models, startPlaceName);
		List<String[]> currentPartRegions = new ArrayList<String[]>();
		for( int i = 0; i < mainPathTNames.size(); i++ ){
			if( i == mainPathTNames.size()-1 ) break;
			String begin = mainPathTNames.get(i);
			String end = mainPathTNames.get(i+1);
			if( this.isAndSplit(begin) && this.isAndJoin(end) ){
				currentPartRegions.add(new String[]{begin, end});
			}
		}
		return currentPartRegions;
	}
	
	/**
	 * 排序好的P不变量活动序列
	 * @param models
	 * @param startPlaceName
	 * @return
	 */
	private List<List<String>> sortActions(List<ProcessModel> models, String startPlaceName){
		List<List<String>> list = new ArrayList<List<String>>();
		for( ProcessModel model : models ){
			List<String> sequence = getSequence(model, startPlaceName);
			list.add(sequence);
		}
		return list;
	}
	
	/**
	 * 分析P不变量,得出每个路径的共同变迁，也就是主路径活动
	 */
	private List<String> getCommonTransition(List<ProcessModel> models, String startPlaceName){
		int num = models.size();//一共有多少P不变量路径
		List<String> tempList = new ArrayList<String>();
		//统计每个Transition在各个P不变量路径中出现的次数
		Map<String, Integer> statMap = new HashMap<String, Integer>(); 
		for( ProcessModel model : models ){
			List<Transition> transitions = model.getTransitions().getTransitions();
			for( Transition t : transitions ){
				String tName = t.getName();
				if( statMap.containsKey(tName) ){
					statMap.put(tName, statMap.get(tName)+1);
				}else{
					statMap.put(tName, 1);
				}
			}
		}
		//如果Transition的次数和P不变量的路径相同，就认为是主路径活动
		for( String tName : statMap.keySet() ){
			if( num == statMap.get(tName) )
				tempList.add(tName);
		}
		//由于不知道变迁的执行顺序，所以要重新排列下 之所以选取第一条是因为P不变量中的路径包含所有主路径任务
		List<String> tNameSequence = this.getSequence(models.get(0), startPlaceName);
		List<String> mainPathTNames = new ArrayList<String>();
		for( String tName : tNameSequence ){
			if( tempList.contains(tName) )
				mainPathTNames.add(tName);
		}
		return mainPathTNames;
	}
	

	/**
	 * 对这个P不变量的活动进行顺序排序 
	 * @param model
	 * @param startPlaceName
	 * @return
	 */
	private List<String> getSequence(ProcessModel model, String startPlaceName){
		Place place = model.getPlaceByName(startPlaceName);
		List<String> tNameSequence = new ArrayList<String>();
		while(!place.isIsLast()){
			String tName = place.getOutputs().get(0);
			List<String> tNames = place.getOutputs();
			Transition transition = model.getTransitionByName(tName);
			tNameSequence.add(transition.getName());
			//这里应对的是testNew3的情况
			String pName = null;
			List<String> pNames = transition.getOutputs();
			if( pNames.size() == 1 ){
				pName =pNames.get(0);
			}else{
				for( String nominatePName : pNames ){
					Place tempPlace = model.getPlaceByName(nominatePName);
					String tempT = tempPlace.getOutputs().get(0);
					if(model.getTransitionByName(tempT).getInputs().size() == 1 ){
						pName = nominatePName;
						break;
					}
				}
			}
			place = model.getPlaceByName(pName);
		}
		return tNameSequence;
	}
	
	/**
	 * 判断一个变迁是否是and-split
	 * @param model
	 * @param tName
	 * @return
	 */
	private boolean isAndSplit(String tName){
		Transition t = model.getTransitionByName(tName);
		return t.getOutputs().size() >=2?true:false;
		
	}
	
	/**
	 * 判断一个变迁是否是and-join
	 * @param model
	 * @param tName
	 * @return
	 */
	private boolean isAndJoin(String tName){
		Transition t = model.getTransitionByName(tName);
		return t.getInputs().size() >=2?true:false;
	}
	

	//--------------------Getter&Setter
	public List<String> getMainPathTNames() {
		return mainPathTNames;
	}
	public void setMainPathTNames(List<String> mainPathTNames) {
		this.mainPathTNames = mainPathTNames;
	}
	public List<RegionAndRules> getRegionAndRulesList() {
		return RegionAndRulesList;
	}
	public void setRegionAndRulesList(List<RegionAndRules> regionAndRulesList) {
		RegionAndRulesList = regionAndRulesList;
	}
}
