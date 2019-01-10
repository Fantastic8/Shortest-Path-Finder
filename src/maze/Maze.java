package maze;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StreamTokenizer;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

public class Maze extends JFrame {
	//--------Essential
	private AddStyle ADS;//Fast Custom Button
	//-----Data
	//int
	private int MazeEdge=50;
	private int AddMode=0;//Choose a mode to add a point in maze
	private int MazeData[][][];//0-Information;1-G Value; 2-H Value; 3-F Value
	private int xframeold;//Help to Drag Frame
	private int yframeold;
	private int[] DirectionCost;//Direction Cost
	private long Sleep;
	
	//File
	private File Map;//this map
	
	//Boolean
	private boolean DrawPath;
	private boolean ShowInfo;
	private boolean ClearSquare=false;//Drag to Clear Square
	private boolean MazeDragged=false;//To Set Maze color effectively
	private boolean Saveable=false;//
	
	//JPanel
	private JPanel MazeSquareInfoPane;//Show a Label which contains this Square's information
	private JPanel MainJPanel;//MainPanel
	private JPanel MazePane;//A JPanel to Draw Maze
	private JPanel MazeInfoPane;//A LayeredPane to Show Info of Suqare
	private PathJPanel MazePathPane;//A LayeredPane to Show Path of Maze
	
	//---MazeColor size 100*100
	//white-Blank
	//black-Obstacle
	//green-Start Point
	//red-End Point
	//orangeOpen List
	//blue-Close List	
	
	//Image Info
	
	//-----Maze Configuration
	//ImageIcon
	private ImageIcon ImageGreenPoint;//Start Point
	private ImageIcon MainBG;//BackGround Image
	private ImageIcon ImageRedPoint;//Start Point
	private ImageIcon ImageBlackPoint;//Start Point
	private ImageIcon ImageSelectPoint;//Selected Point
	private ImageIcon ImageClearAll;//ClearAll
	private ImageIcon ImageClearPath;//ClearPath
	private ImageIcon ImageSizeOK;
	private ImageIcon ImageStart;
	private ImageIcon ImagePause;
	private ImageIcon ImageContinue;
	private ImageIcon ImageNew;
	private ImageIcon ImageImport;
	private ImageIcon ImageSave;
	private ImageIcon ImageSaveAs;
	private ImageIcon ImageSquareInfoUp;
	private ImageIcon ImageSquareInfoDown;
	private ImageIcon ImageSelectItem;
	private ImageIcon ImageSelectSquare;
	private ImageIcon[] MazeColor;//0-white,1-green,2-red,3-black,4-orange,5-blue
	private ImageIcon[] Direction;
	//Label
	private JLabel mainbackground;//BackGroun Label
	private JLabel LabelGreenPoint;
	private JLabel LabelRedPoint;
	private JLabel LabelBlackPoint;
	private JLabel LabelSelectPoint;
	private JLabel LabelGreenPointInfo;
	private JLabel LabelRedPointInfo;
	private JLabel LabelBlackPointInfo;
	private JLabel LabelClearAll;//ClearAll
	private JLabel LabelClearPath;//ClearPath
	private JLabel LabelMazeSize;
	private JLabel LabelSizeOK;
	private JLabel LabelStart;
	private JLabel LabelPause;
	private JLabel LabelContinue;
	private JLabel LabelNew;
	private JLabel LabelImport;
	private JLabel LabelSave;
	private JLabel LabelSaveAs;
	private JLabel LabelSquareInfoUp;
	private JLabel LabelSquareInfoDown;
	private JLabel LabelSelectItem;
	private JLabel LabelSelectSquare;
	private JLabel LabelSquarePos;
	private JLabel LabelSquareG;
	private JLabel LabelSquareH;
	private JLabel LabelSquareF;
	private JLabel LabelSquareStatus;
	private JLabel LabelMapName;
	private JLabel LabelHeuristicScale;
	private JLabel LabelGreedyScale;
	private JLabel[][] MazeLabel;
	private JLabel[][] MazeDirection;
	private JLabel[][] MazeF;
	private JLabel[][] MazeG;
	private JLabel[][] MazeH;
	//JTextField
	private JTextField TextSleep;
	//Button
	private JButton ButtonClearAll;
	private JButton ButtonClearPath;
	private JButton ButtonSizeOK;
	private JButton ButtonStart;
	private JButton ButtonPause;
	private JButton ButtonContinue;
	private JButton ButtonNew;
	private JButton ButtonImport;
	private JButton ButtonSave;
	private JButton ButtonSaveAs;
	//JTextArea
	private JTextArea TextAreaData;
	private JTextArea TextAreaResult;
	//JScrollPane
	private JScrollPane ScrollData;
	private JScrollPane ScrollResult;
	//ComboBox
	private JComboBox BoxMazeSize;
	private JComboBox BoxAlgorithm;
	//ComboBoxModel
	private ComboBoxModel ModelMazeSize;
	private ComboBoxModel ModelAlgorithm;
	//ModelString 
	private String[] StringMazeSize={"10","20","50","100"};
	private String[] StringAlgorithm={"A* Origin","Dijstra","Key Point"};
	//JSlider
	private JSlider SliderHeuristicScale;
	private JSlider SliderGreedyScale;
	//JSpinner
	private JSpinner SpinnerHeuristicScale;
	private JSpinner SpinnerGreedyScale;
	//Thread
	//private Thread AlgorithmThread;
	private AlgorithmController AlgorithmObj;
	//Information:0-Blank 1-StartPoint 2-EndPoint 3-Obstacle 4-OpenList 5-CloseList
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Maze MazeFrame = new Maze();
					MazeFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Maze() {
		//----Data Configuration----
		ADS=new AddStyle();
		MazeColor=new ImageIcon[6];
		MazeColor[0]=new ImageIcon("UI/White.jpg");
		MazeColor[1]=new ImageIcon("UI/Green.jpg");
		MazeColor[2]=new ImageIcon("UI/Red.jpg");
		MazeColor[3]=new ImageIcon("UI/Black.jpg");
		MazeColor[4]=new ImageIcon("UI/Orange.jpg");
		MazeColor[5]=new ImageIcon("UI/Blue.jpg");
		//Image Data
		Direction=new ImageIcon[8];
		Direction[0]=new ImageIcon("UI/Top Left.png");
		Direction[1]=new ImageIcon("UI/Up.png");
		Direction[2]=new ImageIcon("UI/Top Right.png");
		Direction[3]=new ImageIcon("UI/Right.png");
		Direction[4]=new ImageIcon("UI/Down Right.png");
		Direction[5]=new ImageIcon("UI/Down.png");
		Direction[6]=new ImageIcon("UI/Down Left.png");
		Direction[7]=new ImageIcon("UI/Left.png");
		
		//Direction Cost
		DirectionCost=new int[8];
		DirectionCost[0]=14;
		DirectionCost[1]=10;
		DirectionCost[2]=14;
		DirectionCost[3]=10;
		DirectionCost[4]=14;
		DirectionCost[5]=10;
		DirectionCost[6]=14;
		DirectionCost[7]=10;
		
		//Image Info
		ImageSquareInfoUp=new ImageIcon("UI/InfoLebelUp68x188.png");
		ImageSquareInfoDown=new ImageIcon("UI/InfoLabelDown68x0.png");
		
		DrawPath=false;
		ShowInfo=false;
		Map=null;
		//---Draw Pane
		MainPane();
	}
	public void MainPane()
	{
		//------------PanelData Configuration------------
		MainJPanel=(JPanel)getContentPane();//Other Button
		MazePane=new JPanel();//The Square Label of Maze
		MazeInfoPane=new JPanel();//The Direction and G ,H,F value of its Square
		MazePathPane=new PathJPanel();// Draw shortest Path 
		MazeSquareInfoPane=new JPanel();//Show A Label Which goes with Square to show its Information 
		//------------Panel Configuration------------
		setUndecorated(true);//Abandon Border
		setResizable(false);//UnResizable
		setBounds(210,10, 1500, 1010);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		MazePane.setBounds(250, 5, 1000, 1000);
		MazeInfoPane.setBounds(250, 5, 1000, 1000);
		MazePathPane.setBounds(250, 5, 1000, 1000);
		MazeSquareInfoPane.setBounds(0, 0, 1500,1000);
		
		MazePane.setLayout(null);
		MazeInfoPane.setLayout(null);
		MazePathPane.setLayout(null);
		MazeSquareInfoPane.setLayout(null);
		
		MainJPanel.setOpaque(false);
		MazeInfoPane.setOpaque(false);
		MazePathPane.setOpaque(false);
		MazeSquareInfoPane.setOpaque(false);
		
		getContentPane().add(MazePane);
		this.getLayeredPane().add(MazeInfoPane,new Integer(Integer.MAX_VALUE-2));
		this.getLayeredPane().add(MazePathPane,new Integer(Integer.MAX_VALUE-1));
		this.getLayeredPane().add(MazeSquareInfoPane,new Integer(Integer.MAX_VALUE));
		
		//Info Square
		LabelSquareInfoUp=new JLabel(ImageSquareInfoUp);
		LabelSquareInfoDown=new JLabel(ImageSquareInfoDown);
		
		LabelSquareStatus=new JLabel();
		LabelSquarePos=new JLabel();
		LabelSquareG=new JLabel();
		LabelSquareH=new JLabel();
		LabelSquareF=new JLabel();
		LabelSquareInfoUp.setBounds(0, 0, ImageSquareInfoUp.getIconWidth(), ImageSquareInfoUp.getIconHeight());
		LabelSquareInfoDown.setBounds(0, 0, ImageSquareInfoDown.getIconWidth(), ImageSquareInfoDown.getIconHeight());
		LabelSquarePos.setBounds(0, 0, 130, 50);
		LabelSquareG.setBounds(0, 0, 50, 30);
		LabelSquareH.setBounds(0, 0, 50, 30);
		LabelSquareF.setBounds(0, 0, 50, 30);
		LabelSquareStatus.setBounds(0, 0, 80, 30);
		LabelSquarePos.setFont(new Font("Lao UI",0,35));
		LabelSquareG.setFont(new Font("Lao UI",0,20));
		LabelSquareH.setFont(new Font("Lao UI",0,20));
		LabelSquareF.setFont(new Font("Lao UI",0,20));
		LabelSquareStatus.setFont(new Font("Lao UI",0,15));
		LabelSquarePos.setVisible(false);
		LabelSquareG.setVisible(false);
		LabelSquareH.setVisible(false);
		LabelSquareF.setVisible(false);
		LabelSquareStatus.setVisible(false);
		LabelSquareInfoUp.setVisible(false);
		LabelSquareInfoDown.setVisible(false);
		
		MazeSquareInfoPane.add(LabelSquarePos);
		MazeSquareInfoPane.add(LabelSquareG);
		MazeSquareInfoPane.add(LabelSquareH);
		MazeSquareInfoPane.add(LabelSquareF);
		MazeSquareInfoPane.add(LabelSquareStatus);
		MazeSquareInfoPane.add(LabelSquareInfoUp);
		MazeSquareInfoPane.add(LabelSquareInfoDown);
		
		//Load BackGround Image
		MainBG=new ImageIcon("UI/Maze.jpg");
		
		//BackGround Label
		mainbackground=new JLabel(MainBG);
		mainbackground.setBounds(0, 0, MainBG.getIconWidth(),MainBG.getIconHeight());
		this.getLayeredPane().add(mainbackground, new Integer(Integer.MIN_VALUE));
		
		//---Drag Effect---
		this.addMouseListener(new MouseAdapter() 
		{
		  @Override
		  public void mousePressed(MouseEvent e) {
		  xframeold = e.getX();
		  yframeold = e.getY();
		  }
		 });
		this.addMouseMotionListener(new MouseMotionAdapter() {
			  public void mouseDragged(MouseEvent e) {
			  int xOnScreen = e.getXOnScreen();
			  int yOnScreen = e.getYOnScreen();
			  int xframenew = xOnScreen - xframeold;
			  int yframenew = yOnScreen - yframeold;
			  Maze.this.setLocation(xframenew, yframenew);
			  }
			 });
		//---BorderButton---
		ADS.AddBorderButton(MainJPanel, Maze.this);
		//------------Add Other Components------------
//Start Pause Continue;
		//Start
		ADS.AddMyButton(MainJPanel, "UI/ButtonStart.png", 75, 32);
		ImageStart=ADS.getimageicon();
		LabelStart=ADS.getLabel();
		ButtonStart=ADS.getbutton();
		LabelStart.setVisible(true);
		ButtonStart.setVisible(true);
		ButtonStart.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				ImageStart.setImage(ImageStart.getImage().getScaledInstance(ImageStart.getIconWidth()-2,ImageStart.getIconHeight()-1, Image.SCALE_REPLICATE));//scale
				LabelStart.setIcon(ImageStart);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				ImageStart.setImage(ImageStart.getImage().getScaledInstance(ImageStart.getIconWidth()+2,ImageStart.getIconHeight()+1, Image.SCALE_REPLICATE));//scale
				LabelStart.setIcon(ImageStart);
				//Reset Path Data
				for(int y=0;y<MazeEdge;y++)
				{
					for(int x=0;x<MazeEdge;x++)
					{
						//MazeLabel[][],Maze[][],MazeData[][][]
						if(MazeData[y][x][0]<=3)//Start Point,End Point,Obstacle
						{
							continue;
						}
						MazeData[y][x][0]=0;
						MazeLabel[y][x].setIcon(MazeColor[0]);
						MazeData[y][x][1]=0;//G
						MazeData[y][x][2]=0;//H
						MazeData[y][x][3]=0;//F
						MazeDirection[y][x].setVisible(false);
						MazeH[y][x].setVisible(false);
						MazeG[y][x].setVisible(false);
						MazeF[y][x].setVisible(false);
					}
				}
				MazePathPane.repaint();//Clear Path
				MazeInfoPane.repaint();
				MazePathPane.repaint();
				
				TextAreaData.setText("");//Clear Data Area
				TextAreaResult.setText("");//Clear Result Area
				//check
				if(!check())
				{
					return;
				}
				//Choose Algorithm
				switch (BoxAlgorithm.getSelectedIndex())
				{
				case 0:AlgorithmObj=new AArray(Maze.this);break;
				case 1:AlgorithmObj=new Dijstra(Maze.this);break;
				case 2:AlgorithmObj=new KeyPoint(Maze.this);break;
				default:return;
				}
				
				//change to pause
				LabelStart.setVisible(false);
				ButtonStart.setVisible(false);
				LabelPause.setVisible(true);
				ButtonPause.setVisible(true);
				
				//Start Algorithm
				new Thread(AlgorithmObj).start();
			}
		});
		//Pause
		ADS.AddMyButton(MainJPanel, "UI/ButtonPause.png", 73, 32);
		ImagePause=ADS.getimageicon();
		LabelPause=ADS.getLabel();
		ButtonPause=ADS.getbutton();
		LabelPause.setVisible(false);
		ButtonPause.setVisible(false);
		ButtonPause.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				ImagePause.setImage(ImagePause.getImage().getScaledInstance(ImagePause.getIconWidth()-2,ImagePause.getIconHeight()-1, Image.SCALE_REPLICATE));//scale
				LabelPause.setIcon(ImagePause);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				ImagePause.setImage(ImagePause.getImage().getScaledInstance(ImagePause.getIconWidth()+2,ImagePause.getIconHeight()+1, Image.SCALE_REPLICATE));//scale
				LabelPause.setIcon(ImagePause);
				AlgorithmObj.Pause();
				//change to Continue
				LabelPause.setVisible(false);
				ButtonPause.setVisible(false);
				LabelContinue.setVisible(true);
				ButtonContinue.setVisible(true);
			}
		});
		//Continue
		ADS.AddMyButton(MainJPanel, "UI/ButtonContinue.png", 30, 32);
		ImageContinue=ADS.getimageicon();
		LabelContinue=ADS.getLabel();
		ButtonContinue=ADS.getbutton();
		LabelContinue.setVisible(false);
		ButtonContinue.setVisible(false);
		ButtonContinue.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				ImageContinue.setImage(ImageContinue.getImage().getScaledInstance(ImageContinue.getIconWidth()-2,ImageContinue.getIconHeight()-1, Image.SCALE_REPLICATE));//scale
				LabelContinue.setIcon(ImageContinue);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				ImageContinue.setImage(ImageContinue.getImage().getScaledInstance(ImageContinue.getIconWidth()+2,ImageContinue.getIconHeight()+1, Image.SCALE_REPLICATE));//scale
				LabelContinue.setIcon(ImageContinue);
				AlgorithmObj.Continue();
				//change to Pause
				LabelPause.setVisible(true);
				ButtonPause.setVisible(true);
				LabelContinue.setVisible(false);
				ButtonContinue.setVisible(false);
			}
		});
//Data Area
		TextAreaData=new JTextArea();
		ScrollData=new JScrollPane(TextAreaData);
		TextAreaData.setBounds(1265, 52,220, 592);
		ScrollData.setBounds(1265, 52,220, 592);
		ScrollData.setBorder(null);
		TextAreaData.setLineWrap(true);
		TextAreaData.setEditable(false);
		TextAreaData.setFont(new Font("Lao UI",0,18));
		ScrollData.setAutoscrolls(true);
		MainJPanel.add(ScrollData);
//Result Area
		TextAreaResult=new JTextArea();
		ScrollResult=new JScrollPane(TextAreaResult);
		TextAreaResult.setBounds(1265, 693,220, 299);
		ScrollResult.setBounds(1265, 693,220, 299);
		ScrollResult.setBorder(null);
		TextAreaResult.setLineWrap(true);
		TextAreaResult.setEditable(false);
		TextAreaResult.setFont(new Font("Lao UI",0,18));
		ScrollResult.setAutoscrolls(true);
		MainJPanel.add(ScrollResult);
//Map
		//Map Label
		LabelMapName=new JLabel();
		LabelMapName.setBounds(20, 150, 150, 50);
		LabelMapName.setFont(new Font("Lao UI",0,25));
		MainJPanel.add(LabelMapName);
		
		//Import Button
		ADS.AddMyButton(MainJPanel, "UI/ButtonImport.png", 170, 170);
		ImageImport=ADS.getimageicon();
		LabelImport=ADS.getLabel();
		ButtonImport=ADS.getbutton();
		ButtonImport.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				ImageImport.setImage(ImageImport.getImage().getScaledInstance(ImageImport.getIconWidth()-1,ImageImport.getIconHeight()-1, Image.SCALE_REPLICATE));//scale
				LabelImport.setIcon(ImageImport);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				//Save current file
				if(Saveable==true)
				{
					int response=JOptionPane.showOptionDialog(null, "Are you going to Save Current Map?", "Save", JOptionPane.DEFAULT_OPTION, JOptionPane.OK_CANCEL_OPTION, null,new String[]{"Save","Don't Save","Cancel"}, "Save");
					if(response==2)//Cancel
					{
						return;
					}
					else if(response==0)//Save
					{
						try {
							SaveCurrentMap();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					Saveable=false;
				}
				
				ImageImport.setImage(ImageImport.getImage().getScaledInstance(ImageImport.getIconWidth()+1,ImageImport.getIconHeight()+1, Image.SCALE_REPLICATE));//scale
				LabelImport.setIcon(ImageImport);
				JFileChooser MapChooser=new JFileChooser();
				MapChooser.setFileFilter(new FileFilter(){//Only Accept *.maze
		            @Override
		            public boolean accept(File pathname) {
		                // TODO Auto-generated method stub
		                String s = pathname.getName().toLowerCase();
		                if(s.endsWith(".maze")||!s.contains(".")){
		                    return true;
		                }
		                return false;
		            }
					@Override
					public String getDescription() {
						// TODO Auto-generated method stub
						return ".maze";
					}
		        });
				MapChooser.setCurrentDirectory(new File("E:\\My Project\\MazeMap"));
				MapChooser.showOpenDialog(Maze.this);
				Map=MapChooser.getSelectedFile();
				if(Map==null)//Cancel
				{
					return;
				}
				if(!Map.getName().toLowerCase().endsWith(".maze"))//Format
				{
					JOptionPane.showMessageDialog(null,  "Error Format!", "Error!",JOptionPane.ERROR_MESSAGE);
					Map=null;//empty map
					return;
				}
				try {
					ImportMap(Map);//Import
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				MazePane.repaint();
				MazePathPane.repaint();
				MazeInfoPane.repaint();
			}
		});
		//New
		ADS.AddMyButton(MainJPanel, "UI/ButtonNew.png", 20, 210);
		ImageNew=ADS.getimageicon();
		LabelNew=ADS.getLabel();
		ButtonNew=ADS.getbutton();
		ButtonNew.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				ImageNew.setImage(ImageNew.getImage().getScaledInstance(ImageNew.getIconWidth()-1,ImageNew.getIconHeight()-1, Image.SCALE_REPLICATE));//scale
				LabelNew.setIcon(ImageNew);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				ImageNew.setImage(ImageNew.getImage().getScaledInstance(ImageNew.getIconWidth()+1,ImageNew.getIconHeight()+1, Image.SCALE_REPLICATE));//scale
				LabelNew.setIcon(ImageNew);
				
				if(Saveable)//Need to Save Current Map
				{
					int response=JOptionPane.showOptionDialog(null, "Are You Going To Save Current File?", "Save", JOptionPane.DEFAULT_OPTION, JOptionPane.OK_CANCEL_OPTION, null,new String[]{"Save","Don't Save","Cancel"}, null);
					if(response==2)//cancel
					{
						return;
					}
					else if(response==0)//Save
					{
						try {
							SaveCurrentMap();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					Saveable=false;
				}
				Map=null;
				LabelMapName.setText("");
				ClearMazeInformation();
			}
		});
		//Save
		ADS.AddMyButton(MainJPanel, "UI/ButtonSave.png", 90, 210);
		ImageSave=ADS.getimageicon();
		LabelSave=ADS.getLabel();
		ButtonSave=ADS.getbutton();
		ButtonSave.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				ImageSave.setImage(ImageSave.getImage().getScaledInstance(ImageSave.getIconWidth()-1,ImageSave.getIconHeight()-1, Image.SCALE_REPLICATE));//scale
				LabelSave.setIcon(ImageSave);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				ImageSave.setImage(ImageSave.getImage().getScaledInstance(ImageSave.getIconWidth()+1,ImageSave.getIconHeight()+1, Image.SCALE_REPLICATE));//scale
				LabelSave.setIcon(ImageSave);
				
				//Already Exist A File
				if(Map!=null)
				{
					try {
						SaveCurrentMap();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					return;
				}
				//Save A New File
				JFileChooser MapSaver=new JFileChooser();
				MapSaver.setDialogTitle("Save");
				MapSaver.setDialogType(JFileChooser.SAVE_DIALOG);
				MapSaver.setCurrentDirectory(new File("E:\\My Project\\MazeMap"));
				MapSaver.setFileFilter(new FileFilter(){//Only Accept *.maze
		            @Override
		            public boolean accept(File pathname) {
		                // TODO Auto-generated method stub
		                return true;
		            }
					@Override
					public String getDescription() {
						// TODO Auto-generated method stub
						return ".maze";
					}
		        });
				MapSaver.showSaveDialog(Maze.this);
				File tempfile=MapSaver.getSelectedFile();
				if(tempfile==null)
				{
					return;
				}
				String address=tempfile.getAbsolutePath();
				if(tempfile.exists())//Update this File
				{
					int r=JOptionPane.showOptionDialog(null,  "File Exist!Do you want to Update this file?", "Warning!",JOptionPane.DEFAULT_OPTION,JOptionPane.OK_CANCEL_OPTION,null,new String[]{"OK","Cancel"},"OK");
					if(r!=0)//Cancel
					{
						return;
					}
					System.out.println(tempfile.getName());
					address=address.substring(0,address.length()-5);
					System.out.println(address);
					tempfile.delete();
				}
				try {
					SaveMap(address);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		//SaveAs
		ADS.AddMyButton(MainJPanel, "UI/ButtonSaveAs.png", 160, 210);
		ImageSaveAs=ADS.getimageicon();
		LabelSaveAs=ADS.getLabel();
		ButtonSaveAs=ADS.getbutton();
		ButtonSaveAs.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				ImageSaveAs.setImage(ImageSaveAs.getImage().getScaledInstance(ImageSaveAs.getIconWidth()-1,ImageSaveAs.getIconHeight()-1, Image.SCALE_REPLICATE));//scale
				LabelSaveAs.setIcon(ImageSaveAs);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				ImageSaveAs.setImage(ImageSaveAs.getImage().getScaledInstance(ImageSaveAs.getIconWidth()+1,ImageSaveAs.getIconHeight()+1, Image.SCALE_REPLICATE));//scale
				LabelSaveAs.setIcon(ImageSaveAs);
				
				//Save A New File
				JFileChooser MapSaver=new JFileChooser();
				MapSaver.setDialogTitle("Save As...");
				MapSaver.setDialogType(JFileChooser.SAVE_DIALOG);
				MapSaver.setCurrentDirectory(new File("E:\\My Project\\MazeMap"));
				MapSaver.setFileFilter(new FileFilter(){//Only Accept *.maze
		            @Override
		            public boolean accept(File pathname) {
		                // TODO Auto-generated method stub
		                return true;
		            }
					@Override
					public String getDescription() {
						// TODO Auto-generated method stub
						return ".maze";
					}
		        });
				MapSaver.showSaveDialog(Maze.this);
				File tempfile=MapSaver.getSelectedFile();
				if(tempfile==null)
				{
					return;
				}
				String address=tempfile.getAbsolutePath();
				if(tempfile.exists())//Update this File
				{
					int r=JOptionPane.showOptionDialog(null,  "File Exist!Do you want to Update this file?", "Warning!",JOptionPane.DEFAULT_OPTION,JOptionPane.OK_CANCEL_OPTION,null,new String[]{"OK","Cancel"},"OK");
					if(r!=0)//Cancel
					{
						return;
					}
					System.out.println(tempfile.getName());
					address=address.substring(0,address.length()-5);
					System.out.println(address);
					tempfile.delete();
				}
				try {
					SaveMap(address);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
//Choose Point
		ImageGreenPoint=new ImageIcon("UI/GreenPoint.png");
		ImageRedPoint=new ImageIcon("UI/RedPoint.png");
		ImageBlackPoint=new ImageIcon("UI/BlackPoint.png");
		ImageSelectPoint=new ImageIcon("UI/SelectPoint.png");
		
		LabelGreenPoint=new JLabel(ImageGreenPoint);
		LabelRedPoint=new JLabel(ImageRedPoint);
		LabelBlackPoint=new JLabel(ImageBlackPoint);
		LabelSelectPoint=new JLabel(ImageSelectPoint);
		
		LabelGreenPointInfo=new JLabel("Start Point");
		LabelRedPointInfo=new JLabel("End Point");
		LabelBlackPointInfo=new JLabel("Obstacle");
		
		LabelGreenPoint.setBounds(190,250, ImageGreenPoint.getIconWidth(), ImageGreenPoint.getIconHeight());
		LabelRedPoint.setBounds(190,300, ImageRedPoint.getIconWidth(), ImageRedPoint.getIconHeight());
		LabelBlackPoint.setBounds(190,350, ImageBlackPoint.getIconWidth(), ImageBlackPoint.getIconHeight());
		LabelSelectPoint.setBounds(0,0, ImageSelectPoint.getIconWidth(), ImageSelectPoint.getIconHeight());
		LabelSelectPoint.setVisible(false);
		
		LabelGreenPointInfo.setBounds(40, 245, 150, 50);
		LabelRedPointInfo.setBounds(40, 295, 150, 50);
		LabelBlackPointInfo.setBounds(40, 345, 150, 50);
		LabelGreenPointInfo.setFont(new Font("Lao UI",0,25));
		LabelRedPointInfo.setFont(new Font("Lao UI",0,25));
		LabelBlackPointInfo.setFont(new Font("Lao UI",0,25));
		
		
		LabelGreenPoint.addMouseListener(new MouseAdapter(){
					@Override
					public void mousePressed(MouseEvent e) {
						LabelSelectPoint.setVisible(true);
						LabelSelectPoint.setLocation(190, 250);
						AddMode=1;
					}
		});
		LabelRedPoint.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				LabelSelectPoint.setVisible(true);
				LabelSelectPoint.setLocation(190, 300);
				AddMode=2;
			}
		});
		LabelBlackPoint.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				LabelSelectPoint.setVisible(true);
				LabelSelectPoint.setLocation(190, 350);
				AddMode=3;
			}
		});
		
		MainJPanel.add(LabelGreenPoint);
		MainJPanel.add(LabelRedPoint);
		MainJPanel.add(LabelBlackPoint);
		MainJPanel.add(LabelSelectPoint);
		MainJPanel.add(LabelGreenPointInfo);
		MainJPanel.add(LabelRedPointInfo);
		MainJPanel.add(LabelBlackPointInfo);
		
//Set Maze Size
		LabelMazeSize=new JLabel("Maze Size");
		LabelMazeSize.setFont(new Font("Lao UI",0,35));
		LabelMazeSize.setBounds(45, 410, 200, 50);
		MainJPanel.add(LabelMazeSize);
		
		ADS.AddMyButton(MainJPanel, "UI/OK.png", 165, 480);
		ImageSizeOK=ADS.getimageicon();
		LabelSizeOK=ADS.getLabel();
		ButtonSizeOK=ADS.getbutton();
		//OK
		ButtonSizeOK.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				ImageSizeOK.setImage(ImageSizeOK.getImage().getScaledInstance(ImageSizeOK.getIconWidth()-2,ImageSizeOK.getIconHeight()-1, Image.SCALE_REPLICATE));//scale
				LabelSizeOK.setIcon(ImageSizeOK);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				ImageSizeOK.setImage(ImageSizeOK.getImage().getScaledInstance(ImageSizeOK.getIconWidth()+2,ImageSizeOK.getIconHeight()+1, Image.SCALE_REPLICATE));//scale
				LabelSizeOK.setIcon(ImageSizeOK);
				//Reset Maze
				if(MazeEdge==Integer.valueOf(BoxMazeSize.getSelectedItem().toString()))
				{
					return;
				}
				
				//Save Current File
				if(Saveable)
				{
					int response=JOptionPane.showOptionDialog(null, "Are You Going To Save Current File?", "Save", JOptionPane.DEFAULT_OPTION, JOptionPane.OK_CANCEL_OPTION, null,new String[]{"Save","Don't Save","Cancel"}, null);
					if(response==2)//cancel
					{
						return;
					}
					else if(response==0)//Save
					{
						try {
							SaveCurrentMap();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					Saveable=false;
					Map=null;
					LabelMapName.setText("");
				}
				
				MazeEdge=Integer.valueOf(BoxMazeSize.getSelectedItem().toString());

				ResetMazeLabel(MazeEdge);
			}
		});
		
		BoxMazeSize=new JComboBox();
		ModelMazeSize=new DefaultComboBoxModel(StringMazeSize);
		BoxMazeSize.setModel(ModelMazeSize);
		BoxMazeSize.setBounds(40, 478, 100, 30);
		BoxMazeSize.setFont(new Font("Lao UI",0,20));
		BoxMazeSize.setSelectedIndex(2);
		MainJPanel.add(BoxMazeSize);
//Choose Algorithm
		BoxAlgorithm=new JComboBox();
		ModelAlgorithm=new DefaultComboBoxModel(StringAlgorithm);
		BoxAlgorithm.setModel(ModelAlgorithm);
		BoxAlgorithm.setBounds(28, 585, 200, 40);
		BoxAlgorithm.setFont(new Font("Lao UI",0,20));
		BoxAlgorithm.setSelectedIndex(0);
		MainJPanel.add(BoxAlgorithm);
		BoxAlgorithm.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if(e.getStateChange()==ItemEvent.SELECTED)
				{
					if(BoxAlgorithm.getSelectedIndex()==0)//A* Algorithm
					{
						LabelHeuristicScale.setVisible(true);
						SliderHeuristicScale.setVisible(true);
						SpinnerHeuristicScale.setVisible(true);
					}
					else
					{
						LabelHeuristicScale.setVisible(false);
						SliderHeuristicScale.setVisible(false);
						SpinnerHeuristicScale.setVisible(false);
					}
				}
			}
			
		});
//Sleep
		TextSleep=new JTextField();
		TextSleep.setBounds(112, 641, 105, 30);
		TextSleep.setFont(new Font("Lao UI",0,20));
		TextSleep.setBorder(null);
		TextSleep.setOpaque(true);
		MainJPanel.add(TextSleep);
		
//Show Square Info
		ImageSelectSquare=new ImageIcon("UI/SelectSquare.png");
		ImageSelectItem=new ImageIcon("UI/SelectItem.png");
		LabelSelectItem=new JLabel(ImageSelectItem);
		LabelSelectSquare=new JLabel(ImageSelectSquare);
		LabelSelectItem.setBounds(190, 725, ImageSelectItem.getIconWidth(), ImageSelectItem.getIconHeight());
		LabelSelectSquare.setBounds(185, 728, ImageSelectSquare.getIconWidth(), ImageSelectSquare.getIconHeight());
		LabelSelectItem.setVisible(false);
		
		LabelSelectSquare.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e)
			{
				ShowInfo=!ShowInfo;
				LabelSelectItem.setVisible(ShowInfo);
			}
		});
		MainJPanel.add(LabelSelectItem);
		MainJPanel.add(LabelSelectSquare);
//Algorithm Setting
	//A*
		LabelHeuristicScale=new JLabel("Heuristic Value");
		LabelHeuristicScale.setBounds(40, 770, 200, 30);
		LabelHeuristicScale.setFont(new Font("Lao UI",0,25));
		MainJPanel.add(LabelHeuristicScale);
		
		SliderHeuristicScale=new JSlider();
		SliderHeuristicScale.setBounds(10, 790, 180, 50);
		SliderHeuristicScale.setOpaque(false);
		SliderHeuristicScale.setMaximum(100);
		SliderHeuristicScale.setMinimum(0);
		SliderHeuristicScale.setValue(10);
		SliderHeuristicScale.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				SpinnerHeuristicScale.setValue(SliderHeuristicScale.getValue());
			}
		});
		MainJPanel.add(SliderHeuristicScale);
		
		SpinnerHeuristicScale=new JSpinner();
		SpinnerHeuristicScale.setBounds(195, 805, 40, 20);
		SpinnerHeuristicScale.setFont(new Font("Lao UI",0,15));
		SpinnerHeuristicScale.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				if((int)SpinnerHeuristicScale.getValue()>100)
				{
					SpinnerHeuristicScale.setValue(100);
				}
				if((int)SpinnerHeuristicScale.getValue()<0)
				{
					SpinnerHeuristicScale.setValue(0);
				}
				SliderHeuristicScale.setValue((int)SpinnerHeuristicScale.getValue());
			}
		});
		SpinnerHeuristicScale.setValue(10);
		MainJPanel.add(SpinnerHeuristicScale);
//ClearAll Button
		ADS.AddMyButton(MainJPanel, "UI/ButtonClearAll.png", 15, 973);
		ImageClearAll=ADS.getimageicon();
		LabelClearAll=ADS.getLabel();
		ButtonClearAll=ADS.getbutton();
		
		ButtonClearAll.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				ImageClearAll.setImage(ImageClearAll.getImage().getScaledInstance(ImageClearAll.getIconWidth()-2,ImageClearAll.getIconHeight()-1, Image.SCALE_REPLICATE));//scale
				LabelClearAll.setIcon(ImageClearAll);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				ImageClearAll.setImage(ImageClearAll.getImage().getScaledInstance(ImageClearAll.getIconWidth()+2,ImageClearAll.getIconHeight()+1, Image.SCALE_REPLICATE));//scale
				LabelClearAll.setIcon(ImageClearAll);
				
				if(AlgorithmObj!=null)
				{
					AlgorithmObj.Pause();
					AlgorithmObj.Stop();
				}
				
				ClearMazeInformation();
			}
		});
		MainJPanel.add(LabelClearAll);

//ClearPath Button
		ADS.AddMyButton(MainJPanel, "UI/ButtonClearPath.png", 128, 973);
		ImageClearPath=ADS.getimageicon();
		LabelClearPath=ADS.getLabel();
		ButtonClearPath=ADS.getbutton();
		
		ButtonClearPath.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				ImageClearPath.setImage(ImageClearPath.getImage().getScaledInstance(ImageClearPath.getIconWidth()-2,ImageClearPath.getIconHeight()-1, Image.SCALE_REPLICATE));//scale
				LabelClearPath.setIcon(ImageClearPath);
			}
			@Override
			public void mouseReleased(MouseEvent e)
			{
				ImageClearPath.setImage(ImageClearPath.getImage().getScaledInstance(ImageClearPath.getIconWidth()+2,ImageClearPath.getIconHeight()+1, Image.SCALE_REPLICATE));//scale
				LabelClearPath.setIcon(ImageClearPath);
				if(AlgorithmObj!=null)
				{
					AlgorithmObj.Pause();
					AlgorithmObj.Stop();
				}
				
				//Clear All Maze
				for(int y=0;y<MazeEdge;y++)
				{
					for(int x=0;x<MazeEdge;x++)
					{
						MazeDirection[y][x].setVisible(false);
						MazeH[y][x].setVisible(false);
						MazeG[y][x].setVisible(false);
						MazeF[y][x].setVisible(false);
						if(MazeData[y][x][0]<=3)
						{
							continue;
						}
						MazeData[y][x][0]=0;
						MazeData[y][x][1]=0;
						MazeData[y][x][2]=0;
						MazeData[y][x][3]=0;
						MazeLabel[y][x].setIcon(MazeColor[0]);
					}
				}
				TextAreaData.setText("");
				MazePane.repaint();
				MazePathPane.repaint();
			}
		});
		MainJPanel.add(LabelClearPath);
		
		
		
		ResetMazeLabel(MazeEdge);//Initialize Maze Interface
	}
//-----Other Method	
//Reset Maze Label
	public void ResetMazeLabel(int edge)
	{
		TextAreaData.setText("");
		Saveable=false;
		//reset image 
		MazeColor[0]=new ImageIcon("UI/White.jpg");
		MazeColor[1]=new ImageIcon("UI/Green.jpg");
		MazeColor[2]=new ImageIcon("UI/Red.jpg");
		MazeColor[3]=new ImageIcon("UI/Black.jpg");
		MazeColor[4]=new ImageIcon("UI/Orange.jpg");
		MazeColor[5]=new ImageIcon("UI/Blue.jpg");
		
		Direction[0]=new ImageIcon("UI/Top Left.png");
		Direction[1]=new ImageIcon("UI/Up.png");
		Direction[2]=new ImageIcon("UI/Top Right.png");
		Direction[3]=new ImageIcon("UI/Right.png");
		Direction[4]=new ImageIcon("UI/Down Right.png");
		Direction[5]=new ImageIcon("UI/Down.png");
		Direction[6]=new ImageIcon("UI/Down Left.png");
		Direction[7]=new ImageIcon("UI/Left.png");
		
		//Clear Original Maze
		MazePane.removeAll();
		MazeInfoPane.removeAll();
		MazePane.repaint();
		MazeInfoPane.repaint();
		MazePathPane.repaint();
		
		//--------Maze Icon Configuration
		for(int i=0;i<6;i++)
		{
			MazeColor[i].setImage(MazeColor[i].getImage().getScaledInstance(1000/edge,1000/edge, Image.SCALE_SMOOTH));//scale
		}	
		
		for(int i=0;i<8;i++)
		{
			Direction[i].setImage(Direction[i].getImage().getScaledInstance(1000/edge,1000/edge, Image.SCALE_SMOOTH));//scale
		}
		//---Add Maze
		MazeLabel=new JLabel[edge][edge];
		MazeData=new int[edge][edge][4];//Maze Data:Information,G,H,F
		MazeDirection=new JLabel[edge][edge];
		MazeF=new JLabel[edge][edge];
		MazeG=new JLabel[edge][edge];
		MazeH=new JLabel[edge][edge];
		
		//initialize Maze
		int xc,yc,e;
		for(int yedge=0;yedge<edge;yedge++)
		{
			for(int xedge=0;xedge<edge;xedge++)
			{
				MazeData[yedge][xedge][0]=0;//initialize Maze Information
				
				MazeData[yedge][xedge][1]=0;//G
				MazeData[yedge][xedge][2]=0;//H
				MazeData[yedge][xedge][3]=0;//F
				
				//new Label
				MazeLabel[yedge][xedge]=new JLabel(MazeColor[0]);
				MazeDirection[yedge][xedge]=new JLabel(Direction[1]);
				MazeF[yedge][xedge]=new JLabel();
				MazeG[yedge][xedge]=new JLabel();
				MazeH[yedge][xedge]=new JLabel();
				
				//setbounds
				xc=(int)(xedge*1000/edge);
				yc=(int)(yedge*1000/edge);
				e=(int)(1000/edge);
				MazeLabel[yedge][xedge].setBounds(xc, yc, e, e);
				MazeDirection[yedge][xedge].setBounds(xc, yc, e, e);
				MazeF[yedge][xedge].setBounds(xc, yc,e, e);
				MazeG[yedge][xedge].setBounds(xc, yc, e, e);
				MazeH[yedge][xedge].setBounds(xc, yc, e, e);
				
				//set visible
				MazeDirection[yedge][xedge].setVisible(false);
				MazeF[yedge][xedge].setVisible(false);
				MazeG[yedge][xedge].setVisible(false);
				MazeH[yedge][xedge].setVisible(false);
				
				//add
				MazePane.add(MazeLabel[yedge][xedge]);
				MazeInfoPane.add(MazeDirection[yedge][xedge],new Integer(Integer.MAX_VALUE));
				MazeInfoPane.add(MazeF[yedge][xedge]);
				MazeInfoPane.add(MazeG[yedge][xedge]);
				MazeInfoPane.add(MazeH[yedge][xedge]);
				//--------Add Listener
				MazeLabel[yedge][xedge].addMouseListener(new MouseAdapter(){
					@Override
					public void mousePressed(MouseEvent e) {
						Saveable=true;//Going to Save this file
						
						JLabel label=(JLabel)e.getComponent();
						MazeDragged=true;
						//Information
						int x=label.getX();
						int y=label.getY();
						int yindex=(y*edge)/1000;
						int xindex=(x*edge)/1000;
						MazeData[yindex][xindex][0]=AddMode;
						System.out.println("("+xindex+","+yindex+")="+AddMode);
						//right click clear square
						if(e.getButton()==MouseEvent.BUTTON3)
						{
							label.setIcon(MazeColor[0]);
							ClearSquare=true;
							MazeData[yindex][xindex][0]=0;
							return;
						}
						
						
						label.setIcon(MazeColor[AddMode]);
					}
					@Override
					public void mouseReleased(MouseEvent e)
					{
						MazeDragged=false;
						ClearSquare=false;
					}
					@Override
					public void mouseEntered(MouseEvent e) {
						JLabel label=(JLabel)e.getComponent();
						//Information
						int x=label.getX();
						int y=label.getY();
						int yindex=(y*edge)/1000;
						int xindex=(x*edge)/1000;
						//Show Infomation of this Square
						if(ShowInfo)
						{
							LabelSquarePos.setVisible(true);
							LabelSquareStatus.setVisible(true);
							LabelSquareG.setVisible(true);
							LabelSquareH.setVisible(true);
							LabelSquareF.setVisible(true);
							
							LabelSquarePos.setText("("+(xindex+1)+","+(edge-yindex)+")");
							LabelSquareG.setText(String.valueOf(MazeData[yindex][xindex][1]));
							LabelSquareH.setText(String.valueOf(MazeData[yindex][xindex][2]));
							LabelSquareF.setText(String.valueOf(MazeData[yindex][xindex][3]));
							String status;
							switch(MazeData[yindex][xindex][0])
							{
							case 0:status="Blank";break;
							case 1:status="Start";break;
							case 2:status="End";break;
							case 3:status="Obstacle";break;
							case 4:status="Open";break;
							case 5:status="Close";break;
							default:status="";break;
							}
							LabelSquareStatus.setText(status);
							if(y<500)//Down
							{
								int xl,yl;
								xl=250+x+(int)500/edge-68;
								yl= y+(int)1000/edge;
								LabelSquareInfoDown.setVisible(true);
								LabelSquareInfoDown.setLocation(xl,yl);
								LabelSquarePos.setLocation(xl+20, yl+20);
								LabelSquareStatus.setLocation(xl+74,yl+64);
								LabelSquareG.setLocation(xl+82,yl+93);
								LabelSquareH.setLocation(xl+82,yl+123);
								LabelSquareF.setLocation(xl+82,yl+152);
								
							}
							else//Up
							{
								int xl,yl;
								xl=250+x-68+(int)500/edge;
								yl= y-188;
								LabelSquareInfoUp.setVisible(true);
								LabelSquareInfoUp.setLocation(xl,yl);
								LabelSquarePos.setLocation(xl+20, yl-5);
								LabelSquareStatus.setLocation(xl+74,yl+38);
								LabelSquareG.setLocation(xl+82,yl+66);
								LabelSquareH.setLocation(xl+82,yl+97);
								LabelSquareF.setLocation(xl+82,yl+127);
							}
						}
						//Not Dragged
						if(MazeDragged==false)
						{
							return;
						}
						MazeData[yindex][xindex][0]=AddMode;
						
						if(ClearSquare)//Clear Square
						{
							label.setIcon(MazeColor[0]);
							MazeData[yindex][xindex][0]=0;
							return;
						}
						label.setIcon(MazeColor[AddMode]);
					}
					@Override
					public void mouseExited(MouseEvent e) {
						if(ShowInfo)
						{
							LabelSquareInfoDown.setVisible(false);
							LabelSquareInfoUp.setVisible(false);
							LabelSquarePos.setVisible(false);
							LabelSquareStatus.setVisible(false);
							LabelSquareG.setVisible(false);
							LabelSquareH.setVisible(false);
							LabelSquareF.setVisible(false);
						}
					}
				});
			}
		}
		MazePane.repaint();
		MazeInfoPane.repaint();
	}
//check
	public boolean check()
	{
		//check point
		boolean start=false,end=false;
		for(int y=0;y<MazeEdge;y++)
		{
			for(int x=0;x<MazeEdge;x++)
			{
				if(MazeData[y][x][0]==1)
				{
					if(start==false)
					{
						start=true;
					}
					else
					{
						JOptionPane.showMessageDialog(null,  "Only One Start Point Can Exist", "Error!",JOptionPane.ERROR_MESSAGE);
						return false;
					}
				}
				else if(MazeData[y][x][0]==2)
				{
					if(end==false)
					{
						end=true;
					}
					else
					{
						JOptionPane.showMessageDialog(null,  "Only One End Point Can Exist", "Error!",JOptionPane.ERROR_MESSAGE);
						return false;
					}
				}
			}
		}
		if(start==false)
		{
			JOptionPane.showMessageDialog(null,  "No Start Point.", "Error!",JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if(end==false)
		{
			JOptionPane.showMessageDialog(null,  "No End Point.", "Error!",JOptionPane.ERROR_MESSAGE);
			return false;
		}
		//check Sleep
		try
		{
			Sleep=Long.parseLong(TextSleep.getText());
		}catch(NumberFormatException e)
		{
			JOptionPane.showMessageDialog(null,  "Sleep Error", "Error!",JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
//Import Map
	public void ImportMap(File map) throws IOException
	{
		System.out.println(map.getAbsolutePath());
		FileReader reader=new FileReader(map);
		BufferedReader readmap=new BufferedReader(reader);
		StreamTokenizer token=new StreamTokenizer(readmap);
		boolean eof=true;
		int size;
		if(token.nextToken()==StreamTokenizer.TT_NUMBER)
		{
			size=(int)token.nval;
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Import Error!");
			return;
		}
		//Set Size to match file's size
		if(size!=MazeEdge)//reset Maze
		{
			int response=JOptionPane.showOptionDialog(null, "Are you going to reset map to match "+size+"*"+size+" size?", "Size Dismatch", JOptionPane.DEFAULT_OPTION, JOptionPane.OK_CANCEL_OPTION, null,new String[]{"Match","Cancel"}, "OK");
			if(response!=0)//cancel
			{
				return;
			}
			MazeEdge=size;//Update MazeEdge
			ResetMazeLabel(size);
		}
		Map=map;//Set File Map
		LabelMapName.setText(Map.getName());
		//set label
		int mode=0;
		for(int y=0;y<MazeEdge&&eof;y++)
		{
			for(int x=0;x<MazeEdge&&eof;x++)
			{
				switch(token.nextToken())
				{
				case StreamTokenizer.TT_NUMBER:mode=(int)token.nval;break;
				case StreamTokenizer.TT_EOF:eof=false;break;
				default:JOptionPane.showMessageDialog(null,  "Error Map!", "Error!",JOptionPane.ERROR_MESSAGE);return;
				}
				//System.out.println("x="+x+" y="+y+" mode="+mode);
				MazeLabel[y][x].setIcon(MazeColor[mode]);
				MazeDirection[y][x].setVisible(false);
				MazeH[y][x].setVisible(false);
				MazeG[y][x].setVisible(false);
				MazeF[y][x].setVisible(false);
				MazeData[y][x][0]=mode;
				MazeData[y][x][1]=0;
				MazeData[y][x][2]=0;
				MazeData[y][x][3]=0;
				System.out.println("Finish eof="+eof);
			}
		}
		readmap.close();
	}
//Save Map
	public void SaveMap(String Address) throws IOException
	{
		File f=new File(Address+".maze");
		if(!f.exists())
		{
			f.createNewFile();
		}
		Map=f;//Set File Map
		LabelMapName.setText(Map.getName());
		FileWriter writer=new FileWriter(f);
		BufferedWriter file=new BufferedWriter(writer);
		writer.write(String.valueOf(MazeEdge));
		for(int y=0;y<MazeEdge;y++)
		{
			for(int x=0;x<MazeEdge;x++)
			{
				if(MazeData[y][x][0]>3)
				{
					writer.write(" ");
					writer.write(String.valueOf(0));
					continue;
				}
				writer.write(" ");
				writer.write(String.valueOf(MazeData[y][x][0]));
				//System.out.println("MazeData[y][x][0])="+MazeData[y][x][0]);
			}
		}
		writer.close();
	}
//SaveCurrentMap
	public void SaveCurrentMap() throws IOException
	{
		Saveable=false;
		if(Map==null)
		{
			//Save A New File
			JFileChooser MapSaver=new JFileChooser();
			MapSaver.setDialogTitle("Save");
			MapSaver.setDialogType(JFileChooser.SAVE_DIALOG);
			MapSaver.setCurrentDirectory(new File("E:\\My Project\\MazeMap"));
			MapSaver.setFileFilter(new FileFilter(){//Only Accept *.maze
	            @Override
	            public boolean accept(File pathname) {
	                // TODO Auto-generated method stub
	                return true;
	            }
				@Override
				public String getDescription() {
					// TODO Auto-generated method stub
					return ".maze";
				}
	        });
			MapSaver.showSaveDialog(Maze.this);
			File tempfile=MapSaver.getSelectedFile();
			if(tempfile==null)
			{
				return;
			}
			String address=tempfile.getAbsolutePath();
			if(tempfile.exists())//Update this File
			{
				int r=JOptionPane.showOptionDialog(null,  "File Exist!Do you want to Update this file?", "Warning!",JOptionPane.DEFAULT_OPTION,JOptionPane.OK_CANCEL_OPTION,null,new String[]{"OK","Cancel"},"OK");
				if(r!=0)//Cancel
				{
					return;
				}
				address=address.substring(0,address.length()-5);
				tempfile.delete();
			}
			try {
				SaveMap(address);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return;
		}
		FileWriter writer=new FileWriter(Map);
		BufferedWriter file=new BufferedWriter(writer);
		file.write(String.valueOf(MazeEdge));
		for(int y=0;y<MazeEdge;y++)
		{
			for(int x=0;x<MazeEdge;x++)
			{
				if(MazeData[y][x][0]>3)
				{
					file.write(" ");
					file.write(String.valueOf(0));
					continue;
				}
				file.write(" ");
				file.write(String.valueOf(MazeData[y][x][0]));
			}
		}
		file.close();
	}
//Clear All Maze Information
	public void ClearMazeInformation()
	{
		if(Map!=null)
		{
			Saveable=true;
		}
		//Clear All Maze
		for(int y=0;y<MazeEdge;y++)
		{
			for(int x=0;x<MazeEdge;x++)
			{
				MazeData[y][x][0]=0;
				MazeData[y][x][1]=0;
				MazeData[y][x][2]=0;
				MazeData[y][x][3]=0;
				MazeLabel[y][x].setIcon(MazeColor[0]);
				MazeDirection[y][x].setVisible(false);
				MazeH[y][x].setVisible(false);
				MazeG[y][x].setVisible(false);
				MazeF[y][x].setVisible(false);
			}
		}
		TextAreaData.setText("");
		MazePane.repaint();
		MazePathPane.repaint();
	}
//get method
	public ImageIcon getIconColor()
	{
		return MazeColor[AddMode];
	}
	public int getMazeEdge()
	{
		return MazeEdge;
	}
	public long getSleep()
	{
		return Sleep;
	}
	public JLabel[][] getMazeLabel()
	{
		return MazeLabel;
	}
	public int[][][] getMazeData()
	{
		return MazeData;
	}
	public ImageIcon[] getMazeColor()
	{
		return MazeColor;
	}
	public JPanel getMazePane()
	{
		return MazePane;
	}
	public JLabel[][] getMazeDirection()
	{
		return MazeDirection;
	}
	public JLabel[][] getMazeG()
	{
		return MazeG;
	}
	public JLabel[][] getMazeH()
	{
		return MazeH;
	}
	public JLabel[][] getMazeF()
	{
		return MazeF;
	}
	public ImageIcon[] getDirection()
	{
		return Direction;
	}
	public JPanel getInfoPane()
	{
		return MazeInfoPane;
	}
	public PathJPanel getPathPane()
	{
		return MazePathPane;
	}
	public JTextArea getDataArea()
	{
		return TextAreaData;
	}
	public JTextArea getResultArea()
	{
		return TextAreaResult;
	}
	public int[] getDirectionCost()
	{
		return DirectionCost;
	}
	public int getAHeuristicScale()
	{
		return SliderHeuristicScale.getValue();
	}
//set method
	public void setStartButton()
	{
		LabelPause.setVisible(false);
		ButtonPause.setVisible(false);
		
		LabelContinue.setVisible(false);
		ButtonContinue.setVisible(false);
		
		LabelStart.setVisible(true);
		ButtonStart.setVisible(true);
		
		MainJPanel.repaint();
	}
}

