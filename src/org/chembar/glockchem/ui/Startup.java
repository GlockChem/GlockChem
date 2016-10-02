package org.chembar.glockchem.ui;

import org.chembar.glockchem.core.AdvNum;
import org.chembar.glockchem.core.Equation;
import org.chembar.glockchem.core.EquationCalculator;
import org.chembar.glockchem.core.EquationCalculator.EquationCondition;
import org.chembar.glockchem.core.EquationCalculator.EquationConditionMass;

public class Startup {
	public static void main(String[] args) throws Exception {
		// 配平前
		Equation aa = new Equation("KSO2 + 2V5 = 2KSO2V5");
		System.out.println(aa);
		
		// 配平后
		EquationCalculator calc = new EquationCalculator(aa);
		EquationCondition condition = new EquationConditionMass(aa.reactant.get(0), new AdvNum(25.0));
		System.out.println(aa);
		
		System.out.println(calc.calcMass(condition, aa.product.get(0)));
	}
}
