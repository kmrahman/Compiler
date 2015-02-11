package edu.utsa.tl13;

import java.util.ArrayList;
import java.util.HashMap;

public  class ILOCtoMIPSConverter {
	static InstructionHolder ILOC_instruction;
	static ArrayList<InstructionHolder>MIPSIns ;
	static int tempregister=0;
	
    public static int offset = 0;
    public static HashMap<String, Integer> symTableOffset = new HashMap();

    
	public static ArrayList<InstructionHolder> getMIPSInstruction (InstructionHolder ILOC_ins ){
		return MIPSIns = MIPSFromILOC(ILOC_ins);
		
	}

	public static ArrayList<InstructionHolder> MIPSFromILOC(InstructionHolder iloc){
		ArrayList<InstructionHolder> mips = new ArrayList<InstructionHolder>();
		String str1,str2;

		// comment
		if (iloc.name.startsWith("#")){ 
			mips.add(iloc);
		}
		else if (iloc.name == "loadI"){
            if (!symTableOffset.containsKey(iloc.dest1)) {
                symTableOffset.put(iloc.dest1, offset);
                if(iloc.dest1.contains("Array"))
                {
                	//arrayStart.put(iloc.dest1, value);
                //	String arrayname= iloc.dest1.substring(0, iloc.dest1.indexOf('[') );
                   // symTableOffset.put(iloc.dest1, offset);
                	int range =(int) Double.parseDouble(iloc.dest1.substring(iloc.dest1.indexOf('[')+1,iloc.dest1.indexOf(']')));
                	offset =  offset - 4 * range;
                }
                else{
                    offset = offset - 4;
                }
                
            }  
            String destMemory = symTableOffset.get(iloc.dest1) +"($fp)";
            str1=nextTempRegister();
			mips.add(new InstructionHolder("li",iloc.src1,null,str1));
			mips.add(new InstructionHolder("sw",destMemory,null,str1));
			resetTempregister();
			return mips;
		}
		// add,sub, mul, div, mod
		else if (ilocToMIPMSArithmaticOP(iloc.name) != null){ 

            if (!symTableOffset.containsKey(iloc.src1)) {
                
           /*     if(iloc.src1.contains("Array"))
                {
                	//arrayStart.put(iloc.dest1, value);
                	String arrayname= iloc.src1.substring(0, iloc.src1.indexOf('[') );
                	int arrayStartOffset = symTableOffset.get(arrayname);
                	int index =(int) Double.parseDouble(iloc.src1.substring(iloc.src1.indexOf('[')+1,iloc.src1.indexOf(']')));
                	int arrayoffset =  arrayStartOffset + 4 * index;
                	symTableOffset.put(iloc.src1, arrayoffset);
                }
                else{*/
                	symTableOffset.put(iloc.src1, offset);              
                	offset = offset - 4;
                
            }    
            String sourceMemory1 = symTableOffset.get(iloc.src1) +"($fp)";
			
            if (!symTableOffset.containsKey(iloc.src2)) {
             /*   if(iloc.src2.contains("Array"))
                {
                	//arrayStart.put(iloc.dest1, value);
                	String arrayname= iloc.src2.substring(0, iloc.src2.indexOf('[') );
                	int arrayStartOffset = symTableOffset.get(arrayname);
                	int index =(int) Double.parseDouble(iloc.src2.substring(iloc.src2.indexOf('[')+1,iloc.src2.indexOf(']')));
                	int arrayoffset =  arrayStartOffset + 4 * index;
                	symTableOffset.put(iloc.src2, arrayoffset);
                }
                else{*/
                	symTableOffset.put(iloc.src2, offset);              
                	offset = offset - 4;
                
                
            }    
            String sourceMemory2 = symTableOffset.get(iloc.src2) +"($fp)";
            
            if (!symTableOffset.containsKey(iloc.dest1)) {
                symTableOffset.put(iloc.dest1, offset);
                offset = offset - 4;
            }   
            String destMemory = symTableOffset.get(iloc.dest1) +"($fp)";

            str1=nextTempRegister();
            str2=nextTempRegister();
            
			mips.add(new InstructionHolder("lw",sourceMemory1,null,str1));
			mips.add(new InstructionHolder("lw",sourceMemory2,null,str2));
			mips.add(new InstructionHolder(ilocToMIPMSArithmaticOP(iloc.name),str1,str2,str1));
			mips.add(new InstructionHolder("sw",destMemory,null,str1));
			
			resetTempregister();
			return mips;
		}
		else if (iloc.name == "readInt") {			
			
            if (!symTableOffset.containsKey(iloc.dest1)) {
                symTableOffset.put(iloc.dest1, offset);
                offset = offset - 4;
            }   
            String destMemory = symTableOffset.get(iloc.dest1) +"($fp)";
			mips.add(new InstructionHolder("li","5",null,"$v0"));
			mips.add(new InstructionHolder("syscall",null,null,null));
			
			str1=nextTempRegister();
			mips.add(new InstructionHolder("add","$v0","$zero",str1));
			mips.add(new InstructionHolder("sw",destMemory,null,str1));
			resetTempregister();
			return mips;
		}
		else if (iloc.name == "writeInt"){
            if (!symTableOffset.containsKey(iloc.src1)) {
                symTableOffset.put(iloc.src1, offset);
                offset = offset - 4;
            }   
            String destMemory = symTableOffset.get(iloc.src1) +"($fp)";            

			mips.add(new InstructionHolder("li","1",null,"$v0"));
			str1=nextTempRegister();
			mips.add(new InstructionHolder("lw",destMemory,null,str1));
			mips.add(new InstructionHolder("add",str1,"$zero","$a0"));
			mips.add(new InstructionHolder("syscall",null	,null,null));
			mips.add(new InstructionHolder("li","4",null,"$v0"));
			mips.add(new InstructionHolder("la","newline",null,"$a0"));
			mips.add(new InstructionHolder("syscall",null	,null,null));
			resetTempregister();
			return mips;
		}

		else if (iloc.name == "jumpl"){
			mips.add(new InstructionHolder ("j",null,null,iloc.dest1));
			return mips;
		}
		else if (iloc.name == "cbr") {
	         if (!symTableOffset.containsKey(iloc.src1)) {
	                symTableOffset.put(iloc.src1, offset);
	                offset = offset - 4;
	            }   
	        String sourceMemory1 = symTableOffset.get(iloc.src1) +"($fp)";

	        str1=nextTempRegister();
			mips.add(new InstructionHolder("lw",sourceMemory1,null,str1));
			mips.add(new InstructionHolder("bne","$zero",iloc.dest1,str1));
			mips.add(new InstructionHolder("j",null,null,iloc.dest2));
			resetTempregister();
			return mips;
		}
		
		else if (iloc.name == "i2i"){
            if (!symTableOffset.containsKey(iloc.src1)) {
                symTableOffset.put(iloc.src1, offset);
                offset = offset - 4;
            }   
            String src1Memory = symTableOffset.get(iloc.src1) +"($fp)";
            
            if (!symTableOffset.containsKey(iloc.dest1)) {
                symTableOffset.put(iloc.dest1, offset);
                offset = offset - 4;
            }   
            String dest1Memory = symTableOffset.get(iloc.dest1) +"($fp)";

            str1=nextTempRegister();
			mips.add(new InstructionHolder("lw",src1Memory,null,str1));
			mips.add(new InstructionHolder("add",str1,"$zero",str1));
			mips.add(new InstructionHolder("sw",dest1Memory,null,str1));
			return mips;
		}
		//logical 
		else if (ilocToMIPSLogical(iloc.name) != null) { 
			String mips_instruction = ilocToMIPSLogical(iloc.name);

	         if (!symTableOffset.containsKey(iloc.src1)) {
	                symTableOffset.put(iloc.src1, offset);
	                offset = offset - 4;
	            }   
	        String sourceMemory1 = symTableOffset.get(iloc.src1) +"($fp)";


	         if (!symTableOffset.containsKey(iloc.src2)) {
	                symTableOffset.put(iloc.src2, offset);
	                offset = offset - 4;
	            }   
	        String sourceMemory2 = symTableOffset.get(iloc.src2) +"($fp)";
	        
            if (!symTableOffset.containsKey(iloc.dest1)) {
                symTableOffset.put(iloc.dest1, offset);
                offset = offset - 4;
            }   
            String destMemory = symTableOffset.get(iloc.dest1) +"($fp)";

            str1=nextTempRegister();
            str2=nextTempRegister();
			mips.add(new InstructionHolder("lw",sourceMemory1,null,str1));
			mips.add(new InstructionHolder("lw",sourceMemory2,null,str2));
			mips.add(new InstructionHolder(mips_instruction,str1,str2,str1 ));
			mips.add(new InstructionHolder("sw",destMemory,null,str1));	
			
			return mips;
		}

		return null;
		
	}
	public static String nextTempRegister(){
		return "$t"+tempregister++;
	}
	public static void resetTempregister(){
		tempregister = 0;
	}
	
	public static String ilocToMIPSLogical(String ilocOp){
		if(ilocOp.equalsIgnoreCase("cmp_LT")) return "slt";
		else if(ilocOp.equalsIgnoreCase("cmp_GT")) return "sgt";
		else if(ilocOp.equalsIgnoreCase("cmp_LE")) return "sle";
		else if(ilocOp.equalsIgnoreCase("cmp_GE")) return "sge";
		else if(ilocOp.equalsIgnoreCase("cmp_EQ")) return "seq";		
		else if(ilocOp.equalsIgnoreCase("cmp_NE")) return "sne";
		
		return null;
	}
	public static String ilocToMIPMSArithmaticOP(String ilocOp){
		if(ilocOp.equalsIgnoreCase("add")) return "addu";
		else if(ilocOp.equalsIgnoreCase("sub")) return "subu";
		else if(ilocOp.equalsIgnoreCase("mul")) return "mul";
		else if(ilocOp.equalsIgnoreCase("div")) return "div";
		else if(ilocOp.equalsIgnoreCase("mod")) return "rem";
		
		return null;
	}

}
