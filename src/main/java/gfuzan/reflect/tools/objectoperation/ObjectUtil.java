package gfuzan.reflect.tools.objectoperation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>获取/设置对象的属性
 *<p>
 *<b>安全性: 线程不安全
 * @author GFuZan
 *
 */
public class ObjectUtil {
    /**
     * 是否输出log
     */
    public boolean showLog = true;

    private Cache cache = null;

    private CacheKey cacheKey = null;

    public ObjectUtil() {
        this.cacheKey = new CacheKey(null, null);
        this.cache = new Cache();
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
    public Object get(Object o, String attName) {
        return get(o, attName, false);
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
    public <T> T get(Object o, String attName, Class<T> returnType) {
        return (T) get(o, attName, false);
    }

    /**
     * 获取属性值
     *
     * @param o
     *            操作对象
     * @param attName
     *            属性名
     * @param opField
     *            直接操作属性
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object get(Object o, String attName, boolean opField) {
        if (o == null || attName == null || attName.isEmpty()) {
            return null;
        } else if (o instanceof Map) {
            return mapGet((Map<String, Object>) o, attName);
        }
        String methodName = attNameHandle("get", attName);

        return operation(o, methodName, attName, null, null, opField);
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
     * @param opField
     *            直接操作属性
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Object o, String attName, Class<T> returnType,
            boolean opField) {
        return (T) get(o, attName, opField);
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
    public <T> T set(T o, String attName, Object value) {
        return (T) set(o, attName, value, null, false);
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
    public <T> T set(T o, String attName, Object value,
            Class<?> paramType) {
        return (T) set(o, attName, value, paramType, false);
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
     * @param opField
     *            直接操作属性
     * @return 操作后对象
     */
    public <T> T set(T o, String attName, Object value, boolean opField) {
        return (T) set(o, attName, value, null, opField);
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
     * @param opField
     *            直接操作属性
     * @return 操作后对象
     */
    @SuppressWarnings("unchecked")
    public <T> T set(T o, String attName, Object value,
            Class<?> paramType, boolean opField) {
        if (o == null || attName == null || attName.isEmpty()) {
            return null;
        } else if (o instanceof Map) {
            mapPush((Map<String, Object>) o, attName, value);
            return o;
        }

        String methodName = attNameHandle("set", attName);

        return (T) operation(o, methodName, attName, paramType, value, opField);
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
     * @param opField
     *            直接操作属性
     * @return get方法返回实际值 set方法返回操作后对象
     */
    private Object operation(Object o, String methodName,
            String attName, Class<?> paramType, Object value, boolean opField) {
        // 方法赋值出错
        boolean opErr = false;
        Object res = null;
        Class<?> type = o.getClass();
        // 不是直接操作属性,尝试使用get/set方法
        if (!opField) {
            try {
                Method method = null;
                if (methodName.startsWith("get")) {
                    // get
                    method = getMethod(type, methodName);
                    res = method.invoke(o);
                } else {
                    // set
                    paramType = paramType == null ? value.getClass()
                            : paramType;
                    method = getMethod(type, methodName, paramType);
                    method.invoke(o, value);
                    res = o;
                }
            } catch (Exception e) {
                opErr = true;
                if (showLog) {
                    System.err.println(getThisName() + ": [WARN] 直接对属性'"
                            + attName + "'进行操作(不借助get/set方法).");
                }
            }
        }

        if (opErr || opField) {
            try {
                Field field = null;
                field = getField(type, attName);
                field.setAccessible(true);

                if (methodName.startsWith("get")) {
                    res = field.get(o);
                } else {
                    field.set(o, value);
                    res = o;
                }
            } catch (Exception e) {
                if (showLog) {
                    System.err.println(getThisName() + ": [ERROR] 属性'"
                            + attName + "'操作失败.");
                }
            }
        }

        return res;
    }

    public void cleanCache() {
        this.cacheKey = new CacheKey(null, null);
        this.cache = new Cache();
    }

    private final Method getMethod(Class<?> type, String methodName, Class<?>... parameterTypes)
            throws NoSuchMethodException, SecurityException {

        CacheKey key = cacheKey.set(type, methodName);
        CacheValue<Method> method = this.cache.getMethod(key);
        if (method == null) {
            key = new CacheKey(type, methodName);
            method = new CacheValue<>();
            this.cache.setMethod(key, method);
            method.value = type.getMethod(methodName, parameterTypes);
        }
        return method.value;
    }

    private final Field getField(Class<?> type, String fieldName) throws NoSuchFieldException, SecurityException {

        CacheKey key = cacheKey.set(type, fieldName);
        CacheValue<Field> field = this.cache.getField(key);
        if (field == null) {
            key = new CacheKey(type, fieldName);
            field = new CacheValue<>();
            this.cache.setField(key, field);
            field.value = type.getDeclaredField(fieldName);
        }
        return field.value;
    }

    /**
     * 属性名处理
     *
     * @param method
     *            方法(get/set)
     * @param attName
     * @return
     */
    private String attNameHandle(String method, String attName) {
        StringBuffer res = new StringBuffer(method);

        if (attName.length() == 1) {
            res.append(attName.toUpperCase());
        } else {
            char[] charArray = attName.toCharArray();

            if (Character.isLowerCase(charArray[0])
                    && Character.isLowerCase(charArray[1])) {
                res.append(Character.toUpperCase(charArray[0]));
                res.append(attName.substring(1));
            } else {
                res.append(attName);
            }
        }

        return res.toString();
    }

    /**
     * map对象设值
     *
     * @param map
     * @param attName
     *            key
     * @param value
     *            值
     */
    private void mapPush(Map<String, Object> map, String attName,
            Object value) {
        map.put(attName, value);
    }

    /**
     * map对象获取值
     *
     * @param map
     * @param attName
     *            属性
     * @return
     */
    private Object mapGet(Map<String, Object> map, String attName) {
        return map.get(attName);
    }

    /**
     * @return 当前类名
     */
    private static String getThisName() {
        String[] split = ObjectUtil.class.getName().split("\\.");
        return split[split.length - 1];

    }

    private static class Cache {
        private Map<CacheKey, CacheValue<Method>> method = null;
        private Map<CacheKey, CacheValue<Field>> field = null;

        public Cache() {
            this.method = new HashMap<>();
            this.field = new HashMap<>();
        }

        public CacheValue<Method> getMethod(CacheKey key) {
            return method.get(key);
        }

        public CacheValue<Field> getField(CacheKey key) {
            return field.get(key);
        }

        public void setMethod(CacheKey key, CacheValue<Method> method) {
            this.method.put(key, method);
        }

        public void setField(CacheKey key, CacheValue<Field> field) {
            this.field.put(key, field);
        }
    }

    protected static class CacheKey {
        public Class<?> type = null;
        public String name = null;

        public CacheKey(Class<?> type, String name) {
            super();
            this.type = type;
            this.name = name;
        }

        public CacheKey set(Class<?> type, String name) {
            this.type = type;
            this.name = name;
            return this;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + ((type == null) ? 0 : type.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            CacheKey other = (CacheKey) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (type == null) {
                if (other.type != null)
                    return false;
            } else if (!type.equals(other.type))
                return false;
            return true;
        }
    }

    protected static class CacheValue<T> {
        public T value = null;
    }
}
