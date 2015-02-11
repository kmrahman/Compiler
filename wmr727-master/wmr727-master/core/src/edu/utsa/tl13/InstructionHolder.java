package edu.utsa.tl13;

public class InstructionHolder {
	String name = null;
	String src1= null;
	String src2 = null;
	String dest1 = null;
	String dest2 = null;
	
	public InstructionHolder (String Iname, String source1, String source2, String dest){					
		this.name = Iname;
		this.src1 = source1;
		this.src2 = source2;
		this.dest1 = dest;
	}
	public InstructionHolder (String name, String source1, String source2, String destination1, String destination2){		
		this(name,source1,source2,destination1);
		this.dest2 = destination2;
		
	}
}
