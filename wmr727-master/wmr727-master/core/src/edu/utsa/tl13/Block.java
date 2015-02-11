package edu.utsa.tl13;

import java.util.ArrayList;

public class Block {
	public ArrayList <Block> parents;
	public ArrayList<Block> childBlock = null;
	public boolean visited = false;
	
	public String code;
	public ArrayList<String> ILOCCode;
	public String name;
	
	public ArrayList<InstructionHolder> instructions;
	public ArrayList<InstructionHolder> mips_instructions;
	public boolean visited_mips = false;

	public Block( ){
		parents = new ArrayList<Block>();
		childBlock = new ArrayList<Block>();
		ILOCCode = new ArrayList<String>();
		this.name = "Block"+(ILOC.currentBlockNo++);
		
		instructions = new ArrayList<InstructionHolder>();
		mips_instructions = new ArrayList<InstructionHolder>();
	}
	
	public void addChildBlock(Block b1){
		childBlock.add(b1);
	}
	public void addParent(Block b){
		parents.add(b);
	}

	public String getName(){
		return this.name;
	}
	public void addILOCCode(String iloc){
		code += "\n" + iloc;
		ILOCCode.add(iloc);
	}
	public void addInstruction(InstructionHolder i) {
		instructions.add(i);
	}
	
	public ArrayList<InstructionHolder> getMIPSInstructions(){
		ArrayList<InstructionHolder> interim = new ArrayList<InstructionHolder>();

		
		for (InstructionHolder temp:instructions) {
			
			interim = ILOCtoMIPSConverter.MIPSFromILOC(temp);
			temp.name = "# " + temp.name;
			mips_instructions.add(temp);
			if (interim != null)
				for (InstructionHolder i_m: interim){
					mips_instructions.add(i_m);
				}
		}
		
		
		return mips_instructions;
	}
}
