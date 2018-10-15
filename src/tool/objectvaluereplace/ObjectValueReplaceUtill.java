package tool.objectvaluereplace;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * <b> 对象内属性值替换</b>
 * <p>
 * 支持[double,byte,float,short,int,long,char,BigDecimal,String]类型值替换<br/>
 * 注意: 请使用基本类型的包装类作为类的属性
 * </p>
 * 
 * @author GFuZan
 *
 */
public class ObjectValueReplaceUtill {
	/**
	 * 是否输出log
	 */
	private static boolean SHOW_LOG = true;

	/**
	 * @param obj
	 *            操作对象
	 * @param srcValue
	 *            要替换的值
	 * @param targetValue
	 *            目标值
	 * @param handleType
	 *            可选 指定替换的属性类型
	 * @return 操作后的对象
	 */
	public static <T> T execute(T obj, String srcValue, String targetValue, Class<?>... handleType) {

		return executeImpl(obj, srcValue, targetValue, handleType);
	}

	/**
	 * @param obj
	 *            操作对象
	 * @param srcValue
	 *            要替换的值
	 * @param targetValue
	 *            目标值
	 * @param showLog
	 *            是否显示log
	 * @param handleType
	 *            可选 指定替换的属性类型
	 * @return 操作后的对象
	 */
	public static <T> T execute(T obj, String srcValue, String targetValue, boolean showLog, Class<?>... handleType) {

		SHOW_LOG = showLog;

		return executeImpl(obj, srcValue, targetValue, handleType);
	}

	/**
	 * @param obj
	 *            操作对象
	 * @param srcValue
	 *            要替换的值
	 * @param targetValue
	 *            目标值
	 * @param handleType
	 *            可选 指定替换的属性类型
	 * @return 操作后的对象
	 */
	private static <T> T executeImpl(T obj, String srcValue, String targetValue, Class<?>... handleType) {
		T res = obj;

		if (res == null) {
			return null;
		}

		Class<?> type = res.getClass();
		try {
			Method[] methods = type.getMethods();
			for (Method m : methods) {
				if (m.getName().indexOf("set") == 0) {
					// 找到对应get方法名
					String getMethodName = getAttGetMethod(m.getName());
					Method getMethod = null;
					try {

						// /获取get对应的方法
						getMethod = type.getMethod(getMethodName);

						// 获取属性值
						Object v = getMethod.invoke(res);

						Object tarV = v;

						try {
							// 执行替换
							tarV = valueReplaceHandle(v, srcValue, targetValue, getMethod.getReturnType(), handleType);
						} catch (InvocationTargetException e) {
							if (SHOW_LOG && "WARN".equals(e.getTargetException().getMessage())) {
								System.err.println(getThisName() + ": [WARN] " + "未操作属性类型: " + getMethod.getReturnType().getName() + ", "+e.getMessage());
							}
							continue;
						}
						
						// 属性赋值
						m.invoke(res, tarV);

					} catch (NoSuchMethodException e) {
						if (SHOW_LOG) {
							System.err.println(getThisName() + ": [WARN] 方法'" + getMethodName + "'没有找到.");
						}
					} catch (InvocationTargetException e) {
						if (SHOW_LOG) {
							System.err.print(getThisName() + ": [WARN] 方法'" + m.getName() + "' 的参数类型为 '" + m.getParameterTypes()[0].getName() + "'");
							System.err.println(",而方法'" + getMethod.getName() + "' 的返回值类型为 '" + getMethod.getReturnType().getName() + "'.");
						}
					}
				}
			}
		} catch (Exception e) {
			System.err.println(getThisName() + ": [ERROR] " + e.getMessage());
		}

		return res;
	}

	/**
	 * @param v
	 *            对象内属性值
	 * @param srcValue
	 *            要替换的值
	 * @param targetValue
	 *            目标值
	 * @param paramType
	 *            对象内属性类型
	 * @param handleType
	 *            可选 指定替换的属性类型
	 * @return 替换后的值
	 * @throws InvocationTargetException
	 *             不支持的类型值
	 */
	private static Object valueReplaceHandle(Object v, String srcValue, String targetValue, Class<?> paramType, Class<?>... handleType) throws InvocationTargetException {

		Object res = v;

		// 若指定了属性类型
		if (handleType != null && handleType.length != 0) {
			// 判断对象内属性类型 是否包含在指定属性类型中
			if (!Arrays.asList(handleType).contains(paramType)) {
				// 未指定类型
				throw new InvocationTargetException(new Throwable("INFO"), "未指定此类型");
			}
		}

		if (BigDecimal.class.equals(paramType)) {
			res = valueReplaceExec(v, srcValue, targetValue, OpAttType.isBigDecimal);
		} else if (Integer.class.equals(paramType)) {
			res = valueReplaceExec(v, srcValue, targetValue, OpAttType.isInteger);
		} else if (Long.class.equals(paramType)) {
			res = valueReplaceExec(v, srcValue, targetValue, OpAttType.isLong);
		} else if (Short.class.equals(paramType)) {
			res = valueReplaceExec(v, srcValue, targetValue, OpAttType.isShort);
		} else if (Character.class.equals(paramType)) {
			res = valueReplaceExec(v, srcValue, targetValue, OpAttType.isCharacter);
		} else if (Byte.class.equals(paramType)) {
			res = valueReplaceExec(v, srcValue, targetValue, OpAttType.isByte);
		} else if (Double.class.equals(paramType)) {
			res = valueReplaceExec(v, srcValue, targetValue, OpAttType.isDouble);
		} else if (Float.class.equals(paramType)) {
			res = valueReplaceExec(v, srcValue, targetValue, OpAttType.isFloat);
		} else if (Boolean.class.equals(paramType)) {
			res = valueReplaceExec(v, srcValue, targetValue, OpAttType.isBoolean);
		} else if (String.class.equals(paramType)) {
			res = valueReplaceExec(v, srcValue, targetValue, OpAttType.isString);
		} else {
			// 不支持的类型
			throw new InvocationTargetException(new Throwable("WARN"), "不支持类型: " + paramType.getName());
		}

		return res;
	}

	/**
	 * @param v
	 *            对象内属性值
	 * @param srcValue
	 *            要替换的值
	 * @param targetValue
	 *            目标值
	 * @param opAttType
	 *            属性类型
	 * @return 替换后的值
	 * @throws InvocationTargetException
	 */
	private static Object valueReplaceExec(Object v, String srcValue, String targetValue, int opAttType) throws InvocationTargetException {

		Object res = v;

		// 目标值为null
		boolean targetValueIsNULL = (targetValue == null);

		// 是要替换的值
		boolean isEqual = false;

		if (v == null && srcValue == null) {
			isEqual = true;
		}
		try {
			switch (opAttType) {
			case OpAttType.isBigDecimal:
				if (isEqual || ((v != null && srcValue != null) && (new BigDecimal(srcValue).compareTo((BigDecimal) v) == 0))) {
					isEqual = true;
					if (!targetValueIsNULL) {
						res = new BigDecimal(targetValue);
					}
				}
				break;
			case OpAttType.isInteger:
				if (isEqual || ((v != null && srcValue != null) && (Integer.parseInt(srcValue) == (Integer) v))) {
					isEqual = true;
					if (!targetValueIsNULL) {
						res = Integer.parseInt(targetValue);
					}
				}
				break;
			case OpAttType.isLong:
				if (isEqual || ((v != null && srcValue != null) && (Long.parseLong(srcValue) == (Long) v))) {
					isEqual = true;
					if (!targetValueIsNULL) {
						res = Long.parseLong(targetValue);
					}
				}
				break;
			case OpAttType.isShort:
				if (isEqual || ((v != null && srcValue != null) && (Short.parseShort(srcValue) == (Short) v))) {
					isEqual = true;
					if (!targetValueIsNULL) {
						res = Short.parseShort(targetValue);
					}
				}
				break;
			case OpAttType.isCharacter:
				if (isEqual || ((v != null && srcValue != null) && (srcValue.toCharArray()[0] == (Character) v))) {
					isEqual = true;
					if (!targetValueIsNULL) {
						if (targetValue.length() == 0) {
							res = '\0';
						} else {
							res = targetValue.charAt(0);
						}
					}
				}
				break;
			case OpAttType.isByte:
				if (isEqual || ((v != null && srcValue != null) && (Byte.parseByte(srcValue) == (Byte) v))) {
					isEqual = true;
					if (!targetValueIsNULL) {
						res = Byte.parseByte(targetValue);
					}
				}
				break;
			case OpAttType.isDouble:
				if (isEqual || ((v != null && srcValue != null) && (Double.parseDouble(srcValue) == (Double) v))) {
					isEqual = true;
					if (!targetValueIsNULL) {
						res = Double.parseDouble(targetValue);
					}
				}
				break;
			case OpAttType.isFloat:
				if (isEqual || ((v != null && srcValue != null) && (Float.parseFloat(srcValue) == (Float) v))) {
					isEqual = true;
					if (!targetValueIsNULL) {
						res = Float.parseFloat(targetValue);
					}
				}
				break;
			case OpAttType.isBoolean:
				if (isEqual || ((v != null && srcValue != null) && ("false".equals(srcValue) || "true".equals(srcValue)) && (Boolean.parseBoolean(srcValue) == (Boolean) v))) {
					isEqual = true;
					if (!targetValueIsNULL && ("false".equals(targetValue) || "true".equals(targetValue))) {
						res = Boolean.parseBoolean(targetValue);
					}
				}
				break;
			case OpAttType.isString:
				if (isEqual || ((v != null && srcValue != null) && (srcValue.equals(v)))) {
					isEqual = true;
					if (!targetValueIsNULL) {
						res = targetValue;
					}
				}
				break;
			}
		} catch (Exception e) {
			//设置值时出现异常
			if (isEqual) {
				throw new InvocationTargetException(new Throwable("WARN"), "设置值失败:  " + srcValue);
			}
		}
		// 需要替换
		if (isEqual) {
			// 目标值为null
			if (targetValueIsNULL) {
				return null;
			} else {
				return res;
			}
		} else {
			// 没有搜索到值
			throw new InvocationTargetException(new Throwable("INFO"), "没有搜索到值: " + srcValue);
		}
	}

	/**
	 * @return set方法对应的get方法名
	 */
	private static String getAttGetMethod(String setMethodName) {
		String getMethodName = "get" + setMethodName.substring(3);
		return getMethodName;

	}

	/**
	 * @return 当前类名
	 */
	private static String getThisName() {
		String[] split = ObjectValueReplaceUtill.class.getName().split("\\.");
		return split[split.length - 1];

	}
	
	class OpAttType {
	    static final int isBigDecimal = 1;
	    static final int isInteger = 2;
	    static final int isLong = 3;
	    static final int isShort = 4;
	    static final int isCharacter = 5;
	    static final int isByte = 6;
	    static final int isDouble = 7;
	    static final int isFloat = 8;
	    static final int isBoolean = 9;
	    static final int isString = 10;
	}
}
