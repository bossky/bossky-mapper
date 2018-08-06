package org.bossky.mapper.annotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.bossky.common.util.Misc;
import org.bossky.mapper.Mapper;
import org.bossky.mapper.MapperClass;
import org.bossky.mapper.Meta;
import org.bossky.mapper.MetaField;
import org.bossky.mapper.MetaType;
import org.bossky.mapper.exception.MapperException;
import org.bossky.mapper.support.AbstractMapper;

/**
 * 基于注释的映射器
 * 
 * @author bo
 *
 * @param <E>
 */
public class AnnotationMapper<E> extends AbstractMapper<E> implements Mapper<E> {
	/** 类 */
	protected Class<E> clazz;
	/** 构造函数 */
	protected Constructor<E> constructor;
	/** 构造参数 */
	protected Object[] initArgs;
	/** 元信息 */
	protected List<AnnotationMeta> metas;

	protected AnnotationMapper(Class<E> clazz, Object[] initArgs) {
		super(MetaType.getMapperName(clazz));
		this.clazz = clazz;
		this.initArgs = initArgs;
	}

	/**
	 * 获取构造
	 * 
	 * @return
	 */
	private Constructor<E> getConstructor() {
		if (null == constructor) {
			constructor = getConstructor(clazz, initArgs);
			constructor.setAccessible(true);
		}
		return constructor;
	}

	/**
	 * 获取构造
	 * 
	 * @param clazz
	 * @param initargs
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <E> Constructor<E> getConstructor(Class<E> clazz, Object... initargs) {
		if (Misc.isEmpty(initargs)) {
			try {
				return clazz.getDeclaredConstructor();
			} catch (NoSuchMethodException e) {
				throw new MapperException(clazz + "没有无参构造函数", e);
			} catch (SecurityException e) {
				throw new MapperException(clazz + "安全异常", e);
			}
		}
		Class<?> parameterTypes[] = new Class<?>[initargs.length];
		for (int i = 0; i < initargs.length; i++) {
			if (null == initargs[i].getClass()) {
				throw new NullPointerException("初始化参数不能为null");
			}
			parameterTypes[i] = initargs[i].getClass();
		}
		Constructor<?>[] constructors = clazz.getDeclaredConstructors();
		out: for (Constructor<?> c : constructors) {
			Class<?>[] classes = c.getParameterTypes();
			if (classes.length != parameterTypes.length) {
				continue;
			}
			for (int i = 0; i < classes.length; i++) {
				if (!classes[i].isAssignableFrom(parameterTypes[i])) {
					break out;
				}
			}
			return (Constructor<E>) c;
		}
		throw new MapperException(clazz + "没有对应的" + Arrays.toString(parameterTypes) + "构造函数");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Meta> getMetas() {
		List<? extends Meta> ms = listMetas();
		return (List<Meta>) ms;
	}

	private List<AnnotationMeta> listMetas() {
		if (null == metas) {
			metas = getLoopMeta(clazz);
		}
		return metas;
	}

	/**
	 * 循环获取多个类的信息,直到类非子类为止
	 * 
	 * @param clazz
	 * @return
	 */
	private static <E> List<AnnotationMeta> getLoopMeta(Class<E> clazz) {
		List<AnnotationMeta> metas = new ArrayList<AnnotationMeta>();
		Class<?> loop = clazz;
		boolean isSubclass = false;
		do {
			metas.addAll(getMeta(clazz));
			// 优先使用mapperclass注释
			MapperClass mapper = loop.getAnnotation(MapperClass.class);
			if (null != mapper) {
				isSubclass = mapper.subclass();
			}
			loop = clazz.getSuperclass();
		} while (isSubclass);
		return metas;
	}

	/**
	 * 获取当前类的元信息
	 * 
	 * @param clazz
	 * @return
	 */
	private static <E> List<AnnotationMeta> getMeta(Class<E> clazz) {
		List<AnnotationMeta> metas = new ArrayList<AnnotationMeta>();
		for (Field field : clazz.getDeclaredFields()) {
			Resource resource = field.getAnnotation(Resource.class);
			MetaField metaField = field.getAnnotation(MetaField.class);
			if (null == resource && null == metaField) {
				continue;
			}
			metas.add(new AnnotationMeta(field));
		}
		return metas;
	}

	@Override
	public E newInstance() {
		try {
			return getConstructor().newInstance(initArgs);
		} catch (InstantiationException e) {
			throw new MapperException("实例化异常", e);
		} catch (IllegalAccessException e) {
			throw new MapperException("访问异常", e);
		} catch (IllegalArgumentException e) {
			throw new MapperException("参数异常", e);
		} catch (InvocationTargetException e) {
			throw new MapperException("调整目标异常", e);
		}
	}

	/** 对象映射器常量 */
	// private static final String MAPPER = "_mapper";
	// private static final String MAP_KEY = "_mapkey";
	// private static final String MAP_VALUE = "_mapvalue";
	// private static final String MULTI_TYPE = "_multitype";
	// private static final String MULTI_VALUE = "_multivalue";

	// @Override
	// public Mapped toMapped(Mapped mapped, E object) {
	// for (Meta m : getMetas()) {
	// String key = m.getName();
	// Object value = m.getValue(object);
	// if (null == value) {
	// continue;// 空值不放入
	// }
	// MetaType type = MetaType.valueOf(value.getClass());
	// if (type.isPrimitive()) {
	// mapped.put(key, toPrimitiveElement(type, value));
	// } else if (type.isObject()) {
	// mapped.put(key, toObjectElement(type, value,
	// mapped.createChildMapped()));
	// } else if (type.isMultiElement()) {
	// mapped.put(key, toMultiElement(type, value,
	// mapped.createChildMappeds()));
	// } else {
	// throw new UnsupportedOperationException();
	// }
	// }
	// return mapped;
	// }

	/**
	 * 转换成mapped
	 * 
	 * @param obj
	 * @param mapped
	 */
	// private void toMapped(Object obj, Mapped mapped) {
	// Mapper<?> mapper = getMapper(obj);
	// if (null == mapper) {
	// throw new MapperException("找不到" + obj.getClass() + "对应的映射器");
	// }
	// mapped.put(MAPPER, mapper.getName());
	// for (Meta m : mapper.getMetas()) {
	// String key = m.getName();
	// Object value = m.getValue(obj);
	// if (null == value) {
	// continue;// 空值不放入
	// }
	// MetaType type = m.getType();
	// if (type.isPrimitive()) {
	// mapped.put(key, toPrimitiveElement(type, value));
	// } else if (type.isObject()) {
	// mapped.put(key, toObjectElement(type, value,
	// mapped.createChildMapped()));
	// } else if (type.isMultiElement()) {
	// mapped.put(key, toMultiElement(type, value,
	// mapped.createChildMappeds()));
	// } else {
	// throw new UnsupportedOperationException();
	// }
	// }
	// }

	// public static MetaType getRealType(Object obj) {
	// return MetaType.valueOf(obj.getClass());
	// }

	/**
	 * 转换原子元素
	 * 
	 * @param type
	 * @param key
	 * @param value
	 * @param mapped
	 * @param mappeds
	 */
	// private Object toPrimitiveElement(MetaType type, Object value) {
	// if (type == MetaType.BYTE) {
	// return String.valueOf(value);
	// } else if (type == MetaType.CHARACTER) {
	// return String.valueOf(value);
	// } else if (type == MetaType.SHORT) {
	// return (Short) value;
	// } else if (type == MetaType.INTEGER) {
	// return (Integer) value;
	// } else if (type == MetaType.LONG) {
	// return (Long) value;
	// } else if (type == MetaType.FLOAT) {
	// return (Float) value;
	// } else if (type == MetaType.DOUBLE) {
	// return (Double) value;
	// } else if (type == MetaType.BOOLEAN) {
	// return (Boolean) value;
	// } else if (type == MetaType.STRING) {
	// return String.valueOf(value);
	// } else if (type == MetaType.DATE) {
	// Date date = (Date) value;
	// return date;
	// } else {
	// throw new UnsupportedOperationException(type.name() + "类型不支持");
	// }
	// }

	/**
	 * 转换对象元素
	 * 
	 * @param type
	 * @param key
	 * @param value
	 * @param mapped
	 */
	// private Object toObjectElement(MetaType type, Object value, Mapped child)
	// {
	// toMapped(value, child);
	// return child;
	// }

	/**
	 * 存入对象元素
	 * 
	 * @param type
	 * @param key
	 * @param value
	 * @param mapped
	 */
	// @SuppressWarnings("unchecked")
	// private Object toMultiElement(MetaType type, Object value, Mappeds child)
	// {
	// if (type == MetaType.ARRAY) {
	// int length = Array.getLength(value);
	// for (int i = 0; i < length; i++) {
	// Object childValue = Array.get(value, i);
	// if (null == childValue) {
	// child.putNull();
	// continue;
	// }
	// MetaType childType = MetaType.valueOf(childValue.getClass());
	// if (childType.isPrimitive()) {
	// child.put(toPrimitiveElement(childType, childValue));
	// } else if (childType.isObject()) {
	// child.put(toObjectElement(childType, childValue,
	// child.createChildMapped()));
	// } else if (childType.isMultiElement()) {
	// child.put(toMultiElement(childType, childValue,
	// child.createChildMappeds()));
	// } else {
	// throw new UnsupportedOperationException("暂不支持" + childType);
	// }
	// }
	// return child;
	// } else if (type == MetaType.SET || type == MetaType.LIST) {
	// Collection<Object> collections = (Collection<Object>) value;
	// for (Object childValue : collections) {
	// if (null == childValue) {
	// child.putNull();
	// continue;
	// }
	// MetaType childType = MetaType.valueOf(childValue.getClass());
	// if (childType.isPrimitive()) {
	// child.put(toPrimitiveElement(childType, childValue));
	// } else if (childType.isObject()) {
	// child.put(toObjectElement(childType, childValue,
	// child.createChildMapped()));
	// } else if (childType.isMultiElement()) {
	// child.put(toMultiElement(childType, childValue,
	// child.createChildMappeds()));
	// } else {
	// throw new UnsupportedOperationException("暂不支持" + childType);
	// }
	// }
	// return child;
	// } else if (type == MetaType.MAP) {
	// Map<Object, Object> map = (Map<Object, Object>) value;
	// for (Map.Entry<Object, Object> entry : map.entrySet()) {
	// Object entrykey = entry.getKey();
	// Object entryvalue = entry.getValue();
	// Mapped mapped = child.createChildMapped();
	// if (null == entrykey) {
	// mapped.putNull(MAP_KEY);
	// } else {
	// MetaType entrykeytype = MetaType.valueOf(entrykey.getClass());
	// if (entrykeytype.isPrimitive()) {
	// mapped.put(MAP_KEY, toPrimitiveElement(entrykeytype, entrykey));
	// } else if (entrykeytype.isObject()) {
	// mapped.put(MAP_KEY, toObjectElement(entrykeytype, entrykey,
	// mapped.createChildMapped()));
	// } else if (entrykeytype.isMultiElement()) {
	// mapped.put(MAP_KEY, toMultiElement(entrykeytype, entrykey,
	// mapped.createChildMappeds()));
	// } else {
	// throw new UnsupportedOperationException(entrykeytype.name() + "类型不支持");
	// }
	// }
	// if (null == entryvalue) {
	// mapped.putNull(MAP_VALUE);
	// } else {
	// MetaType entryvaluetype = MetaType.valueOf(entryvalue.getClass());
	// if (entryvaluetype.isPrimitive()) {
	// mapped.put(MAP_VALUE, toPrimitiveElement(entryvaluetype, entryvalue));
	// } else if (entryvaluetype.isObject()) {
	// mapped.put(MAP_VALUE, toObjectElement(entryvaluetype, entryvalue,
	// mapped.createChildMapped()));
	// } else if (type.isMultiElement()) {
	// mapped.put(MAP_VALUE, toMultiElement(entryvaluetype, entryvalue,
	// mapped.createChildMappeds()));
	// } else {
	// throw new UnsupportedOperationException(type.name() + "类型不支持");
	// }
	// }
	// child.put(mapped);
	// }
	// return child;
	// } else {
	// throw new UnsupportedOperationException(type.name() + "类型不支持");
	// }
	// }

	// @Override
	// public E fromMapped(Mapped mapped) {
	// E result = newInstance();
	// for (AnnotationMeta m : listMetas()) {
	// MetaType type = m.getType();
	// String key = m.getName();
	// if (type.isPrimitive()) {
	// m.setValue(result, getPrimitiveElement(type, key, mapped));
	// } else if (type.isObject()) {
	// m.setValue(result, getObjectElement(type, key, mapped));
	// } else if (type == MetaType.ARRAY) {
	// Mappeds child = mapped.getMappeds(key);
	// if (null == child) {
	// m.setValue(result, null);
	// } else {
	// Class<?> componentType = m.getTypeClass().getComponentType();
	// Object childValue = Array.newInstance(componentType, child.size());
	// for (int i = 0; i < child.size(); i++) {
	// Array.set(childValue, i, getElement(componentType, child, i));
	// }
	// }
	// } else if (type == MetaType.SET) {
	// Mappeds child = mapped.getMappeds(key);
	// if (null == child) {
	// m.setValue(result, null);
	// } else {
	// Type genType = m.getTypeClass().getGenericSuperclass();
	// Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
	// Set<Object> set = new HashSet<Object>(child.size());
	// for (int i = 0; i < child.size(); i++) {
	// set.add(getElement((Class<?>) params[0], child, i));
	// }
	// m.setValue(result, set);
	// }
	// } else if (type == MetaType.LIST) {
	// Mappeds child = mapped.getMappeds(key);
	// if (null == child) {
	// m.setValue(result, null);
	// } else {
	// Type genType = m.getTypeClass().getGenericSuperclass();
	// Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
	// List<Object> list = new ArrayList<Object>(child.size());
	// for (int i = 0; i < child.size(); i++) {
	// list.add(getElement((Class<?>) params[0], child, i));
	// }
	// m.setValue(result, list);
	// }
	// } else if (type == MetaType.MAP) {
	// Mappeds child = mapped.getMappeds(key);
	// if (null == child) {
	// m.setValue(result, null);
	// } else {
	// Type genType = m.field.getGenericType();
	// Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
	// Map<Object, Object> map = new HashMap<Object, Object>(child.size());
	// for (int i = 0; i < child.size(); i++) {
	// Mapped entryMapped = child.getMapped(i);
	// map.put(getElement((Class<?>) params[0], entryMapped, MAP_KEY),
	// getElement((Class<?>) params[1], entryMapped, MAP_VALUE));
	// }
	// m.setValue(result, map);
	// }
	// }
	// }
	// return result;
	// }

	/**
	 * 获取原子元素
	 * 
	 * @param type
	 * @param key
	 * @param mapped
	 * @return
	 */
	// private Object getPrimitiveElement(MetaType type, String key, Mapped
	// mapped) {
	// if (type == MetaType.BYTE) {
	// String value = mapped.getString(key);
	// return Byte.valueOf(value);
	// } else if (type == MetaType.CHARACTER) {
	// String value = mapped.getString(key);
	// if (value.isEmpty()) {
	// return Misc.EMPTYCHAR;
	// } else {
	// return value.charAt(0);
	// }
	// } else if (type == MetaType.BOOLEAN) {
	// return mapped.getBoolean(key);
	// } else if (type == MetaType.SHORT) {
	// return mapped.getShort(key);
	// } else if (type == MetaType.INTEGER) {
	// return mapped.getInteger(key);
	// } else if (type == MetaType.LONG) {
	// return mapped.getLong(key);
	// } else if (type == MetaType.FLOAT) {
	// return mapped.getFloat(key);
	// } else if (type == MetaType.DOUBLE) {
	// return mapped.getDouble(key);
	// } else if (type == MetaType.STRING) {
	// return mapped.getString(key);
	// } else if (type == MetaType.DATE) {
	// return mapped.getDate(key);
	// } else {
	// throw new UnsupportedOperationException();
	// }
	// }

	/**
	 * 获取对象元素
	 * 
	 * @param type
	 * @param key
	 * @param mapped
	 * @return
	 */
	// private Object getObjectElement(MetaType type, String key, Mapped mapped)
	// {
	// if (type == MetaType.OBJECT) {
	// Mapped child = mapped.getMapped(key);
	// if (null == child) {
	// return null;
	// }
	// String mapper = child.getString(MAPPER);
	// return mapperSet.hitMapper(mapper).fromMapped(child);
	// }
	// throw new UnsupportedOperationException();
	// }

	/**
	 * 获取元素
	 * 
	 * @param componentType
	 * @param child
	 * @param index
	 * @return
	 */
	// private Object getElement(Class<?> componentType, Mapped mapped, String
	// key) {
	// MetaType type = MetaType.valueOf(componentType);
	// if (type == MetaType.BYTE) {
	// String value = mapped.getString(key);
	// return Byte.valueOf(value);
	// } else if (type == MetaType.CHARACTER) {
	// String value = mapped.getString(key);
	// if (value.isEmpty()) {
	// return Misc.EMPTYCHAR;
	// } else {
	// return value.charAt(0);
	// }
	// } else if (type == MetaType.BOOLEAN) {
	// return mapped.getBoolean(key);
	// } else if (type == MetaType.SHORT) {
	// return mapped.getShort(key);
	// } else if (type == MetaType.INTEGER) {
	// return mapped.getInteger(key);
	// } else if (type == MetaType.LONG) {
	// return mapped.getLong(key);
	// } else if (type == MetaType.FLOAT) {
	// return mapped.getFloat(key);
	// } else if (type == MetaType.DOUBLE) {
	// return mapped.getDouble(key);
	// } else if (type == MetaType.STRING) {
	// return mapped.getString(key);
	// } else if (type == MetaType.DATE) {
	// return mapped.getDate(key);
	// } else if (type == MetaType.OBJECT) {
	// Mapped child = mapped.getMapped(key);
	// if (null == child) {
	// return null;
	// } else {
	// String mapper = child.getString(MAPPER);
	// return mapperSet.hitMapper(mapper).fromMapped(child);
	// }
	// } else if (type == MetaType.ARRAY) {
	// Mappeds child = mapped.getMappeds(key);
	// if (null == child) {
	// return null;
	// } else {
	// Class<?> childComponentType = componentType.getComponentType();
	// Object childValue = Array.newInstance(childComponentType, child.size());
	// for (int i = 0; i < child.size(); i++) {
	// Array.set(childValue, i, getElement(childComponentType, child, i));
	// }
	// return childValue;
	// }
	// } else if (type == MetaType.SET) {
	// Mappeds child = mapped.getMappeds(key);
	// if (null == child) {
	// return null;
	// } else {
	// Type genType = componentType.getGenericSuperclass();
	// Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
	// Set<Object> set = new HashSet<Object>(child.size());
	// for (int i = 0; i < child.size(); i++) {
	// set.add(getElement((Class<?>) params[0], child, i));
	// }
	// return set;
	// }
	// } else if (type == MetaType.LIST) {
	// Mappeds child = mapped.getMappeds(key);
	// if (null == child) {
	// return null;
	// } else {
	// Type genType = componentType.getGenericSuperclass();
	// Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
	// List<Object> list = new ArrayList<Object>(child.size());
	// for (int i = 0; i < child.size(); i++) {
	// list.add(getElement((Class<?>) params[0], child, i));
	// }
	// return list;
	// }
	// } else if (type == MetaType.MAP) {
	// Mappeds child = mapped.getMappeds(key);
	// if (null == child) {
	// return null;
	// } else {
	// Type genType = componentType.getGenericSuperclass();
	// Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
	// Map<Object, Object> map = new HashMap<Object, Object>(child.size());
	// for (int i = 0; i < child.size(); i++) {
	// Mapped entryMapped = child.getMapped(i);
	// map.put(getElement((Class<?>) params[0], entryMapped, MAP_KEY),
	// getElement((Class<?>) params[1], entryMapped, MAP_VALUE));
	// }
	// return map;
	// }
	// } else {
	// throw new UnsupportedOperationException("不支持的类型" + type);
	// }
	// }

	/**
	 * 获取元素
	 * 
	 * @param componentType
	 * @param child
	 * @param index
	 * @return
	 */
	// private Object getElement(Class<?> componentType, Mappeds mappeds, int
	// index) {
	// MetaType type = MetaType.valueOf(componentType);
	// if (type == MetaType.BYTE) {
	// String value = mappeds.getString(index);
	// return Byte.valueOf(value);
	// } else if (type == MetaType.CHARACTER) {
	// String value = mappeds.getString(index);
	// if (value.isEmpty()) {
	// return Misc.EMPTYCHAR;
	// } else {
	// return value.charAt(0);
	// }
	// } else if (type == MetaType.BOOLEAN) {
	// return mappeds.getBoolean(index);
	// } else if (type == MetaType.SHORT) {
	// return mappeds.getShort(index);
	// } else if (type == MetaType.INTEGER) {
	// return mappeds.getInteger(index);
	// } else if (type == MetaType.LONG) {
	// return mappeds.getLong(index);
	// } else if (type == MetaType.FLOAT) {
	// return mappeds.getFloat(index);
	// } else if (type == MetaType.DOUBLE) {
	// return mappeds.getDouble(index);
	// } else if (type == MetaType.STRING) {
	// return mappeds.getString(index);
	// } else if (type == MetaType.DATE) {
	// return mappeds.getDate(index);
	// } else if (type == MetaType.OBJECT) {
	// Mapped child = mappeds.getMapped(index);
	// if (null == child) {
	// return null;
	// } else {
	// String mapper = child.getString(MAPPER);
	// return mapperSet.hitMapper(mapper).fromMapped(child);
	// }
	// } else if (type == MetaType.ARRAY) {
	// Mappeds child = mappeds.getMappeds(index);
	// if (null == child) {
	// return null;
	// } else {
	// Class<?> childComponentType = componentType.getComponentType();
	// Object childValue = Array.newInstance(childComponentType, child.size());
	// for (int i = 0; i < child.size(); i++) {
	// Array.set(childValue, i, getElement(childComponentType, child, i));
	// }
	// return childValue;
	// }
	// } else if (type == MetaType.SET) {
	// Mappeds child = mappeds.getMappeds(index);
	// if (null == child) {
	// return null;
	// } else {
	// Type genType = componentType.getGenericSuperclass();
	// Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
	// Set<Object> set = new HashSet<Object>(child.size());
	// for (int i = 0; i < child.size(); i++) {
	// set.add(getElement((Class<?>) params[0], child, i));
	// }
	// return set;
	// }
	// } else if (type == MetaType.LIST) {
	// Mappeds child = mappeds.getMappeds(index);
	// if (null == child) {
	// return null;
	// } else {
	// Type genType = componentType.getGenericSuperclass();
	// Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
	// List<Object> list = new ArrayList<Object>(child.size());
	// for (int i = 0; i < child.size(); i++) {
	// list.add(getElement((Class<?>) params[0], child, i));
	// }
	// return list;
	// }
	// } else if (type == MetaType.MAP) {
	// Mappeds child = mappeds.getMappeds(index);
	// if (null == child) {
	// return null;
	// } else {
	// Type genType = componentType.getGenericSuperclass();
	// Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
	// Map<Object, Object> map = new HashMap<Object, Object>(child.size());
	// for (int i = 0; i < child.size(); i++) {
	// Mapped entryMapped = child.getMapped(i);
	// map.put(getElement((Class<?>) params[0], entryMapped, MAP_KEY),
	// getElement((Class<?>) params[1], entryMapped, MAP_VALUE));
	// }
	// return map;
	// }
	// } else {
	// throw new UnsupportedOperationException("不支持的类型" + type);
	// }
	// }

	/**
	 * 获取映射表
	 * 
	 * @param value
	 * @return
	 */
	// protected Mapper<?> getMapper(Object value) {
	// return mapperSet.getMapper(MetaType.getMapperName(value.getClass()));
	// }

}
