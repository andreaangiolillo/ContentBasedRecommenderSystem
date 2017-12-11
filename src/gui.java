import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextArea;
import javax.swing.JFormattedTextField;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.lucene.queryparser.classic.ParseException;

import lucene.Index;
import twitter.User;

import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.DefaultComboBoxModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.awt.Font;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import java.awt.Component;

public class gui {

	private JFrame frame;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gui window = new gui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public gui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
	
		frame.getContentPane().setBackground(new Color(204, 255, 204));
		frame.setBackground(Color.GRAY);
		frame.setBounds(100, 100, 857, 857);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"none", "nytimes", "MTVMusic","realDonaldTrump", "BBCSport", "BillGates", "ClassicMusic361", "piptank" ,"MiniGameReviews", "IGN", "NatGeo","NASA", "ftfinancenews"}));
		comboBox.setMaximumRowCount(10);
		comboBox.setBackground(Color.WHITE);
		comboBox.setBounds(136, 12, 196, 24);
		comboBox.setFont(new Font("Noto Sans CJK TC Bold", Font.BOLD | Font.ITALIC, 12));
		
		
		
		frame.getContentPane().add(comboBox);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(51, 17, 81, 15);
		lblUsername.setFont(new Font("Noto Sans CJK TC Bold", Font.BOLD | Font.ITALIC, 12));
		frame.getContentPane().add(lblUsername);
	
		
		TitledBorder description;
		description = BorderFactory.createTitledBorder("Description");
		description.setTitleFont(new Font("Noto Sans CJK TC Bold", Font.BOLD | Font.ITALIC, 12));
		
		TitledBorder cosine;
		cosine = BorderFactory.createTitledBorder("Cosine Similarity");
		cosine.setTitleFont(new Font("Noto Sans CJK TC Bold", Font.BOLD | Font.ITALIC, 12));
		
		TitledBorder cosine_l;
		cosine_l = BorderFactory.createTitledBorder("Lucene Cosine Similarity");
		cosine_l.setTitleFont(new Font("Noto Sans CJK TC Bold", Font.BOLD | Font.ITALIC, 12));
		
		
		textField_1 = new JTextField();
		textField_1.setBounds(136, 89, 196, 24);
		textField_1.setFont(new Font("Noto Sans CJK TC Bold", Font.BOLD | Font.ITALIC, 12));
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
	
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(66, 93, 81, 15);
		lblName.setFont(new Font("Noto Sans CJK TC Bold", Font.BOLD | Font.ITALIC, 12));
		frame.getContentPane().add(lblName);
		
		
		JLabel lblLocation = new JLabel("Location");
		lblLocation.setBounds(51, 167, 81, 15);
		lblLocation.setFont(new Font("Noto Sans CJK TC Bold", Font.BOLD | Font.ITALIC, 12));
		frame.getContentPane().add(lblLocation);
		
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(136, 163, 196, 24);
		textField_2.setFont(new Font("Noto Sans CJK TC Bold", Font.BOLD | Font.ITALIC, 12));
		frame.getContentPane().add(textField_2);
		
		JLabel lblNewLabel = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("lucene-icon.png")).getImage();
		lblNewLabel.setIcon(new ImageIcon(img));
		lblNewLabel.setBounds(25, 252, 107, 37);
		frame.getContentPane().add(lblNewLabel);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(452, 47, 273, 136);
		frame.getContentPane().add(textArea);
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Noto Sans CJK TC Bold", Font.BOLD | Font.ITALIC, 12));
		textArea.setBorder(description);
		textArea.setAutoscrolls(true);
		textArea.setEditable(false);

		
		JTextArea textArea_2 = new JTextArea();
		textArea_2.setLineWrap(true);
		textArea_2.setFont(new Font("Noto Sans CJK TC Bold", Font.BOLD | Font.ITALIC, 12));
		textArea_2.setBounds(432, 328, 324, 149);
		textArea_2.setBorder(cosine_l);
		textArea_2.setLineWrap(true);
		textArea_2.setAutoscrolls(false);
		textArea_2.setEditable(false);
		JTextArea textArea_1 = new JTextArea();
		textArea_1.setBounds(452, 47, 273, 136);
		textArea_1.setFont(new Font("Noto Sans CJK TC Bold", Font.BOLD | Font.ITALIC, 12));
		textArea_1.setEditable(false);
		textArea_1.setBorder(cosine);
		textArea_1.setLineWrap(true);
		
		JScrollPane sp = new JScrollPane(textArea_2); 
		sp.setBounds(25, 306, 738, 224);
		
		JScrollPane sp1 = new JScrollPane(textArea_1); 
		sp1.setBounds(25, 542, 738, 224);
		System.out.println(sp.getVerticalScrollBar().getValue() + "dddd");
		
		
		frame.getContentPane().add(sp);
		frame.getContentPane().add(sp1);
		comboBox.addItemListener(new ItemListener() {
					
					@Override
					public void itemStateChanged(ItemEvent e){
						// TODO Auto-generated method stub
						if (e.getStateChange() == ItemEvent.SELECTED) {
					          String item = e.getItem().toString();
					          System.out.println(item);
					          if (!item.equalsIgnoreCase("none")){
					        	Index index = new Index();
					          	try {
									User user = index.getUserXML(item);
									textArea.setText(user.getDescription());
									textField_1.setText(user.getName());
									textField_2.setText(user.getLocation());
									
									
									
									
									String s = index.similarity(item);	
									textArea_1.setText(s);
									textArea_1.setSelectionStart(0);
									textArea_1.setSelectionEnd(0);
									
									String s1 = index.similarityLucene(item);
									textArea_2.setText(s1);
									textArea_2.setSelectionStart(0);
									textArea_2.setSelectionEnd(0);
							
					          	}catch (JAXBException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								
					          	}catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
					          	}
								
					          }else{
					        	  textArea_1.setText(null);
					        	  textArea.setText(null);
					        	  textArea_2.setText(null);
					        	  textField_1.setText(null);
					        	  textField_2.setText(null);
					        	  
					        	  
					          }
								
						
						}
					}
				});
		
		
	
		
		
		
		
	}
}
