package edu.utsa.tl13;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MIPSFileWriter {

	public File file;
	public BufferedWriter out;
	
	public MIPSFileWriter(String fileName) throws IOException{
		createFile(fileName);
		
	}
	public void createFile(String fileName) {
		try{
			file = new File(fileName);
			boolean exist = file.createNewFile();
			if (!exist)
			{
				file.delete();
			}

			FileWriter outfstream = new FileWriter(fileName);
			out = new BufferedWriter(outfstream);
			writeLine( new StringBuilder()
					.append(".data\n") 
					.append("newline:	.asciiz \"\\n\" \n")
					.append(".text\n")
					.append(".globl main\n")
					.append("main:\n")
					.append("li $fp, 0x7ffffffc \n").toString());
			
		}catch(Exception e) {}
	} 
	
	
	public void Process_Block_code(Block b){
	
		ArrayList<InstructionHolder> mipscode = b.getMIPSInstructions();

		String mipslines = b.getName() + ":\n";
	
		for (InstructionHolder i : mipscode){	
			if (i.name.startsWith("#"))	
				mipslines += "\n";
			mipslines += processEachInstruction(i);
		}
		try{
			writeLine(mipslines);
		}catch(Exception e){
			System.out.println("Error writing MIPS output file" + e);
		}
	}

	String processEachInstruction(InstructionHolder ins){

		
		if (ins.dest1 != null && ins.dest2 != null){
			if (ins.name.contains("cbr")) {
				return String.format("%s %s, %s, %s  \n",ins.name,ins.src1,ins.dest1,ins.dest2).toString();
			}
				
		}
		else if (ins.dest1 != null) {
			if ( ins.src1 != null && ins.src2 != null) {
				return String.format("%s %s , %s , %s \n",ins.name,ins.dest1,ins.src1,ins.src2).toString();
			}
			else if (ins.src1 == null  && ins.src2 == null){
				return String.format("%s %s  \n",ins.name,ins.dest1).toString();
			}
			else if (ins.src1 != null) {
				return String.format("%s %s , %s  \n",ins.name,ins.dest1,ins.src1).toString();
			}

		}
		else if (ins.dest1 == null && ins.dest2 == null) {
			if ( ins.src1 != null && ins.src2 != null) {
				return String.format("%s %s , %s  \n",ins.name,ins.src1,ins.src2).toString();
			}else if (ins.src1 != null) {
				return String.format("%s %s   \n",ins.name,ins.src1).toString();
			}
			return ins.name + "\n"	;
		}

		return null;
			
	}
	public void writeLine (String s) throws IOException{
		out.write(s);
		out.newLine();
	}
	
	void completeWriting() throws IOException{
		writeLine( new StringBuilder()
		.append("# exit \n") 
		.append("li $v0, 10 \n")
		.append("syscall \n").toString());
		
		out.close();
	}

	
}
