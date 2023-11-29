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
	JRadioButtonMenuItem jr1=new JRadioButtonMenuItem("初级",true);
	JRadioButtonMenuItem jr2=new JRadioButtonMenuItem("中级");
	JRadioButtonMenuItem jr3=new JRadioButtonMenuItem("高级");
	JLabel jt9=new JLabel("得分：0" );
	static JLabel jt10=new JLabel("等级：1" );
	JMenu m1=new JMenu("游戏");
	JMenu m2=new JMenu("帮助");
	JCheckBox jc1;
	JSlider jsl;
//	Dialog dia;//创建对话框
	static ImageIcon  background = new ImageIcon(Constant.backGround1);
	// 把背景图片加到label
	static JLabel label = new JLabel(background);
//	Dialog dia=new Dialog(this, "自定义", false);
	int scor=0;//初始化分数为0
	static int rank=0;//初始化等级为0
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
	JMenuItem ji1=new JMenuItem("开局");
	GameCanvas gc=new GameCanvas(20, 12);//画出20行12列
	private Block block=new Block();//当前块
	private int newspeed=1000;//默认当前等级为1
	MusicPlayer mp=new MusicPlayer();
	Timer time=new Timer();
	MyTask mytask;
	int temp=1;
//	游戏主构造函数
	public MyFrame(String str){
		super(str);
		this.setSize(450, 570);
		Dimension scrSize = 
	               Toolkit.getDefaultToolkit().getScreenSize();//获取屏幕尺寸
		setLocation((scrSize.width - getSize().width) / 2,
		        (scrSize.height - getSize().height) / 2);//设置屏幕居中
		this.setLayout(null);
		//label的大小为jframe的大小
		label.setBounds(0, 0, this.getWidth(), this.getHeight());
		//把label加到jframe的最底层，比jframe上的那个panel还下面
		this.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
		//label比jframe上的那个panel还下面，那需要把那个panel设为透明的，不然就盖住背景了
		JPanel imagePanel = (JPanel) this.getContentPane();
		imagePanel.setOpaque(false);
		addMenu();
		//游戏开始按钮
		ji1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// 如果游戏已经开始，再按一次即显示游戏结束
					
			
				if (playing == true) {
					ji1.setText("开局");
					if(isMusic==true)
					{mp.playStart();}
					gc.setGameOver(true);
					gc.repaint();
					MyFrame.rank=11-Constant.step;
					MyFrame.jt10.setText("等级："+MyFrame.rank);
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
					MyFrame.jt10.setText("等级："+MyFrame.rank);
					ji1.setText("结束游戏");
					playing = true;
					mytask=new MyTask();
					time.schedule(mytask, 0, 100);//100毫秒执行一次
					Thread thread = new Thread(new play());// 调用开始游戏的方法
					thread.start();

				}
			};
		});
		this.add(gc);//添加游戏画布
		
		addRight();//添加右边
		this.setFocusable(true);//设置可获得焦点
		this.requestFocus();//设置焦点
		this.addKeyListener(new MyListener());
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
public static void setBackGround(){
	label.setIcon(background);
}
	/**
	 * 定时下落，用计数方式来实现速度的改变
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
				} // 取消定时器任务
				temp = 1;
			}
		
		}
	}

	private class play implements Runnable {
		public void run() {
/*			if(killThread==true)
				return;*/
			int col = (int) (Math.random() * (gc.getCols() - 3));//随即位置生成列
			int style = Constant.STYLES[(int) (Math.random() * Block.get_addl())][(int) (Math
					.random() * 4)];
			while (playing) {
				if (block != null) {
					//判断当前方块是否死亡
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
//				if(upspeed)//判断是否加速，是的话则进行调用
//				upLevel();
				if(isGameOver()){
					if(isMusic==true)
					{mp.playGameOver();}
					gc.setGameOver(true);
					gc.repaint();
					ji1.setText("开局");
					mytask.cancel();
					playing=false;
					return;
					
				}
				block = new Block(style, -1, col, gc);
				block.jixu();//防止在暂停时候重新开始游戏，暂停字还不消失
				gc.repaint();// 将创建出来的方块显示出来
				block.isAlive = true;
				col = (int) (Math.random() * (gc.getCols() - 3));//随即位置生成列
				style = Constant.STYLES[(int) (Math.random() * Block.get_addl())][(int) (Math
						.random() * 4)];
				pv.setStyle(style);
			}
		}
		/**
		 * 增加速度
		 */
		private void upLevel() {
			if(Constant.step-1<1){
				return;}
			Constant.step=Constant.step-1;	//速度增加一级
				rank++;					
				jt10.setText("等级："+rank);
				
				upspeed=false;//将标志位至为false		
			
		}

		/**
		 * 判断是否满行，满行则调用消行方法。
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
					gc.delete(row);//删除行
					if(isMusic==true)
					{mp.playEraseSound();}
					addScor();//增加分数
					if(scor%10==0)//设置为10分增加一个等级
					upspeed=true;//将速度增加标志位至为true
					if(upspeed==true)
						upLevel();
				}
			}

		}

		/**
		 * 得分的计算方法
		 */
		private void addScor() {
			scor=scor+10;
			jt9.setText("得分："+scor);
		}
	
	}
	/**
	 * 判断最顶层是否有被占用,游戏是否结束
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
		jt10.setText("等级："+rank);
		jt9.setText("得分："+scor);
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
	 *按键监听，上下左右。
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
		 * 菜单添加方法
		 */
		private void addMenu() {
		// TODO Auto-generated method stub
		JMenuBar jb1=new JMenuBar();
//		m1.addKeyListener(new MenuKeyListener());
		//监听Dialog对话框，如果有等级改变则改变选择
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
		
			
//		JMenuItem ji1=new JMenuItem("开局（O）");
		
		jr1.addActionListener(new MenuActionListener());
		
		jr2.addActionListener(new MenuActionListener());
		
		jr3.addActionListener(new MenuActionListener());
		ButtonGroup bg=new ButtonGroup();
		bg.add(jr1);
		bg.add(jr2);
		bg.add(jr3);
		
		  JMenuItem ji2=new JMenuItem("自定义");
		  
		ji2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				zi=new Zidingyi(MyFrame.this,"自定义",false,block,gc);
				zi.setVisible(true);
				if(playing==true)
				block.pause();
			}
		});
		JMenuItem ji3=new JMenuItem("退出");
		ji3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.exit(1);//退出程序
			}
		});
		JMenuItem ji4=new JMenuItem("关于");
		ji4.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JDialog dl=new Version(MyFrame.this,"版本信息",false);

			}
		});
		//调用颜色对话框设置方块颜色
		JMenuItem ji_color=new JMenuItem("方块颜色");
		ji_color.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Color newFrontColor = JColorChooser.showDialog(
					    MyFrame.this,"设置方块颜色",
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
	 * 右界面的添加
	 */
	private void addRight() {
		// TODO Auto-generated method stub
//		JTextField jt1=new JTextField("下一块");
		JLabel jt1=new JLabel("下一块");
		jt1.setFont(new Font("华文行楷", Font.BOLD, 18));
		jt1.setOpaque(false);
//		jt1.setEditable(false);
		jp_pre.setLayout(null);
		jt1.setBounds(5, 0, 80, 20);
		jp_pre.add(jt1);
		pv.setBounds(10, 20, 102, 102);
		jp_pre.add(pv);//添加预览窗口
		jp_pre.setBounds(308, 5, 120, 125);//设置坐标
		jp_pre.setOpaque(false);//设置背景为透明
		MyFrame.this.add(jp_pre);
//		JTextField jt2=new JTextField("功能键盘" );
		JLabel jt2=new JLabel("功能键盘");
		jt2.setFont(new Font("华文行楷", Font.BOLD, 23));
//		jt2.setEditable(false);
		jt2.setOpaque(false);
//		JTextField jt3=new JTextField("快速向下：↓" );
		JLabel jt3=new JLabel("快速向下：↓");
		jt3.setFont(new Font("华文行楷", Font.BOLD, 15));
//		jt3.setEditable(false);
		jt3.setOpaque(false);
//		JTextField jt4=new JTextField("旋转：↑" );
		JLabel jt4=new JLabel("旋转：↑");
		jt4.setFont(new Font("华文行楷", Font.BOLD, 15));
//		jt4.setEditable(false);
		jt4.setOpaque(false);
//		JTextField jt5=new JTextField("向左：←" );
		JLabel jt5=new JLabel("向左：←");
		jt5.setFont(new Font("华文行楷", Font.BOLD, 15));
//		jt5.setEditable(false);
		jt5.setOpaque(false);
//		JTextField jt6=new JTextField("向右：→" );
		JLabel jt6=new JLabel("向右：→");
		jt6.setFont(new Font("华文行楷", Font.BOLD, 15));
//		jt6.setEditable(false);
		jt6.setOpaque(false);
		JLabel jt11=new JLabel("一键下落：空格");
		jt11.setFont(new Font("华文行楷", Font.BOLD, 15));
//		jt6.setEditable(false);
		jt6.setOpaque(false);
//		JTextField jt7=new JTextField("暂停：P" );
		JLabel jt7=new JLabel("暂停：P");
		jt7.setFont(new Font("华文行楷", Font.BOLD, 15));
//		jt7.setEditable(false);
		jt7.setOpaque(false);
//		JTextField jt8=new JTextField("继续：C" );
		JLabel jt8=new JLabel("继续：C");
		jt8.setFont(new Font("华文行楷", Font.BOLD, 15));
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
		jt9.setFont(new Font("华文行楷", Font.BOLD, 26));
		jt10.setFont(new Font("华文行楷", Font.BOLD, 26));
		jp_scor.add(jt10);
		jt9.setBackground(Color.LIGHT_GRAY);
		jt10.setBackground(Color.LIGHT_GRAY);
		jp_scor.setOpaque(false);
		jp_scor.setBounds(320, 360, 100, 140);
		MyFrame.this.add(jp_scor);
	}
	
	/**
	 * 菜单等级的监听
	 *
	 */
	private class MenuActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			//获取JMenuItem对象
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
		new MyFrame("俄罗斯方块");
	}
	
	
}
