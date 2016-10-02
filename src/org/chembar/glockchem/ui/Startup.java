package org.chembar.glockchem.ui;

import org.chembar.glockchem.core.AdvNum;
import org.chembar.glockchem.core.Equation;
import org.chembar.glockchem.core.EquationCalculator;
import org.chembar.glockchem.core.EquationCalculator.EquationCondition;
import org.chembar.glockchem.core.EquationCalculator.EquationConditionMass;

public class Startup {
	public static void main(String[] args) throws Exception {
		// 注意下面的方程式没配平
		// 当配平功能实现时才可以计算哦
		Equation aa = new Equation("H2 + O2 ---> H2O");
		EquationCalculator calc = new EquationCalculator(aa);
		EquationCondition condition = new EquationConditionMass(aa.reactant.get(0), new AdvNum(25.0));
		
		System.out.println(calc.calcMass(condition, aa.product.get(0)));
	}
}
