package edu.utsa.tl13;

import java.util.ArrayList;

public class SSAForm {


	public static void  printDominenece(Block b){
		
		//Get List for current block
		ArrayList<Block> domList =  b.parents; 
		domList.addAll(getDomList(b));

		//Print the List
		System.out.println("Dominance for " + b.name );
		for(Block bl: domList)
		{
			System.out.println(" " + bl.name );
		}
		//Now pass the call to the children
		for(Block bc: b.childBlock){
			printDominenece(bc);
		}
	}
	
	public static void  printFrontier(Block b){
		

		System.out.println("Frontier for " + b.name );
		for(Block bl: b.childBlock)
		{
			System.out.println(" " + bl.name );
		}
		//Now pass the call to the children
		for(Block bc: b.childBlock){
			printFrontier(bc);
		}
	}
	
	public static ArrayList<Block> getDomList(Block b)
	{
		ArrayList<Block> dom = new ArrayList<Block>();
		
		for(Block bp: b.parents)
		{
			dom.addAll(getDomList(bp));
		}
		return dom;
	}
}
