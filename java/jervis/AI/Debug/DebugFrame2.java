package kalmi.AI.Debug;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import java.util.ArrayList;

import kalmi.AI.State;


@SuppressWarnings("serial")
public class DebugFrame2 extends JFrame implements ChangeListener{

	private JPanel contentPane;
	private JTable foodsTable;
	private JTable agentsTable;
	private JSlider progressSlider;
	JTextArea debugInfo;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DebugFrame2 frame = new DebugFrame2();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DebugFrame2() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));
		
		foodsTable = new JTable();
		JScrollPane foodsTableScrollPane = new JScrollPane(foodsTable);
		panel.add(foodsTableScrollPane);
		
		JLabel lblFoods = new JLabel("Foods:");
		panel.add(lblFoods, BorderLayout.NORTH);
		
		progressSlider = new JSlider();
		contentPane.add(progressSlider, BorderLayout.SOUTH);
		progressSlider.addChangeListener(this);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.EAST);
		
		agentsTable = new JTable();
		JScrollPane agentsTableScrollPane = new JScrollPane(agentsTable);
	
		
		panel_1.setLayout(new BorderLayout(0, 0));
		panel_1.add(agentsTableScrollPane);
		
		JLabel lblAgents = new JLabel("Agents:");
		panel_1.add(lblAgents, BorderLayout.NORTH);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.NORTH);
		
		JLabel lblRound = new JLabel("Round:");
		panel_2.add(lblRound);
		
		JLabel label = new JLabel("???");
		panel_2.add(label);
		
		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3, BorderLayout.CENTER);
		
		debugInfo = new JTextArea();
		debugInfo.setEditable(false);
		debugInfo.setColumns(50);
		debugInfo.setRows(30);
		JScrollPane d = new JScrollPane(debugInfo);
		panel_3.add(d);
		
		setVisible(true);
	}
	
	
	private ArrayList<State> history = new ArrayList<State>();
	public void add(State s) {
		history.add(s);
		
		
		
		// Update slider
		progressSlider.setMaximum(history.size()-1);
		if (progressSlider.getValue()>=history.size()-2) {
			progressSlider.setValue(history.size()-1);
		}
	}
	
	public void stateChanged(ChangeEvent e) {
	    JSlider source = (JSlider)e.getSource();
	    if (!source.getValueIsAdjusting()) {
	    	activateStep(progressSlider.getValue());
	    }
	}
	
	private void activateStep(int stepID) {
		if (stepID>=history.size()) return;
		
		final State state = history.get(stepID);
		
		AbstractTableModel foodModel = new AbstractTableModel(){
			public int getColumnCount() {
				return 2;
			}

			public int getRowCount() {
				return state.foods.size();
			}

			public Object getValueAt(int rowIndex, int columnIndex) {
				if(columnIndex == 0){
					return state.foods.get(rowIndex).x;
				}
				else if(columnIndex == 1){
					return state.foods.get(rowIndex).y;
				}
				return "ERROR";
				
			}
		};
		
		foodsTable.setModel(foodModel);
		
		
		
		AbstractTableModel agentModel = new AbstractTableModel(){
			public int getColumnCount() {
				return 5;
			}

			public int getRowCount() {
				return state.agents.length;
			}

			public Object getValueAt(int rowIndex, int columnIndex) {
				int key = rowIndex;
				if(columnIndex == 0){
					return state.agents[key].id;
				}
				else if(columnIndex == 1){
					return state.agents[key].direction.name();
				}
				else if(columnIndex == 2){
					return state.agents[key].position.x;
				}
				else if(columnIndex == 3){
					return state.agents[key].position.y;
				}
				else if(columnIndex == 4){
					return state.agents[key].claustrofobicness;
				}
				return "ERROR";
				
			}
			
			@Override
			   public String getColumnName(int pCol) {
			       return new String[] {
							"Id", "Name", "x", "y", "c"
					}[pCol];
			   }
		};
		
		agentsTable.setModel(agentModel);
		
		
		debugInfo.setText(state.debugInfo.toString());
	}
}
