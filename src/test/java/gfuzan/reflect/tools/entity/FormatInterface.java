package gfuzan.reflect.tools.entity;

import gfuzan.reflect.tools.dataformat.DataFormat;
import gfuzan.reflect.tools.dataformat.DataFormatUitl;

public interface FormatInterface extends toListInterface {

	@DataFormat(style=DataFormatUitl.NUMBER, pattern="\u00A4,###")
	public Object getMlong();

	@DataFormat(style=DataFormatUitl.DATETIME, pattern="yyyy-MM-dd HH:mm:ss")
	public Object getDate();

	@DataFormat(style=DataFormatUitl.NUMBER, pattern="##.##%")
	public Object getMfloat();
}
