package gfuzan.reflect.tools.tree;

import java.util.ArrayList;
import java.util.List;

import gfuzan.reflect.tools.objectoperation.ObjectUtil;

/**
 * <b>树遍历工具
 * <p>  <b>安全性: 线程安全
 * @author GFuZan
 *
 */
public final class TreeUtil {

    /**
     * 树遍历--先序
     * @param tree 树
     * @param opMethod 每个节点的操作函数
     */
    public final static <T extends TreeNode> void treeTraversal(T tree, TreeNodeOpMethod<T> opMethod) {
        StopTypeWrapper stopTypeWrapper = new StopTypeWrapper();
        stopTypeWrapper.stopType = StopType.NoStop;
        treeTraversal(tree, opMethod, 0, stopTypeWrapper);
    }

    /**
     * 树遍历--后序
     * @param tree 树数组
     * @param opMethod 每个节点的操作函数
     */
    public final static <T extends TreeNode> void treeTraversalPos(T tree, TreeNodeOpMethod<T> opMethod) {
        StopTypeWrapper stopTypeWrapper = new StopTypeWrapper();
        stopTypeWrapper.stopType = StopType.NoStop;
        treeTraversalPos(tree, opMethod, 0, stopTypeWrapper);
    }

    /**
     * 森林遍历--先序
     * @param tree 树
     * @param opMethod 每个节点的操作函数
     */
    public final static <T extends TreeNode> void forestTraversal(List<T> treeList, TreeNodeOpMethod<T> opMethod) {
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
     * @param tree 树数组
     * @param opMethod 每个节点的操作函数
     */
    public final static <T extends TreeNode> void forestTraversalPos(List<T> treeList, TreeNodeOpMethod<T> opMethod) {
        StopTypeWrapper stopTypeWrapper = new StopTypeWrapper();
        stopTypeWrapper.stopType = StopType.NoStop;
        if (treeList != null) {
            for (T tree : treeList) {
                treeTraversalPos(tree, opMethod, 0, stopTypeWrapper);
            }
        }
    }

    /**
     * 获取对象转树工具类
     * @param srcAttArray 原属性值数组
     * @param targetAttArray 目标属性值数组
     * @param targetType 目标对象类型
     * <br>示例
     *<pre>
     *  srcAttArray={ { "cityName", "cityCode" },
     *                       { "tierName", "tierCode" }}
     *  targetAttArray={ { "name", "label" },
     *                          { "code" } }
     * 将cityName映射到name和label,cityCode映射到code
     * 将tierName映射到name和label,tierCode映射到code
     * srcAttArray需要从小等级开始写(例中:city是tier的下级)
     * </pre>
     * @return
     */
    public final static <T extends TreeNode> Object2Tree getObject2TreeUtil(String[][] srcAttArray,
            String[][] targetAttArray, Class<T> targetType) {
        if (srcAttArray == null || targetAttArray == null || targetType == null) {
            throw new NullPointerException("Object2TreeUtil.excute()" + ": srcAttArray, targetAttArray ,targetType 均不能为空");
        }
        return new Object2Tree(srcAttArray, targetAttArray, targetType);
    }

    /**
     * 内部方法(先序遍历树)
     * @param tree
     * @param opMethod
     * @param level
     */
    private final static <T extends TreeNode> void treeTraversal(T tree, TreeNodeOpMethod<T> opMethod, int depth,
            StopTypeWrapper stopTypeWrapper) {
        if (tree != null && !StopType.All.equals(stopTypeWrapper.stopType)) {

            // 访问当前节点
            stopTypeWrapper.stopType = opMethod.opFunc(tree, depth);
            // 停止当前节点的遍历
            if (StopType.Node.equals(stopTypeWrapper.stopType)) {
                stopTypeWrapper.stopType = StopType.NoStop;
                return;
            }

            @SuppressWarnings("unchecked")
            List<T> childNodes = (List<T>) tree.getChildNode();
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
     * @param mapTree 树数组
     * @param subAtt 子节点属性
     * @param opMethod 操作函数
     */
    private final static <T extends TreeNode> void treeTraversalPos(T tree, TreeNodeOpMethod<T> opMethod, int depth,
            StopTypeWrapper stopTypeWrapper) {
        if (tree != null && !StopType.All.equals(stopTypeWrapper.stopType)) {
            @SuppressWarnings("unchecked")
            List<T> childNodes = (List<T>) tree.getChildNode();
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
     * @author GFuZan
     *
     */
    public static interface TreeNode {

        /**
        /** 获取当前节点子节点
         * @return childNode
         */
        public List<? extends TreeNode> getChildNode();

        /**
        /** 设置当前节点子节点
         * @param childNode
         */
        public void setChildNode(List<? extends TreeNode> childNode);

    }

    /**
     * 树节点操作函数类
     * @author GFuZan
     *
     */
    public static interface TreeNodeOpMethod<T extends TreeNode> {
        /**
         * 对每个节点的操作
         * @param tree 树
         * @param depth 深度 起始值:0
         * @return 停止当前节点的遍历
         */
        public StopType opFunc(T treeNode, int depth);
    }

    /**
     * 用于树遍历的停止类型枚举类
     * @author GFuZan
     *
     */
    public static enum StopType {
        /**
         *不停止遍历
         */
        NoStop,
        /**
         *停止当前节点的遍历(在后序遍历中与StopType.All一致)
         */
        Node,
        /**
         *停止整个树的遍历
         */
        All;
    }

    /**
     * 停止枚举类的包装类
     * @author GFuZan
     */
    private static final class StopTypeWrapper {
        public StopType stopType;
    }

    /**
     * <b>对象转树
     * <p><b> 安全性: 线程不安全
     * @author GFuZan
     *
     */
    public static class Object2Tree {

        /**
         * 反射工具类
         */
        private ObjectUtil objectUtil = null;

        /**
         * 每个对象对应的属性
         */
        private String[][] attList = null;
        /**
         * 新对象的属性
         */
        private String[][] targetAttList = null;

        /**
         * 返回值类型
         */
        private Class<? extends TreeNode> returnType = null;

        /**
         * 比较方法
         */
        private Comparator<? extends TreeNode> comparator = null;

        /**
         *  执行转换
         *  @param <T> 目标对象类型
         *  @param <S> 原始对象类型
         * @param list 原平铺数据对象列表(已排序)
         * @param returnType 返回List类型(目标对象)
         * @return 转换后的树List
         */
        public <T extends TreeNode, S> List<T> excute(List<S> list) {
            return excute(list, null, null);
        }

        /**
         * 执行转换
         *  @param <T> 目标对象类型
         *  @param <S> 原始对象类型
         * @param list  原平铺数据对象列表(已排序)
         * @param op 属性操作方法(可以影响树的生成)
         * @param returnType 返回List类型(目标对象)
         * @return 转换后的树List
         */
        public <T extends TreeNode, S> List<T> excute(List<S> list, ConvertOpMethod<T, S> op) {
            return excute(list, null, op);
        }

        /**
         *  执行转换
         *  @param <T> 目标对象类型
         *  @param <S> 原始对象类型
         * @param list  原平铺数据对象列表(已排序)
         * @param comparator 比较方法,树生成时用于确定唯一节点
         * @param returnType 返回List类型(目标对象)
         * @return 转换后的树List
         */
        public <T extends TreeNode, S> List<T> excute(List<S> list, Comparator<T> comparator) {
            return excute(list, comparator, null);
        }

        /**
         *  执行转换
         *  @param <T> 目标对象类型
         *  @param <S> 原始对象类型
         * @param list  原平铺数据对象列表(已排序)
         * @param comparator 比较方法,树生成时用于确定唯一节点
         * @param op 属性操作方法(可以影响树的生成)
         * @param returnType 返回List类型(目标对象)
         * @return 转换后的树List
         */
        @SuppressWarnings("unchecked")
        public <T extends TreeNode, S> List<T> excute(List<S> list, Comparator<T> comparator,
                ConvertOpMethod<T, S> op) {
            if (list == null) {
                return null;
            }

            if (comparator != null) {
                this.comparator = comparator;
            }

            List<T> oldList = null;
            List<T> newList = null;
            try {
                objectUtil = new ObjectUtil();
                // 禁用log输出
                objectUtil.showLog = false;

                for (int i = 0; i < list.size(); i++) {
                    S row = list.get(i);
                    // 拆分对象
                    newList = splitObject(row, op);

                    // 连接上下级
                    oldList = levelHandle(oldList, newList);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                objectUtil = null;
            }

            if (oldList != null && !oldList.isEmpty()) {
                T t = oldList.get(oldList.size() - 1);
                return (List<T>) t.getChildNode();
            }
            return null;
        }

        /**
         * 移除空节点(使用生成树时对象比较方法)
         * @param forest 转换后的树
         * @return
         */
        public <T extends TreeNode> List<T> removeEmptyNode(List<T> forest) {
            return this.removeEmptyNode(forest, null);
        }

        /**
         * 移除空节点(使用生成树时对象比较方法)
         * @param forest 转换后的树
         * @param 自定义的空对象
         * @return
         */
        public <T extends TreeNode> List<T> removeEmptyNode(List<T> forest, T emptyObject) {

            if (emptyObject == null) {
                T newObj = null;
                try {
                    newObj = getNewObject();
                } catch (Exception e) {
                    System.err.println("Object2TreeUtil.removeEmptyNode()" + ": 创建对象失败!!");
                }
                emptyObject = newObj;
            }

            final T emptyObj = emptyObject;
            TreeUtil.forestTraversalPos(forest, new TreeNodeOpMethod<T>() {

                @Override
                public StopType opFunc(T obj, int depth) {
                    @SuppressWarnings("unchecked")
                    List<T> subList = (List<T>) obj.getChildNode();
                    removeThisListEmptyNode(subList, emptyObj);
                    return StopType.NoStop;
                }
            });
            removeThisListEmptyNode(forest, emptyObject);
            return forest;
        }

        /**(内部)移除当前List下的空节点
         * @param list
         * @param comparator
         * @param emptyObj
         */
        @SuppressWarnings("unchecked")
        private <T extends TreeNode> void removeThisListEmptyNode(List<T> list, T emptyObj) {
            if (list != null) {
                int len = list.size();
                for (int i = 0; i < len; i++) {
                    T obj = list.get(i);
                    if (((Comparator<T>) this.comparator).compare(obj, emptyObj)) {
                        List<T> subSubList = (List<T>) obj.getChildNode();
                        list.remove(i--);
                        len--;
                        if (subSubList != null) {
                            list.addAll(subSubList);
                        }
                    }
                }
            }
        }

        /**
         *  (内部)上下级处理
         * @param old
         * @param newd
         * @return
         */
        @SuppressWarnings("unchecked")
        private <T extends TreeNode> List<T> levelHandle(List<T> oldList, List<T> newList) {
            if (oldList == null) {
                return newList;
            }

            for (int i = 0; i < oldList.size(); i++) {
                T old = oldList.get(i);
                T newd = newList.get(i);
                if (((Comparator<T>) this.comparator).compare(old, newd)) {
                    setChildren(old, (List<T>) newd.getChildNode());
                } else {
                    oldList.set(i, newd);
                }
            }

            return oldList;
        }

        /**
         *  (内部)拆分对象
         * @param <T> 目标对象
         * @param <S> 原对象
         * @param row 原对象
         * @param comparator 比较方法
         * @param op 操作方法
         * @param returnType
         * @return
         * @throws Exception
         */
        private <T extends TreeNode, S> List<T> splitObject(S row, ConvertOpMethod<T, S> op)
                throws Exception {

            List<T> res = new ArrayList<>();
            T subNode = null;
            // i 新建对象
            for (int srcObjectIndex = 0; srcObjectIndex < attList.length; srcObjectIndex++) {
                T newTarget = getNewObject();
                // 对象属性赋值
                for (int srcAttIndex = 0; srcAttIndex < attList[srcObjectIndex].length; srcAttIndex++) {
                    // 一个原对象属性对应多个目标对象属性
                    for (int targetAttIndexk = 0; targetAttIndexk < targetAttList[srcAttIndex].length; targetAttIndexk++) {
                        objectUtil.set(newTarget, targetAttList[srcAttIndex][targetAttIndexk],
                                objectUtil.get(row, attList[srcObjectIndex][srcAttIndex]));
                    }
                }

                // 设置子属性
                {
                    setChildren(newTarget, subNode);
                    subNode = newTarget;
                }
                // 外部操作
                if (op != null && srcObjectIndex != attList.length - 1) {
                    op.opFunc(newTarget, row, attList.length - 1 - srcObjectIndex);
                }
                res.add(newTarget);
            }

            return res;

        }

        /**
         *  (内部)设置多个子节点
         * @param obj
         * @param subList
         */
        private <T extends TreeNode> void setChildren(T obj, List<T> subList) {
            if (obj == null || subList == null) {
                return;
            }

            for (int i = 0; i < subList.size(); i++) {
                setChildren(obj, subList.get(i));
            }
        }

        /**
         *  (内部)设置单个子节点
         * @param obj
         * @param subNode
         */
        @SuppressWarnings("unchecked")
        private <T extends TreeNode> void setChildren(T obj, T subNode) {
            if (obj == null || subNode == null) {
                return;
            }

            List<T> subList = (List<T>) obj.getChildNode();
            if (subList == null) {
                subList = new ArrayList<>();
                subList.add(subNode);
                obj.setChildNode(subList);
            } else if (!subList.isEmpty()) {
                if (!((Comparator<T>) this.comparator).compare(subNode, subList.get(subList.size() - 1))) {
                    subList.add(subNode);
                }
            } else {
                subList.add(subNode);
            }
        }

        /**
         * (内部)创建新对象
         * @param type
         * @return
         * @throws Exception
         */
        @SuppressWarnings("unchecked")
        private <T extends TreeNode> T getNewObject() throws Exception {
            T t = null;
            try {
                t = (T) this.returnType.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new Exception(e);
            }
            return t;
        }

        /**
         * @param srcAttArray 原属性值数组
         * @param targetAttArray 目标属性值数组
         * @param childrenAtt 子节点List属性
         */
        private <T extends TreeNode> Object2Tree(String[][] srcAttArray, String[][] targetAttArray,
                Class<T> targetType) {
            String[][] attList = null;
            if (srcAttArray != null) {
                attList = new String[srcAttArray.length + 1][];
                for (int i = 0; i < srcAttArray.length; i++) {
                    attList[i] = srcAttArray[i];
                }
                attList[attList.length - 1] = new String[] {};
            }
            this.attList = attList;
            this.targetAttList = targetAttArray;
            this.returnType = targetType;
            this.comparator = new Comparator<T>() {
                @Override
                public boolean compare(T o1, T o2) {
                    if (o1 == o2) {
                        return true;
                    } else if (o1 == null) {
                        return o2 == null ? true : false;
                    } else if (o1.equals(o2)) {
                        return true;
                    }
                    return false;
                }
            };
        }

        private Object2Tree() {
        }

        /**
         * 转换操作方法
         * @author GFuZan
         */
        public static interface ConvertOpMethod<T extends TreeNode, S> {
            /**
             * @param targetObject 目标对象
             * @param srcObject 源对象
             * @param depth 起始值 0
             */
            public void opFunc(T targetObject, S srcObject, int depth);
        }

        /**
         * 比较方法
         * @author GFuZan
         */
        public static interface Comparator<T extends TreeNode> {
            boolean compare(T obj, T emptyObj);
        }
    }
}
