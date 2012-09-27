/*******************************************************************************
	
	AgentGame 2.00.
	Copyright Peter Eredics (BUTE-DMIS) 2010-2011.
	
	DebugFrame.java - GUI of the sample DebugAction
	
*******************************************************************************/


package kalmi.AI.Debug;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.Arrays;

/**
 * The GUI class of the DebugAction displaying the agent's current 
 * belief base.
 */
public class DebugFrame extends JFrame implements ChangeListener{
	/** Serial needed for serialization. */
	private static final long serialVersionUID = 3L;
	
	/** The main text box for displaying perception in text format. */
	private TextArea textArea;
	
	/** The panel holding main content. */
	private JPanel contentPane;
	
	/** The time-setting slider. */ 
	private JSlider progressSlider;
	
	/** Labels for interpreted information. */
	private JLabel labelTime;
	private JLabel labelPos;
	private JLabel labelDir;
	private JLabel labelEnergy;
	
	/** Log of previous belief base states. */
	private ArrayList<String> history = new ArrayList<String>();
	
	
	
	/**
	 * The default constructor creates the frame with all visual objects.
	 */
	public DebugFrame(){
		// Init frame 
		super("DebugFrame");
		setSize(600,400);
		setVisible(true);
		
		// Creat content objects
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(0, 4, 0, 0));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		
		JLabel label = new JLabel("Time:");
		panel_1.add(label);
		
		labelTime = new JLabel("?");
		panel_1.add(labelTime);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2);
		
		JLabel lblPos = new JLabel("Pos:");
		panel_2.add(lblPos);
		
		labelPos = new JLabel("?");
		panel_2.add(labelPos);
		
		JPanel panel_4 = new JPanel();
		panel.add(panel_4);
		
		JLabel lblDir = new JLabel("Dir:");
		panel_4.add(lblDir);
		
		labelDir = new JLabel("?");
		panel_4.add(labelDir);
				
		JPanel panel_6 = new JPanel();
		panel.add(panel_6);
		
		JLabel lblEnergy = new JLabel("Energy:");
		panel_6.add(lblEnergy);
		
		labelEnergy = new JLabel("?");
		panel_6.add(labelEnergy);
		
		textArea = new TextArea();
		contentPane.add(textArea, BorderLayout.CENTER);
		
		progressSlider = new JSlider();
		progressSlider.addChangeListener(this);
		progressSlider.setValue(100);
		contentPane.add(progressSlider, BorderLayout.SOUTH);
	}
	
	
	/**
	 * Process the event of moving the slider.
	 * @param e			The event of moving the slider. 
	 */
	public void stateChanged(ChangeEvent e) {
	    JSlider source = (JSlider)e.getSource();
	    if (!source.getValueIsAdjusting()) {
	    	activateStep(progressSlider.getValue());
	    }
	}
	

	/**
	 * Save the current belief base into the log.
	 * @param ts		The transition system to extract the belife base from.
	 */
	public void add(String s) {
		// Add current state to the history
		history.add(s);
		
		// Update slider
		progressSlider.setMaximum(history.size()-1);
		if (progressSlider.getValue()>=history.size()-2) {
			progressSlider.setValue(history.size()-1);
		}
	}
	
	
	/** 
	 * Load state from the history. The actual state is also processed here with
	 * maximal stepID as input.
	 * @param stepID		ID of the step to load.
	 */
	private void activateStep(int stepID) {
		if (stepID>=history.size()) return;
		
		// Load state in text format
		String[] array = history.get(stepID).split("\\s");
		Arrays.sort(array);
		String Result = "";
		
		// Try to identify data in the text lines
	    for (int x=0; x<array.length; x++) {
	    	Result = Result + array[x] +"\n";
	    	
	    	// Update the GUI with the data found
	    	if (array[x].indexOf("time")==0) 	   		labelTime.setText(array[x].substring(array[x].indexOf("(")+1, array[x].indexOf(")")));
	    	else if (array[x].indexOf("mypos")==0) 		labelPos.setText(array[x].substring(array[x].indexOf("(")+1, array[x].indexOf(")")));
	    	else if (array[x].indexOf("mydir")==0) 		labelDir.setText(array[x].substring(array[x].indexOf("(")+1, array[x].indexOf(")")));
	    	else if (array[x].indexOf("myenergy")==0) 	labelEnergy.setText(array[x].substring(array[x].indexOf("(")+1, array[x].indexOf(")")));	    	
	    }
	    
	    // Load clear text belief base to the text box
	    textArea.setText(Result);
	}
}
 