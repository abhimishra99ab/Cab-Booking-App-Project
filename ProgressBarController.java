package application;

import javafx.beans.property.*;
public class ProgressBarController 
{
	private DoubleProperty time;
	
	public final DoubleProperty dist_fraction_property(double time_passed)
	{
		if(time == null)
		{
			time = new SimpleDoubleProperty(time_passed);
		}
		else
		{
			time.set(time_passed);
		}
		return time;
	}
}
