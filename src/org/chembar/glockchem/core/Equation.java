package org.chembar.glockchem.core;

import java.util.ArrayList;
import java.util.List;

/** 化学方程式抽象
 * @author DuckSoft
 */
public class Equation {
	/** 反应物列表*/
	public List<Pair<Formula,Integer>> reactant = new ArrayList<Pair<Formula,Integer>>();
	/** 生成物列表*/
	public List<Pair<Formula,Integer>> product = new ArrayList<Pair<Formula,Integer>>();
	
	/** 以给定的含有有效格式的化学方程式字符串生成一个Equation对象。<br/>
	 * <p>可接受的格式示例如下：
	 * <li>2C + O2 = 2CO</li>
	 * <li>2C + O2 -> 2CO</li>
	 * <li>2C + O2 === 2CO</li>
	 * <li>2C + O2 ==> 2CO</li>
	 * </p>
	*/
	public Equation(String strEquation) throws Exception {
		this.parseEquation(strEquation);
	}

	/** 分析方程式
	 * @param strEquation 欲分析的方程式
	 * @throws Exception
	 */
	public void parseEquation(String strEquation) throws Exception {
		// 不可重复分析
		assert((this.reactant.isEmpty() && this.product.isEmpty()));
		
		// 避免输入为空
		if (strEquation.isEmpty()) {
			//TODO: 输入为空的处理
			throw new Exception("输入为空");
		}
		
		// 避免重复分析
		
		String partLeft = "";		// 反应物、生成物缓冲区
		String partRight = "";
		boolean isRight = false;	// 是否到了生成物标志 
		boolean bAuxFlag = false;	// 辅助标志
		
		for (char i : strEquation.toCharArray()) {
			if (i == '=' || i == '-') {	// 出现 = 或 - 符号时判定为反应物结束 
				isRight = true;
				if (bAuxFlag == true) {	// 若又出现一次分隔符，则判定为错误 
					throw new Exception("出现多于一个的反应物-生成物分隔符");
				}
				continue;
			} else if (i == ' ' || i == '>') {	// 忽略掉 ---> 格式中的 > 字符以及空白字符
				continue;
			} else {
				if (isRight == true) {	// 若已到了生成物部分 
					bAuxFlag = true; 	// 设定辅助标志 
				}
			}
			
			if (isRight) {
				partRight += String.valueOf(i);
			} else {
				partLeft += String.valueOf(i);
			}
		}
		
		// 现在分离到列表里
		if (partLeft.isEmpty() || partRight.isEmpty()) {
			throw new Exception("所输入的反应物或生成物为空");
		}

		boolean isStarting = true;	// 标志：是否是化学式的开头
		String strTempA = "";			// 系数存储
		String strTempB = "";			// 化学式存储
		
		for (char i : partLeft.toCharArray()) {
			if (isStarting == true) {	// 若为化学式的开头 
				if (('0' <= i) && (i <= '9')) { // 判定是否为数字 
					strTempA += String.valueOf(i);				// 若为数字系数则加入到系数暂存器 
				} else {
					isStarting = false;	// 若非则表示数字部分结束
					
					if (strTempA.isEmpty()){	// 处理没有系数的情况 
						strTempA = "1";		// 给没有系数的项添加系数"1" 
					}
					
					if (i == '+') {	// 防止开头就遇到"+"号的垃圾情况 
						throw new Exception("列表开头遇到空白项");
					} 
					
					strTempB += String.valueOf(i);	// 将本个字符塞入化学式存储器 
				}
			} else {	// 若非化学式的开头 
				if (i == '+') {	// 若为"+"号 
					if (strTempB.isEmpty() || strTempA.isEmpty()) { // 防止化学式或系数为空时加入列表
						throw new Exception("Equation::parseFormulaList: 列表内遇到空白项");
					} else {
						this.reactant.add(new Pair<Formula,Integer>(new Formula(strTempB),new Integer(strTempA)));
						// 初始化状态 
						strTempA = "";
						strTempB = "";
						isStarting = true;
					}
				} else {		// 若非"+"号 
					strTempB += String.valueOf(i);	// 直接加入化学式缓冲 
				}
			}
		}
		
		// 循环后处理
		if (!(strTempA.isEmpty() || strTempB.isEmpty())) {
			this.reactant.add(new Pair<Formula,Integer>(new Formula(strTempB),new Integer(strTempA)));
			// 初始化状态 
			strTempA = "";
			strTempB = "";
			isStarting = true;
		}
		
		strTempA = "";
		strTempB = "";
		isStarting = true;
		
		for (char i : partRight.toCharArray()) {
			if (isStarting == true) {	// 若为化学式的开头 
				if (('0' <= i) && (i <= '9')) { // 判定是否为数字 
					strTempA += String.valueOf(i);				// 若为数字系数则加入到系数暂存器 
				} else {
					isStarting = false;	// 若非则表示数字部分结束
					
					if (strTempA.isEmpty()){	// 处理没有系数的情况 
						strTempA = "1";		// 给没有系数的项添加系数"1" 
					}
					
					if (i == '+') {	// 防止开头就遇到"+"号的垃圾情况 
						throw new Exception("列表开头遇到空白项");
					} 
					
					strTempB += String.valueOf(i);	// 将本个字符塞入化学式存储器 
				}
			} else {	// 若非化学式的开头 
				if (i == '+') {	// 若为"+"号 
					if (strTempB.isEmpty() || strTempA.isEmpty()) { // 防止化学式或系数为空时加入列表
						throw new Exception("Equation::parseFormulaList: 列表内遇到空白项");
					} else {
						this.product.add(new Pair<Formula,Integer>(new Formula(strTempB),new Integer(strTempA)));
						// 初始化状态 
						strTempA = "";
						strTempB = "";
						isStarting = true;
					}
				} else {		// 若非"+"号 
					strTempB += String.valueOf(i);	// 直接加入化学式缓冲 
				}
			}
		}
		
		// 循环后处理
		if (!(strTempA.isEmpty() || strTempB.isEmpty())) {
			this.product.add(new Pair<Formula,Integer>(new Formula(strTempB),new Integer(strTempA)));
			// 初始化状态 
			strTempA = "";
			strTempB = "";
			isStarting = true;
		}
	}
}
