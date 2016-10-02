package org.chembar.glockchem.core;

import org.chembar.glockchem.core.RMDatabase.AtomNameNotFoundException;

/** 化学方程式计算类
 * <p>用作化学方程式的简单比值计算</p>
 * @author DuckSoft
 */
public final class EquationCalculator {
	/** 内部的化学方程式
	 * <p>由构造函数{@link #EquationCalculator(Equation)}为其赋值。</p>
	 * @see #EquationCalculator(Equation)
	 */
	Equation equInner;
	/** 用于计算分子量的数据库 */
	RMDatabase dbRM = new RMDatabase();
	
	/** 构造函数
	 * <p>初始化一个化学方程式计算类。<br>
	 * 注意：该方程式<b>必须平衡</b>（即方程式左右两端必须<b>遵循质量守恒定律</b>）。<br/>
	 * 若方程式不平衡，程序会试图采用高斯消元法进行自动配平并用作计算。（该自动过程<b>会影响到传入的Equation对象</b>）<br>
	 * 若经过自动配平后仍无法平衡，程序会引发一个异常。</p>
	 * @param equToCalculate 用于计算的{@link Equation}对象
	 * @throws Exception 引发的异常
	 */
	public EquationCalculator(Equation equToCalculate) throws Exception {
		// 检查方程式是否已经平衡
		EquationBalancer balancer = new EquationBalancer(equToCalculate);
		
		if (balancer.checkBalance() == true) {
			// 已平衡则直接留用
			this.equInner = equToCalculate;
		} else {
			// 未平衡，尝试使用高斯消元法配平
			if (balancer.balanceGaussian() == false) {
				// 失败了
				throw new Exception("这方程式配不平，怎么算啦！吃屎啦您！");
			}
		}
		
	}
	
	/** 计算相应物质的质量
	 * <p></p>
	 * @param condition 提供的条件
	 * @param refItem 将被计算的物质
	 * @return 计算结果（质量）
	 * @throws AtomNameNotFoundException
	 */
	public AdvNum calcMass(EquationCondition condition, Pair<Formula, Integer> refItem) throws AtomNameNotFoundException {
		return condition.getConditionMass(this.dbRM)
				.divide(this.dbRM.queryMolarMass(condition.getConditionItem().getL()))
				.divide(condition.getConditionItem().getR())
				.multiply(this.dbRM.queryMolarMass(refItem.getL()))
				.multiply(refItem.getR());
	}
	
	/** 计算相应物质的物质的量
	 * <p></p>
	 * @param condition 提供的条件
	 * @param refItem 将被计算的物质
	 * @return 计算结果（物质的量）
	 * @throws AtomNameNotFoundException
	 */
	public AdvNum calcMole(EquationCondition condition, Pair<Formula, Integer> refItem) throws AtomNameNotFoundException {
		return condition.getConditionMole(this.dbRM)
				.divide(condition.getConditionItem().getR())
				.multiply(refItem.getR());
	}
	
	/** 计算条件（质量）
	 * <p>用于为{@link EquationCalculator}的计算提供条件。</p>
	 * @author DuckSoft
	 */
	public final static class EquationConditionMass implements EquationCondition {
		Pair<Formula, Integer> refItem;
		AdvNum massInner;
		
		public EquationConditionMass(Pair<Formula, Integer> refItem, AdvNum massInner) {
			this.massInner = massInner;
			this.refItem = refItem;
		}

		public AdvNum getConditionMass(RMDatabase rmDatabase) throws AtomNameNotFoundException {
			return this.massInner;
		}
		public AdvNum getConditionMole(RMDatabase rmDatabase) throws AtomNameNotFoundException {
			return this.massInner.divide(rmDatabase.queryMolarMass(this.refItem.getL()));
		}
		public Pair<Formula, Integer> getConditionItem() {
			return this.refItem;
		}
	}
	
	/** 计算条件（物质的量）
	 * <p>用于为{@link EquationCalculator}的计算提供条件。</p>
	 * @author DuckSoft
	 */
	public final static class EquationConditionMole implements EquationCondition {
		Pair<Formula, Integer> refItem;
		AdvNum moleInner;
		
		public EquationConditionMole(Pair<Formula, Integer> refItem, AdvNum moleInner) {
			this.moleInner = moleInner;
			this.refItem = refItem;
		}

		public AdvNum getConditionMass(RMDatabase rmDatabase) throws AtomNameNotFoundException {
			return this.moleInner.multiply(rmDatabase.queryMolarMass(this.refItem.getL()));
		}
		public AdvNum getConditionMole(RMDatabase rmDatabase) {
			return this.moleInner;
		}
		public Pair<Formula, Integer> getConditionItem() {
			return this.refItem;
		}
	}
	
	/** 计算条件
	 * <p>用于为{@link EquationCalculator}的计算提供条件。</p>
	 * @author DuckSoft
	 * @see EquationConditionMass
	 * @see EquationConditionMole
	 */
	public static interface EquationCondition {
		/** 获取条件中的质量*/
		public AdvNum getConditionMass(RMDatabase rmDatabase) throws AtomNameNotFoundException;
		/** 获取条件中的物质的量*/
		public AdvNum getConditionMole(RMDatabase rmDatabase) throws AtomNameNotFoundException;
		/** 获取条件中的整个表项*/
		public Pair<Formula, Integer> getConditionItem();
	}
}
