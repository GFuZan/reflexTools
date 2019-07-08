package gfuzan.reflect.tools.entity;

import java.util.List;

import gfuzan.reflect.tools.tree.TreeUtil.TreeNode;

public class TreeNodeVo implements TreeNode<TreeNodeVo> {
    /**
     * 标签
     */
    private String label;
    /**
     * 名称
     */
    private String name;
    /**
     * code
     */
    private String code;
    /**
     * 子节点
     */
    private List<TreeNodeVo> children;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TreeNodeVo other = (TreeNodeVo) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<TreeNodeVo> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNodeVo> children) {
        this.children = children;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public List<TreeNodeVo> getChildNode() {
        return this.getChildren();
    }

    @Override
    public void setChildNode(List<TreeNodeVo> childNode) {
        this.setChildren(childNode);
    }
}
