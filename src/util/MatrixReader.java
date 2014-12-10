package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MatrixReader
{

	public static String FILE_PATH = "C:\\Users\\XY\\Desktop\\OutPut\\After\\matrix";

	@SuppressWarnings("resource")
	public static double[][] getMaxtrix(int index)
	{
		String path = FILE_PATH + index + ".txt";

		FileReader fileReader = null;
		BufferedReader bufferedReader;

		try
		{
			fileReader = new FileReader(path);
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bufferedReader = new BufferedReader(fileReader);
		try
		{
			int count = 0;
			String line = bufferedReader.readLine();

			int row = Integer.parseInt(line);
			int col = 2;
			double[][] matrix = new double[row][col];

			line = bufferedReader.readLine();
			while (line != null)
			{
				String[] nums = line.split(" ");
				matrix[count][0] = Double.parseDouble(nums[0]);
				matrix[count][1] = Double.parseDouble(nums[1]);
				line = bufferedReader.readLine();
				count++;
			}
			return matrix;

		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
