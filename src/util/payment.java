package util;

public class payment {
	private String payid;
	private String rename;
	private String payname;
	public String getPayid() {
		return payid;
	}
	public void setPayid(String payid) {
		this.payid = payid;
	}
	public String getRename() {
		return rename;
	}
	public void setRename(String rename) {
		this.rename = rename;
	}
	public String getPayname() {
		return payname;
	}
	public void setPayname(String payname) {
		this.payname = payname;
	}
	public payment(String payid, String rename, String payname) {
		this.payid = payid;
		this.rename = rename;
		this.payname = payname;
	}
	
}
