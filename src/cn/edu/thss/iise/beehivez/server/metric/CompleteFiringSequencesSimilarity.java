package cn.edu.thss.iise.beehivez.server.metric;

import cn.edu.nju.cfssimilar.CFSSimilarityCalculator;
import org.processmining.framework.models.petrinet.PetriNet;

public class CompleteFiringSequencesSimilarity extends PetriNetSimilarity {

    @Override
    public String getDesription() {
        // TODO Auto-generated method stub
//        return "Token Log similarity based on the TLPM";
        return "Complete Firing Sequence similarity based on PNML";
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
//        return "TokenLogProfileSimilarity";
        return "CompleteFiringSequenceSimilarity";
    }

    @Override
    public float similarity(PetriNet pn1, PetriNet pn2) {
        // TODO Auto-generated method stub
        return getSimilarityValue(pn1, pn2);
    }
    public float getSimilarityValue(PetriNet pn1, PetriNet pn2) {
        if(pn1==null|| pn2==null){
            return -1;
        }

        return CFSSimilarityCalculator.getSimilarityValue(pn1, pn2);
    }



}

