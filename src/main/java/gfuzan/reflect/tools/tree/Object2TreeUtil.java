package gfuzan.reflect.tools.tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import gfuzan.reflect.tools.tree.TreeUtil.StopType;
import gfuzan.reflect.tools.tree.TreeUtil.TreeNode;
import gfuzan.reflect.tools.tree.TreeUtil.TreeNodeOpMethod;

/**
     * <b>对象转树
     * <p><b> 安全性: 线程安全
     * @author GFuZan
     *
     */
public class Object2TreeUtil<T extends TreeNode<T>, S> {

    /**
     * 比较方法
     */
    private Comparator<T> comparator = null;

    /**
     * 转换方法
     */
    private ConvertOpMethod<T, S> convertOpMethod = null;

    /**
     * 执行转换
     * @param list  原平铺数据对象列表(已排序)
     * @return 转换后的树List
     */

    public List<T> excute(List<S> list) {
        if (list == null) {
            return null;
        }

        List<T> oldList = null;
        List<T> newList = null;

        for (int i = 0; i < list.size(); i++) {
            S row = list.get(i);
            // 拆分对象
            newList = splitObject(row);

            // 连接上下级
            oldList = levelHandle(oldList, newList);
        }

        if (oldList != null && !oldList.isEmpty()) {
            T t = oldList.get(oldList.size() - 1);
            return t.getChildNode();
        }
        return null;
    }

    /**
     * 移除空节点
     * @param forest 转换后的树
     * @return
     */
    public List<T> removeEmptyNode(List<T> forest) {
        return this.removeEmptyNode(forest, null);
    }

    /**
     * 移除空节点
     * @param forest 转换后的树
     * @param 自定义的空对象
     * @return
     */
    public List<T> removeEmptyNode(List<T> forest, T emptyObject) {

        if (emptyObject == null) {
            emptyObject = this.convertOpMethod.getTargetObject();
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

    private List<T> removeThisListEmptyNode(List<T> list, T emptyObj) {
        if (list != null) {

            Deque<List<T>> tmpStack = new LinkedList<>();
            for (int i = list.size() - 1; i >= 0; i--) {
                T obj = list.get(i);
                if (this.comparator.compare(obj, emptyObj) == 0) {
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
     * @param newList
     * @return
     */
    private List<T> levelHandle(List<T> oldList, List<T> newList) {
        if (oldList == null) {
            return newList;
        }

        for (int i = 0; i < oldList.size(); i++) {
            T old = oldList.get(i);
            T newd = newList.get(i);
            if (this.comparator.compare(old, newd) == 0) {
                setChildren(old, newd.getChildNode());
                newd.setChildNode(null);
            } else {
                oldList.set(i, newd);
            }
        }

        return oldList;
    }

    /**
     *  (内部)拆分对象
     * @param row 原对象
     * @return
     */
    private List<T> splitObject(S row) {

        List<T> res = convertOpMethod.splitObject(row);
        res.add(convertOpMethod.getTargetObject());

        if (res != null && res.size() > 1) {
            T subObject = res.get(0);
            for (int i = 1; i < res.size(); i++) {
                T thisObject = res.get(i);
                setChildren(thisObject, subObject);
                subObject = thisObject;
            }
        }

        return res;

    }

    /**
     *  (内部)设置多个子节点
     * @param obj
     * @param subList
     */
    private void setChildren(T obj, List<T> subList) {
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
    private void setChildren(T obj, T subNode) {
        if (obj == null || subNode == null) {
            return;
        }

        List<T> subList = obj.getChildNode();
        if (subList == null) {
            subList = new ArrayList<>();
            subList.add(subNode);
            obj.setChildNode(subList);
        } else if (!subList.isEmpty()) {
            if (this.comparator.compare(subNode, subList.get(subList.size() - 1)) != 0) {
                subList.add(subNode);
            }
        } else {
            subList.add(subNode);
        }
    }

    /**
     * @param convertOpMethod 转换方法
     */
    public Object2TreeUtil(ConvertOpMethod<T, S> convertOpMethod) {
        this(convertOpMethod, null);
    }

    /**
     * @param convertOpMethod 转换方法
     * @param comparator 比较方法
     */
    public Object2TreeUtil(ConvertOpMethod<T, S> convertOpMethod,
            Comparator<T> comparator) {

        if (convertOpMethod == null) {
            throw new NullPointerException("参数; targetType,convertOpMethod 不能为空!");
        }
        this.convertOpMethod = convertOpMethod;

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
         * 获取目标对象
         * @return
         */
        public T getTargetObject();

        /**
         * @param index 索引
         * @return 低等级对象在前的List
         */
        public ArrayList<T> splitObject(S srcObject);
    }
}
