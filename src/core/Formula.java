package core;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formula {
	// 化学式类
	String strRaw;	// 原始化学式字符串
	public Map<String,Integer> mapAtomList = new HashMap<String,Integer>();	// 原子清单
	
	public String getRawString() {
		return strRaw;
	}
	// 异常类
	public class InvalidFormulaException extends Exception {
		private static final long serialVersionUID = 2L;
		String formula;
		
		InvalidFormulaException(String inStr) {
			this.formula = inStr;
		}
		
		public String getFormulaString() {
			return this.formula;
		}
	}
	
	// 构造
	public Formula(String inFormula) throws InvalidFormulaException{
		this.strRaw = inFormula;
		this.parseFormula(inFormula, 1);
	}
	
	private void insertList(Pair<String,Integer> pairToInsert) {
		int numTodo = 0;
		try {
			numTodo = this.mapAtomList.get(pairToInsert.getL());
		} catch(Exception e) {
			
		} finally {
			this.mapAtomList.put(pairToInsert.getL(),numTodo + pairToInsert.getR());
		}
	}
	
	private void parseFormula(String inFormula, int numMultiplier) throws InvalidFormulaException {
		//TODO: 化学式分析算法
		
		Matcher sm;	// 正则匹配结果
		
		Pattern e = Pattern.compile("^([A-Z][a-z]*)(\\d*)"),	// 原子匹配正则
				f = Pattern.compile("\\*(\\d*)([^*]+)[\\*]??"),	// 段分隔符"*"匹配 
				g = Pattern.compile("\\(([^\\*]*)\\)(\\d*)"),	// 括号匹配#1
				h = Pattern.compile("\\(([^\\*\\(]*)\\)(\\d*)");// 括号匹配#2
		
		while (!inFormula.isEmpty()) {
			sm = e.matcher(inFormula);
						// 找一坨原子 
						// sm[1]: 原子名称
						// sm[2]: 原子数量(有可能为空白)
			if (sm.find()) {// 若成功提取出原子
				int tempNum;
				if(sm.group(2).isEmpty()) {// 若没有下标 
					tempNum = 1 * numMultiplier;	// 默认下标为1 
				} else {// 有下标
					tempNum = Integer.valueOf(sm.group(2)) * numMultiplier; 
				}
				
				String tempStr = sm.group(1);	// 交给插入算法处理
				this.insertList(new Pair<String,Integer>(tempStr, tempNum));
				inFormula = inFormula.substring(sm.group(0).length());
			} else if (inFormula.charAt(0) == '*') {	// 又来一段 
				sm = f.matcher(inFormula);	// 找段
											// sm[1]: 段乘数
											// sm[2]: 段内容
				if (sm.find()) {
					int tempNum;
					if (sm.group(1).isEmpty()) {
						tempNum = 1;

					} else {
						tempNum = Integer.valueOf(sm.group(1));
					}
					
					String strTemp = sm.group(2);
					inFormula = inFormula.substring(sm.group(0).length());
					this.parseFormula(strTemp, tempNum);
				} else {// 空段的处理
					//TODO: 空段
				}
			} else if (inFormula.charAt(0) == '(') {
				// TODO: 括号的处理
				
				int intCounter = 0; // 第一次出现后半括号前 前半括号出现的次数
				
				for (char ch : inFormula.toCharArray()) {
					if (intCounter > 2) {
						break;
					}
					switch (ch) {
						case '(': intCounter++; break;
						case ')': break;
						case '*': break;
					}
				}
				
				if (intCounter == 1) {	// 仅仅出现了一次，说明在内层 
					sm = h.matcher(inFormula);
					sm.find();
					
					if (!sm.group(1).isEmpty()) {
						int tempNum;
						if (sm.group(2).isEmpty()) {
							tempNum = 1 * numMultiplier;
						} else {
							tempNum = Integer.valueOf(sm.group(2)) * numMultiplier;
						}
						
						String strTemp = sm.group(1);
						inFormula = inFormula.substring(sm.group(0).length());
						this.parseFormula(strTemp, tempNum);
					} else {
						//TODO: fucking
					}
				} else if (intCounter == 2) {	// 出现了不止一次，说明在外层 
					sm = g.matcher(inFormula);
					sm.find();
					
					sm = h.matcher(inFormula);
					sm.find();
					
					int tempNum;
					if (sm.group(2).isEmpty()) {
						tempNum = 1 * numMultiplier;
					} else {
						tempNum = Integer.valueOf(sm.group(2)) * numMultiplier;
					}
					
					String strTemp = sm.group(1);
					inFormula = inFormula.substring(sm.group(0).length());
					this.parseFormula(strTemp, tempNum);
				}
			} else {
				throw new InvalidFormulaException("化学式中发现非法字符 - " + inFormula.substring(0, 1));
			}
		}
	}
}
