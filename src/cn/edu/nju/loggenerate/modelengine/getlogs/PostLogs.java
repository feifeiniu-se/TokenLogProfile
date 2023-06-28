/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.edu.nju.loggenerate.modelengine.getlogs;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.loggenerate.modelengine.enginebeans.OutTimeTokens;
import cn.edu.nju.loggenerate.modelengine.enginebeans.TransitionList;
import cn.edu.nju.loggenerate.modelengine.flowbeans.Token;
import cn.edu.nju.loggenerate.modelengine.flowbeans.Transition;
import cn.edu.nju.loggenerate.modelengine.logbeans.PostLogBean;

/**
 *
 * @author LiChuanyi
 */
public class PostLogs {
    
    public static String getLog(){
    	OutTimeTokens.setUnhandles();
        
    	List<PostLogBean> posts = new ArrayList<PostLogBean>();
    	
    	for(Transition t : TransitionList.getTransitions()){
    		List<Token> t_tokens = new ArrayList<Token>();
    		
    		for(Token token : OutTimeTokens.getAll()){
    			if(!token.handled){
    				if(token.getProducer() == t.getTransitionID()){
    					token.handled = true;
    					t_tokens.add(token);
    				}
    			}
    		}
    		List<PostLogBean> t_posts = new ArrayList<PostLogBean>();
    		
    		for(Token token : t_tokens){
    			boolean already = false;
    			PostLogBean postLogBean = new PostLogBean(t, token.getProduceTime());
    			if(token.getConsumer() > 0){
    				postLogBean.addPost(TransitionList.getByID(token.getConsumer()));
    			}
    			for(PostLogBean plb : t_posts){
    				if(postLogBean.hasSameTime(plb)){
    					already = true;
    					plb.addPost(TransitionList.getByID(token.getConsumer()));
    					break;
    				}
    			}
    			if(!already){
    				t_posts.add(postLogBean);
    			}
    		}
    		
    		for(int i = 0; i < t_posts.size(); i ++){
    			if(!t_posts.get(i).handled){
    				for(int j = i+1; j < t_posts.size(); j ++){
    					if(!t_posts.get(j).handled){
    						if(t_posts.get(i).equals(t_posts.get(j))){
    							t_posts.get(i).addInstance();
    							t_posts.get(j).handled = true;
    						}
    					}
    				}
    				posts.add(t_posts.get(i));
    			}
    		}
    	}
    	
    	String log = "";
    	
    	int eventnum = 0;
    	
    	for(PostLogBean plb : posts){
    		eventnum = eventnum + plb.getTimes();
    		log = log + plb.saveToFile() + "\r\n";
    	}
    	
    	//System.out.println(eventnum);
    	return log;
    }
}
