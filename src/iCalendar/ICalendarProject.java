package iCalendar;
/* ICalendarExample.java */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.SocketException;
import java.util.Iterator;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.Recur;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Value;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.RRule;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;

public class ICalendarProject {

	private Calendar calendar;
	private TimeZone timezone;
	private VTimeZone tz;
	private TimeZoneRegistry registry;
	private String filename = "default.ics";
 
	public static void main(String[] args) throws IOException, ParserException{
		//ICalendarProject ical = new ICalendarProject();
	}
	
	//Creating a new calendar
	public ICalendarProject(){ 
		calendar = new Calendar();
		registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		timezone = registry.getTimeZone(TimeZone.getDefault().getID());
		tz = timezone.getVTimeZone();
		calendar.getProperties().add(new ProdId("Team Flagler's Super iCal4j Calender App Version 6.9"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);		
		System.out.println(tz.toString());
	}
	
//Creating a new calendar
	public ICalendarProject(String selectedTimezone){ 
		calendar = new Calendar();
		registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		timezone = registry.getTimeZone(selectedTimezone);
		tz = timezone.getVTimeZone();
		calendar.getProperties().add(new ProdId("Team Flagler's Super iCal4j Calender App Version 6.9"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);		
	}
	
	public void loadCalendar(String file) throws IOException, ParserException{
		FileReader fR = new FileReader(file);
		BufferedReader bR = new BufferedReader(fR);
		CalendarBuilder cBuilder = new CalendarBuilder();        	
		setCalendar(cBuilder.build(bR));
		setFilename(file);
  }
	
	public String getFilename(){
		return filename;
	}
	
	public void setFilename(String name){
		filename = name;
	}
	
	public Calendar getCalendar(){
		return calendar;
	}
	
	public void setCalendar(Calendar cal){
		calendar = cal;
	}
	
	public TimeZone getTimeZone(){
		return timezone;
	}
	
	public void changeTimezone(String selectedTimezone){
		registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		timezone = registry.getTimeZone(selectedTimezone);
		tz = timezone.getVTimeZone();
	}

	//Creating an event
	//months start at 0 (January)
	public void createNewEvent(Date eventStart, Date eventEnd,  String eventDescription) throws SocketException{
	  
	  //creating event
	  VEvent iCalEvent = new VEvent(eventStart, eventEnd, eventDescription);
	  //adding timezone info
	  iCalEvent.getProperties().add(tz.getTimeZoneId());
		UidGenerator uidGenerator = new UidGenerator("1");
		iCalEvent.getProperties().add(uidGenerator.generateUid());
		calendar.getComponents().add(iCalEvent);

	}
	
//Creating an all day event
	//months start at 0 (January)
	public void createNewAllDayEvent(Date eventDay, String eventDescription) throws SocketException{
	  
		
	  //creating event
	  VEvent iCalEvent = new VEvent(eventDay, eventDescription);
	  Recur recur = new Recur(Recur.WEEKLY,null);
	  recur.setUntil( new DateTime(eventDay.getTime()) );

	  RRule rule = new RRule(recur);
	  iCalEvent.getProperties().add(rule);
	  // initialise as an all-day event..
	  iCalEvent.getProperties().getProperty(Property.DTSTART).getParameters().add(Value.DATE);
		  
		UidGenerator uidGenerator = new UidGenerator("1");
		iCalEvent.getProperties().add(uidGenerator.generateUid());
		calendar.getComponents().add(iCalEvent);

	}
	
	public void createNewWeeklyEvent(Date eventDay, Date RepeatUntil, String eventDescription) throws SocketException{
		
	  //creating event
	  VEvent iCalEvent = new VEvent(eventDay, eventDescription);
	  Recur recur = new Recur(Recur.WEEKLY,null);
	  recur.setUntil( new DateTime(RepeatUntil.getTime()) );

	  RRule rule = new RRule(recur);
	  iCalEvent.getProperties().add(rule);
	  // initialise as an all-day event..
	  iCalEvent.getProperties().getProperty(Property.DTSTART).getParameters().add(Value.DATE);
		  
		UidGenerator uidGenerator = new UidGenerator("1");
		iCalEvent.getProperties().add(uidGenerator.generateUid());
		calendar.getComponents().add(iCalEvent);

	}
	
	@SuppressWarnings("rawtypes")
	public String parseICalendar() throws IOException, ParserException{
	//Now Parsing an iCalendar file
	  FileInputStream fin = new FileInputStream(filename);
	  String str = new String();
	  CalendarBuilder builder = new CalendarBuilder();
	  calendar = builder.build(fin);
	  //Iterating over a Calendar
	  for (Iterator i = calendar.getComponents().iterator(); i.hasNext();) {
	      Component component = (Component) i.next();
	      System.out.println("Component [" + component.getName() + "]");

	      for (Iterator j = component.getProperties().iterator(); j.hasNext();) {
	          Property property = (Property) j.next();
	          str = str + "Property [" + property.getName() + ", " + property.getValue() + "]" + "/n";
	          System.out.println(str);
	      }
	  }
	  
	  return str;
	}
	
	//Saving an iCalendar file
	public void writeICalToFile(){
		try{
		  FileOutputStream fout = new FileOutputStream(filename);
		  CalendarOutputter outputter = new CalendarOutputter();
		  outputter.setValidating(false);
		  outputter.output(calendar, fout);
		}catch(FileNotFoundException e){
			System.out.println("File does not exist in the correct directory.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
