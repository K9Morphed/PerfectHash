//@MattElcock1

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.util.*;

class perfectHash
{
	public static void main(String[] args)
	{
		//Initialise arrays
		ArrayList<Integer> keys = new ArrayList<Integer>();
		ArrayList<String> operations = new ArrayList<String>();
		ArrayList<Integer> hashValues = new ArrayList<Integer>();
		ArrayList<Integer> inHashValues = new ArrayList<Integer>();
		ArrayList<Integer> valsAtLoc = new ArrayList<Integer>();
		ArrayList<Integer> sizeOfInHa = new ArrayList<Integer>();

		//Initialise beggining outer hash function parameters

		int a = 13;
		int b = 1207;
		int p = 40487;

		//Initialise counters
		int i = 0;
		int collisions = 0;
		int numTesHash = 0;

		//Retrieve data

		try
		{
			//Insert all keys

			Scanner inKeys = new Scanner(new File(args[0]));

			while (inKeys.hasNextLine())
			{
				keys.add(Integer.parseInt(inKeys.nextLine()));
				i++;
			}

			i = 0; //Reset counter

			//Insert all opperations

			Scanner inOperations = new Scanner(new File(args[1]));

			while (inOperations.hasNextLine())
			{
				operations.add(inOperations.nextLine());
				i++;
			}
		}
		catch (Exception e)
		{
			System.out.println("ERROR: " + e);
		}

		//Output the above steps
		System.out.println("SETTING HASH TABLE SIZE: " + keys.get(0));
		System.out.print("READ SET OF KEYS: ");

		for(i = 1; i < (keys.size()); i++)
		{
			System.out.print(keys.get(i) + " ");
		}

		System.out.println("\nINITIAL OUTER HASH FUNCTION PARAMETERS: a = " + a + "; b = " + b + "; p = " + p);

		//Look for collisions

		System.out.print("HASHED TO OUTER HASH TABLE AT: ");

		for(i = 1; i < (keys.size()); i++)
		{
			int temp = outerFunction(a, b, p, keys.get(0), keys.get(i));
			System.out.print(temp + " ");
			hashValues.add(temp);
		}

		System.out.println("\n");

		do
		{

			//Caluclate hash locations

			numTesHash++;
			collisions = 0; //Reset the number of colisions
			hashValues.clear(); //Reset the contents of the has values

			for(i = 1; i < (keys.size()); i++)
			{
				hashValues.add(outerFunction(a, b, p, keys.get(0), keys.get(i)));
			}

			for(i = 0; i < hashValues.size(); i++)
			{
				for(int j = i + 1; j < hashValues.size(); j++)
				{
					if(hashValues.get(i).intValue() == hashValues.get(j).intValue())
					{
						collisions++;
					}
				}
			}
			

			System.out.println("NUMBER OF PAIRS OF COLLISIONS IN OUTER HASH TABLE: " + collisions);

			if (collisions > keys.get(0))
			{
				a++;
				b = b + 177;

			}
				
			if (a < 1 || a > p-1)
			{
				a = a % a + 1;
			}

			if (b < 0 || b > p-1)
			{
				b = b % b;
			}

		} while (collisions > keys.get(0));

		if (numTesHash == 1)
		{
			System.out.print(numTesHash + " OUTER HASH FUNCTION TESTED");
		}
		else
		{
			System.out.print(numTesHash + " OUTER HASH FUNCTIONS TESTED");			
		}

		if(a != 13 || b != 1207 || p != 40487)
		{
			System.out.println("\nMODIFIED OUTER HASH FUNCTION PARAMETERS: a = " + a + "; b = " + b + "; p = " + p);
			System.out.print("MODIFIED HASHING TO OUTER HASH TABLE AT: ");
			for(i = 1; i < (keys.size()); i++)
			{
				System.out.print(outerFunction(a, b, p, keys.get(0), keys.get(i)) + " ");
			}
		}

		System.out.println("\n\nKEYS GROUPED ONTO SLOTS:");
		int temp = 0;
		for (i = 0; i < keys.get(0); i++)
		{
			System.out.print("grouping slot " + i + ": ");
			for(int j = 1; j < keys.get(0) + 1; j++)
			{
				if(hashValues.get(j - 1) == i)
				{
					System.out.print(keys.get(j) + " ");
					temp++;
				}
			}
			valsAtLoc.add(temp);
			temp = 0;
			System.out.print("\n");
		}

		//Create empty hash

		for (i = 0; i < valsAtLoc.size(); i++)
		{
			sizeOfInHa.add(valsAtLoc.get(i)*valsAtLoc.get(i));
		}

		int[][] hash = new int[keys.get(0)][];

		for (i = 0; i < hash.length; i++)
		{
			hash[i] = new int[valsAtLoc.get(i) * valsAtLoc.get(i)];
			for (int j = 0; j < hash[i].length; j++)
			{
				hash[i][j] = -1;
			}
		}

		//Create a 2D Array with each inner thing being the hash function

		int[][] inHash = new int[keys.get(0)][2];

		for (i = 0; i < inHash.length; i++)
		{
			inHash[i][0] = 13;
			inHash[i][1] = 1207;
		}

		List<List<Integer>> invalsAtLoc = new ArrayList<List<Integer>>();

		//Calculate hash locations for inner hash
		do
		{
			collisions = 0;
			invalsAtLoc.clear();

			for (i = 0; i < keys.get(0); i++)
			{
				List<Integer> list = new ArrayList<>();
				for(int j = 1; j < keys.get(0) + 1; j++)
				{
					if(hashValues.get(j - 1) == i)
					{				
						 temp = outerFunction(inHash[i][0], inHash[i][1], p, hash[i].length, keys.get(j));
						 list.add(temp);
					}
				}
				invalsAtLoc.add(list);
			}

			//System.out.print(invalsAtLoc);

			for (i = 0; i < invalsAtLoc.size(); i++)
			{
				for (int j = 0; j < invalsAtLoc.get(i).size(); j++)
				{
					if (invalsAtLoc.get(i).size() != 0 && invalsAtLoc.get(i).size() != 1)
					{
						for (int k = j+1; k < invalsAtLoc.get(i).size(); k++)
						{
							if (invalsAtLoc.get(i).get(j) == invalsAtLoc.get(i).get(k))
							{
								inHash[i][0] = inHash[i][0] + 1;
								inHash[i][1] = inHash[i][1] + 177;
								collisions++;
							}

							if (inHash[i][0] < 1 || inHash[i][0] > p-1)
							{
								inHash[i][0] = inHash[i][0] % inHash[i][0] + 1;
							}

							if (inHash[i][1] < 0 || inHash[i][1] > p-1)
							{
								inHash[i][1] = inHash[i][1] % inHash[i][1];
							}
						}					
					}

				}		
			}	
		} while (collisions > 0);

		System.out.println();

		for (i = 0; i < inHash.length; i++)
		{
			if (inHash[i][0] != 13 || inHash[i][1] != 1207)
			{
				System.out.println("slot " + i + "; MODIFIED INNER LEVEL HASH FUNCTION PARAMETERS: a = " + inHash[i][0] + "; b = " + inHash[i][1] + "; p = " + p);
			}
		}

		for (i = 0; i < operations.size(); i++)
		{
			System.out.println("\nPERFORM OPERATION: " + operations.get(i));
			if (operations.get(i).contains("insert"))
			{
				for (int j = 1; j < keys.size(); j++)
				{
					if (operations.get(i).matches("insert " + keys.get(j).toString()))
					{
						int outer = outerFunction(a, b, p, keys.get(0), keys.get(j));
						int inner = outerFunction(inHash[outer][0], inHash[outer][1], p, hash[outer].length, keys.get(j));
						hash[outer][inner] = keys.get(j);
						System.out.println("HASH KEY TO OUTER SLOT: " + outer + ", INNER SLOT: " + inner);
						
						for (int k = 0; k < keys.get(0); k++)
						{
							System.out.print("operation slot " + k + ": ");
							System.out.println(Arrays.toString(hash[k]).replace("[", "").replace("]", "").replace(",", " "));
						}
					}
				}	
			}

			if (operations.get(i).contains("locate")) //If the operation contains "locate"
			{
				for (int j = 1; j < keys.size(); j++) //For every key
				{
					if (operations.get(i).matches("locate " + keys.get(j).toString())) //If the key is in the operation (if we need to look for that key)
					{

						int outer = outerFunction(a, b, p, keys.get(0), keys.get(j));
						int inner = outerFunction(inHash[outer][0], inHash[outer][1], p, hash[outer].length, keys.get(j));
						System.out.println("LOCATED KEY = " + keys.get(j) + " at: " + outer + ", "  + inner);
					}
				}
			}

			if (operations.get(i).contains("delete")) //If the operation contains "locate"
			{
				for (int j = 1; j < keys.size(); j++) //For every key
				{
					if (operations.get(i).matches("delete " + keys.get(j).toString())) //If the key is in the operation (if we need to look for that key)
					{

						int outer = outerFunction(a, b, p, keys.get(0), keys.get(j));
						int inner = outerFunction(inHash[outer][0], inHash[outer][1], p, hash[outer].length, keys.get(j));
						System.out.println("LOCATED KEY = " + keys.get(j) + " at: " + outer + ", "  + inner);

						hash[outer][inner] = -1;

						for (int k = 0; k < keys.get(0); k++)
						{
							System.out.print("operation slot " + k + ": ");
							System.out.println(Arrays.toString(hash[k]).replace("[", "").replace("]", "").replace(",", " "));
						}
					}
				}
			}

		}
	}

	public static int outerFunction(int a, int b, int p, int m, int k)
	{
		int position = ((a*k+b)%p)%m;
		return position;
	}

}