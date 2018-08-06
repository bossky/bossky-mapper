package org.bossky.mapper.json;

import java.util.Date;

import org.bossky.common.util.TimeUtil;
import org.bossky.mapper.Mapped;
import org.bossky.mapper.Mappeds;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 基于json的映射表集合
 * 
 * @author daibo
 *
 */
public class JsonMappeds implements Mappeds {
	/** json数组 */
	protected JSONArray array;

	public JsonMappeds() {
		this.array = new JSONArray();
	}

	protected JsonMappeds(JSONArray array) {
		this.array = array;
	}

	public void putNull() {
		this.array.put(JSONObject.NULL);
	}

	@Override
	public void put(Boolean value) {
		if (null == value) {
			array.put(JSONObject.NULL);
			return;
		}
		array.put(value);
	}

	@Override
	public void put(Short value) {
		if (null == value) {
			array.put(JSONObject.NULL);
			return;
		}
		array.put(value);
	}

	@Override
	public void put(Integer value) {
		if (null == value) {
			array.put(JSONObject.NULL);
			return;
		}
		array.put(value);
	}

	@Override
	public void put(Long value) {
		if (null == value) {
			array.put(JSONObject.NULL);
			return;
		}
		array.put(value);
	}

	@Override
	public void put(Float value) {
		if (null == value) {
			array.put(JSONObject.NULL);
			return;
		}
		array.put(value);
	}

	@Override
	public void put(Double value) {
		if (null == value) {
			array.put(JSONObject.NULL);
			return;
		}
		array.put(value);
	}

	@Override
	public void put(String value) {
		if (null == value) {
			array.put(JSONObject.NULL);
			return;
		}
		array.put(value);
	}

	@Override
	public void put(Date date) {
		if (null == date) {
			array.put(JSONObject.NULL);
			return;
		}
		array.put(TimeUtil.formatCompleteTime(date));
	}

	@Override
	public void put(Mapped mapped) {
		if (null == mapped) {
			array.put(JSONObject.NULL);
			return;
		}
		if (mapped instanceof JsonMapped) {
			array.put(((JsonMapped) mapped).obj);
		} else {
			throw new UnsupportedOperationException("不支持的mapped对象");
		}
	}

	@Override
	public void put(Mappeds mappeds) {
		if (null == mappeds) {
			array.put(JSONObject.NULL);
			return;
		}
		if (mappeds instanceof JsonMappeds) {
			array.put(((JsonMappeds) mappeds).array);
		} else {
			throw new UnsupportedOperationException("不支持的mapped对象");
		}
	}

	@Override
	public void put(Object value) {
		if (null == value) {
			putNull();
		} else if (value instanceof Boolean) {
			put((Boolean) value);
		} else if (value instanceof Short) {
			put((Short) value);
		} else if (value instanceof Integer) {
			put((Integer) value);
		} else if (value instanceof Long) {
			put((Long) value);
		} else if (value instanceof Float) {
			put((Float) value);
		} else if (value instanceof Double) {
			put((Double) value);
		} else if (value instanceof String) {
			put((String) value);
		} else if (value instanceof Date) {
			put((Date) value);
		} else if (value instanceof Mapped) {
			put((Mapped) value);
		} else if (value instanceof Mappeds) {
			put((Mappeds) value);
		} else {
			throw new UnsupportedOperationException("不支持的类型" + value.getClass());
		}
	}

	@Override
	public Boolean getBoolean(int index) {
		if (array.isNull(index)) {
			return null;
		}
		return array.getBoolean(index);
	}

	@Override
	public Short getShort(int index) {
		if (array.isNull(index)) {
			return null;
		}
		Object object = array.get(index);
		return object instanceof Number ? ((Number) object).shortValue() : Short.parseShort((String) object);
	}

	@Override
	public Integer getInteger(int index) {
		if (array.isNull(index)) {
			return null;
		}
		Object object = array.get(index);
		return object instanceof Number ? ((Number) object).intValue() : Integer.parseInt((String) object);
	}

	@Override
	public Long getLong(int index) {
		if (array.isNull(index)) {
			return null;
		}
		Object object = array.get(index);
		return object instanceof Number ? ((Number) object).longValue() : Long.parseLong((String) object);
	}

	@Override
	public Float getFloat(int index) {
		if (array.isNull(index)) {
			return null;
		}
		Object object = array.get(index);
		return object instanceof Number ? ((Number) object).floatValue() : Float.parseFloat((String) object);
	}

	@Override
	public Double getDouble(int index) {
		if (array.isNull(index)) {
			return null;
		}
		Object object = array.get(index);
		return object instanceof Number ? ((Number) object).doubleValue() : Double.parseDouble((String) object);
	}

	@Override
	public String getString(int index) {
		if (array.isNull(index)) {
			return null;
		}
		Object object = array.get(index);
		return String.valueOf(object);
	}

	@Override
	public Date getDate(int index) {
		if (array.isNull(index)) {
			return null;
		}
		Object object = array.get(index);
		return TimeUtil.parseCompleteTime(String.valueOf(object));
	}

	@Override
	public Mapped getMapped(int index) {
		if (array.isNull(index)) {
			return null;
		}
		JSONObject object = array.getJSONObject(index);
		return new JsonMapped(object);
	}

	@Override
	public Mappeds getMappeds(int index) {
		if (array.isNull(index)) {
			return null;
		}
		JSONArray object = array.getJSONArray(index);
		return new JsonMappeds(object);
	}

	@Override
	public int size() {
		return array.length();
	}

	@Override
	public Mapped createChildMapped() {
		return new JsonMapped();
	}

	@Override
	public Mappeds createChildMappeds() {
		return new JsonMappeds();
	}

}
