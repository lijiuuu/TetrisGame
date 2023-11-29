package view;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.*;

import util.Constant;


import model.Block;

/**
 * �Զ�����
 *
 */
public class Zidingyi extends JDialog implements ActionListener{
	JPanel jp_sudu=new JPanel();
	JPanel jp_rank=new JPanel();
	JPanel jp_she=new JPanel();
	JPanel jp_b=new JPanel();
	JRadioButton jr_rank1=new JRadioButton("����");
	JRadioButton jr_rank2=new JRadioButton("�м�");
	JRadioButton jr_rank3=new JRadioButton("�߼�");
	JSlider jsl=new JSlider(1,10);//������1-10
	int newspeed;
	Block block;
	GameCanvas gc;
	ButtonGroup bg_rank=new ButtonGroup();	
	JLabel tj_sudu=new JLabel("�����ٶ�");
	JCheckBox jc1=new JCheckBox("�����Ƿ��Զ�����");
	JCheckBox jc2=new JCheckBox("��Ϸ�����Ƿ񲥷�����");
	JCheckBox jc3=new JCheckBox("��������");
	ImageIcon image=new ImageIcon(Constant.backGround1);
	JLabel label=new JLabel(image);
//	JPanel panel=new JPanel();
	
	public Zidingyi(JFrame j,String s,boolean a,Block block,GameCanvas gc){
		super(j,s,a);
		this.setBounds(0,170, 480, 350);
		this.setVisible(false);
		this.setResizable(false);
		this.setLayout(null);
		this.block=block;
		this.gc=gc;
		label.setBounds(0, 0, this.getWidth(), this.getHeight());
//		panel.add(label);
		
//		this.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
//		JPanel imagePanel = (JPanel) this.getContentPane();
//		imagePanel.setOpaque(false);
//		panel.setBounds(0, 0, 480, 350);
//		this.add(panel);
		add1();
		add2();
		add3();
		add4();
	}

	/**
	 * �����ٶ����
	 */
	public void add1(){
		jp_sudu.setBounds(0, 40, 500, 80);
		jp_sudu.add(tj_sudu);	
		tj_sudu.setFont(new Font("�����п�", Font.BOLD, 15));
		jsl.setValue(11-Constant.step);
		newspeed=11-jsl.getValue();
		//��ȡ������ֵ
		jsl.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent ce) {
				// TODO Auto-generated method stub
				newspeed=11-jsl.getValue();
//				System.out.println(a);
				
			}
			
		});
//		jsl.setBounds(60, 0, 150, 30);
		jsl.setMajorTickSpacing(9);
		jsl.setMinorTickSpacing(1);
		jsl.setPaintTicks(true);
		jsl.setPaintLabels(true);
		jp_sudu.add(jsl);
		JLabel tj_sudu1=new JLabel("�ٶ�1��10���μӿ�");
		tj_sudu1.setFont(new Font("�����п�", Font.BOLD, 15));
		jp_sudu.add(tj_sudu1);
		this.add(jp_sudu);
}
	/**
	 * ���õȼ����
	 */
	public void add2(){
		JPanel jp_rank=new JPanel();
		jp_rank.setBounds(90, 140, 300, 30);
		JLabel tj_shape=new JLabel("������״");
		tj_shape.setFont(new Font("�����п�", Font.BOLD, 15));
		
		int i=Block.get_addl();
		
		jr_rank1.addActionListener(this);
		jr_rank1.setActionCommand("����");

	
		jr_rank2.addActionListener(this);
		jr_rank2.setActionCommand("�м�");

		
		jr_rank3.addActionListener(this);
		jr_rank3.setActionCommand("�߼�");
			
		if(i==7)
			jr_rank1.setSelected(true);
		else if(i==10)
			jr_rank2.setSelected(true);
		else
			jr_rank3.setSelected(true);
		bg_rank.add(jr_rank1);
		bg_rank.add(jr_rank2);
		bg_rank.add(jr_rank3);
		jp_rank.add(tj_shape);
		jp_rank.add(jr_rank1);
		jp_rank.add(jr_rank2);
		jp_rank.add(jr_rank3);
		

		jr_rank1.setBounds(0, 0, 80, 30);
		jr_rank2.setBounds(90, 0, 80, 30);
		jr_rank3.setBounds(180, 0, 80, 30);
		this.add(jp_rank);
		
		}
	/**
	 * ���ӹ������
	 */
	public void add3(){
		JPanel jp_she=new JPanel();
		jp_she.setBounds(20, 190, 400, 30);
//		JCheckBox jc1=new JCheckBox("�����Ƿ��Զ�����");
		jc1.addActionListener(this);
		if(MyFrame.high==true)
			jc1.setSelected(true);
		else
			jc1.setSelected(false);
		jc1.setBounds(0, 0, 160, 30);
		jp_she.add(jc1);

		jc2.addActionListener(this);
//		jc2.setBounds(170, 0, 180, 30);
		jp_she.add(jc2);
		if(MyFrame.isMusic==true)
			jc2.setSelected(true);
		else
			jc2.setSelected(false);
		if(MyFrame.changeBack==true)
			jc3.setSelected(true);
		else
			jc3.setSelected(false);
		jc3.addActionListener(this);
		jp_she.add(jc3);
		this.add(jp_she);
	}
	/**
	 * ȷ����ȡ����ť
	 */
	public void add4(){
		JPanel jp_b=new JPanel();
		jp_b.setBounds(100, 240, 300, 40);
		JButton jb_y=new JButton("ȷ��");
		jb_y.addActionListener(this);
		jp_b.add(jb_y);
		JButton jb_n=new JButton("ȡ��");
		jb_n.addActionListener(this);

		jp_b.add(jb_n);
		JPanel jp_zi=new JPanel();
		  jp_zi.setLayout(null);
		  jp_zi.setBounds(0, 0, this.getWidth(), this.getHeight());
		  jp_zi.add(jp_sudu);
		  jp_zi.add(jp_rank);
		  jp_zi.add(jp_she);
		  jp_zi.add(jp_b);
		  this.add(jp_zi);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() instanceof JButton) {
			int i=Block.get_addl();
			String buttonCommand = e.getActionCommand();//��ȡ��Ϣ
			if (buttonCommand.equals("ȷ��")) {
				String actionCommand = bg_rank.getSelection().getActionCommand();
				if (actionCommand.equals("����") ) {
					Block.set_addl(7);
					
				} else if (actionCommand.equals("�м�")) {
					Block.set_addl(10);

				} else if (actionCommand.equals("�߼�")) {
					Block.set_addl(13);
				}
				if (jc1.isSelected()) {
					MyFrame.high=true;
				}else 
					MyFrame.high=false;
				if (!jc2.isSelected()) {
					MyFrame.isMusic=false;
				}else
					MyFrame.isMusic=true;
				if(jc3.isSelected()){
					MyFrame.changeBack=true;
					MyFrame.background = new ImageIcon(Constant.backGround2);
					MyFrame.setBackGround();}
				else
					{MyFrame.changeBack=false;
					MyFrame.background = new ImageIcon(Constant.backGround1);
				MyFrame.setBackGround();}
				Constant.step=newspeed;
				jsl.setValue(11-Constant.step);
				MyFrame.rank=11-Constant.step;
				MyFrame.jt10.setText("�ȼ���"+MyFrame.rank);
				this.dispose();
//				this.setVisible(false);
			} else if (buttonCommand.equals("ȡ��")) {
				// ��ͣ��ť
				Block.set_addl(i);
				if(i==7)
					jr_rank1.setSelected(true);
				else if(i==10)
					jr_rank2.setSelected(true);
				else
					jr_rank3.setSelected(true);
				this.dispose();
//				this.setVisible(true);
				
				
			}

		}
	}}