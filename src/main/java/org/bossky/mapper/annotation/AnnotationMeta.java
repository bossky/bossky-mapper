package org.bossky.mapper.annotation;

import java.lang.reflect.Field;

import org.bossky.mapper.Meta;
import org.bossky.mapper.MetaType;
import org.bossky.mapper.exception.MapperException;

/**
 * 基于注释的元信息
 * 
 * @author bo
 *
 */
public class AnnotationMeta implements Meta {
	/** 属性 */
	protected Field field;

	public AnnotationMeta(Field field) {
		this.field = field;
		this.field.setAccessible(true);
	}

	@Override
	public String getName() {
		String name = field.getName();
		return name;
	}

	@Override
	public MetaType getType() {
		return MetaType.valueOf(field.getType());
	}

	public Class<?> getTypeClass() {
		return field.getType();
	}

	@Override
	public Object getValue(Object obj) throws MapperException {
		try {
			return field.get(obj);
		} catch (IllegalArgumentException e) {
			throw new MapperException("参数异常", e);
		} catch (IllegalAccessException e) {
			throw new MapperException("参数访问", e);
		}
	}

	@Override
	public Object setValue(Object obj, Object value) throws MapperException {
		try {
			field.set(obj, value);
			return obj;
		} catch (IllegalArgumentException e) {
			throw new MapperException("参数异常", e);
		} catch (IllegalAccessException e) {
			throw new MapperException("访问异常", e);
		}
	}

}
