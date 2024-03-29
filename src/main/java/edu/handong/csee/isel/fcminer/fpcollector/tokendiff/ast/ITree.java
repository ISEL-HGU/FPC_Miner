package edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast;

import java.util.ArrayList;
import java.util.List;

import edu.handong.csee.isel.fcminer.fpcollector.tokendiff.ast.gen.Property;

/**
 * Interface to represent abstract syntax trees.
 */
public interface ITree {

    String OPEN_SYMBOL = "[(";
    String CLOSE_SYMBOL = ")]";
    String SEPARATE_SYMBOL = "@@";

    int NO_ID = Integer.MIN_VALUE;

    String NO_LABEL = "";

    int NO_VALUE = -1;        
    
    void setNode2String(String node2String);
    
    String getNode2String();
    
    void setStartLineNum(int startLineNum);
    
    int getStartLineNum();
    
    void setEndLineNum(int endLineNum);
    
    int getEndLineNum();
    
    /**
     * @return all the nodes contained in the tree, using a pre-order.
     */
    List<ITree> getTrees();
    
    public ArrayList<Property> getParentProps();
    
    public void setParentProps(ArrayList<Property> parentProps);

    Iterable<ITree> preOrder();

    Iterable<ITree> postOrder();

    Iterable<ITree> breadthFirst();

    /**
     * Add the given tree as a child, and update its parent.
     */
    void addChild(ITree t);

    /**
     * Insert the given tree as the position-th child, and update its parent.
     */
    void insertChild(ITree t, int position);

    void setChildren(List<ITree> children);

    /**
     * @return the position of the child, or -1 if the given child is not in the children list.
     */
    int getChildPosition(ITree child);

    /**
     * @param position the child position, starting at 0
     */
    ITree getChild(int position);

    List<ITree> getChildren();

    /**
     * @return a boolean indicating if the tree has at least one child or not
     */
    boolean isLeaf();

    /**
     * @return all the descendants (children, children of children, etc.) of the tree,
     *     using a pre-order.
     */
    List<ITree> getDescendants();

    /**
     * Set the parent of this node. The parent won't have this node in its
     * children list
     */
    void setParent(ITree parent);

    /**
     * Set the parent of this node. The parent will have this node in its
     * children list, at the last position
     */
    void setParentAndUpdateChildren(ITree parent);

    /**
     * @return a boolean indicating if the tree has a parent or not
     */
    boolean isRoot();

    ITree getParent();

    /**
     * @return the list of all parents of the node (parent, parent of parent, etc.)
     */
    List<ITree> getParents();

    /**
     * @return the position of the node in its parent children list
     */
    int positionInParent();

    /**
     * Make a deep copy of the tree.
     * Deep copy of node however shares Metadata
     * @return a deep copy of the tree.
     */
    ITree deepCopy();

    /**
     * @see TreeUtils#computeDepth(ITree)
     * @return the depth of the tree, defined as the distance to the root
     */
    int getDepth();

    void setDepth(int depth);

    /**
     * @see TreeUtils#numbering(Iterable)
     * @see TreeUtils#preOrderNumbering(ITree)
     * @see TreeUtils#postOrderNumbering(ITree)
     * @see TreeUtils#breadthFirstNumbering(ITree)
     * @return the number of the node
     */
    int getId();

    void setId(int id);

    boolean hasLabel();

    String getLabel();

    void setLabel(String label);

    int getPos();

    void setPos(int pos);

    int getLength();

    void setLength(int length);

    /**
     * @return the absolute character index where the tree ends
     */
    default int getEndPos()  {
        return getPos() + getLength();
    }

    int getType();

    void setType(int type);

    /**
     * @return a boolean indicating if the trees have the same type.
     */
    boolean hasSameType(ITree t);

    /**
     * Indicate whether or not the tree is similar to the given tree.
     * @return true if they are compatible and have same label, false either
     */
    boolean hasSameTypeAndLabel(ITree t);

    /**
     * Refresh hash, size, depth and height of the tree.
     * @see com.github.gumtreediff.tree.hash.HashGenerator
     * @see TreeUtils#computeDepth(ITree)
     * @see TreeUtils#computeHeight(ITree)
     * @see TreeUtils#computeSize(ITree)
     */
    void refresh();

    String toStaticHashString();

    String toShortString();
}
