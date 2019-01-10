package maze;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;

import javax.swing.*;

public class AddStyle {
	private JLabel returnlabel=null;
	private JButton returnbutton=null;
	private ImageIcon returnimageicon=null;
	
	public void AddBorderButton(final JPanel jp,final JFrame jf)
	{
		JButton ButtonExit = new JButton();//Close Button
		ButtonExit.setContentAreaFilled(false);
		ButtonExit.setBorderPainted(false);
		ButtonExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				int height=jp.getHeight();
//				int width=jp.getWidth();
//				for(int y=height;y>0;y-=5)//Close Effect
//				{
//					jf.setSize(width, y);
//				}
				System.exit(0);//System Close
			}
		});
		ButtonExit.setBounds(jf.getWidth()-32, 0, 30, 33);
		jp.add(ButtonExit);
		
		JButton ButtonMinimize = new JButton();//Minimize Button
		ButtonMinimize.setContentAreaFilled(false);
		ButtonMinimize.setBorderPainted(false);
		ButtonMinimize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jf.setExtendedState(jf.ICONIFIED);//Minimize
			}
		});
		ButtonMinimize.setBounds(jf.getWidth()-64, 0, 30, 33);
		jp.add(ButtonMinimize);
	}
	public void AddMyButton(JPanel jp,String imageaddress,int x,int y)
	{
		returnimageicon=new ImageIcon(imageaddress);//Load Image
		returnlabel=new JLabel(returnimageicon);//Load Label
		returnlabel.setBounds(x, y, returnimageicon.getIconWidth(), returnimageicon.getIconHeight());
		jp.add(returnlabel);
		returnbutton=new JButton();//Load Transparent Button
		returnbutton.setContentAreaFilled(false);
		returnbutton.setBorderPainted(false);
		returnbutton.setBounds(x, y, returnimageicon.getIconWidth(), returnimageicon.getIconHeight());
		jp.add(returnbutton);
	}
	public JLabel getLabel()
	{
		return returnlabel;
	}
	public JButton getbutton()
	{
		return returnbutton;
	}
	public ImageIcon getimageicon()
	{
		return returnimageicon;
	}
}
