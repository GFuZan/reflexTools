package gfuzan.reflect.tools;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import gfuzan.reflect.tools.entity.B;
import gfuzan.reflect.tools.entity.D;
import gfuzan.reflect.tools.objectoperation.ObjectUtil;
import gfuzan.reflect.tools.objectoperation.ObjectUtil.OpType;

public class ObjectUtilTest {

    @Test
    public void test_01() {

        ObjectUtil objectUtil = new ObjectUtil();

        D d = new D();

        objectUtil.set(d, "a", null);
        // ObjectOperationUtil.set(d, "A", 2);
        objectUtil.set(d, "aA", 3);
        objectUtil.set(d, "Aa", 4);
        objectUtil.set(d, "AA", 5);

        // 对象中包含属性 a 与 A, get set方法重合
        System.out.println("a = " + objectUtil.get(d, "a", Integer.class));
        System.out.println("A = " + objectUtil.get(d, "A", Integer.class));
        System.out.println("aA = " + objectUtil.get(d, "aA", Integer.class));
        System.out.println("Aa = " + objectUtil.get(d, "Aa", Integer.class));
        System.out.println("AA = " + objectUtil.get(d, "AA", Integer.class));

        Map<String, String> map = new HashMap<>();

        objectUtil.set(map, "哈哈", "不好");
        objectUtil.set(map, "1", "2");

        for (String key : map.keySet()) {
            System.out.println(key.toString() + "= " + objectUtil.get(map, key));
        }

    }
    
    /**
     * 获取/设置父类的字段
     */
    @Test
    public void test_02() {
        B o = new B();
        
        ObjectUtil objectUtil = new ObjectUtil();
        
        objectUtil.set(o, "name", "zhang", OpType.onlyField);
        
        System.out.println(objectUtil.get(o, "name", OpType.onlyField));
        
    }
    
    /**
     * getAllField 测试
     */
    @Test
    public void test_03() {
        B o = new B();
        ObjectUtil objectUtil = new ObjectUtil();
        
        {
            List<String> list = objectUtil.getAllFieldName(o);
            
            for(String name: list) {
                System.out.println(name);
            }
        }
        
        {
            List<Field> list = objectUtil.getAllField(o);
            
            for(Field field: list) {
                System.out.println(field.getModifiers()+" " +field.getType().getSimpleName()+" "+field.getName());
            }
        }
    }
}
