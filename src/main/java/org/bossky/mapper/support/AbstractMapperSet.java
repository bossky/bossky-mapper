package org.bossky.mapper.support;

import org.bossky.mapper.Mapper;
import org.bossky.mapper.MapperSet;
import org.bossky.mapper.exception.MapperException;

/**
 * 抽象映射器集合
 * 
 * @author bo
 *
 */
public abstract class AbstractMapperSet implements MapperSet {

	@Override
	public void register(Mapper<?> mapper) {
		if (null == mapper) {
			throw new NullPointerException("映射器不能为空");
		}
		register(mapper.getName(), mapper);
	}

	@Override
	public void unregister(Mapper<?> mapper) {
		if (null == mapper) {
			return;// 忽略空
		}
		unregister(mapper.getName());
	}

	@Override
	public Mapper<?> hitMapper(String mapperName) throws MapperException {
		Mapper<?> m = getMapper(mapperName);
		if (null == m) {
			throw new MapperException("找不到" + mapperName + "对应的映射器");
		}
		return m;
	}
}
