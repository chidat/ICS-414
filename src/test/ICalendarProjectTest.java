package test;

import static org.junit.Assert.*;
import java.io.IOException;
import java.net.SocketException;
import java.text.ParseException;
import javax.swing.JTextField;
import iCalendar.ICalendarProject;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import org.junit.Test;

public class ICalendarProjectTest {
  
  ICalendarProject iCalProject = new ICalendarProject();

  @Test
  public void testLoadCalendar() {
    fail("Not yet implemented");
  }

  /**
   * Tests the getFilename() method.
   */
  @Test
  public void testGetFilename() {
    assertEquals("Check file name", "default.ics", iCalProject.getFilename());
  }

  @Test
  public void testSetFilename() {
    fail("Not yet implemented");
  }

  /**
   * Tests the getCalendar() method.
   */
  @Test
  public void testGetCalendar() {
    Calendar calendar = new Calendar();
    assertEquals("Check calendar", calendar, iCalProject.getCalendar());
  }

  @Test
  public void testSetCalendar() {
    fail("Not yet implemented");
  }

  @Test
  public void testGetTimeZone() {
    fail("Not yet implemented");
  }

  @Test
  public void testChangeTimezone() {
    fail("Not yet implemented");
  }

  /**
   * Tests the createNewEvent() method.
   * @throws SocketException
   * @throws ParseException
   */
  @Test
  public void testCreateNewEvent() throws SocketException, ParseException {
    assertTrue("New event not created.", iCalProject.createNewEvent(new Date("20131020"), new Date("20131021"), new JTextField().getText()));
  }

  /**
   * Tests the createNewAllDayEvent() method.
   * @throws SocketException
   * @throws ParseException
   */
  @Test
  public void testCreateNewAllDayEvent() throws SocketException, ParseException {
    assertTrue("New all-day event not created.", iCalProject.createNewAllDayEvent(new Date("20131020"), "All Day Event 1"));
  }

  @Test
  public void testCreateNewWeeklyEvent() throws SocketException, ParseException {
    assertTrue("New weekly event not created.", iCalProject.createNewWeeklyEvent(new Date("20131020"), new Date("20131120"), "Weekly Event 1"));
  }

  /**
   * Tests the parseICalendar() method.
   * @throws IOException
   * @throws ParserException
   */
  @Test
  public void testParseICalendar() throws IOException, ParserException {
    assertEquals("iCalendar not parsed correctly.", "", iCalProject.parseICalendar());
  }

  /**
   * Tests the writeICalToFile() method.
   */
  @Test
  public void testWriteICalToFile() {
    assertTrue("Calendar not output.", iCalProject.writeICalToFile());
  }

}
