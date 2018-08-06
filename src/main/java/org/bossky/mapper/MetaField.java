package org.bossky.mapper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 元信息属性注释
 * 
 * @author bo
 *
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface MetaField {
	/**
	 * 名称
	 * 
	 * @return
	 */
	public String name() default "";

	/**
	 * 是否唯一
	 * 
	 * @return
	 */
	public boolean uniquene() default false;

	/**
	 * 可索引的
	 * 
	 * @return
	 */
	public boolean indexable() default false;

}
