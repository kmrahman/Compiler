package edu.utsa.tl13;


import java.io.FileWriter;
import java.io.IOException;

public class MyFileWriter {

    private static FileWriter outFileStream;
    
    public MyFileWriter(String fileName) {
        try {
            outFileStream = new FileWriter(fileName);
            this.writeStart();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

     
    public void writeToFile(String label, int nodeNumber) {
        try {
            outFileStream.write("  n" + nodeNumber + " [label=\"" + label + "\",fillcolor=\"/x11/white\",shape=box]" + "\n");
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


