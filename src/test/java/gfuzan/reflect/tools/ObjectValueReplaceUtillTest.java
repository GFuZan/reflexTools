package gfuzan.reflect.tools;

import java.math.BigDecimal;

import org.junit.Test;

import gfuzan.reflect.tools.entity.B;
import gfuzan.reflect.tools.objectvaluereplace.ObjectValueReplaceUtill;

public class ObjectValueReplaceUtillTest {

    @Test
    public void test_01() {
        B b = new B();

        b.setAge(10);
        b.setName("张三");
        b.setSex(false);
        b.setD(0.0);
        b.setMbig(new BigDecimal("666"));
        b.setMbig2(new BigDecimal("0"));
        b.setMbyte((byte) 0);
        b.setMchar('x');
        b.setMfloat(4.44f);
        b.setMint(100);
        b.setMlong(2000l);
        b.setMshort((short) 50);

        System.out.println(b);

//      ObjectValueReplaceUtill.execute(b, "x", "15");

        b = ObjectValueReplaceUtill.execute(b, "false", null);
        b = ObjectValueReplaceUtill.execute(b, null, "true",Boolean.class);

        System.out.println(b);
    }
}
