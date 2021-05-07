/**
 * 
 * @author Name: Hanna Hayek            ID: 207442054
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.StringTokenizer;



public class MemoryManagement {

	public static void main(String[] args) {
		
		boolean useLru = args[0].matches("1");
		String inputFilename = args[1];
		String outputFilename = args[2];
		int mainMemorySize = Integer.parseInt(args[3]);
		int secondaryMemorySize = Integer.parseInt(args[4]);

		MemoryManagementSystem memory = new MemoryManagementSystem(mainMemorySize, secondaryMemorySize, useLru);
		File inFile = new File(inputFilename);
		File outFile = new File(outputFilename);
		try {
			Files.deleteIfExists(outFile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		manageMemory(inFile, outFile, memory);
				
	}

	public static void manageMemory(File inFile,
			File outFile, MemoryManagementSystem memory) {
		try {
			
			FileReader ifr = new FileReader(inFile);
			BufferedReader ibr = new BufferedReader(ifr);

			String line = "";
			while (line != null) {
				line = ibr.readLine();
				if (line != null) {
					StringTokenizer st = new StringTokenizer(line);
					while (st.hasMoreTokens()) {
						String token = st.nextToken().trim();
						if (token.equals("read")) {
							memory.read(Integer.parseInt(st.nextToken().trim()));
						}
						if (token.equals("write")) {
							int index = Integer.parseInt(st.nextToken().trim());
							char c = st.nextToken().trim().charAt(0);
							memory.write(index, c);
						}
						if (token.equals("print")) {
							writeToFile(outFile, memory.toString());
						}
					}
				}
			}

			ibr.close();
			ifr.close();
		}

		catch (Exception e) {
			System.out.println("Error \"" + e.toString() + "\" on file "
					+ outFile.getPath());
			e.printStackTrace();
			System.exit(-1); // brutally exit the program
		}

	}

	private static void writeToFile(File outFile, String toWrite) {

		try {
			FileWriter ofw = new FileWriter(outFile, true);

			// Writing to file
			ofw.append(toWrite);
			ofw.append("\n");
			ofw.close();
		} catch (Exception e) {
			System.out.println("Error \"" + e.toString() + "\" on file "
					+ outFile.getPath());
			e.printStackTrace();
			System.exit(-1); // brutally exit the program
		}

	}

}
