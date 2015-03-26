package proj3;

public final class Coordinate {
	private int rows;
	private int cols;
	//constructor
	public Coordinate(int rows, int cols){
		this.rows=rows;
		this.cols=cols;
	}
	//accessor for cols
	public int getColumn(){
		return cols;
	}
	//accessor for rows
	public int getRow(){
		return rows;
	}
	//Accesses pos
	public int getPos(){
		return (rows*5)+cols;
	}
	
	//public static void main(String[] args){
		
	//}
}
