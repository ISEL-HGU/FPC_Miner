package edu.handong.csee.isel.fcminer.gumtree.core.matchers.optimizations;

import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Mapping;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.MappingStore;
import edu.handong.csee.isel.fcminer.gumtree.core.matchers.Matcher;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;


/**
 * This implements the cross move matcher Theta F.
 *
 */
public class CrossMoveMatcherThetaF extends Matcher {

    private class BfsComparator implements Comparator<Mapping> {

        private HashMap<Integer, Integer> positionSrc;
        private HashMap<Integer, Integer> positionDst;

        private HashMap<Integer, Integer> getHashSet(ITree tree) {
            HashMap<Integer, Integer> map = new HashMap<>();
            ArrayList<ITree> list = new ArrayList<>();
            LinkedList<ITree> workList = new LinkedList<>();
            workList.add(tree);
            while (!workList.isEmpty()) {
                ITree node = workList.removeFirst();
                list.add(node);
                workList.addAll(node.getChildren());
            }
            for (int i = 0; i < list.size(); i++) {
                map.put(list.get(i).getId(), i);
            }
            return map;
        }

        public BfsComparator(ITree src, ITree dst) {
            positionSrc = getHashSet(src);
            positionDst = getHashSet(dst);
        }

        @Override
        public int compare(Mapping o1, Mapping o2) {
            if (o1.first.getId() != o2.first.getId()) {
                return Integer.compare(positionSrc.get(o1.first.getId()),
                        positionSrc.get(o2.first.getId()));
            }
            return Integer.compare(positionDst.get(o1.second.getId()),
                    positionDst.get(o2.second.getId()));
        }

    }

    /**
     * Instantiates a new matcher for Theta F.
     *
     * @param src the src
     * @param dst the dst
     * @param store the store
     */
    public CrossMoveMatcherThetaF(ITree src, ITree dst, MappingStore store) {
        super(src, dst, store);
    }

    @Override
    protected void addMapping(ITree src, ITree dst) {
        assert (src != null);
        assert (dst != null);
        super.addMapping(src, dst);
    }

    /**
     * Match.
     */
    @Override
    public void match() {
        thetaF();
    }

    private void thetaF() {
        LinkedList<Mapping> workList = new LinkedList<>(mappings.asSet());
        Collections.sort(workList, new BfsComparator(src, dst));
        for (Mapping pair : workList) {
            ITree parentOld = pair.getFirst().getParent();
            ITree parentNew = pair.getSecond().getParent();
            if (mappings.hasSrc(parentOld) && mappings.getDst(parentOld) != parentNew) {
                if (mappings.hasDst(parentNew) && mappings.getSrc(parentNew) != parentOld) {
                    ITree parentOldOther = mappings.getSrc(parentNew);
                    ITree parentNewOther = mappings.getDst(parentOld);
                    if (parentOld.getLabel().equals(parentNewOther.getLabel())
                            && parentNew.getLabel().equals(parentOldOther.getLabel())) {
                        boolean done = false;
                        for (ITree childOldOther : parentOldOther.getChildren()) {
                            if (mappings.hasSrc(childOldOther)) {
                                ITree childNewOther = mappings.getDst(childOldOther);
                                if (pair.getFirst().getLabel().equals(childNewOther.getLabel())
                                        && childOldOther.getLabel()
                                                .equals(pair.getSecond().getLabel())
                                        || !(pair.getFirst().getLabel()
                                                .equals(pair.getSecond().getLabel())
                                                || childOldOther.getLabel()
                                                        .equals(childNewOther.getLabel()))) {
                                    if (childNewOther.getParent() == parentNewOther) {
                                        if (childOldOther.getType() == pair.getFirst().getType()) {
                                            mappings.unlink(pair.getFirst(), pair.getSecond());
                                            mappings.unlink(childOldOther, childNewOther);
                                            addMapping(pair.getFirst(), childNewOther);
                                            addMapping(childOldOther, pair.getSecond());
                                            // done = true;
                                        }
                                    }
                                }
                            }
                        }
                        if (!done) {
                            for (ITree childNewOther : parentNewOther.getChildren()) {
                                if (mappings.hasDst(childNewOther)) {
                                    ITree childOldOther = mappings.getSrc(childNewOther);
                                    if (childOldOther.getParent() == parentOldOther) {
                                        if (childNewOther.getType() == pair.getSecond().getType()) {
                                            if (pair.getFirst().getLabel()
                                                    .equals(childNewOther.getLabel())
                                                    && childOldOther.getLabel()
                                                            .equals(pair.getSecond().getLabel())
                                                    || !(pair.getFirst().getLabel()
                                                            .equals(pair.getSecond().getLabel())
                                                            || childOldOther.getLabel().equals(
                                                                    childNewOther.getLabel()))) {
                                                mappings.unlink(pair.getFirst(), pair.getSecond());
                                                mappings.unlink(childOldOther, childNewOther);
                                                addMapping(childOldOther, pair.getSecond());
                                                addMapping(pair.getFirst(), childNewOther);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
