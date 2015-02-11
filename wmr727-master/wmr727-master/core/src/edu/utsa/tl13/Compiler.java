package edu.utsa.tl13;

import java.io.IOException;


public class Compiler {
	
    public static AstFileWriter astOut;
    public static CFGWriter cfgWriter;
    public static MIPSFileWriter mipsWriter;
    
    
    public static int astNodeCounter = 1;
    
    public static void main (String[] args) throws IOException {
        String inputFileName = args[0];//"simple1.tl13";
        int baseNameOffset = inputFileName.length() - 5;

        String baseName;
        if (inputFileName.substring(baseNameOffset).equals(".tl13"))
            baseName = inputFileName.substring(0,baseNameOffset);
        else
            throw new RuntimeException("inputFileName does not end in .tl13");

        String parseOutName = baseName + ".pt.dot";
        String astOutName = baseName + ".ast.dot";
        String cfgOutName = baseName + ".iloc.cfg.dot";
        String mipsOutName = baseName + ".s";

        System.out.println("Input file: " + inputFileName);
        System.out.println("Output file: " + parseOutName);
        System.out.println("AST Output file: " + astOutName);
        System.out.println("CFG Output file: " + cfgOutName);
        System.out.println("MIPS Output file: " + mipsOutName);

    	Scanner scanner = new Scanner(inputFileName);
        MyFileWriter parseOut = new MyFileWriter(parseOutName);
        Compiler.astOut = new AstFileWriter(astOutName);
        
        Compiler.cfgWriter = new CFGWriter(cfgOutName);
        Compiler.mipsWriter = new MIPSFileWriter(mipsOutName); 
	
        Parser parser = new Parser(scanner, parseOut);
        parser.parseProgram();

        parseOut.closeFile();
        astOut.closeFile();

    
    }
}
