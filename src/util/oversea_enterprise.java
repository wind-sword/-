package util;

import java.util.HashMap;
import java.util.Map;

public class oversea_enterprise {
	private String id;
	private String engname;
	private String chnname;
	private String shortname;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEngname() {
		return engname;
	}
	public void setEngname(String engname) {
		this.engname = engname;
	}
	public String getChnname() {
		return chnname;
	}
	public void setChnname(String chnname) {
		this.chnname = chnname;
	}
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	public oversea_enterprise(String id, String engname, String chnname, String shortname) {
		this.id = id;
		this.engname = engname;
		this.chnname = chnname;
		this.shortname = shortname;
	}
	public Map<String,Object> map(){
		Map<String,Object> maps = new HashMap<>();
		maps.put("oid", id);
		maps.put("engname", engname);
		maps.put("chnname", chnname);
		maps.put("shortname", shortname);
		return maps;
	}
}
