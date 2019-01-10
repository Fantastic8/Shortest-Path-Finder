package maze;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class SetPathIcon implements Runnable {
	//ImageIcon
	private ImageIcon[] Color;
	private ImageIcon[] Direction;
	//JLabel
	private JLabel[][] MazeLabel;
	private JLabel[][] MazeDirection;
	private JLabel[][] MazeH;
	private JLabel[][] MazeG;
	private JLabel[][] MazeF;
	//Jpanel
	private JPanel MazePane;
	private JPanel InfoPane;
	//int
	private int[][][] MazeData;
	private int PositionX;
	private int PositionY;
	private int ParentPositionX;
	private int ParentPositionY;
	private int PositionMode;
	private int Size;
	//TextArea
	private JTextArea DataArea;
	public SetPathIcon(Maze maze,int x,int y,int parentx,int parenty,int mode)
	{
		Color=maze.getMazeColor();
		Direction=maze.getDirection();
		MazeLabel=maze.getMazeLabel();
		MazeDirection=maze.getMazeDirection();
		MazeH=maze.getMazeH();
		MazeG=maze.getMazeG();
		MazeF=maze.getMazeF();
		MazeData=maze.getMazeData();
		MazePane=maze.getMazePane();
		InfoPane=maze.getInfoPane();
		
		Size=maze.getMazeEdge();
		PositionX=x;
		PositionY=y;
		ParentPositionX=parentx;
		ParentPositionY=parenty;
		PositionMode=mode;
		
		DataArea=maze.getDataArea();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		MazeLabel[PositionY][PositionX].setIcon(Color[PositionMode]);
		MazeDirection[PositionY][PositionX].setIcon(getDirectionIcon(PositionX,PositionY,ParentPositionX,ParentPositionY));
		MazeDirection[PositionY][PositionX].setVisible(true);
		//TextArea
		DataArea.append("("+(PositionX+1)+","+(Size-PositionY)+")");
		switch(PositionMode)
		{
		case 4:DataArea.append(" Add to Open List\r\n");break;
		case 5:DataArea.append(" Add to Close List\r\n");break;
		default:DataArea.append("\r\n");break;
		}
		DataArea.append("G:"+MazeData[PositionY][PositionX][1]+" H:"+MazeData[PositionY][PositionX][2]+" F:"+MazeData[PositionY][PositionX][3]+"\r\n");
		DataArea.setCaretPosition(DataArea.getText().length());
		
		InfoPane.repaint();
		//InfoPane.revalidate();
		MazePane.repaint();
		//MazePane.revalidate();
	}
	public ImageIcon getDirectionIcon(int x,int y,int parentx,int parenty)
	{
		int xshift,yshift;
		xshift=parentx-x;
		yshift=parenty-y;
		if(xshift==-1)
		{
			if(yshift==-1)
			{
				return Direction[0];
			}
			else if(yshift==0)
			{
				return Direction[7];
			}
			else
			{
				return Direction[6];
			}
		}
		else if(xshift==0)
		{
			if(yshift==-1)
			{
				return Direction[1];
			}
			else if(yshift==0)
			{
				return null;
			}
			else
			{
				return Direction[5];
			}
		}
		else
		{
			if(yshift==-1)
			{
				return Direction[2];
			}
			else if(yshift==0)
			{
				return Direction[3];
			}
			else
			{
				return Direction[4];
			}
		}
	}
}
