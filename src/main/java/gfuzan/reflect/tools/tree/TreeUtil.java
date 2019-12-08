package gfuzan.reflect.tools.tree;

import java.util.LinkedList;
import java.util.List;

/**
 * <b>树遍历工具
 * <p>
 * <b>安全性: 线程安全
 * 
 * @author GFuZan
 *
 */
public final class TreeUtil {

    /**
     * 树遍历--先序
     * 
     * @param tree     树
     * @param opMethod 每个节点的操作函数
     */
    public final static <T extends TreeNode<T>> void treeTraversal(T tree, TreeNodeOpMethod<T> opMethod) {
        StopTypeWrapper stopTypeWrapper = new StopTypeWrapper();
        stopTypeWrapper.stopType = StopType.NoStop;
        treeTraversal(tree, opMethod, 0, stopTypeWrapper);
    }

    /**
     * 树遍历--后序
     * 
     * @param tree     树
     * @param opMethod 每个节点的操作函数
     */
    public final static <T extends TreeNode<T>> void treeTraversalPos(T tree, TreeNodeOpMethod<T> opMethod) {
        StopTypeWrapper stopTypeWrapper = new StopTypeWrapper();
        stopTypeWrapper.stopType = StopType.NoStop;
        treeTraversalPos(tree, opMethod, 0, stopTypeWrapper);
    }

    /**
     * 森林遍历--先序
     * 
     * @param treeList 树数组
     * @param opMethod 每个节点的操作函数
     */
    public final static <T extends TreeNode<T>> void forestTraversal(List<T> treeList, TreeNodeOpMethod<T> opMethod) {
        StopTypeWrapper stopTypeWrapper = new StopTypeWrapper();
        stopTypeWrapper.stopType = StopType.NoStop;
        if (treeList != null) {
            for (T tree : treeList) {
                treeTraversal(tree, opMethod, 0, stopTypeWrapper);
            }
        }
    }

    /**
     * 森林遍历--后序
     * 
     * @param treeList 树数组
     * @param opMethod 每个节点的操作函数
     */
    public final static <T extends TreeNode<T>> void forestTraversalPos(List<T> treeList,
            TreeNodeOpMethod<T> opMethod) {
        StopTypeWrapper stopTypeWrapper = new StopTypeWrapper();
        stopTypeWrapper.stopType = StopType.NoStop;
        if (treeList != null) {
            for (T tree : treeList) {
                treeTraversalPos(tree, opMethod, 0, stopTypeWrapper);
            }
        }
    }

    /**
     * 移除节点
     * 
     * @param tree           树
     * @param nodeComparable 要移除的节点比较器
     */
    public final static <T extends TreeNode<T>> T removeNode(T tree, NodeComparable<T> nodeComparable) {

        if (tree != null) {
            List<T> newRootList = new LinkedList<>();
            newRootList.add(tree);
            newRootList = removeNode(newRootList, nodeComparable);
            if (newRootList.isEmpty()) {
                tree = null;
            }
        }
        return tree;
    }

    /**
     * 移除节点
     * 
     * @param treeList       树数组
     * @param nodeComparable 要移除的节点比较器
     */
    public final static <T extends TreeNode<T>> List<T> removeNode(List<T> treeList, NodeComparable<T> nodeComparable) {

        List<T> newTreeList = treeList;

        if (nodeComparable != null && treeList != null) {
            newTreeList = new LinkedList<>();

            final NodeComparable<T> comparable = nodeComparable;
            // 去除树根
            for (T cNode : treeList) {
                if (!comparable.comparableTo(cNode, 0)) {
                    newTreeList.add(cNode);
                }
            }

            // 去除子节点
            forestTraversalPos(newTreeList, new TreeNodeOpMethod<T>() {

                @Override
                public StopType opFunc(T treeNode, int depth) {
                    List<T> childNodeList = treeNode.getChildNode();
                    List<T> newChildNodeList = new LinkedList<>();
                    if (childNodeList != null) {
                        for (T cNode : childNodeList) {
                            if (!comparable.comparableTo(cNode, depth)) {
                                newChildNodeList.add(cNode);
                            }
                        }
                        treeNode.setChildNode(newChildNodeList);
                    }
                    return StopType.NoStop;
                }

            });
        }

        return newTreeList;
    }

    /**
     * 内部方法(先序遍历树)
     * 
     * @param tree
     * @param opMethod
     * @param level
     */
    private final static <T extends TreeNode<T>> void treeTraversal(T tree, TreeNodeOpMethod<T> opMethod, int depth,
            StopTypeWrapper stopTypeWrapper) {
        if (tree != null && !StopType.All.equals(stopTypeWrapper.stopType)) {

            // 访问当前节点
            stopTypeWrapper.stopType = opMethod.opFunc(tree, depth);
            // 停止当前节点的遍历
            if (StopType.Node.equals(stopTypeWrapper.stopType)) {
                stopTypeWrapper.stopType = StopType.NoStop;
                return;
            }

            List<T> childNodes = tree.getChildNode();
            if (childNodes != null) {
                // 访问子节点
                for (T childNode : childNodes) {
                    treeTraversal(childNode, opMethod, depth + 1, stopTypeWrapper);
                }
            }
        }
    }

    /**
     * 内部方法(树遍历--后序)
     * 
     * @param mapTree  树数组
     * @param subAtt   子节点属性
     * @param opMethod 操作函数
     */
    private final static <T extends TreeNode<T>> void treeTraversalPos(T tree, TreeNodeOpMethod<T> opMethod, int depth,
            StopTypeWrapper stopTypeWrapper) {
        if (tree != null && !StopType.All.equals(stopTypeWrapper.stopType)) {

            List<T> childNodes = tree.getChildNode();
            // 访问子节点
            if (childNodes != null) {
                for (T childNode : childNodes) {
                    treeTraversalPos(childNode, opMethod, depth + 1, stopTypeWrapper);
                }
            }

            // 访问当前节点
            stopTypeWrapper.stopType = opMethod.opFunc(tree, depth);
            // 停止当前节点的遍历即是停止整个树的遍历
            if (StopType.Node.equals(stopTypeWrapper.stopType)) {
                stopTypeWrapper.stopType = StopType.All;
                return;
            }
        }
    }

    /**
     * 树节点
     * 
     * @author GFuZan
     *
     */
    public static interface TreeNode<T extends TreeNode<T>> {

        /**
         * /** 获取当前节点子节点
         * 
         * @return childNode
         */
        public List<T> getChildNode();

        /**
         * /** 设置当前节点子节点
         * 
         * @param childNode
         */
        public void setChildNode(List<T> childNode);

    }

    /**
     * 树节点操作函数类
     * 
     * @author GFuZan
     */
    public static interface TreeNodeOpMethod<T extends TreeNode<T>> {
        /**
         * 对每个节点的操作
         * 
         * @param treeNode 树节点
         * @param depth    深度 起始值:0
         * @return 停止当前节点的遍历
         */
        public StopType opFunc(T treeNode, int depth);
    }

    /**
     * 节点比较器
     * 
     * @author GFuZan
     */
    public static interface NodeComparable<T extends TreeNode<T>> {

        /**
         * 节点比较
         * 
         * @param treeNode 树
         * @param depth    深度 起始值:0
         * @return
         */
        boolean comparableTo(T treeNode, int depth);
    }

    /**
     * 用于树遍历的停止类型枚举类
     * 
     * @author GFuZan
     *
     */
    public static enum StopType {
        /**
         * 不停止遍历
         */
        NoStop,
        /**
         * 停止当前节点的遍历(在后序遍历中与StopType.All一致)
         */
        Node,
        /**
         * 停止整个树的遍历
         */
        All;
    }

    /**
     * 停止枚举类的包装类
     * 
     * @author GFuZan
     */
    private static final class StopTypeWrapper {
        public StopType stopType;
    }
}
