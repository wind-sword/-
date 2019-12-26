package util;

import java.util.HashMap;
import java.util.Map;

public class bank {
	private String id;
	private String name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public bank(String id, String name) {
		this.id = id;
		this.name = name;
	}
	public Map<String,Object> map(){
		Map<String,Object> maps = new HashMap<>();
		maps.put("bid", id);
		maps.put("name", name);
		return maps;
	}
}
