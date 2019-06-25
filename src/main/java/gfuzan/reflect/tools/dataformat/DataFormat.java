package gfuzan.reflect.tools.dataformat;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.math.RoundingMode;

/**
 * Format注解
 *
 * @author GFuZan
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DataFormat {
	public String style();

	public String pattern() default "0";
	public RoundingMode roundingMode() default RoundingMode.HALF_UP ;
}
