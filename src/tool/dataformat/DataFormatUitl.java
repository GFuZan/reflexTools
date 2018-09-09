package tool.dataformat;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * 使用动态代理进行数据Forma
 * 
 * @author GFuZan
 */
public class DataFormatUitl implements InvocationHandler {

	/**
	 * 是否打印log
	 */
	private boolean SHOW_LOG = true;
	/**
	 * 格式化数值
	 */
	public static final String NUMBER = "0";
	/**
	 * 格式化时间
	 */
	public static final String DATETIME = "1";

	private Object target = null;
	private final DecimalFormat numFormat = new DecimalFormat();
	private final SimpleDateFormat dateFormat = new SimpleDateFormat();

	/**
	 * @param obj
	 *            要Format的对象
	 * @param formatInterface
	 *            Format接口类
	 * @return Format接口类对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(T obj, Class<T> formatInterface) {
		T res = null;
		try {
			InvocationHandler ih = DataFormatUitl.class.newInstance();
			setParam(ih, "target", obj);
			res = (T) Proxy.newProxyInstance(formatInterface.getClassLoader(), new Class[] { formatInterface }, ih);
		} catch (Exception e) {
			System.err.println(getThisName() + ": [ERROR] " + e);
		}
		return res;

	}

	/**
	 * @param obj
	 *            要Format的对象
	 * @param formatInterface
	 *            Format接口类
	 * @param printLog
	 *            是否打印log
	 * @return Format接口类对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(T obj, Class<T> formatInterface, boolean printLog) {
		T res = null;
		try {
			InvocationHandler ih = DataFormatUitl.class.newInstance();
			setParam(ih, "target", obj);
			setParam(ih, "SHOW_LOG", printLog);
			res = (T) Proxy.newProxyInstance(formatInterface.getClassLoader(), new Class[] { formatInterface }, ih);
		} catch (Exception e) {
			System.err.println(getThisName() + ": [ERROR] " + e);
		}
		return res;

	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object res = null;
		DataFormat df = method.getAnnotation(DataFormat.class);
		Object value = method.invoke(target, args);
		try {
			switch (df.style()) {
			case NUMBER:
				numFormat.applyPattern(df.pattern());
				res = numFormat.format(value);
				break;
			case DATETIME:
				dateFormat.applyPattern(df.pattern());
				res = dateFormat.format(value);
				break;
			default:
				break;
			}
		} catch (NullPointerException e) {
			res = value;
			if (SHOW_LOG) {
				System.err.println(getThisName() + ": [info] 方法'" + method.getName() + "' 没有Format注解.");
			}
		} catch (IllegalArgumentException e) {
			res = value;
			if (SHOW_LOG) {
				System.err.print(getThisName() + ": [WARN] 方法'" + method.getName() + "' Format失败. ");

				if (value == null) {
					System.err.println(" ('" + method.getName() + "'返回值为 null)");
				} else {
					System.err.println(" (" + e.getMessage() + ")");
				}
			}
		}
		return res;
	}

	/**
	 * 设置参数
	 * 
	 * @throws
	 * @throws NoSuchFieldException
	 */
	private static InvocationHandler setParam(InvocationHandler ih, String name, Object value) throws Exception {
		Field field = DataFormatUitl.class.getDeclaredField(name);
		field.setAccessible(true);
		field.set(ih, value);
		return ih;
	}

	/**
	 * @return 当前类名
	 */
	private static String getThisName() {
		String[] split = DataFormatUitl.class.getName().split("\\.");
		return split[split.length - 1];

	}
}
