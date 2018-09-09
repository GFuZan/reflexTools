package tool.ObjectComputed;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 * <b> 此实现对象内属性的四则运算</b>
 * <p>
 * 1. 请保证运算对象的get方法返回值与对应的set方法参数值类型一致<br/>
 * 2. 本工具实现了[double,byte,float,short,int,long,char,BigDecimal]的运算<br/>
 * 3.对于boolean类型属性所有运算操作均为异或运算<br/>
 * 4对于String类型只有 + 运算 <br/>
 * 注意: 请使用基本类型的包装类作为类的属性
 * </p>
 * 
 * @author GFuZan
 */
public class ObjectComputedUtil {

	/**
	 * 是否输出log
	 */
	private static final boolean SHOW_LOG = true;

	/**
	 * ZERO值,与nullIsZero参数相关
	 */
	private static final Integer ZERO = 0;
	/**
	 * 除法计算错误标志,不建议更改
	 */
	private static final String OP_ERR = "0~E!r`?R^X";

	/**
	 * 除法计算错误默认值
	 */
	private static final Integer ERR_VAL = null;

	/**
	 * BigDecimal的小数点后位数
	 */
	private static final Integer BIG_SCALE = 16;

	/**
	 * BigDecimal精度取舍模式
	 */
	private static final Integer BIG_ROUND_MODE = BigDecimal.ROUND_HALF_UP;

	/**
	 * 对象内属性相加
	 * 
	 * @param o1
	 *            对象1
	 * @param o2
	 *            对象2
	 * @param opAttType
	 *            此参数为可选值<br/>
	 *            指定参与计算的属性类型,不参与计算的属性默认结果为null
	 * @return o1 + o2
	 * 
	 */
	public static <T> T add(T o1, T o2, Class<?>... opAttType) {
		return objectOperation(o1, o2, '+', false, null, opAttType);
	}

	/**
	 * 对象内属性相加
	 * 
	 * @param o1
	 *            对象1
	 * @param o2
	 *            对象2
	 * @param nullIsZero
	 *            计算时是否将 null 当作 zero 处理<br/>
	 *            当两个对象中此属性都为null时,指定此字段无效<br/>
	 *            对[String,Boolean]无效
	 * @param opAttType
	 *            此参数为可选值<br/>
	 *            指定参与计算的属性类型,不参与计算的属性默认结果为null
	 * @return o1 + o2
	 * 
	 */
	public static <T> T add(T o1, T o2, boolean nullIsZero, Class<?>... opAttType) {
		return objectOperation(o1, o2, '+', nullIsZero, null, opAttType);
	}

	/**
	 * 对象内属性相加
	 * 
	 * @param defValueIsOne
	 *            当属性运算结果null运算时,结果对象属性值的来源<br/>
	 *            true o1, false o2
	 * @param o1
	 *            对象1
	 * @param o2
	 *            对象2
	 * @param opAttType
	 *            此参数为可选值<br/>
	 *            指定参与计算的属性类型,不参与计算的属性默认结果为null
	 * @return o1 + o2
	 * 
	 */
	public static <T> T add(boolean defValueIsOne, T o1, T o2, Class<?>... opAttType) {
		return objectOperation(o1, o2, '+', false, defValueIsOne, opAttType);
	}

	/**
	 * 对象内属性相减
	 * 
	 * @param o1
	 *            对象1
	 * @param o2
	 *            对象2
	 * @param opAttType
	 *            此参数为可选值<br/>
	 *            指定参与计算的属性类型,不参与计算的属性默认结果为null
	 * @return o1 - o2
	 */
	public static <T> T subtract(T o1, T o2, Class<?>... opAttType) {
		return objectOperation(o1, o2, '-', false, null, opAttType);
	}

	/**
	 * 对象内属性相减
	 * 
	 * @param o1
	 *            对象1
	 * @param o2
	 *            对象2
	 * @param nullIsZero
	 *            计算时是否将 null 当作 zero 处理<br/>
	 *            当两个对象中此属性都为null时,指定此字段无效<br/>
	 *            对[String,Boolean]无效
	 * @param opAttType
	 *            此参数为可选值<br/>
	 *            指定参与计算的属性类型,不参与计算的属性默认结果为null
	 * @return o1 - o2
	 * 
	 */
	public static <T> T subtract(T o1, T o2, boolean nullIsZero, Class<?>... opAttType) {
		return objectOperation(o1, o2, '-', nullIsZero, null, opAttType);
	}

	/**
	 * 对象内属性相减
	 * 
	 * @param defValueIsOne
	 *            当属性运算结果null运算时,结果对象属性值的来源<br/>
	 *            true o1, false o2
	 * @param o1
	 *            对象1
	 * @param o2
	 *            对象2
	 * @param opAttType
	 *            此参数为可选值<br/>
	 *            指定参与计算的属性类型,不参与计算的属性默认结果为null
	 * @return o1 - o2
	 * 
	 */
	public static <T> T subtract(boolean defValueIsOne, T o1, T o2, Class<?>... opAttType) {
		return objectOperation(o1, o2, '-', false, defValueIsOne, opAttType);
	}

	/**
	 * 对象内属性相乘
	 * 
	 * @param o1
	 *            对象1
	 * @param o2
	 *            对象2
	 * @param opAttType
	 *            此参数为可选值<br/>
	 *            指定参与计算的属性类型,不参与计算的属性默认结果为null
	 * @return o1 * o2
	 */
	public static <T> T multiply(T o1, T o2, Class<?>... opAttType) {
		return objectOperation(o1, o2, '*', false, null, opAttType);
	}

	/**
	 * 对象内属性相乘
	 * 
	 * @param o1
	 *            对象1
	 * @param o2
	 *            对象2
	 * @param nullIsZero
	 *            计算时是否将 null 当作 zero 处理<br/>
	 *            当两个对象中此属性都为null时,指定此字段无效<br/>
	 *            对[String,Boolean]无效
	 * @param opAttType
	 *            此参数为可选值<br/>
	 *            指定参与计算的属性类型,不参与计算的属性默认结果为null
	 * @return o1 * o2
	 * 
	 */
	public static <T> T multiply(T o1, T o2, boolean nullIsZero, Class<?>... opAttType) {
		return objectOperation(o1, o2, '*', nullIsZero, null, opAttType);
	}

	/**
	 * 对象内属性相乘
	 * 
	 * @param defValueIsOne
	 *            当属性运算结果null运算时,结果对象属性值的来源<br/>
	 *            true o1, false o2
	 * @param o1
	 *            对象1
	 * @param o2
	 *            对象2
	 * @param opAttType
	 *            此参数为可选值<br/>
	 *            指定参与计算的属性类型,不参与计算的属性默认结果为null
	 * @return o1 * o2
	 * 
	 */
	public static <T> T multiply(boolean defValueIsOne, T o1, T o2, Class<?>... opAttType) {
		return objectOperation(o1, o2, '*', false, defValueIsOne, opAttType);
	}

	/**
	 * 对象内属性相除
	 * 
	 * @param o1
	 *            对象1
	 * @param o2
	 *            对象2
	 * @param opAttType
	 *            此参数为可选值<br/>
	 *            指定参与计算的属性类型,不参与计算的属性默认结果为null
	 * @return o1 / o2
	 */
	public static <T> T divide(T o1, T o2, Class<?>... opAttType) {
		return objectOperation(o1, o2, '/', false, null, opAttType);
	}

	/**
	 * 对象内属性相除
	 * 
	 * @param o1
	 *            对象1
	 * @param o2
	 *            对象2
	 * @param nullIsZero
	 *            计算时是否将 null 当作 zero 处理<br/>
	 *            当两个对象中此属性都为null时,指定此字段无效<br/>
	 *            对[String,Boolean]无效
	 * @param opAttType
	 *            此参数为可选值<br/>
	 *            指定参与计算的属性类型,不参与计算的属性默认结果为null
	 * @return o1 / o2
	 * 
	 */
	public static <T> T divide(T o1, T o2, boolean nullIsZero, Class<?>... opAttType) {
		return objectOperation(o1, o2, '/', nullIsZero, null, opAttType);
	}

	/**
	 * 对象内属性相除
	 * 
	 * @param defValueIsOne
	 *            当属性运算结果null运算时,结果对象属性值的来源 true o1, false o2
	 * @param o1
	 *            对象1
	 * @param o2
	 *            对象2
	 * @param opAttType
	 *            此参数为可选值<br/>
	 *            指定参与计算的属性类型,不参与计算的属性默认结果为null
	 * @return o1 / o2
	 * 
	 */
	public static <T> T divide(boolean defValueIsOne, T o1, T o2, Class<?>... opAttType) {
		return objectOperation(o1, o2, '/', false, defValueIsOne, opAttType);
	}

	@SuppressWarnings("unchecked")
	private static <T> T objectOperation(T o1, T o2, char op, boolean nullIsZero, Boolean defValueIsOne, Class<?>... opAttType) {
		Class<?> type = o1.getClass();
		Object res = null;
		try {
			res = type.newInstance();

			Method[] methods = type.getMethods();
			for (Method m : methods) {
				if (m.getName().indexOf("set") == 0) {
					// 找到对应get方法名
					String getMethodName = getAttGetMethod(m.getName());
					Method getMethod = null;
					try {
						// 获取两对象相应属性值
						getMethod = type.getMethod(getMethodName);
						Object v1 = getMethod.invoke(o1);
						Object v2 = getMethod.invoke(o2);
						// 运算
						Object opRes = operation(v1, v2, op, nullIsZero, defValueIsOne, opAttType);
						// 赋值
						m.invoke(res, opRes);
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

		return (T) res;
	}

	/**
	 * @return set方法对应的get方法名
	 */
	private static String getAttGetMethod(String setMethodName) {
		String getMethodName = "get" + setMethodName.substring(3);
		return getMethodName;

	}

	/**
	 * @return 类型判断执行相应操作方法
	 */
	private static Object operation(Object o1, Object o2, char op, boolean nullIsZero, Boolean defValueIsOne, Class<?>... opAttType) {
		Object res = null;

		OpAttType ot = attTypeHandle(opAttType);

		if ((!ot.isSetValue || ot.isBigDecimal) && (o1 instanceof BigDecimal || o2 instanceof BigDecimal)) {
			res = operationHandle((BigDecimal) o1, (BigDecimal) o2, op, nullIsZero);
			res = defaultValueHandle(res, o1, o2, defValueIsOne);
			if (isOpErr(res)) {
				if (ERR_VAL != null) {
					res = new BigDecimal(ERR_VAL.toString());
				} else {
					res = null;
				}
			}
		} else if ((!ot.isSetValue || ot.isInteger) && (o1 instanceof Integer || o2 instanceof Integer)) {
			res = operationHandle((Integer) o1, (Integer) o2, op, nullIsZero);
			res = defaultValueHandle(res, o1, o2, defValueIsOne);
			if (isOpErr(res)) {
				res = ERR_VAL;
			}
		} else if ((!ot.isSetValue || ot.isLong) && (o1 instanceof Long || o2 instanceof Long)) {
			res = operationHandle((Long) o1, (Long) o2, op, nullIsZero);
			res = defaultValueHandle(res, o1, o2, defValueIsOne);
			if (isOpErr(res)) {
				res = ERR_VAL;
			}
		} else if ((!ot.isSetValue || ot.isShort) && (o1 instanceof Short || o2 instanceof Short)) {
			res = operationHandle((Short) o1, (Short) o2, op, nullIsZero);
			res = defaultValueHandle(res, o1, o2, defValueIsOne);
			if (isOpErr(res)) {
				if (ERR_VAL != null) {
					res = ERR_VAL.shortValue();
				} else {
					res = null;
				}
			}
		} else if ((!ot.isSetValue || ot.isCharacter) && (o1 instanceof Character || o2 instanceof Character)) {
			res = operationHandle((Character) o1, (Character) o2, op, nullIsZero);
			res = defaultValueHandle(res, o1, o2, defValueIsOne);
			if (isOpErr(res)) {
				if (ERR_VAL != null) {
					res = (char) ERR_VAL.intValue();
				} else {
					res = null;
				}
			}
		} else if ((!ot.isSetValue || ot.isByte) && (o1 instanceof Byte || o2 instanceof Byte)) {
			res = operationHandle((Byte) o1, (Byte) o2, op, nullIsZero);
			res = defaultValueHandle(res, o1, o2, defValueIsOne);
			if (isOpErr(res)) {
				if (ERR_VAL != null) {
					res = ERR_VAL.byteValue();
				} else {
					res = null;
				}
			}
		} else if ((!ot.isSetValue || ot.isDouble) && (o1 instanceof Double || o2 instanceof Double)) {
			res = operationHandle((Double) o1, (Double) o2, op, nullIsZero);
			res = defaultValueHandle(res, o1, o2, defValueIsOne);
			if (isOpErr(res)) {
				if (ERR_VAL != null) {
					res = ERR_VAL.doubleValue();
				} else {
					res = null;
				}
			}
		} else if ((!ot.isSetValue || ot.isFloat) && (o1 instanceof Float || o2 instanceof Float)) {
			res = operationHandle((Float) o1, (Float) o2, op, nullIsZero);
			res = defaultValueHandle(res, o1, o2, defValueIsOne);
			if (isOpErr(res)) {
				if (ERR_VAL != null) {
					res = ERR_VAL.floatValue();
				} else {
					res = null;
				}
			}
		} else if ((!ot.isSetValue || ot.isBoolean) && (o1 instanceof Boolean || o2 instanceof Boolean)) {
			res = operationHandle((Boolean) o1, (Boolean) o2, op);
			res = defaultValueHandle(res, o1, o2, defValueIsOne);
		} else if ((!ot.isSetValue || ot.isString) && (o1 instanceof String || o2 instanceof String)) {
			res = operationHandle((String) o1, (String) o2, op);
			res = defaultValueHandle(res, o1, o2, defValueIsOne);
		} else {
			res = defaultValueHandle(res, o1, o2, defValueIsOne);
		}
		return res;
	}

	/**
	 * 除法计算错误
	 */
	private static boolean isOpErr(Object v) {
		return OP_ERR.equals(v);
	}

	/**
	 * 默认值处理
	 */
	private static Object defaultValueHandle(Object opRes, Object v1, Object v2, Boolean defValueIsOne) {
		if (defValueIsOne != null) {
			if (defValueIsOne == true) {
				opRes = opRes == null ? v1 : opRes;
			} else {
				opRes = opRes == null ? v2 : opRes;
			}
		}
		return opRes;
	}

	private static String operationHandle(String v1, String v2, char op) {
		if (op == '+') {
			return v1 + v2;
		}
		return null;
	}

	private static Boolean operationHandle(Boolean v1, Boolean v2, char op) {
		return v1 == v2 ? false : true;
	}

	private static Object operationHandle(Double v1, Double v2, char op, boolean nullIsZero) {
		if (!nullIsZero) {
			if (v1 == null || v2 == null) {
				return null;
			}
		} else {
			v1 = v1 == null ? ZERO : v1;
			v2 = v2 == null ? ZERO : v2;
		}
		Object res = null;
		switch (op) {
		case '+':
			res = v1 + v2;
			break;
		case '-':
			res = v1 - v2;
			break;
		case '*':
			res = v1 * v2;
			break;
		case '/':
			if (v2 != 0) {
				res = v1 / v2;
			} else {
				res = OP_ERR;
			}
			break;
		}
		return res;
	}

	private static Object operationHandle(Byte v1, Byte v2, char op, boolean nullIsZero) {
		if (!nullIsZero) {
			if (v1 == null || v2 == null) {
				return null;
			}
		} else {
			v1 = v1 == null ? ZERO.byteValue() : v1;
			v2 = v2 == null ? ZERO.byteValue() : v2;
		}
		Object res = null;
		switch (op) {
		case '+':
			res = (byte) (v1 + v2);
			break;
		case '-':
			res = (byte) (v1 - v2);
			break;
		case '*':
			res = (byte) (v1 * v2);
			break;
		case '/':
			if (v2 != 0) {
				res = (byte) (v1 / v2);
			} else {
				res = OP_ERR;
			}
			break;
		}
		return res;
	}

	private static Object operationHandle(Float v1, Float v2, char op, boolean nullIsZero) {
		if (!nullIsZero) {
			if (v1 == null || v2 == null) {
				return null;
			}
		} else {
			v1 = v1 == null ? ZERO : v1;
			v2 = v2 == null ? ZERO : v2;
		}
		Object res = null;
		switch (op) {
		case '+':
			res = v1 + v2;
			break;
		case '-':
			res = v1 - v2;
			break;
		case '*':
			res = v1 * v2;
			break;
		case '/':
			if (v2 != 0) {
				res = v1 / v2;
			} else {
				res = OP_ERR;
			}
			break;
		}
		return res;
	}

	private static Object operationHandle(Short v1, Short v2, char op, boolean nullIsZero) {
		if (!nullIsZero) {
			if (v1 == null || v2 == null) {
				return null;
			}
		} else {
			v1 = v1 == null ? ZERO.shortValue() : v1;
			v2 = v2 == null ? ZERO.shortValue() : v2;
		}
		Object res = null;
		switch (op) {
		case '+':
			res = (short) (v1 + v2);
			break;
		case '-':
			res = (short) (v1 - v2);
			break;
		case '*':
			res = (short) (v1 * v2);
			break;
		case '/':
			if (v2 != 0) {
				res = (short) (v1 / v2);
			} else {
				res = OP_ERR;
			}
			break;
		}
		return res;
	}

	private static Object operationHandle(Integer v1, Integer v2, char op, boolean nullIsZero) {
		if (!nullIsZero) {
			if (v1 == null || v2 == null) {
				return null;
			}
		} else {
			v1 = v1 == null ? ZERO : v1;
			v2 = v2 == null ? ZERO : v2;
		}
		Object res = null;
		switch (op) {
		case '+':
			res = v1 + v2;
			break;
		case '-':
			res = v1 - v2;
			break;
		case '*':
			res = v1 * v2;
			break;
		case '/':
			if (v2 != 0) {
				res = v1 / v2;
			} else {
				res = OP_ERR;
			}
			break;
		}
		return res;
	}

	private static Object operationHandle(Long v1, Long v2, char op, boolean nullIsZero) {
		if (!nullIsZero) {
			if (v1 == null || v2 == null) {
				return null;
			}
		} else {
			v1 = v1 == null ? ZERO : v1;
			v2 = v2 == null ? ZERO : v2;
		}
		Object res = null;
		switch (op) {
		case '+':
			res = v1 + v2;
			break;
		case '-':
			res = v1 - v2;
			break;
		case '*':
			res = v1 * v2;
			break;
		case '/':
			if (v2 != 0) {
				res = v1 / v2;
			} else {
				res = OP_ERR;
			}
			break;
		}
		return res;
	}

	private static Object operationHandle(Character v1, Character v2, char op, boolean nullIsZero) {
		if (!nullIsZero) {
			if (v1 == null || v2 == null) {
				return null;
			}
		} else {
			v1 = v1 == null ? (char) ZERO.intValue() : v1;
			v2 = v2 == null ? (char) ZERO.intValue() : v2;
		}
		Object res = null;
		switch (op) {
		case '+':
			res = (char) (v1 + v2);
			break;
		case '-':
			res = (char) (v1 - v2);
			break;
		case '*':
			res = (char) (v1 * v2);
			break;
		case '/':
			if (v2 != 0) {
				res = (char) (v1 / v2);
			} else {
				res = OP_ERR;
			}
			break;
		}
		return res;
	}

	public static Object operationHandle(BigDecimal v1, BigDecimal v2, char op, boolean nullIsZero) {
		if (!nullIsZero) {
			if (v1 == null || v2 == null) {
				return null;
			}
		} else {
			v1 = v1 == null ? new BigDecimal(ZERO.toString()) : v1;
			v2 = v2 == null ? new BigDecimal(ZERO.toString()) : v2;
		}
		Object res = null;
		switch (op) {
		case '+':
			res = v1.add(v2);
			break;
		case '-':
			res = v1.subtract(v2);
			break;
		case '*':
			res = v1.multiply(v2);
			break;
		case '/':
			if (v2.compareTo(BigDecimal.ZERO) != 0) {
				res = v1.divide(v2, BIG_SCALE, BIG_ROUND_MODE);
			} else {
				res = OP_ERR;
			}
			break;
		}
		return res;
	}

	private static OpAttType attTypeHandle(Class<?>... opAttType) {
		OpAttType res = null;
		try {
			res = OpAttType.class.newInstance();
		} catch (Exception e) {
			System.err.println(getThisName() + ": [ERROR] 创建 'OpAttType' 类失败.");
		}
		;
		for (Class<?> c : opAttType) {
			if (c.getName().indexOf("Integer") != -1) {
				res.isSetValue = true;
				res.isInteger = true;
			} else if (c.getName().indexOf("BigDecimal") != -1) {
				res.isSetValue = true;
				res.isBigDecimal = true;
			} else if (c.getName().indexOf("Long") != -1) {
				res.isSetValue = true;
				res.isLong = true;
			} else if (c.getName().indexOf("Short") != -1) {
				res.isSetValue = true;
				res.isShort = true;
			} else if (c.getName().indexOf("Character") != -1) {
				res.isSetValue = true;
				res.isCharacter = true;
			} else if (c.getName().indexOf("Byte") != -1) {
				res.isSetValue = true;
				res.isByte = true;
			} else if (c.getName().indexOf("Double") != -1) {
				res.isSetValue = true;
				res.isDouble = true;
			} else if (c.getName().indexOf("Float") != -1) {
				res.isSetValue = true;
				res.isFloat = true;
			} else if (c.getName().indexOf("Boolean") != -1) {
				res.isSetValue = true;
				res.isBoolean = true;
			} else if (c.getName().indexOf("String") != -1) {
				res.isSetValue = true;
				res.isString = true;
			}
		}
		return res;
	}

	/**
	 * @return 当前类名
	 */
	private static String getThisName() {
		String[] split = ObjectComputedUtil.class.getName().split("\\.");
		return split[split.length - 1];

	}
}

class OpAttType {
	boolean isBigDecimal;
	boolean isInteger;
	boolean isLong;
	boolean isShort;
	boolean isCharacter;
	boolean isByte;
	boolean isDouble;
	boolean isFloat;
	boolean isBoolean;
	boolean isString;
	// 设置了值
	boolean isSetValue;
}