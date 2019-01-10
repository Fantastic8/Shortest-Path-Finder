package maze;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InvisibleInfoLabel implements Runnable {
	//JLabel
	private JLabel[][] MazeDirection;
	private JLabel[][] MazeH;
	private JLabel[][] MazeG;
	private JLabel[][] MazeF;
	//Jpanel
	private JPanel MazePane;
	private JPanel InfoPane;
	//int
	private int size;
	
	public InvisibleInfoLabel(Maze maze)
	{
		this.size=maze.getMazeEdge();
		this.InfoPane=maze.getInfoPane();
		this.MazePane=maze.getMazePane();
		this.MazeDirection=maze.getMazeDirection();
		this.MazeH=maze.getMazeH();
		this.MazeG=maze.getMazeG();
		this.MazeF=maze.getMazeF();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for(int y=0;y<size;y++)
		{
			for(int x=0;x<size;x++)
			{
				MazeDirection[y][x].setVisible(false);
				MazeG[y][x].setVisible(false);
				MazeH[y][x].setVisible(false);
				MazeF[y][x].setVisible(false);
			}
		}
		MazePane.repaint();
		InfoPane.repaint();
	}
}
