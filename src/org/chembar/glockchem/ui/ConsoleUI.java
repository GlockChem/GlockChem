package org.chembar.glockchem.ui;

import java.util.InputMismatchException;
import java.util.Scanner;

import org.chembar.glockchem.core.AdvNum;
import org.chembar.glockchem.core.Equation;
import org.chembar.glockchem.core.EquationBalancer;
import org.chembar.glockchem.core.EquationCalculator;
import org.chembar.glockchem.core.EquationCalculator.EquationConditionMass;
import org.chembar.glockchem.core.Formula;
import org.chembar.glockchem.core.Pair;
import org.chembar.glockchem.core.RMDatabase.AtomNameNotFoundException;

public class ConsoleUI {
	public static final String version = "0.02 Alpha";
	
	// 显示AdvNum
	private static void showResult(AdvNum result) {
		System.out.print(result.toDouble());
		System.out.println("\n    绝对误差: (-" + result.getErrorMin() + ", +" + result.getErrorMax() + ")\n");
		return;
	}
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Equation equation = null;
		EquationCalculator calc = null;
		EquationBalancer balance;
		int status = 0;
		while(status != -1) {
			switch (status) {
				default: case -1:{
					scanner.close();
					return;
				}
				case 0:	{
					// Splash
					System.out.println("Welcome to GlockChem CUI");
					System.out.println("Version: " + version);
					
					status = 1;
					break;
				}
				case 1: {
					// 输入方程式模式
					System.out.println("\n【输入方程式】");
					System.out.println("请键入需计算的化学方程式，然后按回车键：");
					String strInput = scanner.nextLine();
					
					if (strInput == "") {
						System.out.println("错误：输入为空！");
						break;
					}
					
					try {
						equation = new Equation(strInput);
					} catch (Exception e) {
						System.out.println("分析错误：" + e.getMessage());
						break;
					}
					
					System.out.println("方程式读入成功。");
					
					status = 2;
					break;
				}
				case 2: {
					// 方程式平衡检查
					System.out.println("\n【方程式平衡检查】");
					System.out.println("方程式：" + equation.toString());
					System.out.print("正在检查配平...");
					balance = new EquationBalancer(equation);
					if (balance.checkBalance() == false) {
						System.out.println("未配平");
						
						System.out.print("正在使用高斯消元引擎配平方程式...");
						if (balance.balanceGaussian() == true) {
							System.out.println("配平成功");
							
							status = 2;
							break;
						} else {
							System.out.println("配平失败");
							
							System.out.println("由于无法对未配平的方程式进行相关的化学计算，系统自动退出。");
							status = -1;
							break;
						}

					} else {
						System.out.println("已配平");
						status = 3;
						break;
					}
				}
				case 3:{
					System.out.println("\n【方程式计算选单】");
					
					System.out.println("反应物列表：");
					int numTotal = 0;
					for (Pair<Formula,Integer> pair : equation.reactant) {
						System.out.print("[" + String.valueOf(numTotal++) + "]\t");
						System.out.println(pair.getL().getRawString());
					}
					int numReactant = numTotal;
					System.out.println("生成物列表：");
					for (Pair<Formula,Integer> pair : equation.product) {
						System.out.print("[" + String.valueOf(numTotal++) + "]\t");
						System.out.println(pair.getL().getRawString());
					}
					
					int numEnter = -1;
					while (numEnter == -1) {
						System.out.print("请输入给定条件的化合物的序号，然后按回车键：");
						
						try {
							numEnter = scanner.nextInt();
						} catch (InputMismatchException e) {
							System.out.println("输入无效。");
							numEnter = -1;
							continue;
						} finally {
							if (numEnter >= numTotal || numEnter < 0) {
								System.out.println("输入无效。");
								numEnter = -1;
								continue;
							}
						}
					}
					
					System.out.println("请输入给定条件的质量，然后按回车键：");
					double numCondition = scanner.nextDouble();
					
					
					EquationConditionMass condition;
					if (numEnter < numReactant) {
						condition = new EquationConditionMass(equation.reactant.get(numEnter), new AdvNum(numCondition));
					} else {
						condition = new EquationConditionMass(equation.product.get(numEnter-numReactant), new AdvNum(numCondition));
					}
					
					try {
						calc = new EquationCalculator(equation);
					} catch (Exception e1) {
						System.out.println("遭遇未知错误：方程式经过配平后仍无法配平？");
						status = -1;
						break;
					}
					for (Pair<Formula, Integer> pair : equation.reactant) {
						System.out.print(pair.getL().getRawString());
						System.out.print(": ");
						try {
							showResult(calc.calcMass(condition, pair));
						} catch (AtomNameNotFoundException e) {
							System.out.println("发生错误：未知原子：" + e.getAtom());
							continue;
						}
					}
					
					for (Pair<Formula, Integer> pair : equation.product) {
						System.out.print(pair.getL().getRawString());
						System.out.print(": ");
						try {
							showResult(calc.calcMass(condition, pair));
						} catch (AtomNameNotFoundException e) {
							System.out.println("发生错误：未知原子：" + e.getAtom());
							continue;
						}
					}
					
					System.out.println("计算完毕！感谢您的使用！");
					status = -1;
					break;
				}
			}
		}
	}
}
