package org.chembar.glockchem.ui;

import org.chembar.glockchem.core.AdvNum;
import org.chembar.glockchem.core.Equation;
import org.chembar.glockchem.core.EquationCalculator;
import org.chembar.glockchem.core.EquationCalculator.EquationCondition;
import org.chembar.glockchem.core.EquationCalculator.EquationConditionMass;

public class Startup {
	public static void main(String[] args) throws Exception {
		// 配平前
		Equation aa = new Equation("H2+Ca(CN)2+NaAlF4+FeSO4+MgSiO3+KI+H3PO4+PbCrO4+BrCl+CF2Cl2+SO2-->PbBr2+CrCl3+MgCO3+KAl(OH)4+Fe(SCN)3+PI3+Na2SiO3+CaF2+H2O");
		System.out.println(aa);
		
		// 配平后
		EquationCalculator calc = new EquationCalculator(aa);
		EquationCondition condition = new EquationConditionMass(aa.reactant.get(0), new AdvNum(25.0));
		System.out.println(aa);
		
		System.out.println(calc.calcMass(condition, aa.product.get(0)));
	}
}
