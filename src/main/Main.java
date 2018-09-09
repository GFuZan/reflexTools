package main;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import test.obj.B;
import test.obj.C;
import test.obj.D;
import test.obj.FormatInterface;
import test.obj.toListInterface;
import tool.ObjectComputed.ObjectComputedUtil;
import tool.dataformat.DataFormatUitl;
import tool.obj2list.Object2ListUtil;
import tool.objectoperation.ObjectOperationUtil;

public class Main {

	public static void main(String[] args) throws Exception {
//		format();
//		
//		szys();
		
//		o2l();
		
		oo();
	}

	//对象属性赋值测试
	private static void oo() {
		D d= new D();
		
		ObjectOperationUtil.set(d, "a", null);
//		ObjectOperationUtil.set(d, "A", 2);
		ObjectOperationUtil.set(d, "aA", 3);
		ObjectOperationUtil.set(d, "Aa", 4);
		ObjectOperationUtil.set(d, "AA", 5);
		
		//对象中包含属性 a 与 A, get set方法重合 
		System.out.println("a = "+ ObjectOperationUtil.get(d, "a", Integer.class));
		System.out.println("A = "+ ObjectOperationUtil.get(d, "A", Integer.class));
		System.out.println("aA = "+ ObjectOperationUtil.get(d, "aA", Integer.class));
		System.out.println("Aa = "+ ObjectOperationUtil.get(d, "Aa", Integer.class));
		System.out.println("AA = "+ ObjectOperationUtil.get(d, "AA", Integer.class));
	}

	/**
	 * 数值格式化测试
	 */
	private static FormatInterface format(){
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
		return f;
	}
	
	/**
	 * 四则运算测试
	 */
	private static void szys() {
		B b = new B();
		b.setAge(10);
		b.setName("张三");
		b.setSex(false);
		b.setD(10.00);
		b.setMbig(new BigDecimal("666"));
//		b.setMbig(null);
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
//		a.setMbig(null);
		 b1.setMbyte((byte)10);
		 b1.setMchar('x');
		 b1.setMfloat(4.44f);
		  b1.setMint(100);
		b1.setMlong(2000l);
		 b1.setMshort((short)50);
		b1.setDate(new Date());
		
		B c =new B();
		c.setSex(false);
		c.setD(333.33);
		c.setMbig(new BigDecimal("9.9"));
//		a.setMbig(null);
		 c.setMbyte((byte)10);
		 c.setMchar('x');
		 c.setMfloat(4.44f);
		  c.setMint(100);
		c.setMlong(2000l);
		 c.setMshort((short)50);
		c.setDate(new Date());
		
		
		// 对象四则运算
		System.out.println("+   " + ObjectComputedUtil.add(true, b1, b));
		
		Object o= new Object(); 
		System.out.println("-   " + ObjectComputedUtil.subtract(true,b1, c));
		
		System.out.println("*   " + ObjectComputedUtil.multiply(b1, b, true, Integer.class));
		System.out.println("/   " + ObjectComputedUtil.divide(b1, b, true));
	}
	
	/**
	 * 对象转数组测试
	 */
	private static void o2l() {
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
		
		List<Object> list = Object2ListUtil.toList(format(),toListInterface.class);
		System.out.println(list);
	}
}
