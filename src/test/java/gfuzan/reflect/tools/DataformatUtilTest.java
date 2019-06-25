package gfuzan.reflect.tools;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;

import gfuzan.reflect.tools.dataformat.DataFormatUitl;
import gfuzan.reflect.tools.entity.B;
import gfuzan.reflect.tools.entity.FormatInterface;

public class DataformatUtilTest {
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

        FormatInterface f = DataFormatUitl.newInstance(a, FormatInterface.class);
        System.out.println(f.getMlong());
        System.out.println(f.getDate());
        System.out.println(f.getMfloat());

    }
}
