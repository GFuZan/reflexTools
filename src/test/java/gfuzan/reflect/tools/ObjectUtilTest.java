package gfuzan.reflect.tools;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import gfuzan.reflect.tools.entity.D;
import gfuzan.reflect.tools.objectoperation.ObjectUtil;

public class ObjectUtilTest {

    @Test
    public void test_01() {

        D d = new D();

        ObjectUtil.set(d, "a", null);
        // ObjectOperationUtil.set(d, "A", 2);
        ObjectUtil.set(d, "aA", 3);
        ObjectUtil.set(d, "Aa", 4);
        ObjectUtil.set(d, "AA", 5);

        // 对象中包含属性 a 与 A, get set方法重合
        System.out.println("a = " + ObjectUtil.get(d, "a", Integer.class));
        System.out.println("A = " + ObjectUtil.get(d, "A", Integer.class));
        System.out.println("aA = " + ObjectUtil.get(d, "aA", Integer.class));
        System.out.println("Aa = " + ObjectUtil.get(d, "Aa", Integer.class));
        System.out.println("AA = " + ObjectUtil.get(d, "AA", Integer.class));

        Map<String, String> map = new HashMap<>();

        ObjectUtil.set(map, "哈哈", "不好");
        ObjectUtil.set(map, "1", "2");

        for (String key : map.keySet()) {
            System.out.println(key.toString() + "= " + ObjectUtil.get(map, key));
        }

    }
}
