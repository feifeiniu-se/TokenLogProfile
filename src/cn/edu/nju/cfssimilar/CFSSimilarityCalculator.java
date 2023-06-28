package cn.edu.nju.cfssimilar;

import cn.edu.thss.iise.beehivez.server.basicprocess.CTree;
import cn.edu.thss.iise.beehivez.server.basicprocess.CTreeGenerator;
import cn.edu.thss.iise.beehivez.server.basicprocess.mymodel.MyPetriNet;
import org.processmining.framework.models.petrinet.PetriNet;

public class CFSSimilarityCalculator {
    public static float getSimilarityValue(PetriNet pn1, PetriNet pn2){
        // TODO Auto-generated method stub
        float x = (float) 5.0;
        long time1 = System.nanoTime();
        NewPtsSet ptsSet1 = computeSequenceSet(pn1);
        long time2 = System.nanoTime();
        NewPtsSet ptsSet2 = computeSequenceSet(pn2);
        long time3 = System.nanoTime();
        double[][] seqM = computeSeqMatrix(ptsSet1, ptsSet2);
        double[][] simMatrix_astar = new double[2][2];
        float sim = (float)computeSimilarityForTwoNet_Astar(seqM,ptsSet1,ptsSet2);
        long time4 = System.nanoTime();
//        System.out.print(time2-time1+"\t");
//        System.out.print(time3-time2+"\t");
//        System.out.print(time4-time3+"\t");
//        System.out.println();

        return sim;
    }

    public static NewPtsSet computeSequenceSet(PetriNet petrinet){
        CTree ctree = null;
        CTreeGenerator generator = new CTreeGenerator(
                MyPetriNet.PromPN2MyPN(petrinet));
        ctree = generator.generateCTree();
        TTreeGenerator ttg = new TTreeGenerator();
        NewPtsSet nps = ttg.generatTTree(ctree, 2, "petri");//???????
//        nps.showSet();
        return nps;//???????
    }

    /**
     * 计算两触发序列集合的矩阵，矩阵的值为触发序列的相似度
     * @param seqSet1
     * @param seqSet2
     * @return
     */
    public static double[][] computeSeqMatrix(NewPtsSet seqSet1, NewPtsSet seqSet2){
        if(seqSet1.getNPSet().size()<=seqSet2.getNPSet().size()){
            double[][] seqM = new double[seqSet1.getNPSet().size()][seqSet2.getNPSet().size()];
            for(int i = 0;i<seqSet1.getNPSet().size();i++){
                for(int j = 0;j<seqSet2.getNPSet().size();j++){
                    seqM[i][j] = seqSet1.getNPSet().get(i).SequenceSimilarity(seqSet2.getNPSet().get(j));
                }
//				System.out.println(i);
            }
            return seqM;
        }
        else{
            double[][] seqM = new double[seqSet2.getNPSet().size()][seqSet1.getNPSet().size()];
            for(int i = 0;i<seqSet2.getNPSet().size();i++){
                for(int j = 0;j<seqSet1.getNPSet().size();j++){
                    seqM[i][j] = seqSet2.getNPSet().get(i).SequenceSimilarity(seqSet1.getNPSet().get(j));
                }
            }
            return seqM;
        }
    }

    /**
     * 给定两个模型序列集合，返回相似度的值，A*算法

     */
    public static double computeSimilarityForTwoNet_Astar(double[][] seqM, NewPtsSet seqSet1, NewPtsSet seqSet2){
        if(seqSet1.getNPSet().size()<=seqSet2.getNPSet().size()){
            return seqSet1.setSimilarity_Astar(seqM,seqSet2);
        }
        else{
            return seqSet2.setSimilarity_Astar(seqM,seqSet1);
        }

    }

    public static void main(String[] args){

    }

}
