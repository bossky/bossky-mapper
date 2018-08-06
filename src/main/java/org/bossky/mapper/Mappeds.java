package org.bossky.mapper;

import java.util.Date;

/**
 * 映射集
 * 
 * @author daibo
 *
 */
public interface Mappeds {
	/**
	 * 创建子映射
	 * 
	 * @return
	 */
	public Mapped createChildMapped();

	/**
	 * 创建子映射集
	 * 
	 * @return
	 */
	public Mappeds createChildMappeds();

	/**
	 * 存入一个空项
	 */
	public void putNull();

	/**
	 * 存入一个布尔值
	 * 
	 * @param value
	 */
	public void put(Boolean value);

	/**
	 * 存入一个短整型
	 * 
	 * @param value
	 */
	public void put(Short value);

	/**
	 * 存入一个整型
	 * 
	 * @param value
	 */
	public void put(Integer value);

	/**
	 * 存入一个长整型
	 * 
	 * @param value
	 */
	public void put(Long value);

	/**
	 * 存入一个单精度浮点数
	 * 
	 * @param value
	 */
	public void put(Float value);

	/**
	 * 存入一个双精度浮点数
	 * 
	 * @param value
	 */
	public void put(Double value);

	/**
	 * 存入一个字符串
	 * 
	 * @param value
	 */
	public void put(String value);

	/**
	 * 存入一个日期
	 * 
	 * @param date
	 */
	public void put(Date date);

	/**
	 * 存入一个映射
	 * 
	 * @param mapped
	 */
	public void put(Mapped mapped);

	/**
	 * 获取一个映射集
	 * 
	 * @param mappeds
	 */
	public void put(Mappeds mappeds);

	/**
	 * 存入值
	 * 
	 * @param key
	 * @param value
	 *            只支持java的基本类型,String,Date,Mapped和Mappeds
	 */
	public void put(Object value);

	/**
	 * 获取值
	 * 
	 * @param index
	 */
	public Boolean getBoolean(int index);

	/**
	 * 获取值
	 * 
	 * @param index
	 */
	public Short getShort(int index);

	/**
	 * 获取值
	 * 
	 * @param index
	 */
	public Integer getInteger(int index);

	/**
	 * 获取值
	 * 
	 * @param index
	 */
	public Long getLong(int index);

	/**
	 * 获取值
	 * 
	 * @param index
	 */
	public Float getFloat(int index);

	/**
	 * 获取值
	 * 
	 * @param index
	 */
	public Double getDouble(int index);

	/**
	 * 获取值
	 * 
	 * @param index
	 */
	public String getString(int index);

	/**
	 * 获取值
	 * 
	 * @param index
	 */
	public Date getDate(int index);

	/**
	 * 获取值
	 * 
	 * @param index
	 */
	public Mapped getMapped(int index);

	/**
	 * 获取值
	 * 
	 * @param index
	 */
	public Mappeds getMappeds(int index);

	/**
	 * 映射表大小
	 * 
	 * @return
	 */
	public int size();

}
