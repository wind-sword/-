package util;

import java.util.HashMap;
import java.util.Map;

public class Management {
	private String id;
	private String inner;
	private String name;
	private String superid;
	private String rank;
	private String vrank;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInner() {
		return inner;
	}
	public void setInner(String inner) {
		this.inner = inner;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSuperid() {
		return superid;
	}
	public void setSuperid(String superid) {
		this.superid = superid;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getVrank() {
		return vrank;
	}
	public void setVrank(String vrank) {
		this.vrank = vrank;
	}
	public Management(String id, String inner, String name, String superid, String rank, String vrank) {
		this.id = id;
		this.inner = inner;
		this.name = name;
		this.superid = superid;
		this.rank = rank;
		this.vrank = vrank;
	}
	public Map<String,Object> map() {
		Map<String,Object> map = new HashMap<>();
		map.put("mid", id);
		map.put("inner", inner);
		map.put("name", name);
		map.put("rank", rank);
		map.put("vrank", vrank);
		return map;
	}
}
