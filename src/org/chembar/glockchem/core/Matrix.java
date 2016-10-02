package org.chembar.glockchem.core;
// 并未完成
// TODO:完善

/** 矩阵类
 * <p>为配合{@link EquationBalancer#balanceGaussian()}而写的一个矩阵的简易实现。</p>
 * @author DuckSoft
 */
public final class Matrix {
	public int[][] matrix;
	
	/**构造函数
	 * <p>构造一个新的矩阵类。<br>
	 * 注意：矩阵的行数和列数必须为正数！</p>
	 * @param lines 新矩阵的行数。
	 * @param cols 新矩阵的列数。
	 */
	public Matrix(int lines, int cols) {
		assert((lines > 0) && (cols > 0));
		// 初始化矩阵
		matrix = new int[lines][cols];
	}
	
	public Matrix(int[][] matArray) {
		this.matrix = matArray;
	}
	
	public final int[][] toArray() {
		return this.matrix;
	}
	
	public final String toString() {
		String strTemp = new String();
		for (int ln=0; ln<this.matrix.length; ln++) {
			for (int col=0; col<this.matrix[0].length; col++) {
				strTemp += "[";
				strTemp += this.matrix[ln][col];
				strTemp += "]";
			}
			strTemp += "\n";
		}
		
		return strTemp;
	}
	
	public final void swapLines(int ln1, int ln2, int pos) {
	    for (int i = pos; i < matrix[ln1].length; ++i) {
	        int tmp = matrix[ln1][i];
	        matrix[ln1][i] = matrix[ln2][i];
	        matrix[ln2][i] = tmp;
	    }
	}
}
