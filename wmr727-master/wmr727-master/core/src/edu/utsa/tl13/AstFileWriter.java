package edu.utsa.tl13;


import java.io.FileWriter;
import java.io.IOException;

public class AstFileWriter {

    private static FileWriter outFileStream;
    
    public AstFileWriter(String fileName) {
        try {
            outFileStream = new FileWriter(fileName);
            this.writeStart();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void writeToFile(String label, int node, Boolean type) {
        try {
            if (type) {
            	if (label.contains(":=readInt")||label.contains("WriteInt"))
            	    outFileStream.write("  n" + node + " [label=\"" + label + "\",fillcolor=\"/x11/gray\",shape=box, color=white]" + "\n");
            	else if (label.contains(":=")|| label.contains("Int")|| label.contains("*")|| label.contains("div")|| label.contains("mod")|| label.contains("+")|| label.contains("-"))
                	outFileStream.write("  n" + node + " [label=\"" + label + "\",fillcolor=\"/x11/green\",shape=box, color=white]" + "\n");
              else if(SymbolTable.symTable.containsKey(label)&& (SymbolTable.symTable.get(label).equals("int"))){
            	  outFileStream.write("  n" + node + " [label=\"" + label + "\",fillcolor=\"/x11/green\",shape=box, color=white]" + "\n");
              }
              else  if (label.contains("=")||label.contains("!=")||label.contains("<")||label.contains(">")||label.contains("<=")||label.contains(">="))
              	    outFileStream.write("  n" + node + " [label=\"" + label + "\",fillcolor=\"/x11/blue\",shape=box, color=white]" + "\n");
              else if(SymbolTable.symTable.containsKey(label)&& (SymbolTable.symTable.get(label).equals("bool"))){
            	  outFileStream.write("  n" + node + " [label=\"" + label + "\",fillcolor=\"/x11/blue\",shape=box, color=white]" + "\n");
              }
              
              else
              	outFileStream.write("  n" + node + " [label=\"" + label + "\",fillcolor=\"/x11/white\",shape=box, color=block]" + "\n");
              	
           }
            else {
                	outFileStream.write("  n" + node + " [label=\"" + label + "\",fillcolor=\"/x11/pink\",shape=box]" + "\n");
                }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void writeToFile(int node1Number, int node2Number) {
        try {
            outFileStream.write("  n" + node1Number + " -> " + "n" + node2Number + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeStart() {
        try {
            outFileStream.write("digraph parseTree {" + "\n");
            outFileStream.write("  ordering=out;" + "\n");
            outFileStream.write("  node [shape = box, style = filled];" + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeEnd() {
        try {
            outFileStream.write("}\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void closeFile() {
        try {
            this.writeEnd();
            outFileStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}


