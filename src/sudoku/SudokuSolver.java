/* Author: Nisha Chaudhari
 */ 
package sudoku;
 
import java.util.ArrayList;

public class SudokuSolver {
	int[][] matrix;
	int N;
	// we have N*N matrix
	public SudokuSolver(int[][] matrix){
		this.matrix = matrix;
		N = matrix.length;
	}

	int[][] solveSudoku(){
		// check  if the given input matrix is valid
		boolean valid = checkValidInput();
		if(valid){
			// solve the Sudoku.Start from 0,0 position
			boolean solved = fillMatrix(0,0);
			return matrix;
		}else{
			return null;
		}
	}

	// Backtracking Technique:
	// Matrix is filled recursively 
	// If we reach a dead end, we backtrack and try other solution 
	boolean fillMatrix(int row,int col){
		// Move to next row
		if(col == N){
			row++;	
			//matrix is filled completely.		
			if(row == N && col == N){
				return true;
			}
			col = 0;
		}		
		// If the cell is empty then fill it else move to next cell
		if(matrix[row][col] == 0){
			// Get all possible values for a cell		
			ArrayList<Integer> values = getPossibleValuesForCell(row,col);

			// Fill the cell with one possible value and move to next cell
			// done = true indicates remaining matrix can be filled with values.get(i)
			// done = false indicates remaining matrix reaches dead end if we use values.get(i)
			for(int i=0;i<values.size();i++){
				matrix[row][col] = values.get(i);
				boolean done = fillMatrix(row,col+1);
				if(done){
					return done;
				}else{
					// We reached dead end.So revert the filled value and try another possible value 
					matrix[row][col] = 0;
				}				
			}
		}else{
			boolean done = fillMatrix(row,col+1);
			return done;							
		}
		return false;
	}

	// This method returns all possible values for a cell(row,col)
	ArrayList<Integer> getPossibleValuesForCell(int row,int col){
		boolean[] usedNumbers = new boolean[N+1];

		// usedNumbers array contains true for all used numbers from given row,column and box		
		// used numbers from row row
		for(int i=0;i<N;i++){
			if(matrix[i][col] != 0){
				usedNumbers[matrix[i][col]] = true;
			}
		}

		// used numbers from column col
		for(int i=0;i<N;i++){
			if(matrix[row][i] != 0){
				usedNumbers[matrix[row][i]] = true;
			}
		}

		// given cell(row,col) lies in Box b
		// find start and end indices for b
		int boxSize = (int) Math.sqrt(N);
		int rowStart =-1;
		int rowEnd = -1;
		for(int i=0;i<N ;i+=boxSize){
			int j= i+boxSize -1;
			if(row >= i && row <= j){
				rowStart = i;
				rowEnd = j;
			}
		}
		int colStart =-1;
		int colEnd = -1;
		for(int i=0;i<N ;i+=boxSize){
			int j= i+boxSize -1;
			if(col >= i && col <= j){
				colStart = i;
				colEnd = j;
			}
		}

		// find used numbers from box
		for(int i=rowStart;i<=rowEnd;i++){
			for(int j=colStart;j<=colEnd;j++){				
				if(matrix[i][j] != 0){
					usedNumbers[matrix[i][j]] = true;
				}
			}
		}
		ArrayList<Integer> possibleValues= new ArrayList<Integer>();
		// put non used numbers in possibleValues arraylist
		for(int i=1;i<usedNumbers.length;i++){
			if(!usedNumbers[i]){
				possibleValues.add(i);
			}
		}
		return possibleValues;
	}

	//Check if the given Sudoku input is valid
	boolean checkValidInput(){
		// boxes array contains N boxes and numbers in each box
		// rows array contains N rows and numbers in each row
		// cols array contains N columns and numbers in each column
		boolean[][] boxes = new boolean[N][N+1];
		boolean[][] rows = new boolean[N][N+1];
		boolean[][] cols = new boolean[N][N+1];
		int boxRow = 0;
		int boxCol = 0;
		int boxIndex = 0;
		int boxSize = (int)Math.sqrt(N);
		int oldNumber = 0;	
		
		try{
			//Traverse the matrix and see if any number is duplicate in a row/column/box
			for(int i=0;i<N;i++){
				if(boxRow<boxSize){
					boxIndex = oldNumber;
				}else if(boxRow == boxSize ){
					boxRow = 0;
					boxIndex++;
				}
				oldNumber = boxIndex;
				boxCol = 0;
				
				for(int j=0;j<N;j++){
					if(boxCol == boxSize){
						boxCol = 0;
						boxIndex++;
					}
					if(matrix[i][j] != 0){
						if(matrix[i][j] < 0 || matrix[i][j] > N){
							throw new SudokuException("Invalid input number "+matrix[i][j]);
						}
						// Return false if there are duplicate number in a row OR duplicate number in a column OR duplicate number in a box
						if(boxes[boxIndex][matrix[i][j]] || rows[i][matrix[i][j]] || cols[j][matrix[i][j]]){
							throw new SudokuException("Given Sudoku input contains duplicate element in same row/column/box element: "+matrix[i][j]);
						}
						boxes[boxIndex][matrix[i][j]] = true ;
						rows[i][matrix[i][j]] = true;
						cols[j][matrix[i][j]] = true;
					}
					boxCol++;
				}
				boxRow++;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}

	public static void main(String[] args){
		String inputFile = "C:\\workspace\\test\\src\\sudokuInput.csv";
		String outputFile = "E:\\output.csv";

		CSVHandler c = new CSVHandler(); 
		int[][] matrix = c.readCSV(inputFile);
		if(matrix != null){
			SudokuSolver s = new SudokuSolver(matrix);	
			matrix= s.solveSudoku();
			c.writeCSV(matrix, outputFile);
		}
	}
}