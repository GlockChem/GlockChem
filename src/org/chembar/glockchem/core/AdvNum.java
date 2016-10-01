package org.chembar.glockchem.core;

/** AdvNum - 带误差计算的双精度类型
 * @author DuckSoft
 * @version 0.1
 */
public class AdvNum {
	// === 内部数据 ===
	/** AdvNum的内部数值。
	 * @see #numInner
	 * @see #numMin
	 * @see #numMax
	 * */
	double numInner;
	/** AdvNum的最大值。
	 * @see #numInner
	 * @see #numMin
	 * @see #numMax
	 * */
	double numMax;
	/** AdvNum的最小值。
	 * @see #numInner
	 * @see #numMin
	 * @see #numMax
	 * */
	double numMin;
	
	// === 类型转换 ===
	/** 将AdvNum对象转为String
	 * @return 所得String
	 */
	public String toString() {
		return "{" + String.valueOf(this.numInner) + 
				"; " + String.valueOf(this.numMin) + 
				", " + String.valueOf(this.numMax) + "}";
	}
	/** 将AdvNum对象转为double
	 * @return AdvNum对象的内部数值{@link #numInner}
	 */
	public double toDouble() {
		return this.numInner;
	}
	// === 构造函数 ===
	/** 构造函数
	 * <p>该构造函数可以构造一个空的AdvNum。</p>
	 * @see #AdvNum()
	 * @see #AdvNum(double)
	 * @see #AdvNum(double, double)
	 * @see #AdvNum(double, double, double)	 */ 
	public AdvNum() {
		super();
	}
	/** 构造函数
	 * <p>该构造函数可以构造某一双精度值的AdvNum。</p>
	 * @param numIn 所需写入所得AdvNum中的双精度值
	 * @see #AdvNum()
	 * @see #AdvNum(double)
	 * @see #AdvNum(double, double)
	 * @see #AdvNum(double, double, double)	 */ 
	public AdvNum(double numIn) {
		this.numInner = this.numMax = this.numMin = numIn;
	}
	/** 构造函数
	 * <p>该构造函数可以构造以某一双精度数为中心产生误差的AdvNum。</p>
	 * @param numCenter 所需写入的中心数值
	 * @param numError （<b>请给定正值</b>）为中心数值指定的误差数值
	 * @see #AdvNum()
	 * @see #AdvNum(double)
	 * @see #AdvNum(double, double)
	 * @see #AdvNum(double, double, double)	 */ 
	public AdvNum(double numCenter, double numError) {
		// 误差值必须为非负数
		assert(numError >= 0);
		
		this.numInner = numCenter;
		this.numMin = numCenter - numError;
		this.numMax = numCenter + numError;
	}
	/** 构造函数
	 * <p>该构造函数可以按用户的想法构建AdvNum。</p>
	 * <p><b>注意最大值务必要大于最小值！</b></p>
	 * @param numCenter 所需写入的中心数值
	 * @param numInMin 为中心数值指定的最小值
	 * @param numInMax 为中心数值指定的最大值
	 * @see #AdvNum()
	 * @see #AdvNum(double)
	 * @see #AdvNum(double, double)
	 * @see #AdvNum(double, double, double)	 */ 
	public AdvNum(double numCenter, double numInMin, double numInMax) {
		// 务必最大值大于最小值
		assert(numInMin <= numInMax);
		
		this.numInner = numCenter;
		this.numMin = numInMin;
		this.numMax = numInMax;
	}
	
	// === 基本运算函数 ===
	public AdvNum add(AdvNum numIn) {
		double numInner = this.numInner + numIn.numInner;
		double numMin = this.numMin + numIn.numMin;
		double numMax = this.numMax + numIn.numMax;
		
		return new AdvNum(numInner, numMin, numMax);
	}
	public AdvNum add(double numIn) {
		return add(new AdvNum(numIn));
	}
	public AdvNum subtract(AdvNum numIn) {
		double numInner = this.numInner - numIn.numInner;
		double numMin = this.numMin - numIn.numMax;
		double numMax = this.numMax - numIn.numMin;
		
		return new AdvNum(numInner, numMin, numMax);
	}
	public AdvNum subtract(double numIn) {
		return subtract(new AdvNum(numIn));
	}
	public AdvNum multiply(AdvNum numIn) {
		double numInner = this.numInner * numIn.numInner;
		double numMin = this.numMin * numIn.numMin;
		double numMax = this.numMax * numIn.numMax;
		
		return new AdvNum(numInner, numMin, numMax);
	}
	public AdvNum multiply(double numIn) {
		return multiply(new AdvNum(numIn));
	}
	public AdvNum divide(AdvNum numIn) {
		double numInner = this.numInner / numIn.numInner;
		double numMin = this.numMin / numIn.numMax;
		double numMax = this.numMax / numIn.numMin;
		
		return new AdvNum(numInner, numMin, numMax);
	}
	public AdvNum divide(double numIn) {
		return divide(new AdvNum(numIn));
	}

	// === 操作函数 ===
	public AdvNum set(double numToSet) {
		this.numInner = this.numMin = this.numMax = numToSet;
		return this;
	}
	
	/** 获取AdvNum的中心化数
	 * <p>注意该方法仅获取中心化数，并不使得AdvNum中心化。<br>
	 * 若要令某个AdvNum中心化，请参见{@link #Centerize()}方法。</p>
	 * @see #Centerize()
	 * @return 中心化数——即该数最大值与最小值的平均数
	 */
	public double getCenterizedNumber() {
		return (this.numMin + this.numMax) / 2;
	}
	
	/** 令AdvNum中心化
	 * <p>本方法可令AdvNum中心化，即使其内部数据变为该AdvNum最大值与最小值的平均数。<br>
	 * 若仅需获取某AdvNum的中心化数，请参见{@link #getCenterizedNumber()}方法。</p>
	 * @see #getCenterizedNumber()
	 * @return 所得AdvNum
	 */
	public AdvNum Centerize() {
		this.numInner = this.getCenterizedNumber();
		return this;
	}
}
