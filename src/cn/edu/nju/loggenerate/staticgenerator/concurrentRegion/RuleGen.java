package cn.edu.nju.loggenerate.staticgenerator.concurrentRegion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 规则的生成，包含剔除规则和插入活动规则
 * @author Administrator
 *
 */
public class RuleGen {


	private List<List<String>> irrelevantPaths;
	private List<List<String>> relevantPaths;
	private Map<String, Integer> irrelevantTNameMap = new HashMap<String, Integer>();
	
	public RuleGen(List<List<String>> irrelevantPaths, List<List<String>> relevantPaths){
		this.irrelevantPaths = irrelevantPaths;
		this.relevantPaths = relevantPaths;
		this.mapGen();
	}
	/**
	 * 剔除规则的生成
	 * @return
	 */
	public List<Rule> eliminateRules(){
		List<Rule> eliminateRules = new ArrayList<Rule>();
		for( List<String> relevantPath : relevantPaths ){
			for( int i = 0; i < relevantPath.size(); i++ ){
				String preTran = relevantPath.get(i);
				if( !irrelevantTNameMap.containsKey(preTran) ) continue;
				//如果已经是该路径最后的活动，那就break
				if( i == relevantPath.size()-1 )
					break;
				//如果后续还有活动
				int j = i+1;
				String postTran = relevantPath.get(j);
				//跳过不属于主路径上的活动
				while( !irrelevantTNameMap.containsKey(postTran) && j <= relevantPath.size()-1  ){
					postTran = relevantPath.get(++j);
				}
				//如果相邻的两个活动是不属于一个irrelevantPath的，那么他们之间存在着前后的约束关系，添加进规则
				if( irrelevantTNameMap.get(preTran) != irrelevantTNameMap.get(postTran) ){
					Rule rule = new Rule(preTran, postTran);
					eliminateRules.add(rule);
				}
				
			}
		}
		return eliminateRules;
	}
	
	/**
	 * 插入规则的生成
	 * @return
	 */
	public List<Rule> insertRules(){
		List<Rule> insertRules = new ArrayList<Rule>();
		for( List<String> relevantPath : relevantPaths ){
			for( int i = 1; i < relevantPath.size(); i++ ){
				
				String curTran = relevantPath.get(i);
				//如果当前活动是主路径活动，跳过
				if( irrelevantTNameMap.containsKey(curTran) ) continue;
				//如果当前活动不是主路径活动，那就是待插入活动
				String preTran = relevantPath.get(i-1);
				//如果当前活动已经是最后个活动了
				Rule rule;
				if( i == relevantPath.size()-1 ){
					rule = new Rule(preTran, curTran, null);
					
				}else{
					int j = i;
					String postTran = relevantPath.get(j+1);
					//跳过不属于主路径上的活动
					while( !irrelevantTNameMap.containsKey(postTran) && j <= relevantPath.size()-1  ){
						postTran = relevantPath.get(++j);
					}
					rule = new Rule(preTran, curTran, postTran);
				}
				insertRules.add(rule);
			}
		}
		return insertRules;
	}
	
	//------------------Utils
	private void mapGen(){
		//创建个Map，key为活动名称，value为名称所在的路径序号
		for(  int i = 0; i < irrelevantPaths.size(); i++ ){
			for( String tName : irrelevantPaths.get(i) ){
				irrelevantTNameMap.put(tName, i);
			}
		}
	}
}
