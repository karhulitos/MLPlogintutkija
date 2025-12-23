package logintutkija;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeriesDataItem;

public class Alarm {
	private int alarmnr = 0;
	private TimeSeriesDataItem alarm_start_item = new TimeSeriesDataItem(new Second(), 0.0);
	private TimeSeriesDataItem alarm_end_item = new TimeSeriesDataItem(new Second(), 0.0);
	
	
	//set/get
	public int getAlarmnr() {
		return alarmnr;
	}
	public void setAlarmnr(int alarmnr) {
		this.alarmnr = alarmnr;
	}
	public TimeSeriesDataItem getAlarm_start_item() {
		return alarm_start_item;
	}
	public void setAlarm_start_item(TimeSeriesDataItem alarm_start_item) {
		this.alarm_start_item = alarm_start_item;
	}
	public TimeSeriesDataItem getAlarm_end_item() {
		return alarm_end_item;
	}
	public void setAlarm_end_item(TimeSeriesDataItem alarm_end_item) {
		this.alarm_end_item = alarm_end_item;
	}
}
