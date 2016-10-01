package core;

/** Pair
 * @author DuckSoft
 *
 * @param <L> Pair左侧类型
 * @param <R> Pair右侧类型
 * 
 */
public class Pair<L,R> {
    private L l;
    private R r;
    public String toString() {
    	return "( " + l.toString() + "," + r.toString() + ") ";
    }
    public Pair(L l, R r){
        this.l = l;
        this.r = r;
    }
    public L getL(){ return l; }
    public R getR(){ return r; }
    public void setL(L l){ this.l = l; }
    public void setR(R r){ this.r = r; }
}