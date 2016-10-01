package org.chembar.glockchem.ui;

import org.chembar.glockchem.core.*;
import org.chembar.glockchem.core.EquationCalculator.EquationCondition;
import org.chembar.glockchem.core.EquationCalculator.EquationConditionMass;

public class Startup {
	public static void main(String[] args) throws Exception {
		// 已实现界面与实现分离
		// 进一步计划中
		Equation aa = new Equation("2H2 + O2 ---> 2H2O");
		EquationCalculator calc = new EquationCalculator(aa);
		EquationCondition condition = new EquationConditionMass(aa.reactant.get(0), new AdvNum(25.0));
		
		System.out.println(calc.calcMass(condition, aa.product.get(0)));
	}
}
