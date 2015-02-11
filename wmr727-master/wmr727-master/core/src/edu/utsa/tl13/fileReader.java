package edu.utsa.tl13;

import java.io.*;
/** K M Sabidur Rahman
 * Scanner uses this class to read from file avoiding white spaces.
 * 
 */ 
public class fileReader {
	public Reader scanFromFile;
	String tokenValue;
	
	public fileReader(	Reader scanFromFile) {
			this.scanFromFile= scanFromFile;
	}
	
	public String readFromFile(){


		try{

			StringBuilder strbld = new StringBuilder();			
			int pointer = scanFromFile.read();
				// whitespace at beginning
		        while ((pointer == '\n')  || (pointer == '\t') ||  (pointer == '\r')||(pointer == ' ')) {
		            pointer = scanFromFile.read();
		        }
		
		        while ((pointer != -1) && (pointer != ' ') && (pointer != '\t') && (pointer != '\n') && (pointer != '\r')) {
		            // ignore comments
		            if (pointer == '%') {
		                while ((pointer != -1) && (pointer != '\n' && pointer != '\r')) {  //  || (pointer != '\r')
		                    pointer = scanFromFile.read();
		                }
	
	                // ignore whitespace
	                while ((pointer == '\n')  || (pointer == '\t') ||  (pointer == '\r')||(pointer == ' ')) {
	                    pointer = scanFromFile.read();
	                }
	                continue;
	            }
	
	            strbld.append((char) pointer);
	            pointer = scanFromFile.read();
	        }
		    this.tokenValue = strbld.toString();


		} catch(IOException iox){
			 System.out.print("Token reading error found");
		}
	    return tokenValue;
	}
}
