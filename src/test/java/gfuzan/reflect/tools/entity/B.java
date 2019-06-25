package gfuzan.reflect.tools.entity;

import java.math.BigDecimal;
import java.util.Date;

import gfuzan.reflect.tools.obj2list.Object2List;

public class B extends A implements FormatInterface {

	@Override
	public String toString() {
		return super.toString()+"B [sex=" + sex + ", d=" + d + ", mbyte=" + mbyte + ", mfloat="
				+ mfloat + ", mshort=" + mshort + ", mint=" + mint + ", mlong="
				+ mlong + ", mchar=" + mchar + ", mbig=" + mbig + ",mbig2=" + mbig2 + ", mStr=" + mStr + ", date=" + date +"]";
	}
	private Boolean sex;
	private Double d;
	private Byte mbyte;
	private Float mfloat;
	private Short mshort;
	private Integer mint;
	private Long mlong;
	private String mStr;
	private Character mchar;

	private BigDecimal mbig;

	private BigDecimal mbig2;

	private Date date;

	public Boolean getSex() {
		return sex;
	}

	public void setSex(Boolean sex) {
		this.sex = sex;
	}

	public Double getD() {
		return d;
	}

	public void setD(Double d) {
		this.d = d;
	}

	public Byte getMbyte() {
		return mbyte;
	}

	public void setMbyte(Byte mbyte) {
		this.mbyte = mbyte;
	}

	public Float getMfloat() {
		return mfloat;
	}

	public void setMfloat(Float mfloat) {
		this.mfloat = mfloat;
	}
	@Object2List(1)
	public Short getMshort() {
		return mshort;
	}

	public void setMshort(Short mshort) {
		this.mshort = mshort;
	}

	public Integer getMint() {
		return mint;
	}

	public void setMint(Integer mint) {
		this.mint = mint;
	}

	@Override
	public Long getMlong() {
		return mlong;
	}

	public void setMlong(Long mlong) {
		this.mlong = mlong;
	}

	public Character getMchar() {
		return mchar;
	}

	public void setMchar(Character mchar) {
		this.mchar = mchar;
	}

	public BigDecimal getMbig() {
		return mbig;
	}

	public void setMbig(BigDecimal mbig) {
		this.mbig = mbig;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	public B clone(){
		B res = null;
		try {
			res = (B) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return res;
	}

	public String getmStr() {
		return mStr;
	}

	public void setmStr(String mStr) {
		this.mStr = mStr;
	}

	public BigDecimal getMbig2() {
		return mbig2;
	}

	public void setMbig2(BigDecimal mbig2) {
		this.mbig2 = mbig2;
	}

}
