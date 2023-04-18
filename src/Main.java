import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		
		String name;
		do {
			System.out.print("Enter a valid name\n>");
			name = keyboard.nextLine();
		}while(!validName(name));
		
		String hashedName = hash(name);
		
		if(exists(hashedName)) {
			System.out.println("Name exists. Exiting.");
			System.exit(0);
		}
		
		String ssn;
		do{
			System.out.print("\nEnter an SSN (ex. 123-45-6789)\n>");
			ssn = keyboard.nextLine();
		}while(!validSsn(ssn));
		
		String hashedSsn = hash(ssn);
		
		if(exists(hashedSsn)) {
			System.out.println("SSN exists. Exiting.");
			System.exit(0);
		}

		//Append both hashes to a file separated by commas.
		save(hashedName + "," + hashedSsn);
		
		keyboard.close();
		System.exit(0);
	}
	
	private static void save(String line) {
		try {
			FileWriter fw = new FileWriter("db", true);
			PrintWriter pw = new PrintWriter(fw);
			
			pw.println(line);
			
			pw.close();
			
			System.out.println("Success.");
		}catch(Exception e) {
			System.out.println("Writing to file failed.");
			System.exit(5);
		}
	}
	
	private static boolean exists(String hash) {
		boolean result = false;
		
		try {
			File in = new File("db");
			Scanner file = new Scanner(in);
			
			while(file.hasNext()) {
				if(file.nextLine().contains(hash)) {
					result = true;
				}
			}
			
			file.close();
		} catch (Exception e) {
			//System.out.println("Warning: lookup failed, db missing.");
		}
		
		return result;
	}
	
	private static String hash(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
		    md.update(input.getBytes());
		    byte[] digest = md.digest();
		    
		    //to string
		    String result = String.format("%032x", new BigInteger(1, digest));
		    System.out.println("Hash result " + result);
		    
		    return result;
		}catch(Exception e) {
			System.out.println("Error hashing.");
			
			return null;
		}
	}
	
	/*
	 * Valid names are letters and spaces. Case does not matter.
	 * Max length is 255.
	 */
	private static boolean validName(String input) {
		if(input == null)
			return false;
		
		if(input.length() < 1)
			return false;
		
		if(input.length() > 256)
			return false;
		
		if(!isLetters(input))
			return false;
		
		return true;
	}
	
	/*
	 * Checks a string is only letters and spaces.
	 */
	private static boolean isLetters(String input) {
		Pattern p = Pattern.compile("^[a-zA-Z ]+$");
		Matcher m = p.matcher(input);
		
		return m.find();
	}
	
	/*
	 * Valid SSN is nnn-nn-nnnn. Where n is 0 through 9.
	 */
	private static boolean validSsn(String input) {
		if(input == null)
			return false;
		
		if(input.length() != 11)
			return false;
		
		if(!isNumbers(input))
			return false;
		
		if(input.charAt(3) != '-' || input.charAt(6) != '-')
			return false;
		
		return true;
	}
	
	/*
	 * Checks a string is only numbers and dashes.
	 */
	private static boolean isNumbers(String input) {
		Pattern p = Pattern.compile("^[0-9-]+$");
		Matcher m = p.matcher(input);
		
		return m.find();
	}
	
}
