package edu.utsa.tl13;

import java.io.IOException;

public class ILOC {

	
	public static Block ILOCDeclarationList(Block inBlock, DeclarationListNode dl){
		
		
		for (DeclarationsNode d: dl.declarations){
			if(d.TokenValue.contains("Array")){
				inBlock.addILOCCode(String.format("loadI 0 =&gt; %s","r_"+ d.TokenValue + "[" + d.arrayRange + "]"));
				inBlock.addInstruction(new InstructionHolder("loadI","0",null,"r_" + d.TokenValue + "[" + d.arrayRange + "]"));
			}
			else{
			inBlock.addILOCCode(String.format("loadI 0 =&gt; %s","r_"+ d.TokenValue ));
			inBlock.addInstruction(new InstructionHolder("loadI","0",null,"r_" + d.TokenValue));
			}
		}
		return inBlock;
	}
	
	public static void PrintILOCCode(Block block) throws IOException{
		
		Compiler.cfgWriter.ProcessBlock(block);
		
		
		for (Block b: block.parents){
			Compiler.cfgWriter.addEdge(block.getName(), b.getName());		
		}
		
		for (Block b: block.childBlock){
			if (b.visited == false) {
				Compiler.cfgWriter.addEdge(block.getName(), b.getName());
				PrintILOCCode(b);
			}
			else {
				Compiler.cfgWriter.addEdge(block.getName(), b.getName());
			}
			
		}
		
	}

	public static void WriteMIPSCode(Block block) throws IOException{
		
		Compiler.mipsWriter.Process_Block_code(block);
		block.visited_mips = true;
		for (Block b: block.childBlock){
			if (b.visited_mips == false) {
				WriteMIPSCode(b);
				
			}
				
		}
	}
	
	public static Block ILOCWhile(Block inBlock, StatementNode stmt){
		

		Block expBlock = new Block();
		Block stmtBlock = new Block();
		Block exitBlock = new Block();
		
		WhileStatementNode whiles = (WhileStatementNode)stmt;
		
		inBlock.addILOCCode(String.format("jumpl -&gt; %s", expBlock.getName()));
		inBlock.addInstruction(new InstructionHolder("jumpl",null,null,expBlock.getName()));
		
		inBlock.addChildBlock(expBlock);
		String t1 = ILOCExpression(expBlock, whiles.expr);
		
		expBlock.addILOCCode(String.format("cbr %s -&gt; %s , %s",t1,stmtBlock.getName(),exitBlock.getName()));
		expBlock.addInstruction(new InstructionHolder("cbr",t1,null,stmtBlock.getName(),exitBlock.getName()));
		
		Block stmt_exit_block = ILOCDoStatement(stmtBlock, whiles.doStmt);
		
		stmt_exit_block.addILOCCode(String.format("jumpl -&gt; %s",expBlock.getName()));
		stmt_exit_block.addInstruction(new InstructionHolder("jumpl",null,null,expBlock.getName()));
		
		stmt_exit_block.addParent(expBlock);
		
		expBlock.addChildBlock(stmtBlock);
		expBlock.addChildBlock(exitBlock);	
	
		
		return exitBlock;
		
	}
	
public static  Block ILOCDoStatement (Block inBlock, DoStatementNode sl){
		
		Block block = inBlock;
		for (StatementNode stmt: sl.doStmt){
			if (stmt.getClass().getName().equals(ASSIGNMENT)){
				block = ILOCAssignment(block, stmt);
			}
			else if (stmt.getClass().getName().equals(WRITEINT)){
				block = ILOCWriteInt(block, stmt);
			}
			else if (stmt.getClass().getName().equals(IFSTATEMENT)){
				block = ILOCIF(block, stmt);
			}
			else if (stmt.getClass().getName().equals(WHILESTATEMENT)){
				block = ILOCWhile(block, stmt);
			}
			
		}
		return block;
	}

	public static Block ILOCIF(Block inBlock, StatementNode stmt){
		
		IfStatementNode ifs = (IfStatementNode) stmt;
		
		Block if_start_blk = inBlock; 
		Block if_blk = new Block();
		Block else_blk = new Block();
		Block exit_blk = new Block();
		
		String str1;
		str1 = ILOCExpression(if_start_blk, ifs.expr);
		if (ifs.elseStmt!= null){
			if_start_blk.addILOCCode(String.format("cbr %s =&gt; %s, %s",str1,if_blk.getName(),else_blk.getName()));
			inBlock.addInstruction(new InstructionHolder("cbr",str1,null,if_blk.getName(),else_blk.getName()));
			
		}else {
			if_start_blk.addILOCCode(String.format("cbr %s =&gt; %s, %s",str1,if_blk.getName(),exit_blk.getName()));
			inBlock.addInstruction(new InstructionHolder("cbr",str1,null,if_blk.getName(),exit_blk.getName()));
		}
		
		if_start_blk.addChildBlock(if_blk);
		
		Block if_stmt_end_block = ILOCThenStatementList(if_blk, ifs.thenStmt);
		if_stmt_end_block.addChildBlock(exit_blk);
		
		if (ifs.elseStmt != null){
			if_start_blk.addChildBlock(else_blk);
			Block else_stmt_end_block = ILOCElseStatementList(else_blk, ifs.elseStmt);
			else_stmt_end_block.addChildBlock(exit_blk);
		} else {
			if_start_blk.addChildBlock(exit_blk);
		}
		
		return exit_blk;
		
	}
	
public static  Block ILOCThenStatementList (Block inBlock, ThenStatementNode sl){
		
		Block block = inBlock;
		for (StatementNode stmt: sl.thenStmt){
			if (stmt.getClass().getName().equals(ASSIGNMENT)){
				block = ILOCAssignment(block, stmt);
			}
			else if (stmt.getClass().getName().equals(WRITEINT)){
				block = ILOCWriteInt(block, stmt);
			}
			else if (stmt.getClass().getName().equals(IFSTATEMENT)){
				block = ILOCIF(block, stmt);
			}
			else if (stmt.getClass().getName().equals(WHILESTATEMENT)){
				block = ILOCWhile(block, stmt);
			}
			
		}
		return block;
	}
	
public static  Block ILOCElseStatementList (Block inBlock, ElseStatementNode sl){
	
	Block block = inBlock;
	for (StatementNode stmt: sl.elseStmt){
		if (stmt.getClass().getName().equals(ASSIGNMENT)){
			block = ILOCAssignment(block, stmt);
		}
		else if (stmt.getClass().getName().equals(WRITEINT)){
			block = ILOCWriteInt(block, stmt);
		}			
		else if (stmt.getClass().getName().equals(IFSTATEMENT)){
			block = ILOCIF(block, stmt);
		}
		else if (stmt.getClass().getName().equals(WHILESTATEMENT)){
			block = ILOCWhile(block, stmt);
		}
		
	}
	return block;
}
	
	public static  Block ILOCStatementList (Block inBlock, StatementListNode sl){
		
		Block block = inBlock;
		for (StatementNode stmt: sl.stmts){
			if (stmt.getClass().getName().equals(ASSIGNMENT)){
				block = ILOCAssignment(block, stmt);
			}
			else if (stmt.getClass().getName().equals(WRITEINT)){
				block = ILOCWriteInt(block, stmt);
			}
			else if (stmt.getClass().getName().equals(IFSTATEMENT)){
				block = ILOCIF(block, stmt);
			}
			else if (stmt.getClass().getName().equals(WHILESTATEMENT)){
				block = ILOCWhile(block, stmt);
			}
			
		}
		return block;
	}
	
	
	public static Block ILOCWriteInt(Block inBlock, StatementNode stmt){
		String str1; 
		WriteIntNode wrti = (WriteIntNode)stmt;
		
		str1 = ILOCExpression(inBlock,wrti.expr );
		//System.out.println("***In writeInt " );
		//if(WriteIntNode)
		/*if(str1.contains("Arr"))
		{	System.out.println("Got Arr" + str1);
		    str1 = str1 + "[" + wrti.expr.expr.term.fact.ident.arrayIndex + "]";
		}*/
		inBlock.addILOCCode(String.format("writeInt %s", str1));
		inBlock.addInstruction(new InstructionHolder("writeInt",str1,null,null)	);
		
		return inBlock;
	}
	
	public static Block ILOCAssignment(Block inBlock, StatementNode s){
		
		AssignmentsNode assignment = (AssignmentsNode)s;
		String str1,str2; 
		
		str1 = ILOCExpression(inBlock, assignment.ident);
	/*	if(str1.contains("Arr"))
		{	System.out.println("Got Arr" + str1);
		    str1 = str1 + "[" + assignment.ident.arrayIndex + "]";
		}
		*/
		if (assignment.expr != null){			
			str2 = ILOCExpression(inBlock, assignment.expr);
			
			inBlock.addILOCCode(String.format("i2i %s =&gt; %s", str2,str1));
			inBlock.addInstruction(new InstructionHolder("i2i",str2,null,str1)	);
			
		}
		else if (assignment.readInt != null) {
			inBlock.addILOCCode(String.format("readInt =&gt; %s", str1));
			inBlock.addInstruction(new InstructionHolder("readInt",null,null,str1)	);
		}
		return inBlock;
	}
	
	public static String ILOCExpression(Block inBlock, BasicNode  node){
		String reg=null, str1= null,str2 = null; //registers

		//System.out.println("Classname" + node.getClass().getName());

		switch (node.getClass().getName()){
		
		case WRITEINT : 
		case  EXPRESSION : {
			ExpressionNode expr = (ExpressionNode) node;

			//System.out.println("***In exEpr " + expr.expr.TokenValue);
			
			if (expr.exExpr != null){
				str1 = ILOCExpression(inBlock,expr.exExpr.exprs.get(1));
				str2 = ILOCExpression(inBlock,expr.exExpr.exprs.get(0));
				
				reg = nextRegister();
			//	System.out.println("***In exTerm " + expr.exExpr.TokenValue);
				inBlock.addILOCCode(getArithmeticInstruction(expr.exExpr.TokenValue, str1, str2, reg));
				inBlock.addInstruction(new InstructionHolder(optoInstruction(expr.exExpr.TokenValue),str1,str2,reg));

				
				return reg;
			}
			else 
				str1 = ILOCExpression(inBlock,expr.expr);
			
			return str1;
		}
		case SIMPLEEXPRESSION:{
			SimpleExpressionNode sExp = (SimpleExpressionNode) node;

		//	System.out.println("***In sExpr term " );

			
			if (sExp.exTerm != null){
				str1 = ILOCExpression(inBlock,sExp.exTerm.terms.get(1));
				str2 = ILOCExpression(inBlock,sExp.exTerm.terms.get(0));
				
				reg = nextRegister();
			//	System.out.println("***In exTerm " + sExp.exTerm.TokenValue);
				inBlock.addILOCCode(getArithmeticInstruction(sExp.exTerm.TokenValue, str1, str2, reg));
				inBlock.addInstruction(new InstructionHolder(optoInstruction(sExp.exTerm.TokenValue),str1,str2,reg));

				
				return reg;
			}
			else 
				str1 = ILOCExpression(inBlock,sExp.term);
			
			return str1;
		}
		
		case EXEXPRESSION:{			
			ExtendedExpressionNode exExp = (ExtendedExpressionNode) node;
			

			for (SimpleExpressionNode sExp : exExp.exprs){
				str1 = str1 +  ILOCExpression(inBlock,sExp);			
			}

			return str1;
		}
		case TERM:{
			TermNode term = (TermNode)node;

			//System.out.println("***In term  " + term.TokenValue);
			


			if (term.exFact != null	){
				str1 = ILOCExpression(inBlock,term.exFact.factors.get(1));
				str2 = ILOCExpression(inBlock,term.exFact.factors.get(0));
				
				reg = nextRegister();
			//	System.out.println("***In exTerm " + term.exFact.TokenValue);
				if(term.exFact.TokenValue.equalsIgnoreCase("mod"))
					{	getModInstruction(term.exFact.TokenValue, str1, str2, reg, inBlock);
					    inBlock.addInstruction(new InstructionHolder(optoInstruction(term.exFact.TokenValue),str1,str2,reg));
					}
				else	
					{
						inBlock.addILOCCode(getArithmeticInstruction(term.exFact.TokenValue, str1, str2, reg));
						inBlock.addInstruction(new InstructionHolder(optoInstruction(term.exFact.TokenValue),str1,str2,reg));
					}
				return reg;
				}
			else
				str1 = ILOCExpression(inBlock,term.fact);
			
			return str1;
		}


		case FACTOR: {
			FactorNode fact = (FactorNode) node;

		//	System.out.println("In factor " + fact.TokenValue);
			
			if (fact.expr != null) return ILOCExpression(inBlock,fact.expr);
			else if (fact.ident != null) return ILOCExpression(inBlock,fact.ident);
			else if (fact.num != null){ 

			//	System.out.println("In num " + fact.num.TokenValue);
				return ILOCExpression(inBlock,fact.num);}
			else if (fact.boolIt != null) return ILOCExpression(inBlock,fact.boolIt);
			break;
		}
		

			
		case NUMNODE:{
			NumNode n = (NumNode) node;

			//System.out.println("In num " + n.TokenValue);
			
			reg = nextRegister();
			inBlock.addILOCCode(String.format("loadI %d =&gt; %s",(int) Double.parseDouble(n.TokenValue),reg));
			inBlock.addInstruction(new InstructionHolder( "loadI" ,""+(int) Double.parseDouble(n.TokenValue),null,reg));
			return reg;
		}
		
		case BOOLNODE:{
			BoolItNode bol = (BoolItNode) node;
			reg = nextRegister();
			int boolvalue = 0;
			if(bol.TokenValue.equalsIgnoreCase("true"))
				boolvalue =  1;
			else if (bol.TokenValue.equalsIgnoreCase("false"))
				boolvalue = 0;   // boolean true to integer 1, boolean false to integer 0
			
			inBlock.addILOCCode(String.format("loadI %d =&gt; %s",boolvalue,reg));
			inBlock.addInstruction(new InstructionHolder( "loadI" ,""+boolvalue ,null,reg));
			
			return reg;
		}
		
		case IDENTNODE: {
			IdentNode ident = (IdentNode)node;
			if(ident.TokenValue.contains("Array"))
				return "r_" + ident.TokenValue + "[" + ident.arrayIndex +"]";
			else
				return "r_"+ident.TokenValue;
			
		}
		
	}	
	return null;
		
	}
	
	
	public static final String EXPRESSION = "edu.utsa.tl13.ExpressionNode";
	public static final String EXEXPRESSION = "edu.utsa.tl13.ExtendedExpressionNode";
	public static final String SIMPLEEXPRESSION = "edu.utsa.tl13.SimpleExpressionNode";
	public static final String TERM = "edu.utsa.tl13.TermNode";
	public static final String EXTERM = "edu.utsa.tl13.ExtendedTermNode";
	public static final String FACTOR = "edu.utsa.tl13.FactorNode";
	public static final String EXFACTOR = "edu.utsa.tl13.ExtendedFactorNode";
	public static final String NUMNODE = "edu.utsa.tl13.NumNode";
	public static final String IDENTNODE = "edu.utsa.tl13.IdentNode";
	public static final String BOOLNODE = "edu.utsa.tl13.BoolltNode";
	public static final String ASSIGNMENT = "edu.utsa.tl13.AssignmentsNode";
	public static final String WRITEINT = "edu.utsa.tl13.WriteIntNode";
	public static final String IFSTATEMENT = "edu.utsa.tl13.IfStatementNode";
	public static final String WHILESTATEMENT = "edu.utsa.tl13.WhileStatementNode";
	public static int currentBlockNo = 1;
	
	private static int index = -1;
	
	private static String nextRegister(){
		index ++;
		return "r_"+index;
	}
	
	private static String getModInstruction ( String operator, String operand1, String operand2, String result, Block inBlock) {
		String temp1 = nextRegister();
		String temp2 = nextRegister();
		inBlock.addILOCCode(String.format("div %s, %s =&gt; %s",operand1,operand2,temp1)); 
		inBlock.addILOCCode(String.format("\nmult %s, %s =&gt; %s",temp1,operand2,temp2));
		inBlock.addILOCCode(String.format("\nsub %s, %s =&gt; %s",operand1,temp2,result));
		return null;
	}

	private static String getArithmeticInstruction ( String operator, String operand1, String operand2, String result) {
		String instruction = null;
		
		switch(operator){
		case "+": {
			return String.format("add %s, %s =&gt; %s",operand1,operand2,result);
		}
		case "-": {
			return String.format("sub %s, %s =&gt; %s",operand1,operand2,result);
		}
		case "*": {
			return String.format("mult %s, %s =&gt; %s",operand1,operand2,result);
		}
		case "div": {
			return String.format("div %s, %s =&gt; %s",operand1,operand2,result);
		}
		case "mod": {
			String temp1 = nextRegister();
			String temp2 = nextRegister();
			return String.format("div %s, %s =&gt; %s",operand1,operand2,temp1) 
					+ String.format("\nmul %s, %s =&gt; %s",temp1,operand2,temp2)
					+ String.format("\nsub %s, %s =&gt; %s",operand1,temp2,result);
		}
		case "<": {
			return String.format("cmp_LT %s, %s =&gt; %s",operand1,operand2,result);
		}
		case ">": {
			return String.format("cmp_GT %s, %s =&gt; %s",operand1,operand2,result);
		}
		case "<=": {
			return String.format("cmp_LE %s, %s =&gt; %s",operand1,operand2,result);
		}
		case ">=": {
			return String.format("cmp_GE %s, %s =&gt; %s",operand1,operand2,result);
		}
		case "=": {
			return String.format("cmp_EQ %s, %s =&gt; %s",operand1,operand2,result);
		}
		case "!=": {
			return String.format("cmp_NE %s, %s =&gt; %s",operand1,operand2,result);
		}
		
		}
			
		return instruction;
	}
	
	public static String optoInstruction(String op)
	{
		if(op.equalsIgnoreCase("+")) return  "add";
		else if(op.equalsIgnoreCase("-")) return  "sub";
		else if(op.equalsIgnoreCase("*")) return  "mul";
		else if(op.equalsIgnoreCase("div")) return  "div";
		else if(op.equalsIgnoreCase("mod")) return  "mod";
		else if(op.equalsIgnoreCase("<")) return  "cmp_LT";
		else if(op.equalsIgnoreCase(">")) return  "cmp_GT";
		else if(op.equalsIgnoreCase("<=")) return  "cmp_LE";
		else if(op.equalsIgnoreCase(">=")) return  "cmp_GE";
		else if(op.equalsIgnoreCase("=")) return  "cmp_EQ";
		else if(op.equalsIgnoreCase("!=")) return  "cmp_NE";
		
		return "no match";

	}
}

