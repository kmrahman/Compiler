package edu.utsa.tl13;

import java.io.IOException;
import java.util.ArrayList;

/** K M Sabidur Rahman
 * This part of the program will parse according to grammar and write to .pt file using MyFileWriter
 * 
 */ 

public class Parser {

	    private Scanner scanner;
	    private MyFileWriter fileWriter;

	    private Token token;
	    private int nodeNumber;
	    public static String tokenValue;
	    //AST Nodes
	    public ProgramNode programNode;
	    public DeclarationListNode dcltList;
	    public StatementListNode stmtList;
	    public SymbolTable symbolTable;

      
	    

	    public Parser(Scanner scanner, MyFileWriter fileWriter) {
	        this.scanner = scanner;
	        this.fileWriter = fileWriter;
	        


	    }

	    public void parseProgram() throws IOException {

	        this.token = scanner.scanToken();
	        this.nodeNumber = 1;
	        int nodeId=1;
	       
	        
	        this.fileWriter.writeToFile("program", this.nodeNumber);
	        
	        if (this.token == Token.PROGRAM) {
	            this.nodeNumber++;
	            fileWriter.writeToFile("PROGRAM", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);

	            this.token = scanner.scanToken();
	            this.nodeNumber++;
	            fileWriter.writeToFile("declarations", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);
	            
	            //AST//
	            this.symbolTable = new SymbolTable();
	            this.programNode = new ProgramNode();
	            this.dcltList = new DeclarationListNode();
	            this.programNode.addDeclList(this.dcltList);
	            //////
	            
	            parseDeclarations(this.nodeNumber);

	            if (this.token == Token.BEGIN) {
	                this.nodeNumber++;
	                fileWriter.writeToFile("BEGIN", this.nodeNumber);
	                fileWriter.writeToFile(nodeId, this.nodeNumber);

	                this.token = scanner.scanToken();
	                this.nodeNumber++;
	                fileWriter.writeToFile("statementSequence", this.nodeNumber);
	                fileWriter.writeToFile(nodeId, this.nodeNumber);
	                
	                ////AST//
	                this.stmtList = new StatementListNode();
	                this.programNode.addStmtList(this.stmtList);
	                /////
	                
	                parseStatementSequence(this.nodeNumber, this.stmtList);

	                if (this.token == Token.END) {
	                    this.nodeNumber++;
	                    fileWriter.writeToFile("END", this.nodeNumber);
	                    fileWriter.writeToFile(nodeId, this.nodeNumber);

	                    this.token = scanner.scanToken();

	                    if (this.token == Token.EOF) {
	                        System.out.println("Parsing complete");
	                    } else {
	                        System.err.println("Parser Error: Extra line(s) after the program");
	                    }
	                    
	                    //AST////
	                    
                        Boolean type =  programNode.typeChecking();
                        programNode.draw(1);
                       ///////////////////////////////////////
                        
                        if(type)
                        {
                        	///////////////ILOCOperations
	                        System.out.println("Type checking OK; Building CFG and MIPS.. ");
                        	
                        	Block b = new Block();
                			b = ILOC.ILOCDeclarationList(b, dcltList);
                			Block end = ILOC.ILOCStatementList(b, stmtList);
                			Compiler.cfgWriter.last_block = end;

                			
                			try {
								ILOC.PrintILOCCode(b);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                			try {
								CFGWriter.completeWriting(b,end);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

                			
                			try {
                				SSAForm.printDominenece(b);
                				SSAForm.printFrontier(b);
                				
                    			ILOC.WriteMIPSCode(b);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                			
                			try {
                				Compiler.mipsWriter.completeWriting();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

                			
                			//Compiler.mipsWriter.completeWriting();
                        	
                        	
                        	
                        	/////////////////////////////////////
                        }
                        
                        
	                } else {
	                    System.err.println("Parser Error: TOKEN : END expected but TOKEN : " + Parser.tokenValue + " found");
	                }

	            } else {
	                System.err.println("Parser Error: TOKEN : begin expected but TOKEN : " + Parser.tokenValue + " found");
	            }
	        } else {
	            System.err.println("Parser Error: TOKEN : program expected but TOKEN : " + Parser.tokenValue + " found");
	        }
	    }

	    public void parseDeclarations(int nodeId) {
	        if (this.token == Token.VAR) {
	            this.nodeNumber++;
	            fileWriter.writeToFile("VAR", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);

	            this.token = scanner.scanToken();
	            //Array Modification
	            
	        	String tempToken = Parser.tokenValue;
	        	int arrayRange = -1;
	        	//String tempTokenForParsetree = Parser.tokenValue;
	        	
	            if (this.token == Token.ident) {
	                this.nodeNumber++;
	                if(Parser.tokenValue.contains("Arr"))
	                {
	                	//System.err.println("** Array" + Parser.tokenValue + " Declared.\n");
	                
	                	this.token = scanner.scanToken();
	                	if(this.token == Token.ArrayLeft)
	                	{
	                		this.token = scanner.scanToken();
	                		arrayRange =(int) Double.parseDouble(Parser.tokenValue);
	                		
	                		this.token = scanner.scanToken();
	                		
		                	if(this.token == Token.ArrayRight)
		                	{
		                		//System.err.println("** Array " + tempToken + " Range. " + arrayRange);
		                		//tempToken = tempToken + "[" +arrayRange + "]";
		    	                fileWriter.writeToFile("ident: " + tempToken + "[" + arrayRange + "]" , this.nodeNumber);
		    	                fileWriter.writeToFile(nodeId, this.nodeNumber);
		                		
		                	}else
			                	{
			                        System.err.println("Parser Error: TOKEN : ] expected but TOKEN : " + Parser.tokenValue + " found");
			                    }
	                		
	                	}else
		                	{
		                        System.err.println("Parser Error: TOKEN : [ expected but TOKEN : " + Parser.tokenValue + " found");
		                    }
	                }
	                else{
	                fileWriter.writeToFile("ident: " + 	tempToken, this.nodeNumber);
	                fileWriter.writeToFile(nodeId, this.nodeNumber);
	                }
	                
	                ///AST////
	                DeclarationsNode dclr = new DeclarationsNode();
	                dclr.TokenValue = tempToken; //Parser.tokenValue;
	                if(arrayRange != -1)
	                	dclr.arrayRange = arrayRange;
	                /////
	                
	                this.token = scanner.scanToken();
	                if (this.token == Token.AS) {
	                    this.nodeNumber++;
	                    fileWriter.writeToFile("AS", this.nodeNumber);
	                    fileWriter.writeToFile(nodeId, this.nodeNumber);

	                    this.token = scanner.scanToken();
	                    this.nodeNumber++;
	                    fileWriter.writeToFile("type", this.nodeNumber);
	                    fileWriter.writeToFile(nodeId, this.nodeNumber);
	                    
	                    //AST/////
	                    dclr.declType = Parser.tokenValue;
	                    this.dcltList.addChild(dclr);
	                    //SymbolTable Insertion///
	                    this.symbolTable.insert(dclr.TokenValue, dclr.declType, dclr);
	                    ///
	                    
	                    
	                    parseType(this.nodeNumber);

	                    if (this.token == Token.SC) {
	                        this.nodeNumber++;
	                        fileWriter.writeToFile(";", this.nodeNumber);
	                        fileWriter.writeToFile(nodeId, this.nodeNumber);

	                        this.token = scanner.scanToken();
	                        this.nodeNumber++;
	                        fileWriter.writeToFile("declarations", this.nodeNumber);
	                        fileWriter.writeToFile(nodeId, this.nodeNumber);
	                        parseDeclarations(this.nodeNumber);
	                    } else {
	                        System.err.println("Parser Error: TOKEN : SC expected but TOKEN : " + Parser.tokenValue + " found");
	                    }
	                } else {
	                    System.err.println("Parser Error: TOKEN : AS expected but TOKEN : " + Parser.tokenValue + " found");
	                }
	            } else {
	                System.err.println("Parser Error: TOKEN : IDENT expected but TOKEN : " + Parser.tokenValue + " found");
	            }
	        } else {
	            // Empty production
	            this.nodeNumber++;
	            fileWriter.writeToFile("e", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);
	        }
	        if (this.token == Token.VAL) {
	            this.nodeNumber++;
	            fileWriter.writeToFile("VAL", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);

	            this.token = scanner.scanToken();
	            //MLList Modification
	            
	        	String tempToken = Parser.tokenValue;
	                this.nodeNumber++;
	                fileWriter.writeToFile("ident: " + 	tempToken, this.nodeNumber);
	                fileWriter.writeToFile(nodeId, this.nodeNumber);
	                //}
	                
	                ///AST////
	                DeclarationsNode dclr = new DeclarationsNode();
	                dclr.TokenValue = tempToken; //Parser.tokenValue;

	                ArrayList<String> mlArray = new ArrayList<String>();
	                String mlnumbers = null;
	                
	                this.token = scanner.scanToken();
	                if (this.token == Token.OP4) {
	                    this.nodeNumber++;
	                    fileWriter.writeToFile("=", this.nodeNumber);
	                    fileWriter.writeToFile(nodeId, this.nodeNumber);

	                    this.token = scanner.scanToken();
	                    if(this.token == Token.ArrayLeft)
	                    {
	                    	this.token = scanner.scanToken();
	                    	mlArray.add(Parser.tokenValue);
	                    	mlnumbers = "[" + Parser.tokenValue + ", ";
	                    //	System.out.println("List: " + tempToken + " [" + mlnumbers + " ]");
	                    	
	                    	this.token = scanner.scanToken();
	                    	while(this.token.equals(Token.ArrayRight) == false )
	                    	{
		                    	this.token = scanner.scanToken();
		                    	mlArray.add(Parser.tokenValue);
		                    	mlnumbers = mlnumbers + Parser.tokenValue + ",";
		                    	this.token = scanner.scanToken();
		                    	
	                    	}
	                    	mlnumbers = mlnumbers + "]";
		                    this.nodeNumber++;
		                    fileWriter.writeToFile(mlnumbers, this.nodeNumber);
		                    fileWriter.writeToFile(nodeId, this.nodeNumber);
		                    System.out.println("List: " + tempToken + " [" + mlnumbers + " ]");
	                    }else
	                    	System.err.println("Parser Error: TOKEN : [ expected but TOKEN : " + Parser.tokenValue + " found");
	                    
	                    
		                this.token = scanner.scanToken();
		                if (this.token == Token.AS) {
		                    this.nodeNumber++;
		                    fileWriter.writeToFile("AS", this.nodeNumber);
		                    fileWriter.writeToFile(nodeId, this.nodeNumber);
		                }
	                    this.token = scanner.scanToken();
	                    this.nodeNumber++;
	                    fileWriter.writeToFile("type", this.nodeNumber);
	                    fileWriter.writeToFile(nodeId, this.nodeNumber);
	                    
	                    dclr.declType = Parser.tokenValue;
	                    dclr.mlValues = mlArray;
	                    
	                    this.token = scanner.scanToken();
	                    this.nodeNumber++;
	                    fileWriter.writeToFile("list", this.nodeNumber);
	                    fileWriter.writeToFile(nodeId, this.nodeNumber);
	                    
	                    //AST/////

	                    
	                    this.dcltList.addChild(dclr);
	                    //SymbolTable Insertion///
	                    this.symbolTable.insert(dclr.TokenValue, dclr.declType, dclr);
	                    ///
	                    
	                    
	                   // parseType(this.nodeNumber);
	                    this.token = scanner.scanToken();
	                    
	                    if (this.token == Token.SC) {
	                        this.nodeNumber++;
	                        fileWriter.writeToFile(";", this.nodeNumber);
	                        fileWriter.writeToFile(nodeId, this.nodeNumber);

	                        this.token = scanner.scanToken();
	                        this.nodeNumber++;
	                        fileWriter.writeToFile("declarations", this.nodeNumber);
	                        fileWriter.writeToFile(nodeId, this.nodeNumber);
	                        if((this.token == Token.VAR)||(this.token == Token.VAL))
	                        parseDeclarations(this.nodeNumber);
	                    } else {
	                        System.err.println("Really? Parser Error: TOKEN : SC expected but TOKEN : " + Parser.tokenValue + " found");
	                    }
	                } else {
	                    System.err.println("Parser Error: TOKEN : AS expected but TOKEN : " + Parser.tokenValue + " found");
	                }
	            } else {
	                System.err.println("Parser Error: TOKEN : IDENT expected but TOKEN : " + Parser.tokenValue + " found");
	            }
	         
	    }

	    public void parseType(int nodeId) {
	        if ((this.token == Token.INT) || (this.token == Token.BOOL)) {
	            this.nodeNumber++;
	            fileWriter.writeToFile("" + this.token, this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);

	            this.token = scanner.scanToken();
	        } else {
	            System.err.println("Parser Error: TOKEN : INT|BOOL expected but TOKEN : " + Parser.tokenValue + " found");
	        }
	    }

	    public void parseStatementSequence(int nodeId, BasicNode parent) {

	        // Is it Follow of statementSequence End or ELSE?
	        if ((this.token == Token.END) || (this.token == Token.ELSE)) {
	            this.nodeNumber++;
	            fileWriter.writeToFile("e", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);

	        } else {
	            this.nodeNumber++;
	            fileWriter.writeToFile("statement", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);

	            parseStatement(this.nodeNumber, parent);

	            if (this.token == Token.SC) {
	                this.nodeNumber++;
	                fileWriter.writeToFile(";", this.nodeNumber);
	                fileWriter.writeToFile(nodeId, this.nodeNumber);

	                this.token = scanner.scanToken();
	                this.nodeNumber++;
	                fileWriter.writeToFile("statementSequence", this.nodeNumber);
	                fileWriter.writeToFile(nodeId, this.nodeNumber);
	                parseStatementSequence(this.nodeNumber, parent);
	            } else {
	                System.err.println("Parser Error: TOKEN : SC expected but TOKEN : " + Parser.tokenValue + " found");
	            }
	        }
	        
	    }

	    public void parseStatement(int nodeId, BasicNode parent) {
	        if (this.token == Token.ident) {
	            // lookahead check, not matched
	            this.nodeNumber++;
	            fileWriter.writeToFile("assignment", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);
	            parseAssignment(this.nodeNumber, parent);
	            
	        } else if (this.token == Token.IF) {
	            this.nodeNumber++;
	            fileWriter.writeToFile("ifStatement", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);
	            parseIfStatement(this.nodeNumber, parent);
	            
	        } else if (this.token == Token.WHILE) {
	            this.nodeNumber++;
	            fileWriter.writeToFile("whileStatement", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);
	            parseWhileStatement(this.nodeNumber, parent);
	            
	        } else if (this.token == Token.WRITEINT) {
	            this.nodeNumber++;
	            fileWriter.writeToFile("writeint", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);
	            parseWriteInt(this.nodeNumber, parent);
	            
	        } else {
	            System.err.println("Parser Error: TOKEN : ident|IF|WHILE|WRITEINT expected but TOKEN : " + Parser.tokenValue + " found");
	        }

	    }

	    public void parseAssignment(int nodeId, BasicNode parent) {
	        if (this.token == Token.ident) {  
	            this.nodeNumber++;
	            
	            // ARray modification
	            
	            int arrayIndex = -1;
            	String tempToken = Parser.tokenValue;
            	//System.err.println("** Array" + Parser.tokenValue + " found.\n");
                if(Parser.tokenValue.contains("Arr"))
                {
                	//System.err.println("** Array" + Parser.tokenValue + " found.\n");
                
                	this.token = scanner.scanToken();
                	if(this.token == Token.ArrayLeft)
                	{
                		this.token = scanner.scanToken();
                		arrayIndex =(int) Double.parseDouble(Parser.tokenValue);
                		
                		this.token = scanner.scanToken();
                		
	                	if(this.token == Token.ArrayRight)
	                	{
	                		//System.err.println("** Array " + tempToken + " Range. " + arrayIndex);
	                		//tempToken = tempToken + "[" +arrayIndex + "]";
	    	                fileWriter.writeToFile("ident: " + tempToken + "[" +arrayIndex + "]" , this.nodeNumber);
	    	                fileWriter.writeToFile(nodeId, this.nodeNumber);
	                		
	                	}else
		                	{
		                        System.err.println("Parser Error: TOKEN : ] expected but TOKEN : " + Parser.tokenValue + " found");
		                    }
                		
                	}else
	                	{
	                        System.err.println("Parser Error: TOKEN : [ expected but TOKEN : " + Parser.tokenValue + " found");
	                    }
                }
                else{
                	System.out.println("Does not go in! " + Parser.tokenValue + " found");
                fileWriter.writeToFile("ident: " + 	tempToken, this.nodeNumber);
                fileWriter.writeToFile(nodeId, this.nodeNumber);
                }
              //AST////
                IdentNode identNode = new IdentNode();
                identNode.TokenValue = tempToken;
                if(arrayIndex != -1){
                	identNode.arrayIndex = arrayIndex;
                }
                
	            
	          //  fileWriter.writeToFile("ident: " + Parser.tokenValue, this.nodeNumber);
	            //fileWriter.writeToFile(nodeId, this.nodeNumber);
	            //AST////
	          //  IdentNode identNode = new IdentNode();
	            //identNode.TokenValue = Parser.tokenValue;
	            ///////
	            
	            
	            this.token = scanner.scanToken();
	            if (this.token == Token.ASGN) { 
	                this.nodeNumber++;
	                fileWriter.writeToFile(":=", this.nodeNumber);
	                fileWriter.writeToFile(nodeId, this.nodeNumber);
	                
	                //AST////
	                AssignmentsNode assignmnt = new AssignmentsNode();
	                assignmnt.addChild(identNode);
	                
	                if (!SymbolTable.symTable.containsKey(identNode.TokenValue)) {
	                    System.err.println("Parser Error: No vairable " + identNode.TokenValue + " Declared.\n");
	                    identNode.type_OK = false;
	                } 
	                parent.addChild(assignmnt);
	                
	                if (!SymbolTable.symTable.containsKey(identNode.TokenValue)) {
	                    System.err.println("Parser Error: No vairable " + identNode.TokenValue + " Declared.\n");
	                    identNode.type_OK = false;
	                } 
	                ///

	                this.token = scanner.scanToken();

	                parseAssignmentPrime(nodeId, assignmnt);
	            } else {
	                System.err.println("Parser Error: TOKEN : ASGN expected but TOKEN : " + Parser.tokenValue + " found");
	            }
	        } else {
	            System.err.println("Parser Error: Control never comes here!");
	        }
	    }

	    public void parseAssignmentPrime(int nodeId, BasicNode parent) {
	        if (this.token == Token.READINT) { 
	            this.nodeNumber++;
	            fileWriter.writeToFile("READINT", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);
	            
	            //AST////
	            ReadIntNode readIntNode = new ReadIntNode();
	            parent.addChild(readIntNode);
	            ////
	            
	            this.token = scanner.scanToken();
	        } else { // we have to check first of expression
	            this.nodeNumber++;
	            fileWriter.writeToFile("expression", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);
	            
	            //AST
	            parseExpression(this.nodeNumber, parent);
	        }
	    }

	    public void parseIfStatement(int nodeId, BasicNode parent) {
	        if (this.token == Token.IF) {
	            this.nodeNumber++;
	            fileWriter.writeToFile("IF", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);
	            
	            //AST///
	            IfStatementNode ifStmtNode = new IfStatementNode();
	            parent.addChild(ifStmtNode);
	            ////

	            this.token = scanner.scanToken();
	            this.nodeNumber++;
	            fileWriter.writeToFile("expression", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);
	            
	            //AST
	            parseExpression(this.nodeNumber, ifStmtNode);

	            if (this.token == Token.THEN) { // match
	                this.nodeNumber++;
	                fileWriter.writeToFile("THEN", this.nodeNumber);
	                fileWriter.writeToFile(nodeId, this.nodeNumber);

	                //AST////
	                ThenStatementNode thenStmtNode = new ThenStatementNode();
	                ifStmtNode.addChild(thenStmtNode);
	                ///
	                
	                
	                this.token = scanner.scanToken();
	                this.nodeNumber++;
	                fileWriter.writeToFile("statementSequence", this.nodeNumber);
	                fileWriter.writeToFile(nodeId, this.nodeNumber);
	                
	                //AST
	                parseStatementSequence(this.nodeNumber, thenStmtNode);

	                this.nodeNumber++;
	                fileWriter.writeToFile("elseClause", this.nodeNumber);
	                fileWriter.writeToFile(nodeId, this.nodeNumber);
	                
	                //AST
	                parseElseClause(this.nodeNumber, ifStmtNode);

	                if (this.token == Token.END) { // match
	                    this.nodeNumber++;
	                    fileWriter.writeToFile("END", this.nodeNumber);
	                    fileWriter.writeToFile(nodeId, this.nodeNumber);
	                    this.token = scanner.scanToken();
	                } else {
	                    System.err.println("Parser Error: TOKEN : END expected but TOKEN : " + Parser.tokenValue + " found");
	                }
	            } else {
	                System.err.println("Parser Error: TOKEN : THEN expected but TOKEN : " + Parser.tokenValue + " found");
	            }
	        }
	    }

	    public void parseElseClause(int nodeId, BasicNode parent) {
	        if (this.token == Token.ELSE) {
	            this.nodeNumber++;
	            fileWriter.writeToFile("ELSE", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);
	            
	            //AST///
	            ElseStatementNode elseStmt = new ElseStatementNode();
	            parent.addChild(elseStmt);
	            ///

	            this.token = scanner.scanToken();
	            this.nodeNumber++;
	            fileWriter.writeToFile("statementSequence", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);
	            
	            //AST
	            parseStatementSequence(this.nodeNumber, elseStmt);

	        } // follow of else
	        else if (this.token == Token.END) {
	            this.nodeNumber++;
	            fileWriter.writeToFile("e", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);

	        } else {
	            System.err.println("Parser Error: TOKEN : END expected but TOKEN : " + Parser.tokenValue + " found");
	        }
	    }

	    public void parseWhileStatement(int nodeId, BasicNode parent) {

	        if (this.token == Token.WHILE) { // match
	        	
	            this.nodeNumber++;
	            fileWriter.writeToFile("WHILE", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);
	            
	            //AST///
	            WhileStatementNode whileStmt = new WhileStatementNode();
	            parent.addChild(whileStmt);
	            //

	            this.token = scanner.scanToken();
	            this.nodeNumber++;
	            fileWriter.writeToFile("expression", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);
	            
	            //AST//
	            parseExpression(this.nodeNumber, whileStmt);

	            if (this.token == Token.DO) { // match
	                this.nodeNumber++;
	                fileWriter.writeToFile("DO", this.nodeNumber);
	                fileWriter.writeToFile(nodeId, this.nodeNumber);
	                
	                //AST///
	                DoStatementNode doStmt = new DoStatementNode();
	                whileStmt.addChild(doStmt);
	                //

	                this.token = scanner.scanToken();
	                this.nodeNumber++;
	                fileWriter.writeToFile("statementSequence", this.nodeNumber);
	                fileWriter.writeToFile(nodeId, this.nodeNumber);
	                
	                //ASt
	                parseStatementSequence(this.nodeNumber, doStmt);

	                if (this.token == Token.END) { // match
	                    this.nodeNumber++;
	                    fileWriter.writeToFile("END", this.nodeNumber);
	                    fileWriter.writeToFile(nodeId, this.nodeNumber);

	                    this.token = scanner.scanToken();
	                } else {
	                    System.err.println("Parser Error: TOKEN : END expected but TOKEN : " + Parser.tokenValue + " found");
	                }

	            } else {
	                System.err.println("Parser Error: TOKEN : DO expected but TOKEN : " + Parser.tokenValue + " found");
	            }
	        }
	    }

	    public void parseWriteInt(int nodeId, BasicNode parent) {
	        if (this.token == Token.WRITEINT) {

	            this.nodeNumber++;
	            fileWriter.writeToFile("WRITEINT", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);
	            
	            //AST 
	            WriteIntNode writeInt = new WriteIntNode();
	            parent.addChild(writeInt);
	            //

	            this.token = scanner.scanToken();
	            this.nodeNumber++;
	            fileWriter.writeToFile("expression", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);
	            
	            //AST
	            parseExpression(this.nodeNumber, writeInt);
	        } else {
	            System.err.println("Parser Error: TOKEN : WRITEINT expected but TOKEN : " + Parser.tokenValue + "found");
	        }

	    }

	    public void parseExpression(int nodeId, BasicNode parent) {
	    	//AST////
	        ExpressionNode expr = new ExpressionNode();
	        parent.addChild(expr);
	        //
	        
	        this.nodeNumber++;
	        fileWriter.writeToFile("simpleExression", this.nodeNumber);
	        fileWriter.writeToFile(nodeId, this.nodeNumber);
	        parseSimpleExpression(this.nodeNumber, expr);
	        


	        parseExpressionPrime(nodeId, expr);
	    }

	    public void parseExpressionPrime(int nodeId, BasicNode parent) {
	        if (this.token == Token.OP4) { // match
	        	
	        	//AST// 
	            ExtendedExpressionNode exExpr = new ExtendedExpressionNode();
	            exExpr.TokenValue = Parser.tokenValue;
	            parent.addChild(exExpr);
	            //
	        	
	            this.nodeNumber++;
	            fileWriter.writeToFile(Parser.tokenValue, this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);

	            this.token = scanner.scanToken();
	            this.nodeNumber++;
	            fileWriter.writeToFile("simpleExpression", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);
	            
	            //AST
	            parseSimpleExpression(this.nodeNumber, exExpr);
	            
	        } // follow of parseExpressionPrime
	        else if ((this.token == Token.THEN) || (this.token == Token.DO) || (this.token == Token.SC) || (this.token == Token.RP)) {
	            this.nodeNumber++;
	            fileWriter.writeToFile("e", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);
	        }
	    }

	    public void parseSimpleExpression(int nodeId, BasicNode parent) {
	    	
	    	//AST//
	        SimpleExpressionNode simpleExpr = new SimpleExpressionNode();
	        parent.addChild(simpleExpr);
	        //
	        
	        
	        this.nodeNumber++;
	        fileWriter.writeToFile("term", this.nodeNumber);
	        fileWriter.writeToFile(nodeId, this.nodeNumber);
	        parseTerm(this.nodeNumber, simpleExpr);
	        parseSimpleExpressionPrime(nodeId, simpleExpr);
	    }

	    public void parseSimpleExpressionPrime(int nodeId, BasicNode parent) {
	        if (this.token == Token.OP3) { // match
	        	//AST
	            ExtendedTermNode exTerm = new ExtendedTermNode();
	            exTerm.TokenValue = Parser.tokenValue;
	            parent.addChild(exTerm);
	            //
	        	
	            this.nodeNumber++;
	            fileWriter.writeToFile(Parser.tokenValue, this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);

	            this.token = scanner.scanToken();
	            this.nodeNumber++;
	            fileWriter.writeToFile("term", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);
	            
	            //AST
	            parseTerm(this.nodeNumber, exTerm);
	        } // follow of simpleExpressionPrime
	        else if ((this.token == Token.THEN) || (this.token == Token.DO) || (this.token == Token.SC) || (this.token == Token.RP)) {
	            // epsilon production exist
	            this.nodeNumber++;
	            fileWriter.writeToFile("e", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);
	        }
	    }

	    public void parseTerm(int nodeId, BasicNode parent) {
	    	
	    	//AST 
	        TermNode term = new TermNode();
	        parent.addChild(term);
	        //
	        
	        this.nodeNumber++;
	        fileWriter.writeToFile("factor", this.nodeNumber);
	        fileWriter.writeToFile(nodeId, this.nodeNumber);

	        parseFactor(this.nodeNumber, term);
	        parseTermPrime(nodeId, term);

	    }

	    public void parseTermPrime(int nodeId, BasicNode parent) {
	        if (this.token == Token.OP2) { // match
	        	
	        	//AST
	            ExtendedFactorNode exFactor = new ExtendedFactorNode();
	            exFactor.TokenValue = Parser.tokenValue;
	            parent.addChild(exFactor);
	            //
	        	
	            this.nodeNumber++;
	            fileWriter.writeToFile(Parser.tokenValue, this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);

	            this.token = scanner.scanToken();
	            this.nodeNumber++;
	            fileWriter.writeToFile("factor", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);
	            
	            
	            parseFactor(this.nodeNumber, exFactor);
	        } // follow of termPrime
	         else if ((this.token == Token.THEN) || (this.token == Token.DO) || (this.token == Token.SC) || (this.token == Token.RP) || (this.token == Token.OP3) || (this.token == Token.OP4)) {
	            this.nodeNumber++;
	            fileWriter.writeToFile("e", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);
	        }
	    }

	    public void parseFactor(int nodeId, BasicNode parent) {
	    	String tempToken = null; 
	    	String tempHeading = null;
	    	
	        if ((this.token == Token.ident) || (this.token == Token.boollit) || (this.token == Token.num)) { // match
	            
	        	//AST
	            if (this.token == Token.ident) {

	            	tempHeading = Token.ident.toString();
	            	// Array Modification
	            	
	            	int arrayIndex = -1;
	            	tempToken = Parser.tokenValue;
	            	//System.err.println("** Array" + Parser.tokenValue + " found.\n");
	                if(Parser.tokenValue.contains("Arr"))
	                {
	                //	System.err.println("** Array" + Parser.tokenValue + " found.\n");
	                
	                	this.token = scanner.scanToken();
	                	if(this.token == Token.ArrayLeft)
	                	{
	                		this.token = scanner.scanToken();
	                		arrayIndex =(int) Double.parseDouble(Parser.tokenValue);
	                		
	                		this.token = scanner.scanToken();
	                		
		                	if(this.token == Token.ArrayRight)
		                	{
		                		//System.err.println("** Array " + tempToken + " Range. " + arrayIndex);	                	
		                		//tempToken = tempToken + "[" +arrayIndex + "]";
		    	             //   fileWriter.writeToFile("ident: " + tempToken , this.nodeNumber);
		    	               // fileWriter.writeToFile(nodeId, this.nodeNumber);
		                		
		                	}else
			                	{
			                        System.err.println("Parser Error: TOKEN : ] expected but TOKEN : " + Parser.tokenValue + " found");
			                    }
	                		
	                	}else
		                	{
		                        System.err.println("Parser Error: TOKEN : [ expected but TOKEN : " + Parser.tokenValue + " found");
		                    }
	                }
	                else{
	                	System.out.println("Does not go in! " + Parser.tokenValue + " found");
	                //fileWriter.writeToFile("ident: " + 	tempToken, this.nodeNumber);
	                //fileWriter.writeToFile(nodeId, this.nodeNumber);
	                }
	                IdentNode identNode = new IdentNode();
	                identNode.TokenValue = tempToken;
	                if(arrayIndex != -1){
	                	identNode.arrayIndex = arrayIndex;
	                }
	                
	                
	                
	                FactorNode factor = new FactorNode();
	                factor.addChild(identNode);
	                parent.addChild(factor);
	                
	                if (!SymbolTable.symTable.containsKey(identNode.TokenValue)) {
	                    System.err.println("Parser Error: No vairable " + identNode.TokenValue + " Declared.\n");
	                    identNode.type_OK = false;
	                } 
	            }

	            if (this.token == Token.boollit) {
	                BoolItNode boolItNode = new BoolItNode();
	                boolItNode.TokenValue = Parser.tokenValue;
	                tempToken = Parser.tokenValue;
	                tempHeading = Token.boollit.toString();
	                
	                FactorNode factor = new FactorNode();
	                factor.addChild(boolItNode);
	                parent.addChild(factor);
	            }

	            if (this.token == Token.num) {
	                NumNode numNode = new NumNode();
	                numNode.TokenValue = Parser.tokenValue;
	                tempToken = Parser.tokenValue;
	                tempHeading = Token.num.toString();

	                FactorNode factor = new FactorNode();
	                factor.addChild(numNode);
	                parent.addChild(factor);
	            }
	        	this.nodeNumber++;
	            fileWriter.writeToFile("" + tempHeading + ": " + tempToken, this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);
	            this.token = scanner.scanToken();
	        } else if (this.token == Token.LP) { // match
	            this.nodeNumber++;
	            fileWriter.writeToFile("(", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);

	            //AST//
                FactorNode factor = new FactorNode();
                parent.addChild(factor);
	            //
	            
	            this.token = scanner.scanToken();
	            this.nodeNumber++;
	            fileWriter.writeToFile("expression", this.nodeNumber);
	            fileWriter.writeToFile(nodeId, this.nodeNumber);
	            
	            //AST//
	            parseExpression(this.nodeNumber, factor);

	            if (this.token == Token.RP) { // match
	                this.nodeNumber++;
	                fileWriter.writeToFile(")", this.nodeNumber);
	                fileWriter.writeToFile(nodeId, this.nodeNumber);

	                this.token = scanner.scanToken();
	            } else {
	                System.err.println("Parser Error: TOKEN : RP expected but TOKEN : " + Parser.tokenValue + " found");
	            }
	        } else { // check again
	            System.err.println("Parser Error: TOKEN : IDENT|BOOLIT|NUM|LP expected but TOKEN : " + Parser.tokenValue + " found");
	        }
	    }
	    

	}


