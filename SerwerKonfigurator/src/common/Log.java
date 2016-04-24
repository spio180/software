package common;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Log {
	
	public static Boolean WriteToLog(String message) {
		Boolean result = false;
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date today = (Date) Calendar.getInstance().getTime();       
		String logDate = dateFormat.format(today);
		String logDateTime = dateTimeFormat.format(today);	
		String logFilePath = new StringBuilder().append(System.getProperty("user.dir")).append("\\log").append(logDate).append(".txt").toString();
		String messageAndDateTime = new StringBuilder().append(logDateTime).append(" ").append(message).append("\n").toString();
		
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(logFilePath));
		    out.write(messageAndDateTime);
		    out.close();
		    result = true;
		}
		catch (IOException e) {
		    result = false;
		}
		
		return result;
	}
}