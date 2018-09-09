package tool.objectoperation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 获取/设置对象的属性
 * 
 * @author GFuZan
 *
 */
public class ObjectOperationUtil {
	/**
	 * 是否输出log
	 */
	private static final boolean SHOW_LOG = true;

	private ObjectOperationUtil() {

	}

	/**
	 * 获取属性值
	 * 
	 * @param o
	 *            操作对象
	 * @param attName
	 *            属性名
	 * @return
	 */
	public static Object get(Object o, String attName) {
		if (o == null || attName == null || attName.isEmpty()) {
			return null;
		}
		String methodName = attNameHandle("get", attName);

		return Operation(o, methodName, attName, null, null);
	}

	/**
	 * 获取属性值
	 * 
	 * @param o
	 *            操作对象
	 * @param attName
	 *            属性名
	 * @param returnType
	 *            返回值类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(Object o, String attName, Class<T> returnType) {
		if (o == null || attName == null || attName.isEmpty()) {
			return null;
		}
		String methodName = attNameHandle("get", attName);

		return (T) Operation(o, methodName, attName, null, null);
	}

	/**
	 * <b>设置属性值</b>
	 * <p>
	 * 通过参数值确定类型,请保证参数值不为null
	 * </p>
	 * 
	 * @param o
	 *            操作对象
	 * @param attName
	 *            属性名
	 * @param value
	 *            参数值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T set(T o, String attName, Object value) {
		if (o == null || attName == null || attName.isEmpty()) {
			return null;
		}
		String methodName = attNameHandle("set", attName);

		return (T) Operation(o, methodName, attName, null, value);
	}

	/**
	 * 设置属性值
	 * 
	 * @param o
	 *            操作对象
	 * @param attName
	 *            属性名
	 * @param value
	 *            参数值
	 * @param paramType
	 *            参数类型
	 * @return 操作后对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T set(T o, String attName, Object value, Class<?> paramType) {
		if (o == null || attName == null || attName.isEmpty()) {
			return null;
		}
		String methodName = attNameHandle("set", attName);

		return (T) Operation(o, methodName, attName, paramType, value);
	}

	/**
	 * @param o
	 *            操作对象
	 * @param methodName
	 *            方法名
	 * @param attName
	 *            属性名
	 * @param value
	 *            值
	 * @return get方法返回实际值 set方法返回操作后对象
	 */
	private static Object Operation(Object o, String methodName, String attName, Class<?> paramType, Object value) {
		// 方法赋值出错
		boolean opErr = false;
		Object res = null;
		Class<?> type = o.getClass();
		try {
			Method method = null;
			if (methodName.indexOf("get") != -1) {
				// get
				method = type.getMethod(methodName);
				res = method.invoke(o);
			} else {
				// set
				paramType = paramType == null ? value.getClass() : paramType;
				method = type.getMethod(methodName, paramType);
				method.invoke(o, value);
				res = o;
			}
		} catch (Exception e) {
			opErr = true;
			if (SHOW_LOG) {
				System.err.println(getThisName() + ": [WARN] 直接对属性'" + attName + "进行操作(不借助get/set方法).");
			}
		}

		if (opErr) {
			try {
				Field field = null;
				field = type.getDeclaredField(attName);
				field.setAccessible(true);

				if (methodName.indexOf("get") != -1) {
					res = field.get(o);
				} else {
					field.set(o, value);
					res = o;
				}
			} catch (Exception e) {
				if (SHOW_LOG) {
					System.err.println(getThisName() + ": [ERROR] 属性'" + attName + "'操作失败.");
				}
			}
		}

		return res;
	}

	/**
	 * 属性名处理
	 * 
	 * @param method
	 *            方法(get/set)
	 * @param attName
	 * @return
	 */
	private static String attNameHandle(String method, String attName) {
		StringBuffer res = new StringBuffer(method);

		if (attName.length() == 1) {
			res.append(attName.toUpperCase());
		} else {
			char[] charArray = attName.toCharArray();

			if (Character.isLowerCase(charArray[0]) && Character.isLowerCase(charArray[1])) {
				res.append(Character.toUpperCase(charArray[0]));
				res.append(attName.substring(1));
			} else {
				res.append(attName);
			}
		}

		return res.toString();
	}

	/**
	 * @return 当前类名
	 */
	private static String getThisName() {
		String[] split = ObjectOperationUtil.class.getName().split("\\.");
		return split[split.length - 1];

	}
}
