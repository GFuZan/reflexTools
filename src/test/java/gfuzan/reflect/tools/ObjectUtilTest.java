package gfuzan.reflect.tools;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import gfuzan.reflect.tools.entity.D;
import gfuzan.reflect.tools.objectoperation.ObjectUtil;

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
}
