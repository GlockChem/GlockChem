package org.chembar.glockchem.core;

import java.util.HashMap;
import java.util.Map;

/** RMDatabase - 带误差的原子量数据库
 * @author DuckSoft
 * @version 0.1
 */
public final class RMDatabase {
	final static String dataRMName[] = {"H","He","Li","Be","B","C","N","O","F","Ne","Na","Mg","Al","Si","P","S","Cl","Ar","K","Ca","Sc","Ti","V","Cr","Mn","Fe","Co","Ni","Cu","Zn","Ga","Ge","As","Se","Br","Kr","Rb","Sr","Y","Zr","Nb","Mo","Tc","Ru","Rh","Pd","Ag","Cd","In","Sn","Sb","Te","I","Xe","Cs","Ba","La","Ce","Pr","Nd","Pm","Sm","Eu","Gd","Tb","Dy","Ho","Er","Tm","Yb","Lu","Hf","Ta","W","Re","Os","Ir","Pt","Au","Hg","Tl","Pb","Bi","Th","Pa","U"};
	final static double dataRMError[] = {0.00007,0.000002,0.002,0.0000003,0.007,0.0008,0.0002,0.0003,0.000000006,0.0006,0.00000002,0.0006,0.0000007,0.0003,0.000000005,0.005,0.002,0.001,0.0001,0.004,0.000005,0.001,0.0001,0.0006,0.000003,0.002,0.000004,0.0004,0.003,0.02,0.001,0.01,0.000006,0.008,0.001,0.002,0.0003,0.01,0.00002,0.002,0.00002,0.01,0.0004,0.02,0.00002,0.01,0.0002,0.004,0.003,0.007,0.001,0.03,0.00003,0.006,0.00000006,0.007,0.00007,0.001,0.00002,0.003,0.2,0.02,0.001,0.03,0.00002,0.001,0.00002,0.003,0.00002,0.005,0.0001,0.02,0.00002,0.01,0.001,0.03,0.003,0.009,0.000005,0.02,0.0002,0.1,0.00001,0.0004,0.00002,0.00003};	
	final static double dataRMValue[] = {1.00794,4.002602,6.941,9.0121831,10.811,12.0107,14.0067,15.9994,18.998403163,20.1797,22.98976928,24.3050,26.9815385,28.0855,30.973761998,32.065,35.453,39.948,39.0983,40.078,44.955908,47.867,50.9415,51.9961,54.938044,55.845,58.933194,58.6934,63.546,65.38,69.723,72.64,74.921595,78.971,79.904,83.798,85.4678,87.62,88.90584,91.224,92.90637,95.95,98.9072,101.07,102.90550,106.42,107.8682,112.414,114.818,118.710,121.760,127.60,126.90447,131.293,132.90545196,137.327,138.90547,140.116,140.90766,144.242,144.9,150.36,151.964,157.25,158.92535,162.500,164.93033,167.259,168.93422,173.054,174.9668,178.49,180.94788,183.84,186.207,190.23,192.217,195.084,196.966569,200.59,204.3833,207.2,208.98040,232.0377,231.03588,238.02891};
	private Map<String,AdvNum> RMData = new HashMap<String,AdvNum>();
	
	public static final class AtomNameNotFoundException extends Exception {
		private static final long serialVersionUID = 1L;
		private String atomName;
		
		AtomNameNotFoundException(String inAtom) {
			this.atomName = inAtom;
		}
		
		public String getAtom() {
			return this.atomName;
		}
	}
	
	public RMDatabase() {// 构造函数
		for (int i=0; i<dataRMName.length; i++) {
			this.RMData.put(dataRMName[i], new AdvNum(dataRMValue[i], dataRMError[i]));
		}
	}
	
	public final AdvNum queryAtom(String inAtom) throws AtomNameNotFoundException {// 查询某原子的摩尔质量
		if (this.RMData.containsKey(inAtom)) {
			return this.RMData.get(inAtom);
		} else {
			throw new AtomNameNotFoundException(inAtom);
		}
	}
	
	/** 查询Formula的摩尔质量
	 * <p>在不需要误差分析的场合，可以使用{@link #queryMolarMassDouble(Formula)}替代本方法。</p>
	 * @param inFormula 欲查询的Formula对象
	 * @return AdvNum类型表示的Formula的摩尔质量（含误差）
	 * @throws AtomNameNotFoundException
	 */
	public final AdvNum queryMolarMass(Formula inFormula) throws AtomNameNotFoundException {// 查询某化学式的摩尔质量
		AdvNum mass = new AdvNum();
		
		for (Map.Entry<String,Integer> entry : inFormula.mapAtomList.entrySet()) {
			mass = mass.add(this.queryAtom(entry.getKey()).multiply(entry.getValue()));
		}
		
//		System.out.println(inFormula.getRawString() + " - " + mass.toDouble());
		return mass;
	}
	
	/** 查询Formula的摩尔质量
	 * <p>由于该方法会忽略原子量表中的误差，建议使用{@link #queryMolarMass(Formula)}替代以取得完整的摩尔质量信息。</p>
	 * @param inFormula 欲查询的Formula对象
	 * @return double类型表示的Formula的摩尔质量
	 * @throws AtomNameNotFoundException
	 */
	public final double queryMolarMassDouble(Formula inFormula) throws AtomNameNotFoundException {// 查询某化学式的摩尔质量
		double mass = 0;
		
		for (Map.Entry<String,Integer> entry : inFormula.mapAtomList.entrySet()) {
			mass += (this.queryAtom(entry.getKey()).toDouble() * entry.getValue());
		}
		
//		System.out.println(inFormula.getRawString() + " - " + mass);
		return mass;
	}
}

