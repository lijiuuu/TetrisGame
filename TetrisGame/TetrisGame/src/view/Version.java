package view;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
public class Version extends JDialog{
	JLabel jl1=new JLabel("太原理工大学 软件工程系 ");
	JLabel jl2=new JLabel("作者：学号 2013006273 周珈萱");
	JLabel jl3=new JLabel("俄罗斯方块1.0");
	JPanel jp=new JPanel();
//	JTextField jt=new JTextField("111");
	public Version(JFrame j,String s,boolean b){
		super(j,s,b);
//		this.setLayout(null);
//		this.setLayout();
		this.setBounds(400, 120, 200, 200);
		this.setVisible(true);
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		jp.setLayout(new GridLayout(3, 1));
//		jt.setBounds(50, 50, 30, 30);
//		jl.setBounds(50, 50, 30, 30);
//		jp.setBounds(0, 0, 200, 300);
			jp.add(jl1);
			jp.add(jl2);
			jp.add(jl3);
		this.add(jp);
	}
}
