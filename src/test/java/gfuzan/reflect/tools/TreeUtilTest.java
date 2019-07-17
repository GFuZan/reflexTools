package gfuzan.reflect.tools;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;

import gfuzan.reflect.tools.entity.TableData;
import gfuzan.reflect.tools.entity.TreeNodeVo;
import gfuzan.reflect.tools.tree.Object2TreeUtil;
import gfuzan.reflect.tools.tree.Object2TreeUtil.ConvertOpMethod;
import gfuzan.reflect.tools.tree.TreeUtil;
import gfuzan.reflect.tools.tree.TreeUtil.StopType;

public class TreeUtilTest {

    @Test
    public void test_01() throws Exception {
        List<TableData> tableDataList = new ArrayList<>();

        // 生成数据
        for (int a = 0; a < 50; a++) {
            String aName = "aName" + a;
            String aCode = "aCode" + a;

            for (int b = 0; b < 10; b++) {
                String bName = "bName" + a + "_" + b;
                String bCode = "bCode" + a + "_" + b;
                for (int c = 0; c < 10; c++) {
                    String cName = "cName" + a + "_" + b + "_" + c;
                    String cCode = "cCode" + a + "_" + b + "_" + c;
                    for (int d = 0; d < 10; d++) {
                        String dName = "dName" + a + "_" + b + "_" + c + "_" + d;
                        String dCode = "dCode" + a + "_" + b + "_" + c + "_" + d;

                        tableDataList.add(new TableData(aName, aCode, bName, bCode, cName, cCode, dName, dCode));
                    }
                }
            }
        }

        // 创建转换工具
        Object2TreeUtil<TreeNodeVo, TableData> object2Tree = new Object2TreeUtil<>(
                new ConvertOpMethod<TreeNodeVo, TableData>() {

                    @Override
                    public TreeNodeVo getTargetObject() {
                        return new TreeNodeVo();
                    }

                    @Override
                    public ArrayList<TreeNodeVo> splitObject(TableData srcObject) {

                        // ArrayList中目标对象必须遵循从低层到高层的顺序
                        ArrayList<TreeNodeVo> targetList = new ArrayList<>();

                        // 第三层
                        TreeNodeVo targetObject = getTargetObject();
                        targetObject.setCode(srcObject.getdCode());
                        targetObject.setLabel(srcObject.getdName());
                        targetObject.setName(srcObject.getdName());
                        targetList.add(targetObject);

                        // 第二层
                        targetObject = getTargetObject();
                        targetObject.setCode(srcObject.getcCode());
                        targetObject.setLabel(srcObject.getcName());
                        targetObject.setName(srcObject.getcName());
                        targetList.add(targetObject);

                        // 第一层
                        targetObject = getTargetObject();
                        targetObject.setCode(srcObject.getbCode());
                        targetObject.setLabel(srcObject.getbName());
                        targetObject.setName(srcObject.getbName());
                        targetList.add(targetObject);

                        // 第零层
                        targetObject = getTargetObject();
                        targetObject.setCode(srcObject.getaCode());
                        targetObject.setLabel(srcObject.getaName());
                        targetObject.setName(srcObject.getaName());
                        targetList.add(targetObject);

                        return targetList;
                    }
                });

        long now = System.currentTimeMillis();

        // tableDataList 已排序[ order by dCode, cCode, bCode, aCode]
        List<TreeNodeVo> RptYumCityVoList = object2Tree.excute(tableDataList);
        System.out.println("生成树耗时: " + (System.currentTimeMillis() - now) + "ms");
        
        // 移除空节点
        List<TreeNodeVo> treeList = object2Tree.removeEmptyNode(RptYumCityVoList);

        final List<TreeNodeVo> resList = new ArrayList<>();
        now = System.currentTimeMillis();
        TreeUtil.forestTraversal(treeList, new TreeUtil.TreeNodeOpMethod<TreeNodeVo>() {
            @Override
            public StopType opFunc(TreeNodeVo node, int depth) {
                if (depth == 3) {
                    resList.add(node);
                    // System.out.println(tree);
                    return StopType.Node;
                }
                return null;
            }
        });
        System.out.println("遍历耗时: " + (System.currentTimeMillis() - now) + "ms");
        System.out.println("指定深度的节点个数: " + resList.size());

        FileOutputStream fos = new FileOutputStream("D:/json.txt");
        fos.write(new Gson().toJson(treeList).getBytes());
        fos.close();
    }

}
