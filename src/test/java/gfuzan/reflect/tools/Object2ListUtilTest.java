package gfuzan.reflect.tools;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import gfuzan.reflect.tools.entity.B;
import gfuzan.reflect.tools.entity.toListInterface;
import gfuzan.reflect.tools.obj2list.Object2ListUtil;

public class Object2ListUtilTest {
    @Test
    public void test_01() {

        B a = new B();
        a.setAge(999999);
        a.setName("李四");
        // a.setSex(false);
        a.setD(333.33);
        a.setMbig(new BigDecimal("9.9"));
        // a.setMbyte((byte)10);
        // a.setMchar('x');
        a.setMfloat(44f);
        // // a.setMint(100);
        a.setMlong(1234567890l);
        // a.setMshort((short)50);
        a.setDate(new Date());

        List<Object> list = Object2ListUtil.toList(a, toListInterface.class);
        System.out.println(list);

    }
}
