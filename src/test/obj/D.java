package test.obj;

public class D {
	private Integer a;
	private Integer A;
	private Integer aA;
	private Integer Aa;
	private Integer AA;

	public Integer getA() {
		return a;
	}

	public void setA(Integer a) {
		this.a = a;
	}

	public Integer getaA() {
		return aA;
	}

	public void setaA(Integer aA) {
		this.aA = aA;
	}

	public Integer getAa() {
		return Aa;
	}

	public void setAa(Integer aa) {
		Aa = aa;
	}

	public Integer getAA() {
		return AA;
	}

	public void setAA(Integer aA) {
		AA = aA;
	}

	@Override
	public String toString() {
		return "D [a=" + a + ", A=" + A + ", aA=" + aA + ", Aa=" + Aa + ", AA=" + AA + "]";
	}
}
