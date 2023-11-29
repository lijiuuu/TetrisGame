package view;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.Constant;
//import javax.swing.Timer;
import java.util.TimerTask;
import java.util.Timer;
import javax.management.*;
import control.MusicPlayer;
import control.PreView;

import model.Block;
import model.Box;


import java.awt.*;
import java.awt.event.*;

public class MyFrame extends JFrame{
	JPanel jp_pan=new JPanel();
	JPanel jp_pre=new JPanel();
	JPanel jp_ctrl=new JPanel();
	JPanel jp_scor=new JPanel();
	Zidingyi zi;
	JRadioButtonMenuItem jr1=new JRadioButtonMenuItem("����",true);
	JRadioButtonMenuItem jr2=new JRadioButtonMenuItem("�м�");
	JRadioButtonMenuItem jr3=new JRadioButtonMenuItem("�߼�");
	JLabel jt9=new JLabel("�÷֣�0" );
	static JLabel jt10=new JLabel("�ȼ���1" );
	JMenu m1=new JMenu("��Ϸ");
	JMenu m2=new JMenu("����");
	JCheckBox jc1;
	JSlider jsl;
//	Dialog dia;//�����Ի���
	static ImageIcon  background = new ImageIcon(Constant.backGround1);
	// �ѱ���ͼƬ�ӵ�label
	static JLabel label = new JLabel(background);
//	Dialog dia=new Dialog(this, "�Զ���", false);
	int scor=0;//��ʼ������Ϊ0
	static int rank=0;//��ʼ���ȼ�Ϊ0
	int highC=0;
	boolean upspeed=false;
	boolean isTime=true;
	boolean runstop;
	static boolean isRank=false;
	static boolean changeBack=false;
	public static boolean playing=false;
	static boolean isMusic=true;
	static boolean high=false;
	PreView pv=new PreView();
	JMenuItem ji1=new JMenuItem("����");
	GameCanvas gc=new GameCanvas(20, 12);//����20��12��
	private Block block=new Block();//��ǰ��
	private int newspeed=1000;//Ĭ�ϵ�ǰ�ȼ�Ϊ1
	MusicPlayer mp=new MusicPlayer();
	Timer time=new Timer();
	MyTask mytask;
	int temp=1;
//	��Ϸ�����캯��
	public MyFrame(String str){
		super(str);
		this.setSize(450, 570);
		Dimension scrSize = 
	               Toolkit.getDefaultToolkit().getScreenSize();//��ȡ��Ļ�ߴ�
		setLocation((scrSize.width - getSize().width) / 2,
		        (scrSize.height - getSize().height) / 2);//������Ļ����
		this.setLayout(null);
		//label�Ĵ�СΪjframe�Ĵ�С
		label.setBounds(0, 0, this.getWidth(), this.getHeight());
		//��label�ӵ�jframe����ײ㣬��jframe�ϵ��Ǹ�panel������
		this.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
		//label��jframe�ϵ��Ǹ�panel�����棬����Ҫ���Ǹ�panel��Ϊ͸���ģ���Ȼ�͸�ס������
		JPanel imagePanel = (JPanel) this.getContentPane();
		imagePanel.setOpaque(false);
		addMenu();
		//��Ϸ��ʼ��ť
		ji1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// �����Ϸ�Ѿ���ʼ���ٰ�һ�μ���ʾ��Ϸ����
					
			
				if (playing == true) {
					ji1.setText("����");
					if(isMusic==true)
					{mp.playStart();}
					gc.setGameOver(true);
					gc.repaint();
					MyFrame.rank=11-Constant.step;
					MyFrame.jt10.setText("�ȼ���"+MyFrame.rank);
					 runstop=true;
					 block.isAlive=false;
					 block=new Block();
					 mytask.cancel();

					playing = false;
				} else {
					reset();
					if(isMusic==true)
					{mp.playStart();}
					MyFrame.rank=11-Constant.step;
					MyFrame.jt10.setText("�ȼ���"+MyFrame.rank);
					ji1.setText("������Ϸ");
					playing = true;
					mytask=new MyTask();
					time.schedule(mytask, 0, 100);//100����ִ��һ��
					Thread thread = new Thread(new play());// ���ÿ�ʼ��Ϸ�ķ���
					thread.start();

				}
			};
		});
		this.add(gc);//�����Ϸ����
		
		addRight();//����ұ�
		this.setFocusable(true);//���ÿɻ�ý���
		this.requestFocus();//���ý���
		this.addKeyListener(new MyListener());
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
public static void setBackGround(){
	label.setIcon(background);
}
	/**
	 * ��ʱ���䣬�ü�����ʽ��ʵ���ٶȵĸı�
	 * 
	 */
	private class MyTask extends TimerTask {

		@Override
		public void run() {
			temp++;
			if (temp % Constant.step == 0) {
				block.newL = block.x;
				block.newH = block.y + 1;
//				block.yy();
				if (block.pausing == true)
					return;
				if (high == true) {
					block.earse();
					highC++;
					if (highC == 4) {
						gc.addRow();
						highC = 0;
					}
				}
				if (block.isMoveAble(block.newH, block.newL)) {
					block.earse();
					block.y++;
					block.display();
					gc.repaint();

				} else {
					block.isAlive = false;
					gc.repaint();
					// cancel();
				} // ȡ����ʱ������
				temp = 1;
			}
		
		}
	}

	private class play implements Runnable {
		public void run() {
/*			if(killThread==true)
				return;*/
			int col = (int) (Math.random() * (gc.getCols() - 3));//�漴λ��������
			int style = Constant.STYLES[(int) (Math.random() * Block.get_addl())][(int) (Math
					.random() * 4)];
			while (playing) {
				if (block != null) {
					//�жϵ�ǰ�����Ƿ�����
					if (block.isAlive) {
						try {
							Thread.currentThread().sleep(100);
						} catch (InterruptedException ie) {
							ie.printStackTrace();
						}
						continue;
					}
				}
				isFullLine();
//				if(upspeed)//�ж��Ƿ���٣��ǵĻ�����е���
//				upLevel();
				if(isGameOver()){
					if(isMusic==true)
					{mp.playGameOver();}
					gc.setGameOver(true);
					gc.repaint();
					ji1.setText("����");
					mytask.cancel();
					playing=false;
					return;
					
				}
				block = new Block(style, -1, col, gc);
				block.jixu();//��ֹ����ͣʱ�����¿�ʼ��Ϸ����ͣ�ֻ�����ʧ
				gc.repaint();// �����������ķ�����ʾ����
				block.isAlive = true;
				col = (int) (Math.random() * (gc.getCols() - 3));//�漴λ��������
				style = Constant.STYLES[(int) (Math.random() * Block.get_addl())][(int) (Math
						.random() * 4)];
				pv.setStyle(style);
			}
		}
		/**
		 * �����ٶ�
		 */
		private void upLevel() {
			if(Constant.step-1<1){
				return;}
			Constant.step=Constant.step-1;	//�ٶ�����һ��
				rank++;					
				jt10.setText("�ȼ���"+rank);
				
				upspeed=false;//����־λ��Ϊfalse		
			
		}

		/**
		 * �ж��Ƿ����У�������������з�����
		 */
		private void isFullLine() {
			// TODO Auto-generated method stub

			for (int i = 0; i < 20; i++) {
				int row = 0;
				boolean flag = true;
				for (int j = 0; j < 12; j++) {
					if (!gc.getBox(i, j).isColorBox()) {
						flag = false;
						break;
					}
				}

				if (flag == true) {
					row = i;
					gc.delete(row);//ɾ����
					if(isMusic==true)
					{mp.playEraseSound();}
					addScor();//���ӷ���
					if(scor%10==0)//����Ϊ10������һ���ȼ�
					upspeed=true;//���ٶ����ӱ�־λ��Ϊtrue
					if(upspeed==true)
						upLevel();
				}
			}

		}

		/**
		 * �÷ֵļ��㷽��
		 */
		private void addScor() {
			scor=scor+10;
			jt9.setText("�÷֣�"+scor);
		}
	
	}
	/**
	 * �ж�����Ƿ��б�ռ��,��Ϸ�Ƿ����
	 */
	private boolean isGameOver() {
		for (int i = 0; i < 12; i++) {
			Box box = gc.getBox(0, i);
			if (box.isColorBox())
				return true;
			
		}return false;
	}

	private void reset() {
		scor=0;
		rank=0;
		jt10.setText("�ȼ���"+rank);
		jt9.setText("�÷֣�"+scor);
		upspeed=false;
		playing=true;
		runstop=false;
//		block.pausing=false;
//		isTime=true;
//		block=new Block();
//		block.isAlive=false;
		gc.setGameOver(false);
		gc.repaint();
		gc.reset();
	}
/*	private class MenuKeyListener extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			int i = e.getKeyCode();
			switch (i) {
			case KeyEvent.VK_C:
				System.out.println("111");;
				break;
			case KeyEvent.VK_DOWN:
				block.moveDown();
				break;
			case KeyEvent.VK_LEFT:
				block.moveLeft();
				break;
		}
	}
		}*/
	/**
	 * 
	 *�����������������ҡ�
	 */
	private class MyListener extends KeyAdapter{


		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			int i = e.getKeyCode();
			switch (i) {
			case KeyEvent.VK_UP:
				block.moveUp();
				break;
			case KeyEvent.VK_DOWN:
				block.moveDown();
				break;
			case KeyEvent.VK_LEFT:
				block.moveLeft();
				break;
			case KeyEvent.VK_RIGHT:
				block.moveRight();
				break;
			case KeyEvent.VK_SPACE:
				block.quickDown();
				break;
			case KeyEvent.VK_P:
				block.pause();
				break;
			case KeyEvent.VK_C:
				block.jixu();
				break;

			}
		}
		
	}

	
		
		/**
		 * �˵���ӷ���
		 */
		private void addMenu() {
		// TODO Auto-generated method stub
		JMenuBar jb1=new JMenuBar();
//		m1.addKeyListener(new MenuKeyListener());
		//����Dialog�Ի�������еȼ��ı���ı�ѡ��
		m1.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				int i=Block.get_addl();
				if(i==7)
					jr1.setSelected(true);
				else if(i==10)
					jr2.setSelected(true);
				else
					jr3.setSelected(true);
			}
			
		});
		
			
//		JMenuItem ji1=new JMenuItem("���֣�O��");
		
		jr1.addActionListener(new MenuActionListener());
		
		jr2.addActionListener(new MenuActionListener());
		
		jr3.addActionListener(new MenuActionListener());
		ButtonGroup bg=new ButtonGroup();
		bg.add(jr1);
		bg.add(jr2);
		bg.add(jr3);
		
		  JMenuItem ji2=new JMenuItem("�Զ���");
		  
		ji2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				zi=new Zidingyi(MyFrame.this,"�Զ���",false,block,gc);
				zi.setVisible(true);
				if(playing==true)
				block.pause();
			}
		});
		JMenuItem ji3=new JMenuItem("�˳�");
		ji3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.exit(1);//�˳�����
			}
		});
		JMenuItem ji4=new JMenuItem("����");
		ji4.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JDialog dl=new Version(MyFrame.this,"�汾��Ϣ",false);

			}
		});
		//������ɫ�Ի������÷�����ɫ
		JMenuItem ji_color=new JMenuItem("������ɫ");
		ji_color.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Color newFrontColor = JColorChooser.showDialog(
					    MyFrame.this,"���÷�����ɫ",
	                 gc.getBlockColor());
					if (newFrontColor != null)
						gc.setBlockColor(newFrontColor);
				}
			}
		);
		MyFrame.this.setJMenuBar(jb1);
		jb1.add(m1);
		jb1.add(m2);
		m1.add(ji1);
		m1.add(jr1);
		m1.add(jr2);
		m1.add(jr3);
		m1.add(ji2);
		m1.add(ji_color);
		m1.add(ji3);
		
		m2.add(ji4);	
	}
	/**
	 * �ҽ�������
	 */
	private void addRight() {
		// TODO Auto-generated method stub
//		JTextField jt1=new JTextField("��һ��");
		JLabel jt1=new JLabel("��һ��");
		jt1.setFont(new Font("�����п�", Font.BOLD, 18));
		jt1.setOpaque(false);
//		jt1.setEditable(false);
		jp_pre.setLayout(null);
		jt1.setBounds(5, 0, 80, 20);
		jp_pre.add(jt1);
		pv.setBounds(10, 20, 102, 102);
		jp_pre.add(pv);//���Ԥ������
		jp_pre.setBounds(308, 5, 120, 125);//��������
		jp_pre.setOpaque(false);//���ñ���Ϊ͸��
		MyFrame.this.add(jp_pre);
//		JTextField jt2=new JTextField("���ܼ���" );
		JLabel jt2=new JLabel("���ܼ���");
		jt2.setFont(new Font("�����п�", Font.BOLD, 23));
//		jt2.setEditable(false);
		jt2.setOpaque(false);
//		JTextField jt3=new JTextField("�������£���" );
		JLabel jt3=new JLabel("�������£���");
		jt3.setFont(new Font("�����п�", Font.BOLD, 15));
//		jt3.setEditable(false);
		jt3.setOpaque(false);
//		JTextField jt4=new JTextField("��ת����" );
		JLabel jt4=new JLabel("��ת����");
		jt4.setFont(new Font("�����п�", Font.BOLD, 15));
//		jt4.setEditable(false);
		jt4.setOpaque(false);
//		JTextField jt5=new JTextField("���󣺡�" );
		JLabel jt5=new JLabel("���󣺡�");
		jt5.setFont(new Font("�����п�", Font.BOLD, 15));
//		jt5.setEditable(false);
		jt5.setOpaque(false);
//		JTextField jt6=new JTextField("���ң���" );
		JLabel jt6=new JLabel("���ң���");
		jt6.setFont(new Font("�����п�", Font.BOLD, 15));
//		jt6.setEditable(false);
		jt6.setOpaque(false);
		JLabel jt11=new JLabel("һ�����䣺�ո�");
		jt11.setFont(new Font("�����п�", Font.BOLD, 15));
//		jt6.setEditable(false);
		jt6.setOpaque(false);
//		JTextField jt7=new JTextField("��ͣ��P" );
		JLabel jt7=new JLabel("��ͣ��P");
		jt7.setFont(new Font("�����п�", Font.BOLD, 15));
//		jt7.setEditable(false);
		jt7.setOpaque(false);
//		JTextField jt8=new JTextField("������C" );
		JLabel jt8=new JLabel("������C");
		jt8.setFont(new Font("�����п�", Font.BOLD, 15));
//		jt8.setEditable(false);
		jt8.setOpaque(false);
		jp_ctrl.setLayout(new GridLayout(8, 1, 0, 0));
//		jp_ctrl.setBorder(BorderFactory.createBevelBorder(EtchedBorder.LOWERED));
		jp_ctrl.add(jt2);
		jp_ctrl.add(jt3);
		jp_ctrl.add(jt4);
		jp_ctrl.add(jt5);
		jp_ctrl.add(jt6);
		jp_ctrl.add(jt11);
		jp_ctrl.add(jt7);
		jp_ctrl.add(jt8);
		jp_ctrl.setOpaque(false);
		jp_ctrl.setBounds(310, 145, 120, 200);
		MyFrame.this.add(jp_ctrl);
//		jt9.setEditable(false);
		jt9.setOpaque(false);
		jt9.setForeground(Color.BLACK);
//		jt10.setEditable(false);
		jt10.setOpaque(false);
		jt10.setForeground(Color.BLACK);
		jp_scor.setLayout(new GridLayout(2, 1, 0, 20));
		jp_scor.add(jt9);
		jt9.setFont(new Font("�����п�", Font.BOLD, 26));
		jt10.setFont(new Font("�����п�", Font.BOLD, 26));
		jp_scor.add(jt10);
		jt9.setBackground(Color.LIGHT_GRAY);
		jt10.setBackground(Color.LIGHT_GRAY);
		jp_scor.setOpaque(false);
		jp_scor.setBounds(320, 360, 100, 140);
		MyFrame.this.add(jp_scor);
	}
	
	/**
	 * �˵��ȼ��ļ���
	 *
	 */
	private class MenuActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			//��ȡJMenuItem����
			JMenuItem j=((JMenuItem)e.getSource());
			if(j==jr1){
//				newLevel=Constant.LEVEL_1;
				Block.set_addl(7);
			}
			if(j==jr2){
				Block.set_addl(10);
			}
			if(j==jr3){
//				high=true;
				Block.set_addl(13);
			}
				
		}
		
	}
	public static void main(String[] args) {
		new MyFrame("����˹����");
	}
	
	
}
