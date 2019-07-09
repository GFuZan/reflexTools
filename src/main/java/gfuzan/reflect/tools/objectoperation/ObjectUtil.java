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

    private final static String GET = "get";
    private final static String SET = "set";

    public ObjectUtil() {
        cleanCache();
    }

    /**
     * 获取属性值
     *
     * @param o
     *            操作对象
     * @param fieldName
     *            属性名
     * @return
     */
    public Object get(Object o, String fieldName) {
        return get(o, fieldName, false);
    }

    /**
     * 获取属性值
     *
     * @param o
     *            操作对象
     * @param fieldName
     *            属性名
     * @param returnType
     *            返回值类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Object o, String fieldName, Class<T> returnType) {
        return (T) get(o, fieldName, false);
    }

    /**
     * 获取属性值
     *
     * @param o
     *            操作对象
     * @param fieldName
     *            属性名
     * @param opField
     *            直接操作属性
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object get(Object o, String fieldName, boolean opField) {
        if (o == null || fieldName == null || fieldName.isEmpty()) {
            return null;
        } else if (o instanceof Map) {
            return mapGet((Map<String, Object>) o, fieldName);
        }

        return operation(o, GET, fieldName, null, null, opField);
    }

    /**
     * 获取属性值
     *
     * @param o
     *            操作对象
     * @param fieldName
     *            属性名
     * @param returnType
     *            返回值类型
     * @param opField
     *            直接操作属性
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(Object o, String fieldName, Class<T> returnType,
            boolean opField) {
        return (T) get(o, fieldName, opField);
    }

    /**
     * <b>设置属性值</b>
     * <p>
     * 通过参数值确定类型,请保证参数值不为null
     * </p>
     *
     * @param o
     *            操作对象
     * @param fieldName
     *            属性名
     * @param value
     *            参数值
     * @return
     */
    public <T> T set(T o, String fieldName, Object value) {
        return (T) set(o, fieldName, value, null, false);
    }

    /**
     * 设置属性值
     *
     * @param o
     *            操作对象
     * @param fieldName
     *            属性名
     * @param value
     *            参数值
     * @param paramType
     *            参数类型
     * @return 操作后对象
     */
    public <T> T set(T o, String fieldName, Object value,
            Class<?> paramType) {
        return (T) set(o, fieldName, value, paramType, false);
    }

    /**
     * 设置属性值
     *
     * @param o
     *            操作对象
     * @param fieldName
     *            属性名
     * @param value
     *            参数值
     * @param opField
     *            直接操作属性
     * @return 操作后对象
     */
    public <T> T set(T o, String fieldName, Object value, boolean opField) {
        return (T) set(o, fieldName, value, null, opField);
    }

    /**
     * 设置属性值
     *
     * @param o
     *            操作对象
     * @param fieldName
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
    public <T> T set(T o, String fieldName, Object value,
            Class<?> paramType, boolean opField) {
        if (o == null || fieldName == null || fieldName.isEmpty()) {
            return null;
        } else if (o instanceof Map) {
            mapPush((Map<String, Object>) o, fieldName, value);
            return o;
        }

        return (T) operation(o, SET, fieldName, paramType, value, opField);
    }

    /**
     * @param o
     *            操作对象
     * @param methodName
     *            方法名
     * @param fieldName
     *            属性名
     * @param value
     *            值
     * @param opField
     *            直接操作属性
     * @return get方法返回实际值 set方法返回操作后对象
     */
    private Object operation(Object o, String methodType, String fieldName, Class<?> paramType, Object value,
            boolean opField) {
        // 方法赋值出错
        boolean opErr = false;
        Object res = null;
        Class<?> type = o.getClass();
        // 不是直接操作属性,尝试使用get/set方法
        if (!opField) {
            Method method = null;
            try {
                if (GET.equals(methodType)) {
                    method = getMethod(type, fieldName, methodType);
                    if (method != null) {
                        res = method.invoke(o);
                    } else {
                        opErr = true;
                    }
                } else {
                    paramType = paramType == null ? value.getClass() : paramType;
                    method = getMethod(type, fieldName, methodType, paramType);
                    if (method != null) {
                        method.invoke(o, value);
                        res = o;
                    } else {
                        opErr = true;
                    }
                }
            } catch (Exception e) {
                opErr = true;
            }
            if (opErr && showLog) {
                System.err.println(getThisName() + ": [WARN] 直接对属性'"
                        + fieldName + "'进行操作(不借助get/set方法).");
            }
        }

        if (opErr || opField) {
            opErr = false;
            Field field = null;
            try {
                field = getField(type, fieldName);
                if (field != null) {

                    field.setAccessible(true);

                    if (GET.equals(methodType)) {
                        res = field.get(o);
                    } else {
                        field.set(o, value);
                        res = o;
                    }
                } else {
                    opErr = true;
                }
            } catch (Exception e) {
                opErr = true;
            }
            if (opErr && showLog) {
                System.err.println(getThisName() + ": [ERROR] 属性'"
                        + fieldName + "'操作失败.");
            }
        }

        return res;
    }

    /**
     * 清理缓存
     */
    public void cleanCache() {
        this.cacheKey = new CacheKey(null, null);
        this.cache = new Cache();
    }

    private Method getMethod(Class<?> type, String fieldName, String methodType, Class<?>... parameterTypes) {

        CacheKey key = cacheKey.set(type, fieldName, methodType);
        CacheValue<Method> method = this.cache.getMethod(key);
        if (method == null) {
            key = new CacheKey(type, fieldName, methodType);
            method = new CacheValue<>();
            this.cache.setMethod(key, method);
            try {
                method.value = type.getMethod(methodNameHandle(fieldName, methodType), parameterTypes);
            } catch (NoSuchMethodException | SecurityException e) {
                method.value = null;
            }
        }
        return method.value;
    }

    /**
     * 获取属性
     * @param type 类
     * @param fieldName 属性名
     * @return 属性
     */
    public Field getField(Class<?> type, String fieldName) {

        CacheKey key = cacheKey.set(type, fieldName);
        CacheValue<Field> field = this.cache.getField(key);
        if (field == null) {
            key = new CacheKey(type, fieldName);
            field = new CacheValue<>();
            this.cache.setField(key, field);
            try {
                field.value = type.getDeclaredField(fieldName);
            } catch (NoSuchFieldException | SecurityException e) {
                field.value = null;
            }
        }
        return field.value;
    }

    /**
     * 方法名处理
     *
     * @param method
     *            方法(get/set)
     * @param fieldName
     * @return
     */
    private String methodNameHandle(String fieldName, String methodType) {
        StringBuffer res = new StringBuffer(methodType);

        if (fieldName.length() == 1) {
            res.append(fieldName.toUpperCase());
        } else {
            char[] charArray = fieldName.toCharArray();

            if (Character.isLowerCase(charArray[0])
                    && Character.isLowerCase(charArray[1])) {
                res.append(Character.toUpperCase(charArray[0]));
                res.append(fieldName.substring(1));
            } else {
                res.append(fieldName);
            }
        }

        return res.toString();
    }

    /**
     * map对象设值
     *
     * @param map
     * @param fieldName
     *            key
     * @param value
     *            值
     */
    private void mapPush(Map<String, Object> map, String fieldName,
            Object value) {
        map.put(fieldName, value);
    }

    /**
     * map对象获取值
     *
     * @param map
     * @param fieldName
     *            属性
     * @return
     */
    private Object mapGet(Map<String, Object> map, String fieldName) {
        return map.get(fieldName);
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
        public String fieldName = null;
        public String methodType = null;

        public CacheKey(Class<?> type, String fieldName) {
            super();
            this.type = type;
            this.fieldName = fieldName;
            this.methodType = null;
        }

        public CacheKey(Class<?> type, String fieldName, String methodType) {
            super();
            this.type = type;
            this.fieldName = fieldName;
            this.methodType = methodType;
        }

        public CacheKey set(Class<?> type, String fieldName) {
            this.type = type;
            this.fieldName = fieldName;
            this.methodType = null;
            return this;
        }

        public CacheKey set(Class<?> type, String fieldName, String methodType) {
            this.type = type;
            this.fieldName = fieldName;
            this.methodType = methodType;
            return this;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((methodType == null) ? 0 : methodType.hashCode());
            result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
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
            if (methodType == null) {
                if (other.methodType != null)
                    return false;
            } else if (!methodType.equals(other.methodType))
                return false;
            if (fieldName == null) {
                if (other.fieldName != null)
                    return false;
            } else if (!fieldName.equals(other.fieldName))
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
