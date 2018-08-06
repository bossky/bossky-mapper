package org.bossky.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bossky.common.util.Misc;

/**
 * 元信息类型
 * 
 * @author bo
 *
 */
public enum MetaType {
	BYTE, SHORT, INTEGER, LONG, FLOAT, DOUBLE, BOOLEAN, CHARACTER, STRING, DATE, OBJECT, ARRAY, SET, MAP, LIST, UNKNOWN;
	/**
	 * 是否原子属性,java的内置原子属性+字符串+日期
	 * 
	 * @return
	 */
	public boolean isPrimitive() {
		return this == MetaType.BYTE || this == MetaType.SHORT || this == MetaType.INTEGER || this == MetaType.LONG
				|| this == MetaType.FLOAT || this == MetaType.FLOAT || this == MetaType.DOUBLE
				|| this == MetaType.BOOLEAN || this == MetaType.CHARACTER || this == MetaType.STRING
				|| this == MetaType.DATE;
	}

	/**
	 * 是否为对象
	 * 
	 * @return
	 */
	public boolean isObject() {
		return this == OBJECT;
	}

	/**
	 * 多元类型,数组,集合,映射表之类的
	 * 
	 * @return
	 */
	public boolean isMultiElement() {
		return this == MetaType.ARRAY || this == MetaType.SET || this == LIST || this == MAP;
	}

	/**
	 * 获取类对应的数据类型
	 * 
	 * @param clazz
	 * @return
	 */
	public static MetaType valueOf(Class<?> clazz) {
		if (clazz.equals(Byte.class) || clazz.equals(byte.class)) {
			return BYTE;
		} else if (clazz.equals(Short.class) || clazz.equals(short.class)) {
			return SHORT;
		}
		if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
			return INTEGER;
		}
		if (clazz.equals(Long.class) || clazz.equals(long.class)) {
			return LONG;
		}
		if (clazz.equals(Float.class) || clazz.equals(float.class)) {
			return FLOAT;
		}
		if (clazz.equals(Double.class) || clazz.equals(double.class)) {
			return DOUBLE;
		}
		if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
			return BOOLEAN;
		}
		if (clazz.equals(Character.class) || clazz.equals(char.class)) {
			return CHARACTER;
		}
		if (clazz.equals(String.class) || clazz.equals(CharSequence.class)) {
			return STRING;
		}
		if (Date.class.isAssignableFrom(clazz)) {
			return DATE;
		}
		// 多元类型
		if (clazz.isArray()) {
			return ARRAY;
		}
		if (Set.class.isAssignableFrom(clazz)) {
			return SET;
		}
		if (Map.class.isAssignableFrom(clazz)) {
			return MAP;
		}
		if (List.class.isAssignableFrom(clazz)) {
			return LIST;
		}
		return OBJECT;
	}

	public static MetaType forName(String typeName) {
		if (Misc.isEmpty(typeName)) {
			return null;
		}
		for (MetaType type : values()) {
			if (Misc.eq(type.name(), typeName)) {
				return type;
			}
		}
		return null;
	}

	/**
	 * 获取类对应的映射名称
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getMapperName(Class<?> clazz) {
		MapperClass mapper = clazz.getAnnotation(MapperClass.class);
		if (null != mapper && !Misc.isEmpty(mapper.name())) {
			return mapper.name();
		}
		if (clazz.isArray()) {
			return ARRAY.name();
		}
		if (Set.class.isAssignableFrom(clazz)) {
			return SET.name();
		}
		if (List.class.isAssignableFrom(clazz)) {
			return LIST.name();
		}
		if (Map.class.isAssignableFrom(clazz)) {
			return MAP.name();
		}
		return clazz.getSimpleName();
	}

}
