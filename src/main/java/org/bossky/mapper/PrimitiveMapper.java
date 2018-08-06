package org.bossky.mapper;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.bossky.mapper.exception.MapperException;

/**
 * 原子映射类(用于兼容原子类型,字符串和日期)
 * 
 * @author daibo
 *
 */
public class PrimitiveMapper<E> implements Mapper<E> {

	protected Class<E> clazz;

	protected PrimitiveMeta meta;
	private static Mapper<Byte> BYTE = new PrimitiveMapper<Byte>(Byte.class);
	private static Mapper<Character> CHARACTER = new PrimitiveMapper<Character>(Character.class);
	private static Mapper<Boolean> BOOLEAN = new PrimitiveMapper<Boolean>(Boolean.class);
	private static Mapper<Short> SHORT = new PrimitiveMapper<Short>(Short.class);
	private static Mapper<Integer> INTEGER = new PrimitiveMapper<Integer>(Integer.class);
	private static Mapper<Long> LONG = new PrimitiveMapper<Long>(Long.class);
	private static Mapper<Float> FLOAT = new PrimitiveMapper<Float>(Float.class);
	private static Mapper<Double> DOUBLE = new PrimitiveMapper<Double>(Double.class);
	private static Mapper<String> STRING = new PrimitiveMapper<String>(String.class);
	private static Mapper<Date> DATE = new PrimitiveMapper<Date>(Date.class);

	public static Mapper<?>[] PRIMITIVE_MAPPERS = { BYTE, CHARACTER, BOOLEAN, SHORT, INTEGER, LONG, FLOAT, DOUBLE,
			STRING, DATE };

	public PrimitiveMapper(Class<E> clazz) {
		this.clazz = clazz;
		meta = new PrimitiveMeta(clazz);
	}

	@Override
	public String getName() {
		return MetaType.getMapperName(clazz);
	}

	@Override
	public List<Meta> getMetas() {
		return Collections.singletonList(meta);
	}

	@Override
	public E newInstance() {
		return null;
	}

	// @Override
	// public Mapped toMapped(Mapped mapped, E object) {
	// MetaType type = meta.getType();
	// String key = meta.getName();
	// Object value = object;
	// if (type == MetaType.BYTE) {
	// mapped.put(key, String.valueOf(value));
	// } else if (type == MetaType.CHARACTER) {
	// mapped.put(key, String.valueOf(value));
	// } else if (type == MetaType.SHORT) {
	// mapped.put(key, (Short) value);
	// } else if (type == MetaType.INTEGER) {
	// mapped.put(key, (Integer) value);
	// } else if (type == MetaType.LONG) {
	// mapped.put(key, (Long) value);
	// } else if (type == MetaType.FLOAT) {
	// mapped.put(key, (Float) value);
	// } else if (type == MetaType.DOUBLE) {
	// mapped.put(key, (Double) value);
	// } else if (type == MetaType.BOOLEAN) {
	// mapped.put(key, (Boolean) value);
	// } else if (type == MetaType.STRING) {
	// mapped.put(key, String.valueOf(value));
	// } else if (type == MetaType.DATE) {
	// Date date = (Date) value;
	// mapped.put(key, date);
	// } else {
	// throw new UnsupportedOperationException();
	// }
	// return mapped;
	// }
	//
	// @SuppressWarnings("unchecked")
	// @Override
	// public E fromMapped(Mapped mapped) {
	// MetaType type = meta.getType();
	// String key = meta.getName();
	// if (type == MetaType.BYTE) {
	// String value = mapped.getString(key);
	// return (E) Byte.valueOf(value);
	// } else if (type == MetaType.CHARACTER) {
	// String value = mapped.getString(key);
	// Character ch;
	// if (value.isEmpty()) {
	// ch = Misc.EMPTYCHAR;
	// } else {
	// ch = value.charAt(0);
	// }
	// return (E) ch;
	// } else if (type == MetaType.BOOLEAN) {
	// return (E) mapped.getBoolean(key);
	// } else if (type == MetaType.SHORT) {
	// return (E) mapped.getShort(key);
	// } else if (type == MetaType.INTEGER) {
	// return (E) mapped.getInteger(key);
	// } else if (type == MetaType.LONG) {
	// return (E) mapped.getLong(key);
	// } else if (type == MetaType.FLOAT) {
	// return (E) mapped.getFloat(key);
	// } else if (type == MetaType.DOUBLE) {
	// return (E) mapped.getDouble(key);
	// } else if (type == MetaType.STRING) {
	// return (E) mapped.getString(key);
	// } else if (type == MetaType.DATE) {
	// return (E) mapped.getDate(key);
	// } else {
	// throw new UnsupportedOperationException();
	// }
	// }

	class PrimitiveMeta implements Meta {

		protected Class<E> clazz;
		private final static String META_NAME = "value";

		public PrimitiveMeta(Class<E> clazz) {
			this.clazz = clazz;
		}

		@Override
		public String getName() {
			return META_NAME;
		}

		@Override
		public MetaType getType() {
			return MetaType.valueOf(clazz);
		}

		@Override
		public Class<?> getTypeClass() {
			return clazz;
		}

		@Override
		public Object getValue(Object obj) throws MapperException {
			return obj;
		}

		@Override
		public Object setValue(Object obj, Object value) throws MapperException {
			return value;
		}
	}
}
