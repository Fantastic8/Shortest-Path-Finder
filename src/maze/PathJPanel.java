package maze;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PathJPanel extends JPanel{
	private int[][][] Path;
	private Point Start;
	private Point End;
	private int size;
	private JLabel[][] MazeLabel;
	public boolean Draw;
	
	public PathJPanel()
	{
		super();
		Draw=false;
	}
	public void setPath(Maze maze,Point Start,Point End,int[][][] Path)
	{
		this.size=maze.getMazeEdge();
		this.Start=Start;
		this.End=End;
		this.Path=Path;
		this.MazeLabel=maze.getMazeLabel();
		Draw=true;
	}
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		if(!Draw)
		{
			return;
		}
		float lineWidth = 1.7f;
	    ((Graphics2D)g).setStroke(new BasicStroke(lineWidth));
	    ((Graphics2D)g).setColor(Color.WHITE);
		int x1,x2,y1,y2;
		x1=End.x;
		y1=End.y;
		while(true)
		{
			x2=Path[y1][x1][0];
			y2=Path[y1][x1][1];
			g.drawLine(MazeLabel[y1][x1].getX()+(500/size), MazeLabel[y1][x1].getY()+(500/size),MazeLabel[y2][x2].getX()+(500/size),MazeLabel[y2][x2].getY()+(500/size));
			x1=x2;
			y1=y2;
			if(x1==Start.x&&y1==Start.y)
			{
				break;
			}
		}
	}
	@Override
	public void repaint()
	{
		Draw=false;
		super.repaint();
	}
}
