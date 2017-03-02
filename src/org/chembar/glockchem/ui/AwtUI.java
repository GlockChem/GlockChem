package org.chembar.glockchem.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import org.chembar.glockchem.core.Equation;
import org.chembar.glockchem.core.EquationBalancer;
import org.chembar.glockchem.core.EquationCalculator;

public class AwtUI extends Frame{
	private Button a,b,c,d,e;
	public static AwtUI window;
	Equation equation = null;
	EquationCalculator calc = null;
	EquationBalancer balance;
	bbb bbb1;
	TextField t;
	String pass;
	public AwtUI(String str) {
		super(str);
		setSize(400,400);
		setVisible(true);
		add(a=new Button("开始"));
		addWindowListener(new WindowAdapter(){
			 public void windowClosing(WindowEvent e) {
				exit();
			}
		  });
		setVisible(true);
		pass = "请输入方程式";
	}
	public class bbb extends KeyAdapter implements ActionListener{
		  public void actionPerformed(ActionEvent e) {
			 if(e.getSource()==a){
				 start(pass);
			 }
			 if(e.getSource()==b || e.getSource()==t){
				 input();
			 }
			 if(e.getSource()==c){
				 
			 }
			 if(e.getSource()==e){
				 init();
			 }
			 requestFocus();
		  }
	}
	private void start(String str) {
		removeAll();
		setLayout(new FlowLayout(FlowLayout.CENTER,30,20));
		add(new Label(str));
		t = new TextField("输入方程式", 40);
		add(t);
		add(b = new Button("确定"));
		b.addActionListener(bbb1);
		t.addActionListener(bbb1);
		setVisible(true);
	}
	private void input() {
		String strInput = t.getText();
		if (strInput == "") {
			start("错误：输入为空！");
		}
		try {
			equation = new Equation(strInput);
		} catch (Exception e) {
			start("分析错误：" + e.getMessage());
		}
		bal("方程式读入成功。");
	}
	private void bal(String str) {
		removeAll();
		setLayout(new GridLayout(6,1));
		add(new Label(str));
		add(new Label("正在检查配平..."));
		setVisible(true);
		balance = new EquationBalancer(equation);
		if (balance.checkBalance() == false) {
			add(new Label("未配平"));
			add(new Label("正在使用高斯消元引擎配平方程式..."));
			if (balance.balanceGaussian() == true) {
				bal("配平成功");
			} else {
				add(new Label("配平失败"));
				add(a = new Button("确定"));
				pass= "配平失败";
				a.addActionListener(bbb1);
			}
		} else {
			cal("已配平");
		}
		setVisible(true);
	}
	private void cal(String str) {
		removeAll();
		setLayout(new GridLayout(7,1));
		add(new Label(str));
		add(new Label(equation.toString()));
		//TODO 
	}
	public void exit(){
		setVisible(false);
		dispose();
		System.exit(0);
	}
	private void init(){
		window = new AwtUI("GlockChem GUI - v." + ConsoleUI.version);
		init1();
	}
	private void init1(){
		bbb1 = window.new bbb();
		a.addActionListener(bbb1);
	}
	public static void main(String args[]) {
		window = new AwtUI("GlockChem GUI - v." + ConsoleUI.version);
		window.init1();
	}
}
