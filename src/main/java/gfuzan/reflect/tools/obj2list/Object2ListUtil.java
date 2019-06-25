package gfuzan.reflect.tools.obj2list;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 将对象转为数组
 *
 * @author GFuZan
 */
public class Object2ListUtil {

	private Object2ListUtil() {

	}

	/**
	 * 将对象转换为List
	 *
	 * @param o
	 *            进行转换的对象
	 * @param toListInterface
	 *            包含toList注解的此对象接口
	 * @return List
	 */
	public static List<Object> toList(Object o, Class<?> toListInterface) {
		List<Object> res = new ArrayList<>();
		// 转为map
		Map<Integer, Object> map = toMap(o, toListInterface);

		// 获取map中key的最大值与最小值
		Map<String, Integer> key = getMaxKey(map.keySet());
		if (key == null) {
			return res;
		}

		int min = key.get("min");
		int max = key.get("max");

		for (int i = min; i <= max; i++) {
			res.add(map.get(i));
		}

		return res;
	}

	/**
	 * 将对象转换为Map
	 *
	 * @param o
	 *            进行转换的对象
	 * @param toListInterface
	 *            包含toList注解的此对象接口
	 * @return Map
	 */
	public static Map<Integer, Object> toMap(Object o, Class<?> toListInterface) {
		Map<Integer, Object> res = new HashMap<Integer, Object>();
		if (o == null) {
			return res;
		}

		Method[] methods = toListInterface.getMethods();
		for (Method method : methods) {
			Object2List ol = method.getAnnotation(Object2List.class);
			if (ol == null) {
				continue;
			}
			try {
				res.put(ol.value(), method.invoke(o));
			} catch (Exception e) {
				System.err.println(getThisName() + ": [ERROR] '" + e.getMessage());
			}
		}

		return res;
	}

	private static Map<String, Integer> getMaxKey(Set<Integer> keys) {
		if (keys == null || keys.isEmpty()) {
			return null;
		}

		int min = (int) keys.toArray()[0];
		int max = min;

		for (Integer key : keys) {
			max = key > max ? key : max;
			min = key < min ? key : min;
		}

		Map<String, Integer> res = new HashMap<>();
		res.put("min", min);
		res.put("max", max);
		return res;
	}

	/**
	 * @return 当前类名
	 */
	private static String getThisName() {
		String[] split = Object2ListUtil.class.getName().split("\\.");
		return split[split.length - 1];

	}
}
