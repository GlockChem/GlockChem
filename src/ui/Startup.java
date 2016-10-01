package ui;

import core.Equation;

public class Startup {
	public static void main(String[] args) throws Exception {
		// TODO: 真正的化学计算
		Equation a = new Equation("CO2 + H2O --> H2CO3");
		System.out.println(a.reactant);
		System.out.println(a.product);
		System.out.print(a.checkBalance());
		
//		
//		Formula a = new Formula("Cu2(OH)2CO3");
//		System.out.println(a.getRawString());
//		System.out.print(a.mapAtomList);
//		
//		RMDatabase db = new RMDatabase();
//		AdvNum rm = db.queryMolarMass(a);
//		System.out.println("\nRM: " + rm.getValue());
	}
}
