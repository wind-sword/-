package util;

public class collection {
	private String reid;
	private String payname;
	private String rename;
	String banks;
	String tels;
	public String getReid() {
		return reid;
	}
	public void setReid(String reid) {
		this.reid = reid;
	}
	public String getPayname() {
		return payname;
	}
	public void setPayname(String payname) {
		this.payname = payname;
	}
	public String getRename() {
		return rename;
	}
	public void setRename(String rename) {
		this.rename = rename;
	}
	public collection(String reid, String payname, String rename) {
		this.reid = reid;
		this.payname = payname;
		this.rename = rename;
	}
	
}