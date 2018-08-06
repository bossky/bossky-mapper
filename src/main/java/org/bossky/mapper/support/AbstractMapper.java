package org.bossky.mapper.support;

import java.util.Collections;
import java.util.List;

import org.bossky.mapper.Mapper;
import org.bossky.mapper.Meta;

/**
 * 抽象的映射器
 * 
 * @author bo
 *
 * @param <E>
 */
public abstract class AbstractMapper<E> implements Mapper<E> {
	/** 名称 */
	protected String name;

	public AbstractMapper(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<Meta> getMetas() {
		return Collections.emptyList();
	}

	@Override
	public E newInstance() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Mapper) {
			Mapper<?> m = (Mapper<?>) obj;
			return null == name ? null == m.getName() : name.equals(m.getName());
		}
		return false;
	}

}
