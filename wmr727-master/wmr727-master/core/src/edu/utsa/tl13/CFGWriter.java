package edu.utsa.tl13;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;


public class CFGWriter {

	public File file;
	public BufferedWriter out;
	Block last_block;
	
	public CFGWriter(String file_name) throws IOException{
		createFileStart(file_name);
		
	}
	public void createFileStart(String file_name) {
		try{
			file = new File(file_name);
			boolean exist = file.createNewFile();
			if (!exist)
			{
				file.delete();
			}

			FileWriter fstream = new FileWriter(file_name);
			out = new BufferedWriter(fstream);
			writeLine( new StringBuilder()
					.append("digraph cfgviz {\n") 
					.append("node [shape = none];\n")
					.append("edge [tailport = s];\n")
					.append("entry\n")
					.append("subgraph cluster {\n")
					.append("color=\"/x11/white\"\n").toString());
			
		}catch(Exception e) {}
	} 
	public void writeLine (String s) throws IOException{
		out.write(s);
		out.newLine();
	}

	
	public void ProcessBlock(Block b){

		ArrayList<String> iloc = b.ILOCCode;
		String blockCode = b.getName() + " [label=<<table border=\"0\">" + processHeaderLine(b.getName());
		for (String s: iloc){
			blockCode += processEachLine(s);
		}
		if(b.getName().equals(last_block.getName()))
			blockCode += processEachLine("exit");
		
		try{
		writeLine (blockCode + "</table>>,fillcolor=\"/x11/white\",shape=box]");
		b.visited = true;
		}catch(Exception e){
			System.out.println("Error writing cfg output file" + e);
		}
	}
	public void addEdge(String from, String to){
		try{
			writeLine(String.format("%s -> %s",from,to));
		}catch(Exception e){
			
		}
	}
	String processEachLine(String s){
		return String.format("<tr><td border=\"0\" colspan=\"1\">%s</td></tr>",s);
	}
	String processHeaderLine(String s){
		return String.format("<tr><td border=\"1\" colspan=\"1\">%s</td></tr>",s);
	}
	void close() throws IOException{
		out.close();
	}
    public static void completeWriting(Block start, Block end) throws IOException{
        Compiler.cfgWriter.writeLine("}\n");
        Compiler.cfgWriter.writeLine(String.format("entry -> %s",start.getName()));
        Compiler.cfgWriter.writeLine(String.format("%s -> %s",end.getName(),"end"));
        Compiler.cfgWriter.writeLine(String.format("}"));
        Compiler.cfgWriter.close();
}
	
}
