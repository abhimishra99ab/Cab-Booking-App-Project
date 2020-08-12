package application;

public class travel 
{
	String[] locations = {"Secunderabad", "BPHC" , "Banjara Hills", "Panjagutta", "Jubilee Hills", "Hi-Tech city", "Nampally", "Golconda Fort"};
	int[][] distances = {{0, 25, 11, 9, 13, 20, 9, 18},
						 {25, 0, 31, 30, 35, 38, 31, 40},
						 {11, 31, 0, 4, 5, 9, 6, 10},
						 {9, 30, 4, 0, 6, 10, 6, 10},
						 {13, 35, 5, 6, 0, 6, 10, 9},
						 {20, 38, 9, 10, 6, 0, 9, 13},
						 {9, 31, 6, 6, 10, 9, 0, 10},
						 {18, 40, 10, 10, 9, 13, 10, 0}};
	public int dist_bw_loc(String loc1, String loc2)
	{
		int i1 = index_in_loc(loc1);
		int i2 = index_in_loc(loc2);
		return distances[i1][i2];
	}
	public int index_in_loc(String loc)
	{
		for(int i=0 ; i<8 ; i++)
		{
			if(loc.equals(locations[i]))
			{
				return i;
			}
		}
		return -1;
	}
	
}
