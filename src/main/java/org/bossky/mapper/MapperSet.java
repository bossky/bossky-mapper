package org.bossky.mapper;

import org.bossky.mapper.exception.MapperException;

/**
 * 映射器集合
 * 
 * @author bo
 *
 */
public interface MapperSet {

	/**
	 * 注册映射器
	 * 
	 * @param mapper
	 */
	public void register(Mapper<?> mapper);

	/**
	 * 注册映射器
	 * 
	 * @param name
	 * @param mapper
	 */
	public void register(String mapperName, Mapper<?> mapper);

	/**
	 * 注销
	 * 
	 * @param mapper
	 */
	public void unregister(Mapper<?> mapper);

	/**
	 * 注销
	 * 
	 * @param name
	 */
	public Mapper<?> unregister(String mapperName);

	/**
	 * 获取映射表,与get不同的是hit找不到会抛出异常
	 * 
	 * @param mapperName
	 * @return
	 */
	public Mapper<?> hitMapper(String mapperName) throws MapperException;

	/**
	 * 获取映射器
	 * 
	 * @param name
	 * @return
	 */
	public Mapper<?> getMapper(String mapperName);

	/**
	 * 获取映射表,与get不同的是hit找不到会抛出异常
	 * 
	 * @param clazz
	 * @return
	 */
	public Mapper<?> hitMapper(Class<?> clazz) throws MapperException;

	/**
	 * 获取映射器
	 * 
	 * @param name
	 * @return
	 */
	public Mapper<?> getMapper(Class<?> clazz);

	/**
	 * 注册映射器
	 * 
	 * @param mapper
	 */
	public Mapper<?> register(Class<?> clazz);

	/**
	 * 注销
	 * 
	 * @param name
	 */
	public Mapper<?> unregister(Class<?> clazz);

}
