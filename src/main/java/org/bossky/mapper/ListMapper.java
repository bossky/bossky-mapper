package org.bossky.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bossky.mapper.exception.MapperException;

/**
 * 列表映射
 * 
 * @author daibo
 *
 */
public class ListMapper implements Mapper<List<Object>> {

	private ListMeta meta;
	/** 列表映射器 */
	public static ListMapper LIST = new ListMapper();

	public ListMapper() {
		meta = new ListMeta();
	}

	@Override
	public String getName() {
		return MetaType.getMapperName(List.class);
	}

	@Override
	public List<Meta> getMetas() {
		return Arrays.asList(meta);
	}

	@Override
	public List<Object> newInstance() {
		return new ArrayList<Object>();
	}

	// @SuppressWarnings("unchecked")
	// @Override
	// public Mapped toMapped(Mapped mapped, List<Object> value) {
	// if (null == value) {
	// return null;
	// }
	// Mappeds valuem = mapped.createChildMappeds();
	// Mappeds typem = mapped.createChildMappeds();
	// for (Object v : value) {
	// if (null == v) {
	// valuem.putNull();
	// } else {
	// Mapper<Object> m = (Mapper<Object>) mapperSet.hitMapper(v.getClass());
	// typem.put(m.getName());
	// valuem.put(m.toMapped(mapped, v));
	// }
	// }
	// mapped.put("_listvalue", valuem);
	// mapped.put("_listtype", typem);
	// return mapped;
	// }
	//
	// @SuppressWarnings("unchecked")
	// @Override
	// public List<Object> fromMapped(Mapped mapped) {
	// Mappeds values = mapped.getMappeds("_listvalue");
	// Mappeds types = mapped.getMappeds("_listtype");
	// List<Object> list = new ArrayList<Object>(values.size());
	// for (int i = 0; i < values.size(); i++) {
	// Mapper<Object> m = (Mapper<Object>)
	// mapperSet.hitMapper(types.getString(i));
	// list.add(m.fromMapped(values.getMapped(i)));
	// }
	// return list;
	// }

	class ListMeta implements Meta {

		private final static String META_NAME = "value";

		public ListMeta() {
		}

		@Override
		public String getName() {
			return META_NAME;
		}

		@Override
		public MetaType getType() {
			return MetaType.valueOf(List.class);
		}

		@Override
		public Class<?> getTypeClass() {
			return List.class;
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
