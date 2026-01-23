package logintutkija;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeriesDataItem;

public class Process {
	private int process = 0;
	private TimeSeriesDataItem process_start_item = new TimeSeriesDataItem(new Second(), 0.0);
	private TimeSeriesDataItem process_end_item = new TimeSeriesDataItem(new Second(), 0.0);
	
	
	//set/get
	public int getProcess() {
		return process;
	}
	public void setProcess(int process) {
		this.process = process;
	}
	public TimeSeriesDataItem getProcess_start_item() {
		return process_start_item;
	}
	public void setProcess_start_item(TimeSeriesDataItem process_start_item) {
		this.process_start_item = process_start_item;
	}
	public TimeSeriesDataItem getProcess_end_item() {
		return process_end_item;
	}
	public void setProcess_end_item(TimeSeriesDataItem process_end_item) {
		this.process_end_item = process_end_item;
	}
}
