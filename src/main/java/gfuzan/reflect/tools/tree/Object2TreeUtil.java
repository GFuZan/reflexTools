package gfuzan.reflect.tools.tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import gfuzan.reflect.tools.objectoperation.ObjectUtil;
import gfuzan.reflect.tools.tree.TreeUtil.StopType;
import gfuzan.reflect.tools.tree.TreeUtil.TreeNode;
import gfuzan.reflect.tools.tree.TreeUtil.TreeNodeOpMethod;

/**
     * <b>对象转树
     * <p><b> 安全性: 线程安全
     * @author GFuZan
     *
     */
public class Object2TreeUtil {

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
    private Class<?> returnType = null;

    /**
     * 比较方法
     */
    private Comparator<?> comparator = null;

    /**
     *  执行转换
     *  @param <T> 目标对象类型
     *  @param <S> 原始对象类型
     * @param list 原平铺数据对象列表(已排序)
     * @return 转换后的树List
     */
    public <T extends TreeNode<T>, S> List<T> excute(List<S> list) {
        return excute(list, null);
    }

    /**
     * 执行转换
     *  @param <T> 目标对象类型
     *  @param <S> 原始对象类型
     * @param list  原平铺数据对象列表(已排序)
     * @param op 转换操作方法(可以影响树的生成)
     * @return 转换后的树List
     */

    public <T extends TreeNode<T>, S> List<T> excute(List<S> list, ConvertOpMethod<T, S> op) {
        if (list == null) {
            return null;
        }

        List<T> oldList = null;
        List<T> newList = null;
        ObjectUtil objectUtil = new ObjectUtil();

        // 禁用log输出
        objectUtil.showLog = false;

        try {

            for (int i = 0; i < list.size(); i++) {
                S row = list.get(i);
                // 拆分对象
                newList = splitObject(row, op, objectUtil);

                // 连接上下级
                oldList = levelHandle(oldList, newList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (oldList != null && !oldList.isEmpty()) {
            T t = oldList.get(oldList.size() - 1);
            return (List<T>) t.getChildNode();
        }
        return null;
    }

    /**
     * 移除空节点
     * @param forest 转换后的树
     * @return
     */
    public <T extends TreeNode<T>> List<T> removeEmptyNode(List<T> forest) {
        return this.removeEmptyNode(forest, null);
    }

    /**
     * 移除空节点
     * @param forest 转换后的树
     * @param 自定义的空对象
     * @return
     */
    public <T extends TreeNode<T>> List<T> removeEmptyNode(List<T> forest, T emptyObject) {

        if (emptyObject == null) {
            T newObj = null;
            try {
                newObj = getNewObject(emptyObject);
            } catch (Exception e) {
                System.err.println("Object2TreeUtil.removeEmptyNode()" + ": 创建对象失败!!");
            }
            emptyObject = newObj;
        }

        final T emptyObj = emptyObject;
        TreeUtil.forestTraversalPos(forest, new TreeNodeOpMethod<T>() {

            @Override
            public StopType opFunc(T obj, int depth) {

                List<T> subList = obj.getChildNode();
                obj.setChildNode(removeThisListEmptyNode(subList, emptyObj));
                return StopType.NoStop;
            }
        });

        return removeThisListEmptyNode(forest, emptyObject);
    }

    /**(内部)移除当前List下的空节点
     * @param list
     * @param comparator
     * @param emptyObj
     */
    @SuppressWarnings("unchecked")
    private <T extends TreeNode<T>> List<T> removeThisListEmptyNode(List<T> list, T emptyObj) {
        if (list != null) {

            Deque<List<T>> tmpStack = new LinkedList<>();
            for (int i = list.size() - 1; i >= 0; i--) {
                T obj = list.get(i);
                if (((Comparator<T>) this.comparator).compare(obj, emptyObj) == 0) {
                    List<T> subSubList = (List<T>) obj.getChildNode();
                    list.remove(i);
                    if (subSubList != null) {
                        tmpStack.push(subSubList);
                    }
                }
            }

            for (; !tmpStack.isEmpty();) {
                list.addAll(tmpStack.pop());
            }
        }

        if (list != null && !list.isEmpty()) {
            return list;
        } else {
            return null;
        }
    }

    /**
     *  (内部)上下级处理
     * @param old
     * @param newd
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T extends TreeNode<T>> List<T> levelHandle(List<T> oldList, List<T> newList) {
        if (oldList == null) {
            return newList;
        }

        for (int i = 0; i < oldList.size(); i++) {
            T old = oldList.get(i);
            T newd = newList.get(i);
            if (((Comparator<T>) this.comparator).compare(old, newd) == 0) {
                setChildren(old, (List<T>) newd.getChildNode());
                newd.setChildNode(null);
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
    private <T extends TreeNode<T>, S> List<T> splitObject(S row, ConvertOpMethod<T, S> op, ObjectUtil objectUtil)
            throws Exception {

        List<T> res = new ArrayList<>();
        T subNode = null;
        // i 新建对象
        for (int srcObjectIndex = 0; srcObjectIndex < attList.length; srcObjectIndex++) {
            T newTarget = getNewObject(subNode);
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
                op.opFunc(newTarget, row, attList.length - 2 - srcObjectIndex);
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
    private <T extends TreeNode<T>> void setChildren(T obj, List<T> subList) {
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
    private <T extends TreeNode<T>> void setChildren(T obj, T subNode) {
        if (obj == null || subNode == null) {
            return;
        }

        List<T> subList = (List<T>) obj.getChildNode();
        if (subList == null) {
            subList = new ArrayList<>();
            subList.add(subNode);
            obj.setChildNode(subList);
        } else if (!subList.isEmpty()) {
            if (((Comparator<T>) this.comparator).compare(subNode, subList.get(subList.size() - 1)) != 0) {
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
    private <T extends TreeNode<T>> T getNewObject(T type) throws ReflectiveOperationException {
        T t = null;
        try {
            t = (T) this.returnType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ReflectiveOperationException(e);
        }
        return t;
    }

    /**
     * 对象转树工具类
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
    public <T extends TreeNode<T>> Object2TreeUtil(String[][] srcAttArray, String[][] targetAttArray,
            Class<T> targetType) {
        this(srcAttArray, targetAttArray, null, targetType);
    }

    /**
     * 对象转树工具类
     * @param srcAttArray 原属性值数组
     * @param targetAttArray 目标属性值数组
     * @param comparator 比较条件
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
    public <T extends TreeNode<T>> Object2TreeUtil(String[][] srcAttArray, String[][] targetAttArray,
            Comparator<T> comparator, Class<T> targetType) {
        if (srcAttArray == null || targetAttArray == null || targetType == null) {
            throw new NullPointerException(
                    "参数: srcAttArray, targetAttArray ,targetType 不能为空");
        }

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

        if (comparator != null) {
            this.comparator = comparator;
        } else {
            this.comparator = new Comparator<T>() {
                @Override
                public int compare(T o1, T o2) {
                    if (o1 == o2) {
                        return 0;
                    } else {
                        boolean r = o1 != null ? o1.equals(o2) : o2.equals(o1);
                        return r ? 0 : 1;
                    }
                }
            };
        }
    }

    /**
     * 转换操作方法
     * @author GFuZan
     */
    public static interface ConvertOpMethod<T extends TreeNode<T>, S> {
        /**
         * @param targetObject 目标对象
         * @param srcObject 源对象
         * @param depth 深度 起始值:0
         */
        public void opFunc(T targetObject, S srcObject, int depth);
    }
}