package util;

public class payment4bank {
	private String payid;
	private String bankid;
	private String bankname;
	
	public String getPayid() {
		return payid;
	}
	public void setPayid(String payid) {
		this.payid = payid;
	}
	
	public String getBankid() {
		return bankid;
	}
	public void setBankid(String bankid) {
		this.bankid = bankid;
	}
	
	public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
	
	
	public payment4bank(String payid, String bankid,String bankname) {
		this.payid = payid;
		this.bankid=bankid;
		this.bankname=bankname;
	}
}
