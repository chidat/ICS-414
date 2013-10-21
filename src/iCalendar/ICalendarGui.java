package iCalendar;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.SocketException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.toedter.calendar.JCalendar;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.DateTime;
import edu.emory.mathcs.backport.java.util.Arrays;
 
public class ICalendarGui {
    private final static String INPUT = "Input Form";
    private final static String TEXTPANEL = "View Events";
    private final static String OPTIONS = "Options";
    private final static String TIMEZONELABEL = "Select time zone:";
    private final static String DATESTARTLABEL = "  Enter start Date and time:";
    private final static String DATEENDLABEL = "   Enter end Date and time:";
    private final static String EVENTLABEL = "Comments:";
    private final static String FILENAMELABEL = "                     Filename:";
    private final static String CHECKBOXLABEL = "Weekly:";
    private ICalendarProject iCalP;
    private JButton addButton;
    private JButton cancelButton;
    private JButton filenameSaveButton;
    private JButton filenameLoadButton;
    private JCheckBox weekleyCheckBox;
    private JCalendar dateStartField;
    private JCalendar dateEndField;
    private JTextField filenameField;
    private JTextField commentField;
    private JTextArea calendarField;
    private JScrollPane calendarSP;
    private JComboBox<String> timeZones;
    
    public ICalendarGui(){
    	iCalP = new ICalendarProject();
      addButton = new JButton("Add Event");
      cancelButton = new JButton("Cancel/Clear");
      weekleyCheckBox = new JCheckBox("",false);
      filenameSaveButton = new JButton("Save");
      filenameLoadButton = new JButton("Load");
      filenameField = new JTextField("default.ics",15);
      dateStartField = new JCalendar();
      dateEndField = new JCalendar();
      commentField = new JTextField();
      calendarField = new JTextArea(iCalP.getCalendar().toString());
      calendarField.setLineWrap(true);
      calendarField.setEditable(false);
      calendarField.setVisible(true);
      calendarSP = new JScrollPane(calendarField, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      calendarSP.setPreferredSize(new Dimension(350, 145));
      //Options for the JComboBox 
      //java's timezone ID's are not guarenteed to match iCalendars timezone ID's
      String[] availableTimezones = java.util.TimeZone.getAvailableIDs();
      Arrays.sort(availableTimezones);
      timeZones = new JComboBox<String>(availableTimezones);
    }
    
    private void initializeFields(){
      java.util.Date date = new java.util.Date();
      dateStartField.setDate(date);
      dateEndField.setDate(date);
      commentField.setText("");
      commentField.setColumns(15);
      calendarField.setText(iCalP.getCalendar().toString());
      timeZones.setSelectedItem(java.util.TimeZone.getDefault().getID());
    }
    private JPanel card1(){
    	
    	JPanel card1 = new JPanel() {
				private static final long serialVersionUID = 1L;
      };
      
    	JLabel labelTimeZones = new JLabel(TIMEZONELABEL);
      JLabel labelDateTimeStart = new JLabel(DATESTARTLABEL);
      JLabel labelDateTimeEnd = new JLabel  (DATEENDLABEL);
      JLabel labelEventComments = new JLabel(EVENTLABEL);
      JLabel labelWeekleyCheckBox = new JLabel(CHECKBOXLABEL);
     
      card1.add(labelTimeZones);
      card1.add(timeZones);
      card1.add(labelDateTimeStart);
      card1.add(dateStartField);
      card1.add(labelDateTimeEnd);
      card1.add(dateEndField);
      card1.add(labelWeekleyCheckBox);
      card1.add(weekleyCheckBox);
      card1.add(labelEventComments);
      card1.add(commentField);
      card1.add(addButton);
      card1.add(cancelButton); 
      
      timeZones.addActionListener(new ActionListener(){
      	@Override
      	public void actionPerformed(ActionEvent event){
      		@SuppressWarnings("unchecked")
					JComboBox<String> cbq = (JComboBox<String>)event.getSource();
          String selectedTz = (String)cbq.getSelectedItem();
      		System.out.println("Selected time zone " + selectedTz);
      		iCalP.changeTimezone(selectedTz);
      	}
      });
      
      addButton.addActionListener(new ActionListener(){
      	@Override
      	public void actionPerformed(ActionEvent event){
      		try {
      			if(weekleyCheckBox.isSelected()){
      				iCalP.createNewWeeklyEvent(new DateTime((java.util.Date)dateStartField.getDate()),
      						new DateTime((java.util.Date)dateEndField.getDate()), commentField.getText());
      			}
      			else{
  						iCalP.createNewEvent(new DateTime((java.util.Date)dateStartField.getDate()), 
  								new DateTime((java.util.Date)dateEndField.getDate()), commentField.getText());
      			}
					} catch (SocketException e) {
						e.printStackTrace();
					}
      		iCalP.writeICalToFile();
      		JOptionPane.showMessageDialog(new JFrame(), "Added event \""+ commentField.getText()
      				+ "\" to: " + filenameField.getText(),
							"", JOptionPane.INFORMATION_MESSAGE);
      		initializeFields();
      	}
      });
      
      cancelButton.addActionListener(new ActionListener(){
      	@Override
      	public void actionPerformed(ActionEvent event){
      		initializeFields();
      		
      	}
      });
      
      return card1;
    }
    
    public JPanel card2(){
    	JPanel card2 = new JPanel();
      card2.add(calendarSP);
      return card2;
    }
    
    public JPanel card3(){
    	JPanel card3 = new JPanel();
    	JLabel labelFilename = new JLabel(FILENAMELABEL);
    	
      card3.add(labelFilename);
      card3.add(filenameField);
      card3.add(filenameSaveButton);
      card3.add(filenameLoadButton);
      
      filenameSaveButton.addActionListener(new ActionListener(){
      	@Override
      	public void actionPerformed(ActionEvent event){
      		iCalP.setFilename(filenameField.getText());
      		iCalP.writeICalToFile();
      	}
      });
      
      filenameLoadButton.addActionListener(new ActionListener(){
      	@Override
      	public void actionPerformed(ActionEvent event){
      		loadICal();
      	}
      });
      return card3;
    }
    
    public void addComponentToPane(Container pane) {
        JTabbedPane tabbedPane = new JTabbedPane();
        
        initializeFields();
        JPanel card1 = card1();
        JPanel card2 = card2();
        JPanel card3 = card3();
        
        tabbedPane.addChangeListener(new ChangeListener() {
          @Override
          public void stateChanged(ChangeEvent e) {
              if (e.getSource() instanceof JTabbedPane) {
                  JTabbedPane pane = (JTabbedPane) e.getSource();
                  if(pane.getSelectedIndex() == 1){
                  	calendarField.setText(iCalP.getCalendar().toString());
                  }
              }
          }
        });
        
        tabbedPane.addTab(INPUT, card1);
        tabbedPane.addTab(TEXTPANEL, card2);
        tabbedPane.addTab(OPTIONS, card3);
        pane.add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void loadICal(){
  			try {
					iCalP.loadCalendar(filenameField.getText());
					filenameField.setText(iCalP.getFilename());
      		JOptionPane.showMessageDialog(new JFrame(), "Sucessfully loaded file: " + filenameField.getText(),
							"Load Complete", JOptionPane.INFORMATION_MESSAGE);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(new JFrame(), "I/O Exception! Failed to load file: " + filenameField.getText(),
							"ERROR", JOptionPane.INFORMATION_MESSAGE);
					e.printStackTrace();
				} catch (ParserException e) {
					JOptionPane.showMessageDialog(new JFrame(), "Parser Exception! Failed to load file: " + filenameField.getText(),
							"ERROR", JOptionPane.INFORMATION_MESSAGE);
					e.printStackTrace();
				}
    }
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Team Tannat iCalendar App");
        frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(380,220);
        //Create and set up the content pane.
        ICalendarGui gui = new ICalendarGui();
        gui.addComponentToPane(frame.getContentPane());
 
        //Display the window.
        //frame.pack();
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
    	@SuppressWarnings("unused")
    	ICalendarGui run = new ICalendarGui();
    	/* Use an appropriate Look and Feel */
      try {
          //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
          UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
      } catch (UnsupportedLookAndFeelException ex) {
          ex.printStackTrace();
      } catch (IllegalAccessException ex) {
          ex.printStackTrace();
      } catch (InstantiationException ex) {
          ex.printStackTrace();
      } catch (ClassNotFoundException ex) {
          ex.printStackTrace();
      }
      /* Turn off metal's use of bold fonts */
      UIManager.put("swing.boldMetal", Boolean.FALSE);
       
      //Schedule a job for the event dispatch thread:
      //creating and showing this application's GUI.
      javax.swing.SwingUtilities.invokeLater(new Runnable() {
          @Override
					public void run() {
              createAndShowGUI();
          }
      });
    }
}