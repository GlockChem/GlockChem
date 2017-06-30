package org.chembar.glockchem.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import org.chembar.glockchem.core.AdvNum;
import org.chembar.glockchem.core.Equation;
import org.chembar.glockchem.core.EquationBalancer;
import org.chembar.glockchem.core.EquationCalculator;
import org.chembar.glockchem.core.Formula;
import org.chembar.glockchem.core.Pair;
import org.chembar.glockchem.core.EquationCalculator.EquationConditionMass;
import org.chembar.glockchem.core.RMDatabase.AtomNameNotFoundException;

public class AwtUI extends Frame{
	private Button a,b,c,d;
	public static AwtUI window;
	Equation equation = null;
	EquationCalculator calc = null;
	EquationBalancer balance;
	bbb bbb1;
	TextField t;
	String pass;
	Choice ch;
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
			 if(e.getSource()==b){
				 input();
			 }
			 if(e.getSource()==c){
				 fin();
			 }
			 if(e.getSource()==d){
				 exit();
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
		add(new Label("请选择反应/生成物"));
		ch = new Choice();
		for (Pair<Formula,Integer> pair : equation.reactant) {
			ch.add(pair.getL().getRawString());
		}
		for (Pair<Formula,Integer> pair : equation.product) {
			ch.add(pair.getL().getRawString());
		}
		add(ch);
		add(new Label("请输入给定条件的质量"));
		add(t = new TextField("输入质量"));
		add(c = new Button("确定"));
		c.addActionListener(bbb1);
		setVisible(true);
	}
	
	private void fin() {
		removeAll();
		setLayout(new GridLayout(equation.reactant.size()+equation.product.size()+5,1));
		double numCondition = 0;
		try {
		numCondition = Double.parseDouble(t.getText());
		} catch (Exception e) {
			cal("给定质量无效");
		}
		EquationConditionMass condition = new EquationConditionMass(equation.reactant.get(1), new AdvNum(numCondition));
		pass = ch.getSelectedItem();
		boolean flag = true;
		for (int i = 0; i < equation.reactant.size(); i++){
			if (pass == equation.reactant.get(i).getL().getRawString()) {
				condition = new EquationConditionMass(equation.reactant.get(i), new AdvNum(numCondition));
				flag = false;
			}
		}
		for (int i = 0; i < equation.product.size(); i++) {
			if (pass == equation.product.get(i).getL().getRawString()) {
				condition = new EquationConditionMass(equation.product.get(i), new AdvNum(numCondition));
				flag = false;
			}
		}
		if (flag) {
			start("未知错误1");
		}
		else {
			try {
				calc = new EquationCalculator(equation);
				add(new Label(equation.toString()));
				add(new Label(ch.getSelectedItem() + ": " +  t.getText()));
				for (Pair<Formula, Integer> pair : equation.reactant) {
					pass = pair.getL().getRawString() + ": ";
					try {
						pass = pass + String.format("%.2f", calc.calcMass(condition, pair).toDouble()) + 
								"+" + String.format("%.2f", calc.calcMass(condition, pair).getErrorMax()) + 
								"-" + String.format("%.2f", calc.calcMass(condition, pair).getErrorMin());
					} catch (AtomNameNotFoundException e) {
						cal("发生错误：未知原子：" + e.getAtom());
					}
					add(new Label(pass.substring(0)));
				}
				for (Pair<Formula, Integer> pair : equation.product) {
					pass = pair.getL().getRawString() + ": ";
					try {
						pass = pass + String.format("%.2f", calc.calcMass(condition, pair).toDouble()) + 
								"+" + String.format("%.2f", calc.calcMass(condition, pair).getErrorMax()) + 
								"-" + String.format("%.2f", calc.calcMass(condition, pair).getErrorMin());
					} catch (AtomNameNotFoundException e) {
						cal("发生错误：未知原子：" + e.getAtom());
					}
				}	
				add(d = new Button ("退出"));
				add(a = new Button ("新公式"));
				pass = "";
				d.addActionListener(bbb1);
				a.addActionListener(bbb1);
				setVisible(true);
			} catch (Exception e1) {
				start("未知错误2");
			}
		}
		
	}
	
	public void exit(){
		setVisible(false);
		dispose();
		System.exit(0);
	}
	private void init(){
		window = new AwtUI("GlockChem GUI - v" + ConsoleUI.version);
		init1();
	}
	private void init1(){
		bbb1 = window.new bbb();
		a.addActionListener(bbb1);
	}
	public static void main(String args[]) {
		window = new AwtUI("GlockChem GUI - v" + ConsoleUI.version);
		window.init1();
	}
}
