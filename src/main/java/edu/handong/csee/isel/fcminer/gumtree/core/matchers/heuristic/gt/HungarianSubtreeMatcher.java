package edu.handong.csee.isel.fcminer.gumtree.core.matchers.heuristic.gt;

import edu.handong.csee.isel.fcminer.gumtree.core.utils.HungarianAlgorithm;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.MappingStore;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.MultiMappingStore;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;

import java.util.*;

public class HungarianSubtreeMatcher extends AbstractSubtreeMatcher {

    public HungarianSubtreeMatcher(ITree src, ITree dst, MappingStore store) {
        super(src, dst, store);
    }

    @Override
    public void filterMappings(MultiMappingStore multiMappings) {
        List<MultiMappingStore> ambiguousList = new ArrayList<>();
        Set<ITree> ignored = new HashSet<>();
        for (ITree src: multiMappings.getSrcs())
            if (multiMappings.isSrcUnique(src))
                addMappingRecursively(src, multiMappings.getDst(src).iterator().next());
            else if (!ignored.contains(src)) {
                MultiMappingStore ambiguous = new MultiMappingStore();
                Set<ITree> adsts = multiMappings.getDst(src);
                Set<ITree> asrcs = multiMappings.getSrc(multiMappings.getDst(src).iterator().next());
                for (ITree asrc : asrcs)
                    for (ITree adst: adsts)
                        ambiguous.link(asrc ,adst);
                ambiguousList.add(ambiguous);
                ignored.addAll(asrcs);
            }

        Collections.sort(ambiguousList, new MultiMappingComparator());

        for (MultiMappingStore ambiguous: ambiguousList) {
            System.out.println("hungarian try.");
            List<ITree> lstSrcs = new ArrayList<>(ambiguous.getSrcs());
            List<ITree> lstDsts = new ArrayList<>(ambiguous.getDsts());
            double[][] matrix = new double[lstSrcs.size()][lstDsts.size()];
            for (int i = 0; i < lstSrcs.size(); i++)
                for (int j = 0; j < lstDsts.size(); j++)
                    matrix[i][j] = cost(lstSrcs.get(i), lstDsts.get(j));

            HungarianAlgorithm hgAlg = new HungarianAlgorithm(matrix);
            int[] solutions = hgAlg.execute();
            for (int i = 0; i < solutions.length; i++) {
                int dstIdx = solutions[i];
                if (dstIdx != -1) addMappingRecursively(lstSrcs.get(i), lstDsts.get(dstIdx));
            }
        }
    }

    private double cost(ITree src, ITree dst) {
        return 111D - sim(src, dst);
    }

    private static class MultiMappingComparator implements Comparator<MultiMappingStore> {

        @Override
        public int compare(MultiMappingStore m1, MultiMappingStore m2) {
            return Integer.compare(impact(m1), impact(m2));
        }

        public int impact(MultiMappingStore m) {
            int impact = 0;
            for (ITree src: m.getSrcs()) {
                int pSize = src.getParents().size();
                if (pSize > impact) impact = pSize;
            }
            for (ITree src: m.getDsts()) {
                int pSize = src.getParents().size();
                if (pSize > impact) impact = pSize;
            }
            return impact;
        }

    }

}