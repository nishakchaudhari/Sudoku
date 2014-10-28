/* Author: Nisha Chaudhari
 */ 
package sudoku;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// This class handles CSV read and writes
public class CSVHandler {	
	// reads the given CSV file into matrix
	int[][] readCSV(String inputFile){
		String line = "";
		int N = 0;
		BufferedReader br = null;
		int rowIndex = 0;
		int[][] matrix = null;
		try{
			br = new BufferedReader(new FileReader(inputFile));
			while ((line = br.readLine()) != null) {				
				String[] row = line.split(",");		
				if(N == 0){
					N = row.length;
					matrix = new int[N][N];
				}else if(N != row.length){
					throw new SudokuException("All the rows do no have same number of elements.");
				}
				for(int i=0;i<N;i++){
					matrix[rowIndex][i] = Integer.parseInt(row[i]);
				}
				rowIndex++;
			}
			if(rowIndex != N){
				throw new SudokuException("No. of rows and columns do not match.");
			}
		}catch( IOException | NumberFormatException | SudokuException e){
			e.printStackTrace();
		}finally{
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return matrix;
	}
	
	//Writes given matrix to CSV file
	void writeCSV(int[][] matrix,String outputFile){
		if(matrix != null){
			int N= matrix.length;
			try
			{
				// write all matrix lines to csv file.Each row values will be separated by ,
				FileWriter writer = new FileWriter(outputFile);
				for(int i=0;i<N;i++){
					if(i>=1){
						writer.append('\n');
					}
					int j;
					for (j=0;j<N-1;j++){
						writer.append(matrix[i][j]+",");				    
					}
					writer.append(String.valueOf(matrix[i][j]));		    	
				}		    
				writer.flush();
				writer.close();
			}
			catch(IOException e){
				e.printStackTrace();
			} 
		}
	}
}