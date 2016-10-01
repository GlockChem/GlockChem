package core;

public class AdvNum {
	// 内部数据
	double numInner;
	double numMax;
	double numMin;
	
	// 构造函数
	public AdvNum() {// 默认构造函数
		this.set(0);
	}
	public AdvNum(double numInit) {// 普通数
		this.set(numInit);
	}
	public AdvNum(double numCenter, double numError) {// 手动指定误差
		this.numInner = numCenter;
		this.numMin = numCenter - numError;
		this.numMax = numCenter + numError;
	}
	public AdvNum(double numCenter, double numInMin, double numInMax) {// 指定最大最小数
		this.numInner = numCenter;
		this.numMin = numInMin;
		this.numMax = numInMax;
	}
	
	// 操作函数
	public AdvNum set(double numToSet) {// 赋值
		numInner = numMax = numMin = numToSet;
		return this;
	}
	public AdvNum centerize() {// 中心化
		this.numInner = this.getCenterizedNumber();
		return this;
	}
	public double getCenterizedNumber() {// 获取中心化数
		return (this.numMin + this.numMax) / 2;
	}
	public double getValue() {
		return this.numInner;
	}
	
	// 运算函数
	public AdvNum add(AdvNum numIn) {// 加
		this.numInner += numIn.numInner;
		this.numMin += numIn.numMin;
		this.numMax += numIn.numMax;
		
		return this;
	}
	public AdvNum subtract(AdvNum numIn) {// 减
		this.numInner -= numIn.numInner;
		this.numMin -= numIn.numMin;
		this.numMax -= numIn.numMax;
		
		return this;
	}
	public AdvNum multiply(AdvNum numIn) {// 乘
		this.numInner *= numIn.numInner;
		this.numMin *= numIn.numMin;
		this.numMax *= numIn.numMax;
		
		return this;
	}
	public AdvNum divide(AdvNum numIn) {// 除
		this.numInner /= numIn.numInner;
		this.numMin /= numIn.numMax;
		this.numMax /= numIn.numMin;
		
		return this;
	}
	
	public AdvNum add(double numIn) {// 加
		this.numInner += numIn;
		this.numMin += numIn;
		this.numMax += numIn;
		
		return this;
	}
	public AdvNum subtract(double numIn) {// 减
		this.numInner -= numIn;
		this.numMin -= numIn;
		this.numMax -= numIn;
		
		return this;
	}
	public AdvNum multiply(double numIn) {// 乘
		this.numInner *= numIn;
		this.numMin *= numIn;
		this.numMax *= numIn;
		
		return this;
	}
	public AdvNum divide(double numIn) {// 除
		this.numInner /= numIn;
		this.numMin /= numIn;
		this.numMax /= numIn;
		
		return this;
	}
}
