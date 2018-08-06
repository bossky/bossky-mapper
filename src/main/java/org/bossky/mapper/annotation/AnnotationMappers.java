package org.bossky.mapper.annotation;

import java.util.HashMap;
import java.util.Map;

import org.bossky.common.util.Misc;
import org.bossky.mapper.Mapper;

/**
 * 基于注释的映射器
 * 
 * @author bo
 *
 * @param <E>
 */
public class AnnotationMappers {
	/** 缓存的映射表 */
	private volatile static Map<Key, AnnotationMapper<?>> map = new HashMap<Key, AnnotationMapper<?>>();

	private AnnotationMappers() {

	}

	/**
	 * 主键
	 * 
	 * @author daibo
	 *
	 */
	private static class Key {


		protected Class<?> clazz;

		protected Object[] initargs;

		public Key( Class<?> clazz, Object[] initargs) {
			this.clazz = clazz;
			this.initargs = initargs;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Key) {
				Key other = (Key) obj;
				return  Misc.eq(clazz, other.clazz)
						&& Misc.eq(initargs, other.initargs);
			}
			return false;
		}

	}

	/**
	 * 构造一个映射器
	 * 
	 * @param clazz
	 *            指定类
	 * @param initargs
	 *            构造函数的参数,不能为null
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E> Mapper<E> valueOf( Class<E> clazz, Object... initargs) {
		Key key = new Key(clazz, initargs);
		AnnotationMapper<?> m = map.get(key);
		if (null != m) {
			return (Mapper<E>) m;
		}
		synchronized (map) {
			m = map.get(key);
			if (null != m) {
				return (Mapper<E>) m;// double check
			}
			m = new AnnotationMapper<E>(clazz, initargs);
			map.put(key, m);
		}
		return (Mapper<E>) m;
	}

}
