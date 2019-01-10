package maze;

import java.awt.Point;
import java.text.DecimalFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class Dijstra extends AlgorithmController {
	private int size;
	private int MazeData[][][];
	private long sleep;
	private Maze maze;
	private int Open[][];
	private int Close[][];
	private int Path[][][];
	private int OpenTail;
	private int CloseTail;
	private int CalculatedNumber;
	private int StartX,StartY,EndX,EndY;
	private boolean Find;
	Dijstra(Maze maze)
	{
		this.size=maze.getMazeEdge();
		this.MazeData=maze.getMazeData();
		this.sleep=maze.getSleep();
		
		this.maze=maze;
		
		//Initial setting
		Open=new int[size*size][2];
		Close=new int[size*size][2];
		Path=new int[size][size][2];//Contains point[x,y]'s parent point[x1,y1]
		OpenTail=0;
		CloseTail=0;
		CalculatedNumber=0;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Date starttime=new Date(),endtime=starttime;
		try {
			if(Algorithm())//Find
			{
				endtime=new Date();
				maze.getPathPane().setPath(maze,new Point(StartX,StartY),new Point(EndX,EndY),Path);//Track Path
			}
			else//Path Not Find
			{
				if(IsStop)
				{
					maze.setStartButton();
					return;
				}
				endtime=new Date();
				JOptionPane.showMessageDialog(null,  "Path Not Found", "Warning!",JOptionPane.ERROR_MESSAGE);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Result
		JTextArea Result=maze.getResultArea();
		double during=endtime.getTime()-starttime.getTime();
		long pathnodes=getPathNodes();
		long moveablenodes=0;
		if(Find)
		{
			Result.append("              Find Path\r\n");
		}
		else
		{
			Result.append("          Path Not Found\r\n");
		}
		if(during/1000>100)
		{
			Result.append("Time:"+during/1000+"s\r\n");
		}
		else
		{
			Result.append("Time:"+during+"ms\r\n");
		}
		if(Find)
		{
			Result.append("Path Length:"+maze.getMazeData()[EndY][EndX][1]+"\r\n");
			Result.append("Path Nodes:"+pathnodes+"\r\n");
		}
		Result.append("Calculated Nodes:"+CalculatedNumber+"\r\n");
		for(int y=0;y<size;y++)
		{
			for(int x=0;x<size;x++)
			{
				if(MazeData[y][x][0]!=3)
				{
					moveablenodes++;
				}
			}
		}
		moveablenodes--;
		Result.append("Moveable Nodes:"+moveablenodes+"\r\n");
		if(Find)
		{
			Result.append("Path Ratio:"+new DecimalFormat("#.00000").format((100*(double)pathnodes/(double)CalculatedNumber))+"%\r\n");
		}
		Result.append("Calculated Ratio:"+new DecimalFormat("#.00000").format((100*(double)CalculatedNumber/(double)moveablenodes))+"%\r\n");
		
		maze.setStartButton();
	}
	boolean Algorithm() throws InterruptedException
	{
		//Add Start Point to Open Array
		for(int y=0;y<size;y++)
		{
			for(int x=0;x<size;x++)
			{
				//Initial Path
				Path[y][x][0]=x;
				Path[y][x][1]=y;
				if(MazeData[y][x][0]==1)//Start Point
				{
					//Open Array:0-x,1-y
					Open[OpenTail][0]=x;
					Open[OpenTail][1]=y;
					OpenTail++;
					StartX=x;
					StartY=y;
				}
				else if(MazeData[y][x][0]==2)//End Point
				{
					EndX=x;
					EndY=y;
				}
			}
		}
		//Execute
		int[] Data;
		int Xdata;
		int Ydata;
		boolean Left,Right,Up,Down;
		
		Data=getMinFromOpen();
		Xdata=Data[0];
		Ydata=Data[1];
		//Add Blank Adjacent Square to Open List
		Up=IsBlank(Xdata,Ydata-1,Xdata,Ydata,true);
		Left=IsBlank(Xdata-1,Ydata,Xdata,Ydata,true);
		Right=IsBlank(Xdata+1,Ydata,Xdata,Ydata,true);
		Down=IsBlank(Xdata,Ydata+1,Xdata,Ydata,true);
		if(Up)//top middle
		{
			AddToOpen(Xdata,Ydata-1,Xdata,Ydata);
		}
		if(Left)//middle left
		{
			AddToOpen(Xdata-1,Ydata,Xdata,Ydata);
		}
		if(Right)//middle right
		{
			AddToOpen(Xdata+1,Ydata,Xdata,Ydata);
		}
		if(Down)//bottom middle
		{
			AddToOpen(Xdata,Ydata+1,Xdata,Ydata);
		}
		//Four Corner
		if(IsBlank(Xdata-1,Ydata+1,Xdata,Ydata,(Down&&Left))&&(Down||MazeData[Ydata+1][Xdata][0]!=3)&&(Left||MazeData[Ydata][Xdata-1][0]!=3))//bottom left
		{
			AddToOpen(Xdata-1,Ydata+1,Xdata,Ydata);
		}
		if(IsBlank(Xdata+1,Ydata+1,Xdata,Ydata,(Down&&Right))&&(Down||MazeData[Ydata+1][Xdata][0]!=3)&&(Right||MazeData[Ydata][Xdata+1][0]!=3))//bottom right
		{
			AddToOpen(Xdata+1,Ydata+1,Xdata,Ydata);
		}
		if(IsBlank(Xdata-1,Ydata-1,Xdata,Ydata,(Up&&Left))&&(Up||MazeData[Ydata-1][Xdata][0]!=3)&&(Left||MazeData[Ydata][Xdata-1][0]!=3))//top left
		{
			AddToOpen(Xdata-1,Ydata-1,Xdata,Ydata);
		}
		if(IsBlank(Xdata+1,Ydata-1,Xdata,Ydata,(Up&&Right))&&(Up||MazeData[Ydata-1][Xdata][0]!=3)&&(Right||MazeData[Ydata][Xdata+1][0]!=3))//top right
		{
			AddToOpen(Xdata+1,Ydata-1,Xdata,Ydata);
		}
		
		while(OpenTail>0&&Find==false)
		{
			while(IsPause)//Pause
			{
				Thread.sleep(10);
				if(IsStop)
				{
					return false;
				}
			}
			
			Data=getMinFromOpen();
			Xdata=Data[0];
			Ydata=Data[1];
			//System.out.println("Get Min From Open:("+Xdata+","+Ydata+") F="+MazeData[Ydata][Xdata][3]);
			
			//Add Blank Adjacent Square to Open List
			Up=IsBlank(Xdata,Ydata-1,Xdata,Ydata,true);
			Left=IsBlank(Xdata-1,Ydata,Xdata,Ydata,true);
			Right=IsBlank(Xdata+1,Ydata,Xdata,Ydata,true);
			Down=IsBlank(Xdata,Ydata+1,Xdata,Ydata,true);
			if(Up)//top middle
			{
				AddToOpen(Xdata,Ydata-1,Xdata,Ydata);
			}
			if(Left)//middle left
			{
				AddToOpen(Xdata-1,Ydata,Xdata,Ydata);
			}
			if(Right)//middle right
			{
				AddToOpen(Xdata+1,Ydata,Xdata,Ydata);
			}
			if(Down)//bottom middle
			{
				AddToOpen(Xdata,Ydata+1,Xdata,Ydata);
			}
			//Four Corner
			if(IsBlank(Xdata-1,Ydata+1,Xdata,Ydata,(Down&&Left))&&(Down||MazeData[Ydata+1][Xdata][0]!=3)&&(Left||MazeData[Ydata][Xdata-1][0]!=3))//bottom left
			{
				AddToOpen(Xdata-1,Ydata+1,Xdata,Ydata);
			}
			if(IsBlank(Xdata+1,Ydata+1,Xdata,Ydata,(Down&&Right))&&(Down||MazeData[Ydata+1][Xdata][0]!=3)&&(Right||MazeData[Ydata][Xdata+1][0]!=3))//bottom right
			{
				AddToOpen(Xdata+1,Ydata+1,Xdata,Ydata);
			}
			if(IsBlank(Xdata-1,Ydata-1,Xdata,Ydata,(Up&&Left))&&(Up||MazeData[Ydata-1][Xdata][0]!=3)&&(Left||MazeData[Ydata][Xdata-1][0]!=3))//top left
			{
				AddToOpen(Xdata-1,Ydata-1,Xdata,Ydata);
			}
			if(IsBlank(Xdata+1,Ydata-1,Xdata,Ydata,(Up&&Right))&&(Up||MazeData[Ydata-1][Xdata][0]!=3)&&(Right||MazeData[Ydata][Xdata+1][0]!=3))//top right
			{
				AddToOpen(Xdata+1,Ydata-1,Xdata,Ydata);
			}
			//Add this Square to Close List
			AddToClose(Xdata,Ydata);
		}
		if(Find==false)
		{
			return false;
		}
		return true;
	}
//get method
	public int[] getMinFromOpen()
	{
		int[] Data=new int[2];
		Data[0]=Open[OpenTail-1][0];
		Data[1]=Open[OpenTail-1][1];
		OpenTail--;
		return Data;
	}
	public int getG(int x,int y,int parentx,int parenty)
	{
		int shift=0;
		if(x!=parentx&&y!=parenty)
		{
			shift=14;
		}
		else if(x==parentx&&y==parenty)
		{
			shift=0;
		}
		else
		{
			shift=10;
		}
		return MazeData[parenty][parentx][1]+shift;
	}
//@get method
//add method
	void AddToOpen(int x,int y,int parentx,int parenty) throws InterruptedException
	{
		CalculatedNumber++;
		Thread.sleep(sleep);
		if(x==EndX&&y==EndY)
		{
			MazeData[y][x][1]=getG(x,y,parentx,parenty);
			MazeData[y][x][3]=MazeData[y][x][1];
			Path[y][x][0]=parentx;
			Path[y][x][1]=parenty;
			Find=true;
			return;
		}
		//Data
		//Insert Sort-Descending Sort
		int G=getG(x,y,parentx,parenty);
		int F=G;
		//System.out.println("("+x+","+y+"))'s parent ("+parentx+","+parenty+") F:"+F);
		int index;
		for(index=OpenTail-1;index>=0;index--)
		{
			if(F<MazeData[Open[index][1]][Open[index][0]][3])
			{
				break;
			}
		}
		index++;
		//insert into pos=index;
		//Move
		for(int i=OpenTail-1;i>=index;i--)
		{
			Open[i+1][0]=Open[i][0];
			Open[i+1][1]=Open[i][1];
		}
		Open[index][0]=x;
		Open[index][1]=y;
		
		OpenTail++;
		MazeData[y][x][0]=4;
		MazeData[y][x][1]=G;
		MazeData[y][x][3]=F;
		
		Path[y][x][0]=parentx;
		Path[y][x][1]=parenty;
		
		new Thread(new SetPathIcon(maze,x,y,parentx,parenty,4)).start();
	}
	public void AddToClose(int x,int y) throws InterruptedException
	{
		Thread.sleep(sleep);
		//Data
		Close[CloseTail][0]=x;
		Close[CloseTail][0]=y;
		MazeData[y][x][0]=5;
		//Image
		new Thread(new SetPathIcon(maze,x,y,Path[y][x][0],Path[y][x][1],5)).start();
	}
//@add method
//Is method
	public boolean IsBlank(int x,int y,int Px,int Py,boolean Updateable)
	{
		if(x==EndX&&y==EndY)
		{
			return true;
		}
		if(x<0||x>=size||y<0||y>=size)
		{
			return false;
		}
		if(MazeData[y][x][0]==4)//Open List-Update
		{
			if(!Updateable)
			{
				return false;//a corner can't be reached
			}
			int G=getG(x,y,Px,Py);//New
			int F=G;//New F
			if(MazeData[y][x][3]>F)//Update
			{
				Path[y][x][0]=Px;
				Path[y][x][1]=Py;
				MazeData[y][x][1]=G;
				MazeData[y][x][3]=F;
			}
			return false;
		}
		else if(MazeData[y][x][0]!=0)
		{
			return false;
		}
		return true;
	}
//@Is method
	public long getPathNodes()
	{
		if(Find==false)
		{
			return 0;
		}
		long number=0;
		int x1,x2,y1,y2;
		x1=EndX;
		y1=EndY;
		while(true)
		{
			x2=Path[y1][x1][0];
			y2=Path[y1][x1][1];
			number++;
			if(x2==StartX&&y2==StartY)
			{
				return number;
			}
			x1=x2;
			y1=y2;
		}
	}
}
