package edu.utsa.tl13;

/** K M Sabidur Rahman
 * This part of the program will read from file and tokenize
 * 
 */ 

import java.util.regex.Pattern;
import java.io.*;



public class Scanner {
//	public String fileName;
	public Reader scanFromFile;

	
	public Scanner(String fileName) {
      try{
    	  this.scanFromFile = new FileReader(fileName);

	    } catch (FileNotFoundException ex) {
	        System.out.print("Input File not found");
	    }

	}
	
	public Token scanToken()
	{

			fileReader myreader= new fileReader(scanFromFile);

			
			String tokenValue= myreader.readFromFile();		
            Parser.tokenValue = tokenValue;
            //System.out.println("TOKEN = " + tokenValue);

            if (tokenValue.length() < 1) {
                return Token.EOF;
            }

           
            switch(tokenValue){
            
            case "program":
                return Token.PROGRAM;
            case "var":
                return Token.VAR;
            case "as":
                return Token.AS;
               // break;
            case "int":
                return Token.INT;
               // break;
            case "bool":
                return Token.BOOL;
                //break;
            case "(":
                return Token.LP;                
               // break;
            case ")":
                return Token.RP;
               // break;
            case":=":
                return Token.ASGN;
               // break;
            case ";":
                return Token.SC;
               // break;
            case "*":
                return Token.OP2;
                //break;
            case "div":
                return Token.OP2;
                //break;
            case "mod":
                return Token.OP2;
                //break;
            case "+":
                return Token.OP3;
            	//break;
            case "-":
                return Token.OP3;
               // break;
            case "=":
                return Token.OP4;
            	//break;
            case "!=":
                return Token.OP4;
            	//break;
            case "<":
                return Token.OP4;
            	//break;
            case ">" :
                return Token.OP4;
            	//break;
            case "<=":
                return Token.OP4;
            	//break;
            case ">=":
                return Token.OP4;
            	//break;
            case "if":
                return Token.IF;            	
                //break;
            case "then":
                return Token.THEN;
            	//break;
            case "else":
                return Token.ELSE;
            	//break;
            case "begin":
                return Token.BEGIN;
            case "end":
                return Token.END;
            case "while":
                return Token.WHILE;
            case "do":
                return Token.DO;
            
            case "writeInt":
                return Token.WRITEINT;
            case "readInt":
                return Token.READINT;
            case "true":
                return Token.boollit;
            case "false":
                return Token.boollit;
                
            case "[":
                return Token.ArrayLeft;               
            case "]":
                return Token.ArrayRight;
            case "val":
            	return Token.VAL;
            case ",":
            	return Token.Comma;
                
            }
            //matching patterns
            if (Pattern.matches("[-]*[1-9][0-9]*", tokenValue) || tokenValue.equals("0")) {
                return Token.num;
            } 
           // else if (Pattern.matches("[A-Z][A-Z0-9]*", tokenValue)) {
             //   return Token.ident; }
            
            else if (Pattern.matches("[A-Z][A-Z0-9]*.*", tokenValue)) {
               return Token.ident; }
		//If no token matches
		return Token.INVALID;
		
      }
}

