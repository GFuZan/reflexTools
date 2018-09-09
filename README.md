# reflexTools
反射工具集

<h4>一些java反射应用实例</h4>
<p>
<h5>1. 对象间四则运算工具</h5>
 * 1. 请保证运算对象的get方法返回值与对应的set方法参数值类型一致<br/>
 * 2. 本工具实现了[double,byte,float,short,int,long,char,BigDecimal]的运算<br/>
 * 3.对于boolean类型属性所有运算操作均为异或运算<br/>
 * 4对于String类型只有 + 运算 <br/>
 * 注意: 请使用基本类型的包装类作为类的属性
<pre>
 简单示例:
    //加法
    ObjectComputedUtil.add( obj1, obj2);
</pre>
</p>
<p>
<h5>2. 数据Format工具</h5>
使用动态代理实现,通过注解指定Format格式
<pre>
 简单示例:
 1.定义Format接口
    public interface FormatInterface{
	
        @DataFormat(style=DataFormatUitl.NUMBER, pattern="##.##%")
        public Object getMfloat();
    }
 2.获取此接口对象
    FormatInterface f = DataFormatUitl.newInstance(obj, FormatInterface.class);

 3.返回指定格式值
    f.getMlong();
</pre>
</p>
<p>
<h5>3. 对象转数组工具</h5>
通过注解指定属性的先后顺序,生成数组
<pre>
 简单示例:
 1.定义toList接口
     public interface toListInterface {
        @Object2List(-1)
        public Object getMlong();

        @Object2List(6)
        public Object getDate();
		
        @Object2List(3)
        public Object getMfloat();
    }
 2.进行转换	
    List<Object> list = Object2ListUtil.toList(format(),toListInterface.class);
</pre>
</p>
<p>
<h5>4. 对象属性的get/set工具</h5>
通过指定属性名的方式获取/设置对象的属性
<pre>
 简单示例:
    //对象obj的name属性赋值为"张三"
    ObjectOperationUtil.set(obj, "name", "张三");
</pre>
</p>
