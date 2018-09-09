package test.obj;

import tool.obj2list.Object2List;

public interface toListInterface {
	@Object2List(-1)
	public Object getMlong();
	
	@Object2List(6)
	public Object getDate();
	
	@Object2List(3)
	public Object getMfloat();
}
