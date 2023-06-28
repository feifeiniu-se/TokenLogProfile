package cn.edu.thss.iise.beehivez.server.metric;

import cn.edu.nju.loggenerate.modelengine.enginebeans.PetriLCY;
import cn.edu.nju.loggenerate.modelengine.flowbeans.Token;

import cn.edu.nju.tlpsimilar.main.TLPSimilarityCalculator;
import org.processmining.framework.models.petrinet.PetriNet;

import java.util.List;

public class TokenProfileSimilarity extends PetriNetSimilarity{

    @Override
    public String getDesription() {
        // TODO Auto-generated method stub
        return "Token Log similarity based on the TLPM";
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "TokenLogProfileSimilarity";
    }

    @Override
    public float similarity(PetriNet pn1, PetriNet pn2) {
        // TODO Auto-generated method stub
        return getSimilarityValue(new PetriLCY(pn1), new PetriLCY(pn2));
    }

    // ����������棺
    // (1)����ģ������token��־��
    // (2)������־����profile����
    // (3)����������һ��getSimilarityValue������������ƶ�ֵ��
    public float getSimilarityValue(PetriLCY petri1, PetriLCY petri2) {
        if(petri1==null|| petri2==null){
            return -1;
        }

        // �ֱ�����ģ�͵�token log
        List<Token> petriNet1Tokens = PetriLCY.getTokensOfPetriNet(petri1.getNet());
        List<Token> petriNet2Tokens = PetriLCY.getTokensOfPetriNet(petri2.getNet());

        return getSimilarityValue(petriNet1Tokens, petriNet2Tokens);
    }

    //
    public float getSimilarityValue(List<Token> log1, List<Token> log2) {
        return TLPSimilarityCalculator.getSimilarityValue(log1, log2);
    }

    public static void main(String[] args){

    }


}
