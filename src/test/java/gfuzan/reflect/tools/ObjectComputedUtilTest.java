package gfuzan.reflect.tools;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;

import gfuzan.reflect.tools.entity.B;
import gfuzan.reflect.tools.objectcomputed.ObjectComputedUtil;

public class ObjectComputedUtilTest {
    @Test
    public void test_01() {
        B b = new B();
        b.setAge(10);
        b.setName("张三");
        b.setSex(false);
        b.setD(10.00);
        b.setMbig(new BigDecimal("666"));
        // b.setMbig(null);
        b.setMbyte((byte) 10);
        b.setMchar('x');
        b.setMfloat(4.44f);
        b.setMint(100);
        b.setMlong(2000l);
        b.setMshort((short) 50);

        B b1 = new B();
        b1.setAge(999999);
        b1.setName("李四");
        b1.setSex(false);
        b1.setD(333.33);
        b1.setMbig(new BigDecimal("9.9"));
        // a.setMbig(null);
        b1.setMbyte((byte) 10);
        b1.setMchar('x');
        b1.setMfloat(4.44f);
        b1.setMint(100);
        b1.setMlong(2000l);
        b1.setMshort((short) 50);
        b1.setDate(new Date());

        B c = new B();
        c.setSex(false);
        c.setD(333.33);
        c.setMbig(new BigDecimal("9.9"));
        // a.setMbig(null);
        c.setMbyte((byte) 10);
        c.setMchar('x');
        c.setMfloat(4.44f);
        c.setMint(100);
        c.setMlong(2000l);
        c.setMshort((short) 50);
        c.setDate(new Date());

        // 对象四则运算
        System.out.println("+   " + ObjectComputedUtil.add(b1, b));

        System.out.println("-   " + ObjectComputedUtil.subtract(true, b1, c));

        System.out.println("*   " + ObjectComputedUtil.multiply(b1, b, true, Integer.class));
        System.out.println("/   " + ObjectComputedUtil.divide(b1, b, true));
    }
}
