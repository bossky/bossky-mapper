package org.bossky.mapper;

import java.util.HashMap;
import java.util.Map;

import org.bossky.mapper.annotation.AnnotationMappers;
import org.bossky.mapper.exception.MapperException;
import org.bossky.mapper.support.AbstractMapperSet;

/**
 * 简单的映射表实现
 * 
 * @author daibo
 *
 */
public class SimpleMapperSet extends AbstractMapperSet {
	/** 映射表 */
	protected volatile Map<String, Mapper<?>> map = new HashMap<String, Mapper<?>>();

	public SimpleMapperSet() {
		for (Mapper<?> m : PrimitiveMapper.PRIMITIVE_MAPPERS) {
			register(m);
		}
		register(ListMapper.LIST);
	}

	@Override
	public void register(String name, Mapper<?> mapper) {
		Mapper<?> old = map.get(name);
		if (null != old) {
			if (!old.equals(mapper)) {
				throw new MapperException("已存在名称为" + name + "的映射表");
			}
			return;
		}
		synchronized (map) {
			old = map.get(name);// double check
			if (null != old) {
				if (!old.equals(mapper)) {
					throw new MapperException("已存在名称为" + name + "的映射表");
				}
				return;
			}
			map.put(name, mapper);// 新的会覆盖旧的
		}
	}

	@Override
	public Mapper<?> unregister(String name) {
		synchronized (map) {
			return map.remove(name);
		}
	}

	@Override
	public Mapper<?> getMapper(String name) {
		return map.get(name);
	}

	public Mapper<?> hitMapper(Class<?> clazz) throws MapperException {
		return hitMapper(MetaType.getMapperName(clazz));
	}

	@Override
	public Mapper<?> getMapper(Class<?> clazz) {
		return getMapper(MetaType.getMapperName(clazz));
	}

	@Override
	public Mapper<?> register(Class<?> clazz) {
		Mapper<?> m = AnnotationMappers.valueOf(clazz);
		register(m);
		return m;
	}

	@Override
	public Mapper<?> unregister(Class<?> clazz) {
		return unregister(MetaType.getMapperName(clazz));
	}

}
