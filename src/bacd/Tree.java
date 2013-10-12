/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bacd;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author David
 */
public class Tree extends Object implements Cloneable {
Tree ancestor=null;
Tree nextSister=null;
Tree firstDaughter=null;
double state=0.0;
double cont1=-9999.000;
double cont2=-9999.000;
double contrastCont1=-9999.000;
double contrastCont2=-9999.000;
String discreteState=null;
String disc1="-9999";
String disc2="-9999";
boolean statesSet = false;
Vector downpass=null;
Vector uppass=null;
Vector right=null;
Vector left=null;
Vector descendants=null;


//PointD location = new PointD(0,0);
boolean descending = false;
int numLeaves=0;
String value=null;
//length is the branch length leading up to the node from the ancestor
double scaleFactor = 1.0;
double length=0.0;
double time=0.0;
double fossilAge=0.0;			//age of fossil on the branch
double constraintAge = 0.0;		//constraint age set for node
double tConstraintAge = 0.0;
int maxConstraintDepth = 100000;
int tmaxConstraintDepth = 0;
double maxConstraintAge = 0.0;
int numFossils=0;
double nodeAge=0.0;
double cumulativeTime=0.0;
int depth=0;
int rank=0;
boolean extinct = true;
boolean allTerminalsExtinct = false;


public Tree()
	{
	}

public Tree(Tree t, boolean link)
	{
	if(link)
		{
		this.ancestor = t.ancestor;
		this.nextSister = t.nextSister;
		this.firstDaughter = t.firstDaughter;
		}
	this.value = t.value;
	//this.location = t.location;
	this.numLeaves = t.numLeaves;
	this.descendants = t.descendants;
	this.statesSet = t.statesSet;
	this.state = t.state;
	this.cont1 = t.cont1;
	this.cont2 = t.cont2;
	this.discreteState=t.discreteState;
	this.disc1=t.disc1;
	this.disc2=t.disc2;
	this.length = t.length;
	this.time = t.time;
	this.depth = t.depth;
	this.rank = t.rank;
	this.fossilAge = t.fossilAge;
	this.nodeAge = t.nodeAge;
	this.cumulativeTime= t.cumulativeTime;
	this.numFossils = t.numFossils;
	this.extinct = t.extinct;
	this.allTerminalsExtinct = t.allTerminalsExtinct;
	this.constraintAge = t.constraintAge;
	this.tConstraintAge = t.tConstraintAge;
	this.maxConstraintDepth = t.maxConstraintDepth;
	this.scaleFactor = t.scaleFactor;
	}

public Tree(Tree t)
	{
	this.ancestor = t.ancestor;
	this.nextSister = t.nextSister;
	this.firstDaughter = t.firstDaughter;
	this.value = t.value;
	//this.location = t.location;
	this.numLeaves = t.numLeaves;
	this.descendants = t.descendants;
	this.statesSet = t.statesSet;
	this.state = t.state;
	this.cont1 = t.cont1;
	this.cont2 = t.cont2;
	this.discreteState=t.discreteState;
	this.disc1=t.disc1;
	this.disc2=t.disc2;
	this.length = t.length;
	this.time = t.time;
	this.depth = t.depth;
	this.rank = t.rank;
	this.fossilAge = t.fossilAge;
	this.nodeAge = t.nodeAge;
	this.cumulativeTime= t.cumulativeTime;
	this.numFossils = t.numFossils;
	this.extinct = t.extinct;
	this.allTerminalsExtinct = t.allTerminalsExtinct;
	this.constraintAge = t.constraintAge;
	this.tConstraintAge = t.tConstraintAge;
	this.maxConstraintDepth = t.maxConstraintDepth;
	this.scaleFactor = t.scaleFactor;
	}

public Tree(String label, double length)
	{
	this.value = label;
	this.length = length;
	}

public Tree(String treeString, boolean time)
	{
	Tree t = new Tree(treeFromString(treeString,true));
	//this = new Tree(treeFromString(treeString),true);
	//Tree t = new Tree();
	//t=makeRandomTree(t,10,new Random());
	//t=t.traverseTree(t);
	//this.ancestor = t.ancestor;
	//this.nextSister = t.nextSister;
	//this.firstDaughter = t.firstDaughter;
	//this.value = t.value;
	//this.location = t.location;
	//this.numLeaves = t.numLeaves;
	//TreeWindow tw = new TreeWindow(this);
	this.ancestor = t.ancestor;
	this.nextSister = t.nextSister;
	this.firstDaughter = t.firstDaughter;
	this.value = t.value;
	//this.location = t.location;
	this.numLeaves = t.numLeaves;
	this.descendants = t.descendants;
	this.state = t.state;
	this.length = t.length;
	this.time = t.time;
	this.depth = t.depth;
	this.rank = t.rank;
	this.fossilAge = t.fossilAge;
	this.nodeAge = t.nodeAge;
	this.cumulativeTime= t.cumulativeTime;
	this.numFossils = t.numFossils;
	this.extinct = t.extinct;
	this.allTerminalsExtinct = t.allTerminalsExtinct;
	this.constraintAge = t.constraintAge;
	this.tConstraintAge = t.tConstraintAge;
	this.maxConstraintDepth = t.maxConstraintDepth;
	this.scaleFactor = t.scaleFactor;
	}

public Tree(String treeString)
	{
	Tree t = new Tree(treeFromString(treeString,false));
	//Tree t = new Tree();
	//t=makeRandomTree(t,10,new Random());
	//t=t.traverseTree(t);
	this.ancestor = t.ancestor;
	this.nextSister = t.nextSister;
	this.firstDaughter = t.firstDaughter;
	this.value = t.value;
	//this.location = t.location;
	this.numLeaves = t.numLeaves;
	//TreeWindow tw = new TreeWindow(this);
	}

public Tree(File fileName)
	throws Exception
	{
	Tree t = new Tree(treeFromFile(fileName));

	this.ancestor = t.ancestor;
	this.nextSister = t.nextSister;
	this.firstDaughter = t.firstDaughter;
	this.value = t.value;
	//this.location = t.location;
	this.numLeaves = t.numLeaves;
	} 

public static int treeNodeLength(Tree t, boolean reset)
	{
	if(reset) t = Tree.findRoot(t);
	int cnt=1;
	while(t.firstDaughter!=null) 
		{
		cnt++;
		t = t.firstDaughter;
		}
	return cnt;
	}

public static int treeMaxNodeLength(Tree t, boolean reset)
	{
	if(reset) t = Tree.findRoot(t);
	if (t==null) return 0;
	if(t.firstDaughter==null) return 1;
	int r = Tree.treeMaxNodeLength(t.firstDaughter, false);
	int l = Tree.treeMaxNodeLength(t.firstDaughter.nextSister, false);
	return ((int)Math.max((double)r, (double)l) +1);
	}


//find the total time or max. branch length across all paths from root to tips
//ASSUMES NODE AGES HAVE BEEN SET 
public static double maxTreeTime(Tree t, boolean time, boolean setAges) 
	{
	boolean descending = false;
	double max = 0;
	if(setAges) t = Tree.setNodeAges(t, time);
	while(t.ancestor!=null) t=t.ancestor;
//System.out.println("Node " + t.value);
	if(t.firstDaughter!=null) 
		{
		t=t.firstDaughter;
//System.out.println("Node " + t.value);
		if(t.nodeAge>max) max = t.nodeAge;
		}
	else 
		{
System.out.println("Daughter is null.");
		return max;			
		}
	while(t.ancestor!=null)
		{
		if(t.firstDaughter!=null && !descending) 
			{
			t=t.firstDaughter;
//System.out.println("Node " + t.value);
			if(t.nodeAge>max) max = t.nodeAge;
			}
		else if(t.nextSister!=null)
			{
			t=t.nextSister;
//System.out.println("Node " + t.value);
			if(t.nodeAge>max) max = t.nodeAge;
			descending=false;
			}
		else 
			{
			descending = true;
			t=t.ancestor;
			}
		}
	return max;
	}

//Takes a tree with fossils that have been generated under an exponentially scaled tree and rescales the fossil ages to real time
//ASSUMES FOSSILS WERE GENERATED ON A SCALED TREE
public static Tree rescaleFossilAges(Tree t, double expScale)
	{
	boolean descending = false;

	while(t.ancestor!=null) t=t.ancestor;
	double sf = 1/t.scaleFactor;

	if(t.fossilAge>0.0) t.fossilAge = Math.log(t.fossilAge*sf+1)/expScale;
	if(t.firstDaughter!=null) 
		{
		t=t.firstDaughter;
		if(t.fossilAge>0.0) t.fossilAge = Math.log(t.fossilAge*sf+1)/expScale;
		}
	else return t;			

	while(t.ancestor!=null)
		{
		if(t.firstDaughter!=null && !descending) 
			{
			t=t.firstDaughter;
			if(t.fossilAge>0.0) t.fossilAge = Math.log(t.fossilAge*sf+1)/expScale;
			}
		else if(t.nextSister!=null)
			{
			t=t.nextSister;
			if(t.fossilAge>0.0) t.fossilAge = Math.log(t.fossilAge*sf+1)/expScale;
			descending=false;
			}
		else 
			{
			descending = true;
			t=t.ancestor;
			}
		}
	if(t.ancestor==null)
		return t;
	else
		return null;
	}

//ASSUMES TREE HAS BEEN SCALED TO HAVE MAX LENGTH OF 1.0
public static Tree rescaleTree(Tree t, double expScale, boolean time)
	{
	boolean descending = false;
	double max = 0;
	//t = Tree.setNodeAges(t, time);
	double len = Tree.maxTreeTime(t,time, true);

	while(t.ancestor!=null) t=t.ancestor;
	double sf = 1/t.scaleFactor;

	if(time) {t.time = Math.log(t.nodeAge*sf+1)/expScale;}
	else {t.length = Math.log(t.nodeAge*sf+1)/expScale;}
	//System.out.println("\tTaking the scaling into account:\n\tThe original root age is " + (sf*t.nodeAge) + "\n\tthe rescaled age is " + (t.time));

	if(t.firstDaughter!=null) 
		{
		t=t.firstDaughter;
		if(time) {t.time = Math.log(t.nodeAge*sf+1)/expScale-Math.log(t.ancestor.nodeAge*sf+1)/expScale;}
		else {t.length = Math.log(t.nodeAge*sf+1)/expScale-Math.log(t.ancestor.nodeAge*sf+1)/expScale;}
		}
	else return t;			
	while(t.ancestor!=null)
		{
		if(t.firstDaughter!=null && !descending) 
			{
			t=t.firstDaughter;
			if(time) {t.time = Math.log(t.nodeAge*sf+1)/expScale-Math.log(t.ancestor.nodeAge*sf+1)/expScale;}
			else {t.length = Math.log(t.nodeAge*sf+1)/expScale-Math.log(t.ancestor.nodeAge*sf+1)/expScale;}
			}
		else if(t.nextSister!=null)
			{
			t=t.nextSister;
			if(time) {t.time = Math.log(t.nodeAge*sf+1)/expScale-Math.log(t.ancestor.nodeAge*sf+1)/expScale;}
			else {t.length = Math.log(t.nodeAge*sf+1)/expScale-Math.log(t.ancestor.nodeAge*sf+1)/expScale;}

			descending=false;
			}
		else 
			{
			descending = true;
			t=t.ancestor;
			}
		}
	if(t.ancestor==null)
		return t;
	else
		return null;
	}

//ASSUMES TREE HAS BEEN SCALED TO HAVE MAX LENGTH OF 1.0
public static Tree scaleTreeExp(Tree t, double expScale, double newLen, boolean time)
	{
	//t = Tree.setNodeAges(t, time);
	double len = Tree.maxTreeTime(t,time, true);
//System.out.println("The max tree time is " + len + "(in scaleTreeExp). The the length to scale to is " + newLen + " and the expScale is " + expScale);
	boolean descending = false;
	double max = 0;

	while(t.ancestor!=null) t=t.ancestor;
	if(time) 
		{
		//System.out.println("The original root time is " + t.time);
		t.time = Math.exp(expScale*(t.nodeAge))-Math.exp(expScale*(0));
		double ti = Math.log(t.time+1)/expScale;
		//len = Tree.maxTreeTime(t,time);
		//while(t.ancestor!=null) t=t.ancestor;
		//System.out.println("The exponentially scaled root age is " + t.time + " and the original age is " + ti);
	
		}
	else {t.length = Math.exp(expScale*(t.nodeAge))-Math.exp(expScale*(0));}
	//t.nodeAge = Math.exp(expScale*(t.nodeAge))-1;

	//System.out.println("The branchlength leading to the root is " + t.time);

	if(t.firstDaughter!=null) 
		{
		t=t.firstDaughter;
		if(time) {t.time = Math.exp(expScale*(t.nodeAge))-Math.exp(expScale*(t.ancestor.nodeAge));}
		else {t.length = Math.exp(expScale*(t.nodeAge))-Math.exp(expScale*(t.ancestor.nodeAge));}
			//if(Math.exp(expScale*(t.nodeAge))-Math.exp(expScale*(t.ancestor.nodeAge))<0)
				//System.out.println("ancestor older than descendant");
		//t.nodeAge = Math.exp(expScale*(t.nodeAge))-1;
		}
	else return t;			
	while(t.ancestor!=null)
		{
		if(t.firstDaughter!=null && !descending) 
			{
			t=t.firstDaughter;
			//System.out.println(t.time);
			if(time) {t.time = Math.exp(expScale*(t.nodeAge))-Math.exp(expScale*(t.ancestor.nodeAge));}
			else {t.length = Math.exp(expScale*(t.nodeAge))-Math.exp(expScale*(t.ancestor.nodeAge));}
			//if(Math.exp(expScale*(t.nodeAge))-Math.exp(expScale*(t.ancestor.nodeAge))<0)
				//System.out.println("a ancestor older than descendant: node=" + t.nodeAge + " ancestor=" + t.ancestor.nodeAge);
			//t.nodeAge = Math.exp(expScale*(t.nodeAge))-1;
			//System.out.println("\t" + t.time);
			}
		else if(t.nextSister!=null)
			{
			t=t.nextSister;
			//System.out.println(t.time);
			if(time) {t.time = Math.exp(expScale*(t.nodeAge))-Math.exp(expScale*(t.ancestor.nodeAge));}
			else {t.length = Math.exp(expScale*(t.nodeAge))-Math.exp(expScale*(t.ancestor.nodeAge));}
			//if(Math.exp(expScale*(t.nodeAge))-Math.exp(expScale*(t.ancestor.nodeAge))<0)
				//System.out.println("b ancestor older than descendant: node=" + t.nodeAge + " ancestor=" + t.ancestor.nodeAge);
			//t.nodeAge = Math.exp(expScale*(t.nodeAge))-1;
			//System.out.println("\t" + t.time);

			descending=false;
			}
		else 
			{
			descending = true;
			t=t.ancestor;
			}
		}
	if(t.ancestor==null)
		{
		len = Tree.maxTreeTime(t, time, true);
		//System.out.println("The max tree time is " + Tree.maxTreeTime(t, time, true));
	//	System.out.println("The max tree time is " + Tree.maxTreeTime(t.scaleTree(t, newLen, time), time) + "(in scaleTreeExp a). The the length to scale to is " + newLen + " and the expScale is " + expScale);
		t.scaleFactor = newLen/len;
		return t.scaleTree(t, newLen, time);
		}
		//return t;
	else
		return null;
	}

//scales time or branchlength so that total is newLen (set time=true if time, false otherwise)
public static Tree scaleTree(Tree t, double newLen, boolean time)
	{
	//t = Tree.setNodeAges(t, time);
	double len = Tree.maxTreeTime(t,time, true);
//System.out.println("Max tree time is " + len + ". Length (in scaleTree) to scale to is " + newLen);
	boolean descending = false;
	double max = 0;

	while(t.ancestor!=null) t=t.ancestor;
	if(time) t.time = t.time * newLen / len;
	else t.length = t.length * newLen / len;
	//t.nodeAge = t.nodeAge*newLen / len;

	if(t.firstDaughter!=null) 
		{
		t=t.firstDaughter;
		if(time) t.time = t.time * newLen / len;
		else t.length = t.length * newLen / len;
		//t.nodeAge = t.nodeAge*newLen / len;
		//if(t.nodeAge-t.ancestor.nodeAge<0)
			//System.out.println("p ancestor older than descendant: node=" + t.nodeAge + " ancestor=" + t.ancestor.nodeAge);
		}
	else return t;			
	while(t.ancestor!=null)
		{
		if(t.firstDaughter!=null && !descending) 
			{
			t=t.firstDaughter;
			if(time) t.time = t.time * newLen / len;
			else t.length = t.length * newLen / len;
			//t.nodeAge = t.nodeAge*newLen / len;
			//if(t.nodeAge-t.ancestor.nodeAge<0)
				//System.out.println("d ancestor older than descendant: node=" + t.nodeAge + " ancestor=" + t.ancestor.nodeAge);
			}
		else if(t.nextSister!=null)
			{
			t=t.nextSister;
			if(time) t.time = t.time * newLen / len;
			else t.length = t.length * newLen / len;
			//t.nodeAge = t.nodeAge*newLen / len;
			descending=false;
			//if(t.nodeAge-t.ancestor.nodeAge<0)
				//System.out.println("d ancestor older than descendant: node=" + t.nodeAge + " ancestor=" + t.ancestor.nodeAge);
			}
		else 
			{
			descending = true;
			t=t.ancestor;
			}
		}
	if(t.ancestor==null)
		return t;
	else
		return null;
	}

public static Tree setCumulativeNodeAge(Tree t, boolean time)
	{
	double length = 0.0;
	boolean descending = false;

	while(t.ancestor!=null) t=t.ancestor;
	if(time) length = t.time;
	else length = t.length;
	t.cumulativeTime = length;

	if(t.firstDaughter!=null) 
		{
		t=t.firstDaughter;
		if(time) length += t.time;
		else length += t.length;
		t.cumulativeTime = length;
		}
	else return t;			
	while(t.ancestor!=null)
		{
		if(t.firstDaughter!=null && !descending) 
			{
			t=t.firstDaughter;
			if(time) length += t.time;
			else length += t.length;;
			t.cumulativeTime = length;
			}
		else if(t.nextSister!=null)
			{
			t=t.nextSister;
			if(time) length += t.time;
			else length += t.length;;
			t.cumulativeTime = length;
			descending=false;
			}
		else 
			{
			descending = true;
			t=t.ancestor;
			}
		}
	if(t.ancestor==null)
		return t;
	else
		return null;
	}

public static double getTotalLength(Tree t, boolean time)
	{
	double length = 0.0;
	boolean descending = false;

	while(t.ancestor!=null) t=t.ancestor;
	if(time) length = t.time;
	else length = t.length;

	if(t.firstDaughter!=null) 
		{
		t=t.firstDaughter;
		if(time) length += t.time;
		else length += t.length;
		}
	else return length;			
	while(t.ancestor!=null)
		{
		if(t.firstDaughter!=null && !descending) 
			{
			t=t.firstDaughter;
			if(time) length += t.time;
			else length += t.length;;
			}
		else if(t.nextSister!=null)
			{
			t=t.nextSister;
			if(time) length += t.time;
			else length += t.length;;
			descending=false;
			}
		else 
			{
			descending = true;
			t=t.ancestor;
			}
		}
	return length;
	}

//sets node ages with root ancestor being at t=0
//ASSUMES THAT TREE ALREADY HAS TIMES AND BRANCHLENGTHS SET
//if times are given, records ages in time, else records in total branch length from root
public static Tree setNodeAges(Tree t, boolean time)
	{
	boolean descending = false;
	while(t.ancestor!=null) t=t.ancestor;
	if(time) t.nodeAge = t.time;
	else t.nodeAge = t.length;
	if(t.firstDaughter!=null) 
		{
		t=t.firstDaughter;
		//if(t.time <0) System.out.println("time less than 0! a");
		if(time) t.nodeAge = t.ancestor.nodeAge + t.time;
		else t.nodeAge = t.ancestor.nodeAge + t.length;
		}
	else return t;			
	while(t.ancestor!=null)
		{
		if(t.firstDaughter!=null && !descending) 
			{
			t=t.firstDaughter;
		//if(t.time <0) System.out.println("time less than 0! a");
			if(time) t.nodeAge = t.ancestor.nodeAge + t.time;
			else t.nodeAge = t.ancestor.nodeAge + t.length;
			}
		else if(t.nextSister!=null)
			{
			//if(t.firstDaughter==null) 
				//{ 
				//if(t.time>0) t.nodeAge = t.ancestor.nodeAge + t.ancestor.time;
				//else t.nodeAge = t.ancestor.nodeAge + t.ancestor.length;
				//}
			t=t.nextSister;
		//if(t.time <0) System.out.println("time less than 0! a");
			if(time) t.nodeAge = t.ancestor.nodeAge + t.time;
			else t.nodeAge = t.ancestor.nodeAge + t.length;
			descending=false;
			}
		else 
			{
			//if(t.firstDaughter==null) 
				//{ 
				//if(t.time>0) t.nodeAge = t.ancestor.nodeAge + t.ancestor.time;
				//else t.nodeAge = t.ancestor.nodeAge + t.ancestor.length;
				//}
			descending = true;
			t=t.ancestor;
			}
		}
	if(t.ancestor==null)
		return t;
	else
		return null;
	
	}

public int numSis(Tree T)
	{
	int i=0;
	Tree t = new Tree();
	for(t=T; t!=null; t=t.nextSister)
		i++;
	return i;
	}

public static Tree addTerminalBranchTime(Tree t, double time, boolean ext)
	{
	boolean descending = false;
	while(t.ancestor!=null) t=t.ancestor;
	if(t.firstDaughter!=null) t=t.firstDaughter;
	else
		{
		if(!t.extinct || !ext)
			{
			t.time += time;
			t.length += time;
			}
		return t;
		}
	while(t.ancestor!=null)
		{
		if(t.firstDaughter!=null && !descending) t=t.firstDaughter;
		else if(t.nextSister!=null)
			{
			if(t.firstDaughter==null) 
				{ 
				if(!t.extinct || !ext)
					{
					t.time += time; 
					t.length += time;
					}
				}
			t=t.nextSister;
			descending=false;
			}
		else 
			{
			if(t.firstDaughter==null) 
				{ 
				if(!t.extinct || !ext)
					{
					t.time += time; 
					t.length += time;
					}
				}
			descending = true;
			t=t.ancestor;
			}
		}
	if(t.ancestor==null)
		return t;
	else
		return null;
	}

//addLengthsToAllTerminals, getWaitingTime, makeRandomTree, QuickTree code 
//from Mesquiteproject.org source code EqualRatesSpSampled.java


//ASSUMES FULLY BIFURCATING TREE
public static Tree findTaxon(Tree t, String taxLabel, boolean reset)
	{
	if(t==null)
		return null;
	if(reset) { t = Tree.findRoot(t); }
	if(t.value.equals(taxLabel))
		return t;
	Tree t1 = findTaxon(t.firstDaughter, taxLabel, false);
		if(t1!=null) return t1;
	t1 = findTaxon(t.nextSister, taxLabel, false);
		if(t1!=null) return t1;
	return null;
	}

//ASSUMES TREE IS BIFURCATING
public static Tree allTerminalsExtinct(Tree t, boolean reset)
	{
	if(t==null) return null;
	if(reset) { t = Tree.findRoot(t); }
	if(t.firstDaughter==null && t.extinct)
		{
//System.out.println("Node " + t.value + " is a terminal extinct node.");
		t.allTerminalsExtinct=true;
		return t;
		}
	else if(t.firstDaughter==null)
		{
//System.out.println("Node " + t.value + " is a terminal extant node.");
		}
	else
		{
		t.firstDaughter = allTerminalsExtinct(t.firstDaughter, false);
		t.firstDaughter.nextSister = allTerminalsExtinct(t.firstDaughter.nextSister, false);
		if(t.firstDaughter.allTerminalsExtinct && t.firstDaughter.nextSister.allTerminalsExtinct)
			{
//System.out.println("Node " + t.value + " all extinct; daughter " + t.firstDaughter.value + " nxt sis " + t.firstDaughter.nextSister.value + ". ");
//if(t.ancestor!=null) System.out.println("\tAncestor " + t.ancestor.value + ".");
			t.allTerminalsExtinct = true;
			}
		}
	return t;	
	}

public static int countConstraints(Tree t)
	{
	t=Tree.findRoot(t);
	boolean descending = false;
	int count = 0;
	while(t.ancestor!=null) t=t.ancestor;
	if(t.constraintAge!=0) count++;
	if(t.firstDaughter!=null) 
		{
		t=t.firstDaughter;
		if(t.constraintAge!=0) count++;
		}
	else 
		{
		return count;
		}
	while(t.ancestor!=null)
		{
		if(t.firstDaughter!=null && !descending) 
			{
			t=t.firstDaughter;
			if(t.constraintAge!=0) count++;
			}
		else if(t.nextSister!=null)
			{
			t=t.nextSister;
			if(t.constraintAge!=0) count++;
			descending=false;
			}
		else 
			{
			descending = true;
			t=t.ancestor;
			}
		}
	return count;
	}

//max is the smallest number since we're dealing with backwards time
public static double maxConstraint(Tree t, boolean reset)
	{
	if(reset) t = Tree.findRoot(t);
	if(t==null) { System.out.println("Tree is null"); return 0.0;}
	if(t.firstDaughter==null) return t.constraintAge;
//System.out.println("Node from maxConstraint " + t.value + " constraint " + t.constraintAge);
	double m1 = Tree.maxConstraint(t.firstDaughter, false);
	double m2 = Tree.maxConstraint(t.firstDaughter.nextSister, false);
	double max = t.constraintAge;
	if(m1!=0.0)
		{
		if(max==0.0) max = m1;
		else if(m1<max) max = m1;
		}
	if(m2!=0.0)
		{
		if(max==0.0) max = m2;
		else if(m2<max) max = m2;
		}
	return max;
	}

//leaves non-bifurcating nodes
public static Tree trimTaxon(Tree t, String name, boolean reset)
	throws Exception
	{
	if(reset) t = Tree.findRoot(t);
	if(t==null) 
		{
		System.out.println("Trimmed tree is null.");
		System.exit(0);
		}
	if(t.firstDaughter==null) 
		{
		if(t.value.equals(name)) 
			{
//System.out.println(t.value + " is trimmed." );
			return null;
			}
		else 
			{
			return new Tree(t,false);
			}
		}
	Tree t1 = Tree.trimTaxon(t.firstDaughter,name,false);
	Tree t2 = Tree.trimTaxon(t.firstDaughter.nextSister,name,false);
	if(t1==null && t2==null) 
		{
		return null;
		}
	else if(t1!=null && t2!=null)
		{
		Tree t3 = new Tree(t,false);
		t2.ancestor = t3;
		t1.ancestor=t3;
		t1.nextSister=t2;
		t3.firstDaughter = t1;
		double max = Tree.maxConstraint(t3, true);
		if(t3.constraintAge>max && max!=0.0) 
			{
			t3.constraintAge=0.0;
			}
		if((t3.constraintAge>t2.tConstraintAge && t2.tConstraintAge!=0.0) || t3.constraintAge==0.0)
			{
			if(t2.tConstraintAge<max || max==0) t3.constraintAge=t2.tConstraintAge;
			else t3.constraintAge = 0.0;
			}
		if((t3.constraintAge>t1.tConstraintAge && t1.tConstraintAge!=0.0) || t3.constraintAge==0.0)
			{
			if(t1.tConstraintAge<max || max==0) t3.constraintAge=t1.tConstraintAge;
			else t3.constraintAge = 0.0;
			}
		return t3;
		}
	else if(t1!=null)
		{
		t1.time+= t.time;	//all other variables are set at defaults;
		t1.length+= t.length;	//all other variables are set at defaults;
		if((t1.tConstraintAge>t.constraintAge && t.constraintAge!=0.0) || t1.tConstraintAge==0.0)
			t1.tConstraintAge = t.constraintAge;
		return t1;
		}
	else if(t2!=null)
		{
		t2.time+=t.time;
		t2.length+= t.length;	//all other variables are set at defaults;
		if((t2.tConstraintAge>t.constraintAge && t.constraintAge!=0.0) || t2.tConstraintAge==0.0)
			t2.tConstraintAge = t.constraintAge;
		return t2;
		}
	throw new Exception("Trim taxon error.");
	}

//leaves non-bifurcating nodes
public static Tree trimExtinctTaxa(Tree t, boolean reset)
	throws Exception
	{
//System.out.println("t1");
	if(reset) t = Tree.findRoot(t);
	//double len = Tree.maxTreeTime(t,true,true);
	if(t==null) 
		{
		System.out.println("Trimmed tree is null.");
		System.exit(0);
		//return null;
		}
//System.out.println("t2");
	if(t.firstDaughter==null) 
		{
		if(t.extinct) 
			{
System.out.println(t.value + " is extinct." );
			if(t.constraintAge>0) System.out.println("a\tDaughter is null but constraint: " + t.constraintAge + "\n\n");
			return null;
			}
		else 
			{
//System.out.println("t2a");
			return new Tree(t,false);
			}
		}
//System.out.println("t3");
	Tree t1 = Tree.trimExtinctTaxa(t.firstDaughter,false);
	Tree t2 = Tree.trimExtinctTaxa(t.firstDaughter.nextSister,false);
//System.out.println("t4");
	if(t1==null && t2==null) 
		{
		return null;
		}
	else if(t1!=null && t2!=null)
		{
//System.out.println("t5");
		Tree t3 = new Tree(t,false);
//if(t.constraintAge>0.0 && t3.constraintAge==0.0) System.out.println("b\tError: t3 constraint not set: " + t.constraintAge);
//if(t3.constraintAge>0) System.out.println("c\tprecollapse t3 constraint: " + t3.constraintAge + " at node " + t3.value);
		t2.ancestor = t3;
		t1.ancestor=t3;
		t1.nextSister=t2;
		t3.firstDaughter = t1;
//System.out.println("Running maxConstraint.");
		double max = Tree.maxConstraint(t3, true);
//System.out.println("The max is " + max);
//System.out.println("The max constraint is " + max + " and the age at this node is " + t3.constraintAge);
//System.out.println("age of " + t3.value + " is " + t3.constraintAge);
		if(t3.constraintAge>max && max!=0.0) 
			{
//System.out.println("!\tt3 ("+ t3.constraintAge + ") had a younger constraint than one of its daughters (" + max + ")");
			t3.constraintAge=0.0;
			}
		if((t3.constraintAge>t2.tConstraintAge && t2.tConstraintAge!=0.0) || t3.constraintAge==0.0)
			{
			if(t2.tConstraintAge<max || max==0) t3.constraintAge=t2.tConstraintAge;
			else t3.constraintAge = 0.0;
//System.out.println("d\tCollapsing from " + t2.value + " to " +t3.value);
			}
		if((t3.constraintAge>t1.tConstraintAge && t1.tConstraintAge!=0.0) || t3.constraintAge==0.0)
			{
			if(t1.tConstraintAge<max || max==0) t3.constraintAge=t1.tConstraintAge;
			else t3.constraintAge = 0.0;
//System.out.println("e\tCollapsing from " + t1.value + " to " +t3.value);
			}
		return t3;
		}
	else if(t1!=null)
		{
//System.out.println("t6");
//if(t1.constraintAge>0.0) System.out.println("e\tConstraint age of t1: " + t1.constraintAge);
//if(t.constraintAge>0.0) System.out.println("f\tConstraint age of t prior to trim: " + t.constraintAge);
		t1.time+= t.time;	//all other variables are set at defaults;
		if((t1.tConstraintAge>t.constraintAge && t.constraintAge!=0.0) || t1.tConstraintAge==0.0)
			t1.tConstraintAge = t.constraintAge;
		return t1;
		}
	else if(t2!=null)
		{
//System.out.println("t7");
//if(t2.constraintAge>0.0) System.out.println("g\tConstraint age of t2: " + t2.constraintAge);
//if(t.constraintAge>0.0) System.out.println("h\tConstraint age of t prior to trim: " + t.constraintAge);
		t2.time+=t.time;
		if((t2.tConstraintAge>t.constraintAge && t.constraintAge!=0.0) || t2.tConstraintAge==0.0)
			t2.tConstraintAge = t.constraintAge;
		return t2;
		}
//System.out.println("t8");
	throw new Exception("Trim extinct taxa error.");
	}

public static String[] getInternalNodeLabels(Tree t)
	{
	boolean descending = false;
	Vector labels = new Vector();
	while(t.ancestor!=null) t=t.ancestor;
	if(t.firstDaughter!=null) labels.add(t.value);
	if(t.firstDaughter!=null) 
		{
		t=t.firstDaughter;
		if(t.firstDaughter!=null) labels.add(t.value);
		}
	else 
		{
		String[] out = new String[labels.size()];
        	labels.copyInto(out);
		return out;
		}
	while(t.ancestor!=null)
		{
		if(t.firstDaughter!=null && !descending) 
			{
			t=t.firstDaughter;
			if(t.firstDaughter!=null) labels.add(t.value);
			}
		else if(t.nextSister!=null)
			{
			t=t.nextSister;
			if(t.firstDaughter!=null) labels.add(t.value);
			descending=false;
			}
		else 
			{
			descending = true;
			t=t.ancestor;
			}
		}
	String[] out = new String[labels.size()];
        labels.copyInto(out);
	return out;
	
	}

//ASSUMES NODE AGES HAVE BEEN SET
public static double[] getEvolutionaryDistances(Tree t, String label1, String[] labels)
	{
	double[] dist = new double[labels.length];
	t = Tree.findTaxon(t, label1, true);
	double a1 = t.nodeAge;
	for(int i=0; i<labels.length; i++)
		{
		t = Tree.findTaxon(t, labels[i], true);
		double a2 = t.nodeAge;
		double mrca = 0.0;
		try { Tree.timeAtMRCA(t, label1, labels[i]); } catch(Exception e) { System.out.println("Couldn't find common ancestor of nodes " + label1 + " and " + labels[i]); System.exit(0); }
		dist[i] = a1+a2-2*mrca;
		}
	return dist;
	}



//ASSUMES FOSSILS HAVE BEEN GENERATED ON TREE
//ERROR RANGES FROM 0 TO <INF. AT 0, THERE IS STILL ERROR - USE PLACECORRECTCONSTRAINTS METHOD FOR NO ERROR
public static Tree placeConstraintsWithError(Tree t, double error, Random r)
	{
//Get all the fossil nodes
	String[] fossilNodes = Tree.getNodeLabelsWithFossilsOnBranch(t);

//Get all the internal nodes
	String[] internalNodes = Tree.getInternalNodeLabels(t);
	
	for(int i=0; i<fossilNodes.length; i++)
		{
		t = Tree.findTaxon(t, fossilNodes[i], true);
		double fa = t.nodeAge - t.fossilAge;
		double fa1 = t.fossilAge;
	
		if(fa <=0 || t.nodeAge == fa) 
			{
			System.out.println("Found a node with no fossil where fossil was expected: " + fa);
			System.exit(0);
			}

//Get evolutionary distances between current fossil and all internal nodes
		double[] dist = Tree.getEvolutionaryDistances(t, fossilNodes[i], internalNodes);
		double tot = 0;

//Make probability function
		for(int j=0; j<dist.length; j++)
			{
			dist[j]+=fa;
			dist[j] = 1/(dist[j]+error);
			tot+=dist[j];
			}
//Select node
		double rn = r.nextDouble()*tot;
		double start = 0.0;
		boolean found = false;
		for(int j=0; j<dist.length; j++)
			{
			if(rn > start && rn<start + dist[j])
				{
				t = Tree.findTaxon(t, internalNodes[j], true);
//Place constraint
				found=true;
				if(fa1<t.constraintAge || t.constraintAge==0.0) 
					{
					t.constraintAge = fa1;
					}
				break;
				}
			start += dist[j];
			}
		if(!found)
			{
			System.out.println("No constraint was set for node " + fossilNodes[i] + " with total " + tot + " and random number " + rn);
			System.exit(0);
			}
		}
	return t;
	}

public static int fossilDepth(Tree t, String fossilNodeLabel)
	{
	t = Tree.findTaxon(t, fossilNodeLabel, true);
	int cnt=0;
	while(t.ancestor!=null)
		{
		cnt++;
		t=t.ancestor;
		}
	return cnt;
	}

//public static String oldestFossilNode(Tree t)
	//{
	//String[] fossilNodes=Tree.getNodeLabelsWithFossilsOnBranch(t);
	//double max = -1;
	//String label = "";
	//for(int i=0; i<fossilNodes.length; i++)
		//{
		//t = Tree.findTaxon(t, fossilNodes[i], true);
		//if(t.fossilAge==0.0) 
			//{
			//System.out.println("An incorrect fossil was found with age 0.0");
			//System.exit(0);
			//}
		//if(t.fossilAge<max || max==-1)
			//{
			//max = t.fossilAge;
			//label = t.value;
			//}
		//}
	//return label;
	//}

//ASSUMES THAT FOSSILS HAVE BEEN GENERATED ON TREE
//MUST!!!! RUN TRIM EXTINCT TAXA AFTERWARDS FOR USE WITH DIVERGENCE TIME SOFTWARE
public static Tree placeCorrectConstraints(Tree t)
	{
	t = Tree.findRoot(t);
	double len = Tree.maxTreeTime(t,true,true);
	//double len = Tree.getTotalLength(t, true);
	String[] fossilNodes=Tree.getNodeLabelsWithFossilsOnBranch(t);

//System.out.println("There are " + fossilNodes.length + " fossils at nodes:");
//for(int i=0; i<fossilNodes.length; i++) System.out.print("\t" + fossilNodes[i]);
//System.out.println();

	double age = 0.0;
	for(int i=0;i<fossilNodes.length;i++)
		{
//System.out.println("Finding " + fossilNodes[i]);
		t = Tree.findTaxon(t, fossilNodes[i], true);
if(t==null) System.out.println("Error finding taxon");
		age = t.fossilAge;
//System.out.println("fossil was found with age " + age);
		if(t.fossilAge==0.0) 
			{
			System.out.println("An incorrect fossil was found with age 0.0");
			System.exit(0);
			}
		while(!Tree.hasExtantTaxa(t))
			{
//System.out.println("Testing node " + t.value + " for extant descendants.");
			if(t.ancestor == null)
				{
System.out.println("Ancestor was null." );
				return t;
				}
			t = t.ancestor;
			}
		if(t.ancestor!=null)
			{
			if(age<t.ancestor.constraintAge || t.ancestor.constraintAge==0.0) 
				{
				//t.constraintAge = len - age;
				t.ancestor.constraintAge = age;
//System.out.println("Placing constraint at node " + t.ancestor.value + ":");
//System.out.println("\tNode age: " + (t.ancestor.nodeAge));
//System.out.println("\tFossil age: " + (t.ancestor.constraintAge));
				}
			//else System.out.println("\tMultiple constraints for node - placing oldest constraint");
			}
		}
	return t;
	}

public static int findDistanceBetween(Tree t, String label1, String label2)
	{
	int l1 = 0;
	int l2 = 0;
	Tree l1t = Tree.findTaxon(t, label1, true);
	Tree l2t = Tree.findTaxon(t, label2, true);
	while(l1t!=null)
		{
		l1t = l1t.ancestor;
		l1++;
		}
	while(l2t!=null)
		{
		l2t = l2t.ancestor;
		l2++;
		}
//System.out.println("The depth of " + label1 + " is " + l1 + " and the depth of " + label2 + " is " + l2);
	return Math.abs(l2-l1);
	}

public static String getNodeLabelWithFossilDate(Tree t, double date)
	{
	boolean descending = false;
	String label = "";
	if(t==null) 
		{
		System.out.println("NULL TREE IN FOSSIL DATE");
		return null;
		}
	while(t.ancestor!=null) t=t.ancestor;
	if(t.fossilAge==date) return t.value;
	else if(t.fossilAge>0) {}
//System.out.println(t.value + ": " + t.fossilAge + " " + date);
	if(t.firstDaughter!=null) 
		{
		t=t.firstDaughter;
		if(t.fossilAge==date) return t.value;
		else if(t.fossilAge>0) {}
//System.out.println(t.value + ": " + t.fossilAge + " " + date);
		}
	while(t.ancestor!=null)
		{
		if(t.firstDaughter!=null && !descending) 
			{
			t=t.firstDaughter;
			if(t.fossilAge==date) return t.value;
			else if(t.fossilAge>0) {}
//System.out.println(t.value + ": " + t.fossilAge + " " + date);
			}
		else if(t.nextSister!=null)
			{
			t=t.nextSister;
			if(t.fossilAge==date) return t.value;
			else if(t.fossilAge>0) {}
//System.out.println(t.value + ": " + t.fossilAge + " " + date);
			descending=false;
			}
		else 
			{
			descending = true;
			t=t.ancestor;
			}
		}
	return null;
	}

//ASSUMES THAT THE AGES OF NODES HAVE BEEN SET WITH EXTANT TAXA EQUAL TO 0
//ASSUMES THAT FOSSILS HAVE BEEN GENERATED ON TREE
public static String[] getNodeLabelsWithFossilsOnBranch(Tree t)
	{
	boolean descending = false;
	Vector labels = new Vector();
	while(t.ancestor!=null) t=t.ancestor;
	if(t.fossilAge>0.0) labels.add(t.value);
	if(t.firstDaughter!=null) 
		{
		t=t.firstDaughter;
		if(t.fossilAge>0.0) 
			{
			labels.add(t.value);
		//if(t.firstDaughter == null) System.out.println("fossil at node " + t.value + " on terminal branch.");
			}
		}
	else 
		{
		String[] out = new String[labels.size()];
        	labels.copyInto(out);
		return out;
		}
	while(t.ancestor!=null)
		{
		if(t.firstDaughter!=null && !descending) 
			{
			t=t.firstDaughter;
			if(t.fossilAge>0.0) 
				{
				labels.add(t.value);
		//if(t.firstDaughter == null) System.out.println("fossil at node " + t.value + " on terminal branch.");
				}
			}
		else if(t.nextSister!=null)
			{
			t=t.nextSister;
			if(t.fossilAge>0.0) 
				{
				labels.add(t.value);
		//if(t.firstDaughter == null) System.out.println("fossil at node " + t.value + " on terminal branch.");
				}
			descending=false;
			}
		else 
			{
			descending = true;
			t=t.ancestor;
			}
		}
	String[] out = new String[labels.size()];
        labels.copyInto(out);
	return out;
	}

//ASSUMES THAT THE AGES OF NODES HAVE BEEN SET WITH EXTANT TAXA EQUAL TO 0
public static double timeAtMRCA(Tree t, String taxon1, String taxon2)
	throws Exception
	{
        Tree tree = new Tree(t);
        Tree copy = new Tree(t);
		if(taxon1.equals("1"))
			{
			tree = Tree.findTaxon(tree, taxon1,true);
			return tree.nodeAge;
			}
		if(taxon2.equals("1"))
			{
			tree = Tree.findTaxon(tree, taxon2,true);
			return tree.nodeAge;
			}
			
                Tree copy1 = Tree.findTaxon(copy, taxon1, true);
                tree = Tree.findTaxon(tree, taxon2, true);
                if(copy1.value.equals(tree.value)) return copy1.nodeAge;
                while(tree.ancestor!=null)
                        {
                        tree = tree.ancestor;
                        copy = copy1;
                        while(copy.ancestor!=null)
                                {
                                copy = copy.ancestor;
                                if(copy.value.equals(tree.value))
                                        return copy.nodeAge;
                                }
                        }
        throw new Exception("No ancestor found for " + taxon1 + " and " + taxon2);
	}

/*
public static r8sResults getR8SAges(String file)
	{
	BufferedReader d = null;
	while(d==null)
		{
		try{ d = new BufferedReader(new FileReader(file)); }
		catch(Exception e) { System.out.println("Trouble reading r8s results; retring");return null;}
		}
	String line;
	Vector ages = new Vector();
	Vector nodes = new Vector();
	try{
	while((line=d.readLine())!=null)
		{
		if(line.matches(".*\\[\\*\\*\\].*")) 
			{
//System.out.println(line);
			String[] info = line.split("\\s+");
//System.out.println("\t" + info.length + " words in line.");
			info[2] = info[2].replaceAll("a","");
			ages.add(info[3]);
			nodes.add(info[2]);
			//line = line.replaceAll(",","");
			//line = line.replaceAll(";","");
			//line = line.replaceAll("^\\s+", "");
			//String[] data = line.split("\\s");
			}
		}
	}
	catch(Exception e) { System.out.println("Trouble reading r8s results"); e.printStackTrace(); return null;}
	String[] agess = new String[ages.size()];
	String[] nodess = new String[nodes.size()];
        ages.copyInto(agess);
        nodes.copyInto(nodess);
	
	return new r8sResults(agess,nodess);
	}
*/

public static double calculateReconstructedAgeError(Tree t, String[] nodes, double[] reconAges, double len, FileWriter fw, int rep)
	throws Exception
	{
	double error = 0.0;
	for(int i=0; i<nodes.length;i++)
		{
		t = Tree.findTaxon(t,nodes[i],true);
		error += t.nodeAge - reconAges[i];
//System.out.println("Node " + t.value + " age " + (len-t.nodeAge) + ". Reconstructed age " + reconAges[i] + " constraint age " + (len-t.constraintAge));
		if(t.constraintAge>0.0) fw.write(rep + "\t" + t.value + "\t" + (len-t.nodeAge) + "\t" + reconAges[i] + "\t" + (len-t.constraintAge) + "\n");
		else fw.write(rep + "\t" + t.value + "\t" + (len-t.nodeAge) + "\t" + reconAges[i] + "\t\n");
		}
	if(nodes.length>0) error /= nodes.length;
	return error;
	}

public static double calculateReconstructedAgeErrorSS(Tree t, String[] nodes, double[] reconAges)
	{
	double error = 0.0;
	for(int i=0; i<nodes.length;i++)
		{
		t = Tree.findTaxon(t,nodes[i],true);
		error += (t.nodeAge - reconAges[i]) * (t.nodeAge - reconAges[i]);
		}
	if(nodes.length>1) error /= (nodes.length*(nodes.length-1));
	return error;
	}

//ASSUMES THAT THE AGES OF NODES HAVE BEEN SET WITH EXTANT TAXA EQUAL TO 0
//Calculates error of internal nodes only
//From the Near and Sanderson paper
//E = Sum(NodeActual(i) - NodeReconstructed(i))
//public static double calculateReconstructedAgeError(Tree actual, Tree empirical)
	//throws Exception
	//{
	//if(actual==null || empirical==null) throw new Exception("One or more trees are null.");
	//String[] taxa = Tree.getTaxonLabels(actual);
	//double error = 0.0;
	//for(int i = 1 ; i<taxa.length; i++)
		//{
		//double e1 = Tree.timeAtMRCA(actual, taxa[0], taxa[i]);
		//double e2 = Tree.timeAtMRCA(empirical, taxa[0], taxa[i]);
		//error += e1-e2;
		//}
	//return error;
	//}

//ASSUMES THAT THE AGES OF NODES HAVE BEEN SET WITH EXTANT TAXA EQUAL TO 0
//Calculates error of internal nodes only
//public static double calculateReconstructedAgeErrorSS(Tree actual, Tree empirical)
	//throws Exception
	//{
	//if(actual==null || empirical==null) throw new Exception("One or more trees are null.");
	//String[] taxa = Tree.getTaxonLabels(actual);
	//double error = 0.0;
	//for(int i = 1 ; i<taxa.length; i++)
		//{
		//double e1 = Tree.timeAtMRCA(actual, taxa[0], taxa[i]);
		//double e2 = Tree.timeAtMRCA(empirical, taxa[0], taxa[i]);
		//error += (e1-e2)*(e1-e2);
		//}
	//return error;
	//}

//ASSUMES THAT THE AGES OF NODES HAVE BEEN SET WITH EXTANT TAXA EQUAL TO 0
//ASSUMES THAT FOSSILS HAVE BEEN GENERATED ON TREE
//ASSUMES THAT THERE IS AT LEAST ONE FOSSIL
//ASSUMES THAT NODE CONSTRAINTS HAVE BEEN SET (Tree.setTrueNodeConstraints())
//ASSUMES THAT ALL CONSTRAINTS HAVE AGES GREATER THAN 0
public static double calculateConstraintAgeErrorSS(Tree t)
	{
	boolean descending = false;
	double error = 0.0;
	while(t.ancestor!=null) t=t.ancestor;
	if(t.constraintAge!=0.0) error+=(t.nodeAge-t.constraintAge)*(t.nodeAge-t.constraintAge);
	if(t.firstDaughter!=null) 
		{
		t=t.firstDaughter;
		if(t.constraintAge!=0.0) error+=(t.nodeAge-t.constraintAge)*(t.nodeAge-t.constraintAge);
		}
	else
		{
		if(t.constraintAge!=0.0) return (error+(t.nodeAge-t.constraintAge)*(t.nodeAge-t.constraintAge));
		return error;
		}
	while(t.ancestor!=null)
		{
		if(t.firstDaughter!=null && !descending) 
			{
			t=t.firstDaughter;
			if(t.constraintAge!=0.0) error+=(t.nodeAge-t.constraintAge)*(t.nodeAge-t.constraintAge);
			}
		else if(t.nextSister!=null)
			{
			t=t.nextSister;
			if(t.constraintAge!=0.0) error+=(t.nodeAge-t.constraintAge)*(t.nodeAge-t.constraintAge);
			descending=false;
			}
		else 
			{
			descending = true;
			t=t.ancestor;
			}
		}
	return error;
	}

//ASSUMES THAT THE AGES OF NODES HAVE BEEN SET WITH EXTANT TAXA EQUAL TO 0
//ASSUMES THAT FOSSILS HAVE BEEN GENERATED ON TREE
//ASSUMES THAT THERE IS AT LEAST ONE FOSSIL
//ASSUMES THAT NODE CONSTRAINTS HAVE BEEN SET (Tree.setTrueNodeConstraints())
//ASSUMES THAT ALL CONSTRAINTS HAVE AGES GREATER THAN 0
public static double calculateConstraintAgeError(Tree t)
	{
	boolean descending = false;
	double error = 0.0;
	int tot = 0;
	while(t.ancestor!=null) t=t.ancestor;
	if(t.constraintAge!=0.0) 
		{
		error+=(t.nodeAge-t.constraintAge);
		tot++;
//System.out.println("Calculating constraint error at node " + t.value);
//System.out.println("\tNode age: " + t.nodeAge);
//System.out.println("\tConstraint age: " + t.constraintAge);
//System.out.println("\tDifference: " + (t.nodeAge - t.constraintAge));
//System.out.println("\tCumulative total: " + error);
		}
	if(t.firstDaughter!=null) 
		{
		t=t.firstDaughter;
        if(t.constraintAge!=0.0)
                {
                error+=(t.nodeAge-t.constraintAge);
		tot++;
//System.out.println("Calculating constraint error at node " + t.value);
//System.out.println("\tNode age: " + t.nodeAge);
//System.out.println("\tConstraint age: " + t.constraintAge);
//System.out.println("\tDifference: " + (t.nodeAge - t.constraintAge));
//System.out.println("\tCumulative total: " + error);
                }

		}
	else
		{
		if(t.constraintAge!=0.0) return ((error+t.nodeAge-t.constraintAge)/tot+1);
		return error;
		}
	while(t.ancestor!=null)
		{
		if(t.firstDaughter!=null && !descending) 
			{
			t=t.firstDaughter;
        		if(t.constraintAge!=0.0)
                		{
                		error+=(t.nodeAge-t.constraintAge);
				tot++;
//System.out.println("Calculating constraint error at node " + t.value);
//System.out.println("\tNode age: " + t.nodeAge);
//System.out.println("\tConstraint age: " + t.constraintAge);
//System.out.println("\tDifference: " + (t.nodeAge - t.constraintAge));
//System.out.println("\tCumulative total: " + error);
                		}
			}
		else if(t.nextSister!=null)
			{
			t=t.nextSister;
        		if(t.constraintAge!=0.0)
                		{
                		error+=(t.nodeAge-t.constraintAge);
				tot++;
//System.out.println("Calculating constraint error at node " + t.value);
//System.out.println("\tNode age: " + t.nodeAge);
//System.out.println("\tConstraint age: " + t.constraintAge);
//System.out.println("\tDifference: " + (t.nodeAge - t.constraintAge));
//System.out.println("\tCumulative total: " + error);
                		}

			descending=false;
			}
		else 
			{
			descending = true;
			t=t.ancestor;
			}
		}
	if(tot>0) error /= tot;
	return error;
	}

public static boolean hasExtantTaxa(Tree t)
	{
	if(t==null) return false;
	if(t.firstDaughter==null) 
		{
		if(!t.extinct) return true; 
		}
	else
		{
		boolean tf = false;
		boolean ts = false;
		//if(Tree.hasExtantTaxa(t.firstDaughter)) tf = true; //return true;
		if(Tree.hasExtantTaxa(t.firstDaughter)) return true;
		t = t.firstDaughter.nextSister;
		while(t!=null)
			{
			if(Tree.hasExtantTaxa(t)) 
				{
				//ts = true;
				//break;
				return true;
				}
			t = t.nextSister;
			}
		if(tf && ts) return true;
		}
	return false;
	}

public static Tree makeBirthDeathTree(int totalSpecies, double birth, double death, Random r)
	{
	Tree t = new Tree();
	int totTermTax = 1;
	int totNodes = 1;
	int totTaxa = 1;
	double nextEvent=0.0;
	Vector termTax = new Vector();		//vector to keep track of the terminal taxa

	//make the node for the root
	t.value = "1";
	t.time = 0;
	termTax.add(t.value);
	t.extinct=false;

	//while there are fewer than or equal to totalSpecies
	while(totTermTax<totalSpecies)
		{
		//check if speciation or extinction
		double tse = r.nextDouble();
		boolean spec = true;
		if(tse > birth / (birth + death)) spec = false;

		//get the time of the next event
		if(spec) nextEvent = Utilities.nextExponential(r, (double)totTermTax*birth);
		else nextEvent = Utilities.nextExponential(r, (double)totTermTax*death);
	
		//add time to all current taxa before speciation
		t = addTerminalBranchTime(t,nextEvent,true);
	
		//pick which current species speciates or goes extinct
		int pick = (int)(r.nextDouble()*(double)totTermTax);
		
		//add ancestor/descendant structure to chosen species
		t = findTaxon(t,(String)termTax.elementAt(pick),true);
		if(!spec) 
			{
			t.extinct=true;
			termTax.remove(t.value);
			totTermTax-=1;
			if(totTermTax <=0) 
				{
				System.out.println(totTaxa + "\t0\t" + birth + "\t" + death);
				return null;
				}
			}
			
		else
			{
			t.extinct = true;
			t.firstDaughter = new Tree();
			t.firstDaughter.ancestor = t;
			t.firstDaughter.time = 0.0;
			t.firstDaughter.value=Integer.toString(totNodes+1);
			t.firstDaughter.extinct = false;
		
			t.firstDaughter.nextSister = new Tree();
			t.firstDaughter.nextSister.ancestor = t;
			t.firstDaughter.nextSister.time = 0.0;
			t.firstDaughter.nextSister.value=Integer.toString(totNodes+2);
			t.firstDaughter.nextSister.extinct = false;
		
			totTermTax+=1;
			totTaxa+=1;
			totNodes += 2;
			termTax.remove(t.value);
			termTax.add(t.firstDaughter.value);
			termTax.add(t.firstDaughter.nextSister.value);
			}
		}

	//finally, add time until next speciation to all current taxa
	double tse = r.nextDouble();
	boolean spec = true;
	if(tse > birth / (birth + death)) spec = false;
	if(spec) nextEvent = Utilities.nextExponential(r, (double)totTermTax*birth);
	else nextEvent = Utilities.nextExponential(r, (double)totTermTax*death);
	t = addTerminalBranchTime(t,nextEvent,true);
	
	//finally, add time until next speciation to all current taxa
	//nextEvent = Utilities.nextExponential(r, (double)totTermTax*rate);
	//t = addTerminalBranchTime(t,nextEvent,true);

	//return tree;
	System.out.println(totTaxa + "\t" + totalSpecies + "\t" + birth + "\t" + death);
	return(t);
	}
	
	
public static Tree makeYuleTree(int totalSpecies, double rate, Random r)
	{
	Tree t = new Tree();
	int totTermTax = 1;
	int totNodes = 1;
	double nextEvent=0.0;
	Vector termTax = new Vector();		//vector to keep track of the terminal taxa

	//make the node for the root
	t.value = "1";
	t.time = 0;
	termTax.add(t.value);
	t.extinct=false;

	//while there are fewerthan or equal to totalSpecies
	while(totTermTax<totalSpecies)
		{
		//get the time of the next speciation event
		nextEvent = Utilities.nextExponential(r, (double)totTermTax*rate);
	
		//add time to all current taxa before speciation
		t = addTerminalBranchTime(t,nextEvent,false);
	
		//pick which current species speciates
		int pick = (int)(r.nextDouble()*(double)totTermTax);
		
		//add ancestor/descendant structure to chosen species
		t = findTaxon(t,(String)termTax.elementAt(pick),true);
		t.extinct=true;
		t.firstDaughter = new Tree();
		t.firstDaughter.ancestor = t;
		t.firstDaughter.time = 0.0;
		t.firstDaughter.value=Integer.toString(totNodes+1);
		t.firstDaughter.extinct=true;
	
		t.firstDaughter.nextSister = new Tree();
		t.firstDaughter.nextSister.ancestor = t;
		t.firstDaughter.nextSister.time = 0.0;
		t.firstDaughter.nextSister.value=Integer.toString(totNodes+2);
		t.firstDaughter.nextSister.extinct=true;
	
		totTermTax+=1;
		totNodes += 2;
		termTax.remove(t.value);
		termTax.add(t.firstDaughter.value);
		termTax.add(t.firstDaughter.nextSister.value);
		}
	
	//finally, add time until next speciation to all current taxa
	//nextEvent = Utilities.nextExponential(r, (double)totTermTax*rate, false);
	nextEvent = Utilities.nextExponential(r, (double)totTermTax*rate);
	t = addTerminalBranchTime(t,nextEvent,false);

	//return tree;
	return(t);
	}

//assumes ultrametric tree
public double treeTime(Tree t, boolean root)
	{
	Tree tree;
	double time = 0.0;
	if(root) tree = findRoot(t);
	else tree = t;
	time = t.time;
	while(t.firstDaughter!=null) 
		{
		t=t.firstDaughter;
		time+=t.time;
		}
	return time;
	}

public int maxTreeDepth(Tree t)
	{
	int ds = 0, dd=0;
	if(t.nextSister!=null)
		ds+=maxTreeDepth(t.nextSister);
	if(t.firstDaughter!=null)
		{
		dd++;
		dd+=maxTreeDepth(t.firstDaughter);
		}
	if(ds>dd)
		return ds;
	else 
		return dd;
	}

public static int matchTaxon(String label, String[] list)
	{
	if(label==null || list==null)
		return -1;
	for(int i=0;i<list.length;i++)
		if(label.equals(list[i])) return i;
	return -1;
	}

public static Tree findRoot(Tree t)
	{
	while(t.ancestor!=null)
		t=t.ancestor;
	return t;
	}

public static void describeNode(Tree t)
	{
	if(t==null) return;
	System.out.println("Node label: " + t.value);
	System.out.println("\tContinuous state: " + t.state);
	System.out.println("\tContinuous state1: " + t.cont1);
	System.out.println("\tContinuous state2 " + t.cont2);
	System.out.println("\tBranch length: " + t.length);
	System.out.println("\tBranch time: " + t.time);
	System.out.println("\tDiscrete state: " + t.discreteState);
	System.out.println("\tDiscrete state1: " + t.disc1);
	System.out.println("\tDiscrete state2: " + t.disc2);
	System.out.println("\tDescendant leaves: " + t.numLeaves);
	if(t.nextSister!=null) System.out.println("\tSister node label: " + t.nextSister.value);
	if(t.firstDaughter!=null) System.out.println("\tFirst daughter node label: " + t.firstDaughter.value);
	if(t.ancestor!=null) System.out.println("\tAncestor node label: " + t.ancestor.value);
	return;
	}

/*
public static void describeNode(Tree t)
	{
	if(t==null) return;
	System.out.print("Node label: " + t.value);
	//System.out.println("\tContinuous state: " + t.state);
	//System.out.println("\tBranch length: " + t.length);
	//System.out.println("\tBranch time: " + t.time);
	System.out.print("\t" + t.length);
	System.out.print("\t" + t.time);
	System.out.print("\n");
	//System.out.println("\tDiscrete state: " + t.discreteState);
	//System.out.println("\tDescendant leaves: " + t.numLeaves);
	//if(t.nextSister!=null) System.out.println("\tSister node label: " + t.nextSister.value);
	//if(t.firstDaughter!=null) System.out.println("\tFirst daughter node label: " + t.firstDaughter.value);
	//if(t.ancestor!=null) System.out.println("\tAncestor node label: " + t.ancestor.value);
	return;
	}
*/
	

public static Tree cleanTree(Tree t)
	{
	boolean descending = false;
	while(t.ancestor!=null)
		t=t.ancestor;
	if(t.firstDaughter!=null)	
		{
		t.discreteState=null;
		t.statesSet = false;
		t.state=-9999.000;
		t.cont1=-9999.000;
		t.cont2=-9999.000;
		t.disc1=null;
		t.disc2=null;

		t=t.firstDaughter;
		}
	else return null;
	while(t.ancestor!=null)
		{
		if(t.firstDaughter!=null && !descending)
			{
			t.discreteState=null;
			t.statesSet = false;
			t.state=-9999.000;
			t.cont1=-9999.000;
			t.cont2=-9999.000;
			t.disc1=null;
			t.disc2=null;
			t=t.firstDaughter;
			t.numLeaves=0;
			}
		else if(t.nextSister!=null)
			{
			t.discreteState=null;
			t.statesSet = false;
			t.state=-9999.000;
			t.cont1=-9999.000;
			t.cont2=-9999.000;
			t.disc1=null;
			t.disc2=null;
			t=t.nextSister;
			descending=false;
			}
		else 
			{
			descending = true;
			t.discreteState=null;
			t.statesSet = false;
			t.state=-9999.000;
			t.cont1=-9999.000;
			t.cont2=-9999.000;
			t.disc1=null;
			t.disc2=null;
			t=t.ancestor;
			}
		}
	t.discreteState=null;
	t.statesSet = false;
	t.state=-9999.000;
	t.cont1=-9999.000;
	t.cont2=-9999.000;
	t.disc1=null;
	t.disc2=null;
	if(t.ancestor==null)
		return t;
	else
		return null;
	}

public static Tree traverseTree(Tree t)
	{
	int cntr=0; 
	boolean descending = false;
	System.out.println("");
	while(t.ancestor!=null)
		t=t.ancestor;
	t.numLeaves=0;
//while(t!=null)
	//{
	//descending = false;
	if(t.firstDaughter!=null)	
		{
		////System.out.println("Root\n\tcont1: " + t.cont1 + "\n\tcont2: " + t.cont2);
		//System.out.println("\t\tLength: " + t.length);
		t=t.firstDaughter;
		System.out.println(t.value + " is the first daughter of " + t.ancestor.value);
		t.numLeaves=0;
		t.ancestor.numLeaves=0;
		}
	else
		{
		System.out.println("Tree is null");
		return null;
		}
	while(t.ancestor!=null)
		{
		if(t.firstDaughter!=null && !descending)
			{
                	System.out.println(t.value + " is the mother of " + t.firstDaughter.value);
			//System.out.println("\tcont1: " + t.cont1 + "\n\tcont2: " + t.cont2);
			//System.out.println("\t\tLength: " + t.length);
			t=t.firstDaughter;
			t.numLeaves=0;
			}
		else if(t.nextSister!=null)
			{
                	System.out.println(t.value + " is a sister of " + t.nextSister.value);
			//System.out.println("\tcont1: " + t.cont1 + "\n\tcont2: " + t.cont2);
			//System.out.println("\t\tLength: " + t.length);
			if(t.firstDaughter==null)
				cntr++; //node is terminal
			t=t.nextSister;
                	System.out.println(t.value + " is a sister of " + t.ancestor.firstDaughter.value);
			//System.out.println("\tcont1: " + t.cont1 + "\n\tcont2: " + t.cont2);
			//System.out.println("\t\tLength: " + t.length);
			t.numLeaves=0;
			if(t.firstDaughter!=null)
				{
				if(t.ancestor!=null)
					t.ancestor.numLeaves+=cntr;
				cntr=0;
				}
			descending=false;
			}
		else 
			{
			if(t.firstDaughter==null)
				cntr++; //node is terminal
			descending = true;
			t=t.ancestor;
			t.numLeaves+=cntr;
			if(t.ancestor!=null)
				t.ancestor.numLeaves += t.numLeaves;
			cntr=0;
			}
		}
	//}
	if(t.ancestor==null)
		return t;
	else
		return null;
	}

/*    ***recursive traversal 
	Point p = new Point(0,0);
        int i=0, d=0;
        if(t.ancestor==null)
                System.out.println(t.value + " is the root node.");
        else if(t.nextSister!=null)
                {
                System.out.println(t.value + " is a sister of " + t.nextSister.value);
                        i=traverseTree(t.nextSister);
                        t.ancestor.numLeaves = i;
                }
        if(t.firstDaughter!=null)
                {
                System.out.println(t.value + " is the mother of " + t.firstDaughter.value);
                d = traverseTree(t.firstDaughter);
		t.numLeaves += d;
		if(t.ancestor!=null)
			t.ancestor.numLeaves+=d;
                }
        else
                i++;

        return i+d;
	}

*/

public static String[] getExtantTaxonLabels(Tree t)
	{
	if(t == null)
		{
		System.out.println("The tree is null in getExtantTaxonLabels");
		System.exit(0);
		}
	boolean descending = false;
	while(t.ancestor!=null)
		t=t.ancestor;
	Vector taxa = new Vector();
	if(t.firstDaughter!=null)	{ t=t.firstDaughter; }
	else { return null; }
	while(t.ancestor!=null)
		{
		if(t.firstDaughter!=null && !descending) { t=t.firstDaughter; }
		else if(t.nextSister!=null)
			{
			if(t.firstDaughter==null && !t.extinct)
				{
				taxa.add(t.value);
				}
			t=t.nextSister;
			descending=false;
			}
		else 
			{
			if(t.firstDaughter==null && !t.extinct)
				{
				taxa.add(t.value);
				}
			descending = true;
			t=t.ancestor;
			}
		}
	String[] out = new String[taxa.size()];
        taxa.copyInto(out);
	return out;
	}
public static String[] getTaxonLabels(Tree t)
	{
	if(t == null)
		{
		System.out.println("The tree is null in getTaxonLabels");
		System.exit(0);
		}
	boolean descending = false;
	while(t.ancestor!=null)
		t=t.ancestor;
	Vector taxa = new Vector();
	if(t.firstDaughter!=null)	{ t=t.firstDaughter; }
	else { return null; }
	while(t.ancestor!=null)
		{
		if(t.firstDaughter!=null && !descending) { t=t.firstDaughter; }
		else if(t.nextSister!=null)
			{
			if(t.firstDaughter==null)
				{
				taxa.add(t.value);
				}
			t=t.nextSister;
			descending=false;
			}
		else 
			{
			if(t.firstDaughter==null)
				{
				taxa.add(t.value);
				}
			descending = true;
			t=t.ancestor;
			}
		}
	String[] out = new String[taxa.size()];
        taxa.copyInto(out);
	return out;
	}

public static String[] treesStringsFromAltnexusFile(File fileName, int numTrees)
	{
	String[] trees = new String[numTrees];
	try{
	BufferedReader d = new BufferedReader(new FileReader(fileName));
	String line;
	String[] maxTaxa = new String[1000];
	boolean inTrees = false;
	boolean inTaxa = false;
	boolean readingTrees = false;
	String tree;
	int cnt1=0;
	while((line=d.readLine())!=null)
		{
		if(inTrees && !line.toLowerCase().matches(".end;*")) 
			{
			line = line.replaceAll("\\[.*\\]","");
			String[] dta = line.split("\\s*=\\s*");
			tree = dta[dta.length-1];
			//System.out.println("tree is " + tree);
			//trees[cnt1] = new Tree(tree);
			trees[cnt1] = tree;
			cnt1++;
			if(cnt1==numTrees) return trees;
			//return new Tree(tree);
			}
		if(line.toLowerCase().matches(".*begin trees;.*")) inTrees = true;
		else if(inTrees && line.toLowerCase().matches(".end;*")) return trees;
		}
	}
	catch(Exception e){e.printStackTrace();}
	return null;
	}

public static Tree[] treesFromAltnexusFile(File fileName, int numTrees)
	{
	Tree[] trees = new Tree[numTrees];
	try{
	BufferedReader d = new BufferedReader(new FileReader(fileName));
	String line;
	String[] maxTaxa = new String[1000];
	boolean inTrees = false;
	boolean inTaxa = false;
	boolean readingTrees = false;
	String tree;
	int cnt1=0;
	while((line=d.readLine())!=null)
		{
		if(inTrees && !line.toLowerCase().matches(".end;*")) 
			{
			line = line.replaceAll("\\[.*\\]","");
			String[] dta = line.split("\\s*=\\s*");
			tree = dta[dta.length-1];
			//System.out.println("tree is " + tree);
			trees[cnt1] = new Tree(tree);
			cnt1++;
			if(cnt1==numTrees) return trees;
			//return new Tree(tree);
			}
		if(line.toLowerCase().matches(".*begin trees;.*")) inTrees = true;
		else if(inTrees && line.toLowerCase().matches(".end;*")) return trees;
		}
	}
	catch(Exception e){e.printStackTrace();}
	return null;
	}

//reads the trees from the nexus file
public static Tree[] treesFromNexusFile(File fileName, int numTrees)
	{
	Tree[] trees = new Tree[numTrees];
	try{
	BufferedReader d = new BufferedReader(new FileReader(fileName));
	String line;
	String[] maxTaxa = new String[1000];
	boolean inTrees = false;
	boolean inTaxa = false;
	boolean readingTrees = false;
	String tree;
	int cnt=0, cnt1=0;
	while((line=d.readLine())!=null)
		{
		if(inTrees && inTaxa && !readingTrees)
			{
			if(line.toLowerCase().matches(".*;.*")) readingTrees = true;
			line = line.replaceAll(",","");
			line = line.replaceAll(";","");
			line = line.replaceAll("^\\s+", "");
			String[] data = line.split("\\s");
			maxTaxa[Integer.parseInt(data[0])-1] = data[1];
			cnt++;
			}
		else if(readingTrees)
			{
			line = line.replaceAll("\\[.*\\]","");
			String[] dta = line.split("\\s*=\\s*");
			tree = dta[dta.length-1];
			for(int i=0;i<cnt;i++)
				{
				tree = tree.replaceAll("\\(" + (i+1) + ":", "\\(" + maxTaxa[i] + ":");
				tree = tree.replaceAll("\\(" + (i+1) + ",", "\\(" + maxTaxa[i] + ",");
				tree = tree.replaceAll("," + (i+1) + ":", "," + maxTaxa[i] + ":");
				tree = tree.replaceAll("," + (i+1) + ",", "," + maxTaxa[i] + ",");
				}
			System.out.println("tree is " + tree);
			trees[cnt1] = new Tree(tree);
			cnt=0;
			cnt1++;
			if(cnt1==numTrees) return trees;
			//return new Tree(tree);
			}
		if(line.toLowerCase().matches(".*begin trees;.*")) inTrees = true;
		else if(line.toLowerCase().matches(".*translate.*")) inTaxa = true;
		else if(inTrees && line.toLowerCase().matches(".end;*")) return trees;
		}
	}
	catch(Exception e){e.printStackTrace();}
	return null;
	}

//reads the first tree from file
public static Tree treeFromNexusFile(File fileName)
	{
	try{
	BufferedReader d = new BufferedReader(new FileReader(fileName));
	String line;
	String[] maxTaxa = new String[1000];
	boolean inTrees = false;
	boolean inTaxa = false;
	boolean readingTrees = false;
	String tree;
	int cnt=0;
	while((line=d.readLine())!=null)
		{
		if(inTrees && inTaxa && !readingTrees)
			{
			if(line.toLowerCase().matches(".*;.*")) readingTrees = true;
			line = line.replaceAll(",","");
			line = line.replaceAll(";","");
			line = line.replaceAll("^\\s+", "");
			String[] data = line.split("\\s");
			maxTaxa[Integer.parseInt(data[0])-1] = data[1];
			cnt++;
			}
		else if(readingTrees)
			{
			line = line.replaceAll("\\[.*\\]","");
			String[] dta = line.split("\\s*=\\s*");
			tree = dta[dta.length-1];
			for(int i=0;i<cnt;i++)
				{
				tree = tree.replaceAll("\\(" + (i+1) + ":", "\\(" + maxTaxa[i] + ":");
				tree = tree.replaceAll("\\(" + (i+1) + ",", "\\(" + maxTaxa[i] + ",");
				tree = tree.replaceAll("," + (i+1) + ":", "," + maxTaxa[i] + ":");
				tree = tree.replaceAll("," + (i+1) + ",", "," + maxTaxa[i] + ",");
				}
			//System.out.println("tree is " + tree);
			return new Tree(tree);
			}
		if(line.toLowerCase().matches(".*begin trees;.*")) inTrees = true;
		else if(line.toLowerCase().matches(".*translate.*")) inTaxa = true;
		}
	}
	catch(Exception e){e.printStackTrace();}
	return null;
	}

public Tree treeFromFile(File fileName)
	{	
	try{
	char c;
	FileInputStream fis = new FileInputStream(fileName);
	DataInputStream in = new DataInputStream(fis);
	Tree t = new Tree();
	String sin = new String( in.readLine());
	fis.close();
	in.close();
	t = treeFromString(sin,false);
	return t;
	}
	catch(Exception e){e.printStackTrace();}
	return null;
	}

private static boolean writePagelNode(Tree node, String char1, String char2, FileWriter fw, boolean time)
	{
	try
		{
		if(node==null) return false;
		if(node.ancestor==null) return false;
		else if(char1==null || char2==null) 
			{
			if(!time) fw.write(node.value+","+node.ancestor.value+","+node.length + "\n");
			else fw.write(node.value+","+node.ancestor.value+","+node.time + "\n");
			}
		else 
			{
			if(!time) fw.write(node.value+","+node.ancestor.value +","+ node.length + "," + char1 + "," + char2 + "\n");
			else fw.write(node.value+","+node.ancestor.value +","+ node.time + "," + char1 + "\n");
			}
//System.out.print(node.value+","+node.ancestor.value +","+ node.length + "," + char1 + "," + char2 + "\n");
		return true;
		}
	catch(Exception e) {e.printStackTrace(); }
	return false;
	}

public static boolean treeToPagel(Tree t, String file, boolean inclAnc, boolean time)
	{
	try
		{
		String temp = null;
		FileWriter fw = new FileWriter(file);
		boolean descending = false;
		while(t.ancestor!=null)
			t=t.ancestor;
		if(t.firstDaughter!=null)	
			{
			t=t.firstDaughter;
			if(t.firstDaughter!=null) writePagelNode(t, temp, temp, fw,time);
			else writePagelNode(t, t.discreteState, t.discreteState, fw, time);
			//System.out.println(t.numSis(t));
			}
		else
			return false;
		while(t.ancestor!=null)
			{
			if(t.firstDaughter!=null && !descending)
				{
				t=t.firstDaughter;
				if(t.firstDaughter!=null) writePagelNode(t, temp, temp, fw, time);
				else writePagelNode(t, t.discreteState, t.discreteState, fw, time);
			//System.out.println(t.numSis(t) + " " + t.value);
				}
			else if(t.nextSister!=null)
				{
				t=t.nextSister;
				if(t.firstDaughter!=null) writePagelNode(t, temp, temp, fw, time);
				else writePagelNode(t, t.discreteState, t.discreteState, fw, time);
			//System.out.println(t.numSis(t) + " " + t.value);
				descending=false;
				}
			else 
				{
				descending = true;
				t=t.ancestor;
				}
			}
		fw.close();
		if(t.ancestor==null)
			return true;
		else
			return false;
		}
	catch(Exception e){e.printStackTrace(); }
	return false;
	}

public static boolean writeR8S(Tree t, String file, int nsites, double smoothing, int maxConstraintDepth, double maxConstraint, boolean rootError, double speciationRate, Random r, double len)
	{
	try {
	FileWriter fw = new FileWriter(file);
	fw.write("#NEXUS\n\n");
	fw.write("begin trees;\n");
	fw.write("tree scaledT="+treeToStringTimeOrder(t,false)+"\n");
	fw.write("end;\n\n\n");
	fw.write("begin r8s;\n\n");
	//fw.write("blformat nsites="+nsites+" lengths=persite round=no;\n\n");
	fw.write("blformat nsites="+nsites+" lengths=persite;\n\n");
	fw.write(Tree.writeR8SMRCA(t));
	fw.write("collapse;\n\n");
	fw.write(Tree.writeR8SConstraints(t, maxConstraintDepth, maxConstraint, rootError, speciationRate, r, len) + "\n\n");
	fw.write("set verbose=0;\n");
	fw.write("set smoothing="+smoothing+";\n");
	fw.write("divtime method=pl algorithm=tn;\n");
	fw.write("showage shownamed=yes;\n");
	fw.write("end;\n");
	fw.close();
	}
	catch(Exception e) {e.printStackTrace(); return false;}
	return true;
	}

//ASSUMES NODE AGES HAVE ALREADY BEEN SET
//ASSUMES DEPTH OF MAXIMAL CONSTRAINT HAS BEEN SET
public static double findRootAge(Tree t, int maxConstraintDepth, double maxConstraint, boolean rootError, double speciationRate, Random r)
	//throws Exception
	{
	t = Tree.findRoot(t);
//System.out.println("The root age from findRootAge is " + t.nodeAge + " the maxConstraintDepth is " + maxConstraintDepth + " and the max constraint is " + maxConstraint);
	if(!rootError) return t.nodeAge;	
	if(t.constraintAge>0.0) return t.constraintAge;
	else 
		{
		if(r.nextDouble()>0.5) return(maxConstraint - Utilities.nextGamma(r, speciationRate, maxConstraintDepth));
		else return(maxConstraint - Utilities.nextGamma(r, speciationRate, maxConstraintDepth+1));
		}
	//throw new Exception("Error finding root age.");
	}

//ASSUMES THE ROOT CONSTRAINT HAS ALREADY BEEN SET!!!
public static String writeR8SConstraints(Tree t, int maxConstraintDepth, double maxConstraint, boolean rootError, double speciationRate, Random r, double len)
	{
	//double len = Tree.maxTreeTime(t,true, true);
//System.out.println("The length from writeR8SConstraints is " + len);
	boolean descending = false;
	
	while(t.ancestor!=null) t=t.ancestor;
	if(t.firstDaughter==null) return null;
	if(t.constraintAge==0.0) 
		{
		System.out.println("The root age must be set prior to writing r8s constraints.");
		System.exit(0);
		}
	//String out = "fixage taxon=a"+t.value + " age=" + (len - Tree.findRootAge(t,maxConstraintDepth, maxConstraint, rootError,speciationRate,r)) + ";\n";
	String out = "fixage taxon=a"+t.value + " age=" + (len - t.constraintAge) + ";\n";


	if(t.firstDaughter!=null) 
		{
		t=t.firstDaughter;
		if(t.constraintAge>0.0) out += "constraint taxon=a"+t.value + " minage=" + (len - t.constraintAge) + ";\n";
		}
	else return out;			
	while(t.ancestor!=null)
		{
		if(t.firstDaughter!=null && !descending) 
			{
			t=t.firstDaughter;
			if(t.constraintAge>0.0) out += "constraint taxon=a"+t.value + " minage=" + (len - t.constraintAge) + ";\n";
			}
		else if(t.nextSister!=null)
			{
			t=t.nextSister;
			if(t.constraintAge>0.0) out += "constraint taxon=a"+t.value + " minage=" + (len - t.constraintAge) + ";\n";
			descending=false;
			}
		else 
			{
			descending = true;
			t=t.ancestor;
			}
		}
	return out;	
	}



public static String findTerminal(Tree t)
	{
	if(t==null) 
		{
		System.out.println("Tree is null from findTerminal.");
		return null;
		}
	while(t.firstDaughter!=null) t = t.firstDaughter;
	if(!t.extinct) return t.value;
	else return null;
	}

public static String writeR8SMRCA(Tree t)
	{
	boolean descending = false;
	while(t.ancestor!=null) t=t.ancestor;
	if(t.firstDaughter==null) return null;
	String out = "MRCA a"+t.value + " a" + Tree.findTerminal(t.firstDaughter) + " a" + Tree.findTerminal(t.firstDaughter.nextSister) + ";\n";
	if(t.firstDaughter!=null) 
		{
		t=t.firstDaughter;
		if(t.firstDaughter!=null) out += "MRCA a"+t.value + " a" + Tree.findTerminal(t.firstDaughter) + " a" + Tree.findTerminal(t.firstDaughter.nextSister) + ";\n";
		}
	else return out;			
	while(t.ancestor!=null)
		{
		if(t.firstDaughter!=null && !descending) 
			{
			t=t.firstDaughter;
			if(t.firstDaughter!=null) out += "MRCA a"+t.value + " a" + Tree.findTerminal(t.firstDaughter) + " a" + Tree.findTerminal(t.firstDaughter.nextSister) + ";\n";
			}
		else if(t.nextSister!=null)
			{
			t=t.nextSister;
			if(t.firstDaughter!=null) out += "MRCA a"+t.value + " a" + Tree.findTerminal(t.firstDaughter) + " a" + Tree.findTerminal(t.firstDaughter.nextSister) + ";\n";
			descending=false;
			}
		else 
			{
			descending = true;
			t=t.ancestor;
			}
		}
	return out;	
	}

//CANNOT HANDLE INTERNAL NODE LABELS!!!!!!!!
public static Tree treeFromString(String sin, boolean time)
	{
//System.out.println("Entering treeFromString method\n" + sin);
	Tree t = new Tree();	
	t.value = new String("root");
	int in = 0;
	int cntr=0;
	char c;
	for(int i = 0; i<sin.length(); i++)
		{
		c = sin.charAt(i);
		//System.out.println("Here I am and I'm a " + c);
		if(c=='(')
			{
			//cntr=0;
			Tree n = new Tree();
			String label = new String();
			String len = new String();
			while(sin.charAt(i+1)!='(' && sin.charAt(i+1)!=':' && sin.charAt(i+1)!=',' && sin.charAt(i+1)!=')')
				{
				label += sin.charAt(i+1);
				i++;
				}
			if(label.length()>0) 
				{
				n.value = label;
				cntr++;
				}
			if(sin.charAt(i+1)==':')
			while(sin.charAt(i+1)!=',' && sin.charAt(i+1)!=')')
				{
				if(sin.charAt(i+1)!=':') { len += sin.charAt(i+1); }
				i++;
				}
			if(len.length()!=0) 
			{ if(time) n.time = Double.parseDouble(len); else n.length = Double.parseDouble(len); }
			n.ancestor = t;
			t.firstDaughter = n;
			t=t.firstDaughter;
			}
		else if(c==',')
			{
			Tree s = new Tree();
			s.ancestor = t.ancestor;
			String label = new String();
			while(sin.charAt(i+1)!='(' && sin.charAt(i+1)!=':' && sin.charAt(i+1)!=',' && sin.charAt(i+1)!=')')
				{
				label += sin.charAt(i+1);
				i++;
				}
			if(label.length()>0) 
				{
				s.value = label;
				cntr++;
				}
			String len = new String();
			if(sin.charAt(i+1)==':')
			while(sin.charAt(i+1)!=',' && sin.charAt(i+1)!=')')
				{
				if(sin.charAt(i+1)!=':') { len += sin.charAt(i+1); }
				i++;
				}
			if(len.length()!=0) {
			if(time) s.time = Double.parseDouble(len); else s.length = Double.parseDouble(len); }
			//in++;
			t.nextSister = s;
			t = t.nextSister;
         //if(sin.charAt(i+1)=='(')
         	//{
         	//if(t.ancestor!=null)
         	//t.ancestor.numLeaves+=cntr;
         	//cntr=0;
         	//}
			}
		else if(c==')' && t.ancestor != null)
			{
			t=t.ancestor;
			t.numLeaves+=cntr;
			if(t.ancestor!=null && t.ancestor.value!=null)
				{
				t.ancestor.numLeaves += t.numLeaves;
				//cntr=0;
				}
			if(i+1<sin.length())
			if(sin.charAt(i+1)!=':')
				{
				t.value = "s" + Integer.toString(in);
				in++;
				}
			}
		else if(c==':')
			{
			String len = new String();
			while(sin.charAt(i+1)!=',' && sin.charAt(i+1)!=')' && sin.charAt(i+1)!='(' && sin.charAt(i+1)!=';')
				{
				len+=sin.charAt(i+1);
				i++;
				}
			if(len.length()!=0) 
			{ if(time) t.time = Double.parseDouble(len); else t.length = Double.parseDouble(len); }
			t.value = "t" + Integer.toString(in);
			in++;
			}
		else
			{
//System.out.println("Entering else. Should NEVER reach here unless the character is a ;. It is a " + c);
			if(c==';' || c==' ')
				break;
			else
				{
				cntr++;
				String v = new String();
				for(int k=i; k<sin.length(); k++)
					{
					c=sin.charAt(k);
					if(c==')' || c=='(' || c==',' || c==';' || c== ' ')
						{
						//System.out.println("Reached the else at " + c + " letter " + i);
						//i=k-1;
						i=k;
						break;
						}
					else
						v += c;
					}
				if(v.length()>0)
					t.value = v;
				}
			}
		}
	
	return t;
	}

public static void appendTreeToNexusWConstraints(Tree t, String treeLab, String file)
	{
	try {
	FileWriter fw = new FileWriter(file, true);
	fw.write("\nbegin trees;\n");
	fw.write("tree " + treeLab + "="+treeToStringWConstraints(t)+"\n");
	fw.write("end;\n");
	fw.close();
	}
	catch(Exception e) {e.printStackTrace(); }
	}

public static void appendTreeToNexus(Tree t, String treeLab, String file, boolean writeCon)
	{
	try {
	FileWriter fw = new FileWriter(file, true);
	fw.write("\nbegin trees;\n");
	fw.write("tree " + treeLab + "=" + treeToStringTimeOrder(t,writeCon)+"\n");
	fw.write("end;\n");
	fw.close();
	}
	catch(Exception e) {e.printStackTrace(); }
	}

public static void treesToNexus(int nTree, int ntax, String file)
	{
	try {
	FileWriter fw = new FileWriter(file,true);
	Random r = new Random();
	fw.write("begin trees;\n");
	for(int i=0; i<nTree; i++) 
		{
		fw.write("tree random" + i + "="+treeToStringTimeOrder(makeYuleTree(ntax,1,r),false)+"\n");
		if(i%10==0) System.out.println("Generating tree " + i );
		}
	fw.write("end;\n");
	fw.close();
	}
	catch(Exception e) {e.printStackTrace(); }
	}


public static void treesToNexus(int nTree, int ntax, int seqLen, String file)
	{
	try {
	FileWriter fw = new FileWriter(file);
	Random r = new Random();
	fw.write("#NEXUS\nbegin data;\ndimensions ntax="+ntax+" nchar="+seqLen+";\n");
	fw.write("format datatype=dna missing=? gap=-;\nmatrix\n");
	for(int i=1; i<=ntax; i++)
		fw.write("a" + i + "   " + Utilities.makeRandSeq(seqLen, r) + "\n");
	fw.write(";\nend;\n\n");
	fw.write("begin trees;\n");
	for(int i=0; i<nTree; i++) 
		{
		fw.write("tree random" + i + "="+treeToStringTimeOrder(makeYuleTree(ntax,1,r),false)+"\n");
		if(i%10==0) System.out.println("Generating tree " + i );
		}
	fw.write("end;\n");
	fw.close();
	}
	catch(Exception e) {e.printStackTrace(); }
	}

public static void treeToNexus(Tree t, int ntax, int seqLen, String file, boolean writeCon)
	{
//if(writeCon) System.out.println("Writing constraints");
	try {
	String[] taxa = Tree.getTaxonLabels(t);
	String nex = treeToStringTimeOrder(t,writeCon);
	FileWriter fw = new FileWriter(file);
	Random r = new Random();
	fw.write("#NEXUS\nbegin data;\ndimensions ntax="+ntax+" nchar="+seqLen+";\n");
	fw.write("format datatype=dna missing=? gap=-;\nmatrix\n");
	for(int i=0; i<taxa.length; i++)
		fw.write("a" + taxa[i] + "   " + Utilities.makeRandSeq(seqLen, r) + "\n");
	fw.write(";\nend;\n\n");
	fw.write("begin trees;\ntree random1="+nex+"\nend;");
	
	fw.close();
	}
	catch(Exception e) {e.printStackTrace(); }
	}

public static String treeToStringWConstraints(Tree T)
	{
	Tree t = new Tree();
	int tot=1;
	t = T;
	int ns = 0;
	String s = new String();
	boolean up=true;

	if(t.firstDaughter==null)
		return"";
	else
		{
		t = t.firstDaughter;
		if( t.nextSister !=null)
			s += "(";
//System.out.println("The tree is " + s);
		while(t.ancestor!=null)
			{
			if(t.firstDaughter!=null && up)
				{
				if(t.firstDaughter.nextSister!=null)
					s += "(";
				t=t.firstDaughter;
				}
			else
				{
				if(t.firstDaughter==null) { s += "a" + tot + ":" + t.time; tot++; }
				if(t.nextSister!=null)
					{
					up=true;
					t=t.nextSister;
					s += ",";
					}
				else if(t.nextSister == null)
					{
					if(t.ancestor!=null)
					if(t.ancestor.firstDaughter != t)
						{
						if(t.constraintAge>0) 
							{
							s += ")f" + t.constraintAge + ":" + t.ancestor.time ;
							}
						else s += "):" + t.ancestor.time;
						}
					if(t.ancestor!=null)
						t=t.ancestor;
					up=false;
					}
				}
			}
		}
	return s + ";";
	}

public static String treeToStringTimeOrder(Tree t,boolean writeCon)
	{
	//Tree t = new Tree();
	double len = Tree.maxTreeTime(t,true,false);
	
	t = Tree.findRoot(t);
	int tot=1;
	//t = T;
	int ns = 0;
	String s = new String();
	boolean up=true;
	//String fos = "";
	String fos = "";
//if(t.constraintAge>0) System.out.println("!!!0 Node " + t.value + " with constraint " + t.constraintAge);

	if(t.firstDaughter==null)
		return"";
	else
		{
//System.out.println("At node " + t.value + " and d number of fossils is " + t.numFossils);
//if (t.numFossils>0) System.out.println("a" + t.value);
//if(t.numFossils>0) fos += "a" + t.value;
		t = t.firstDaughter;
//if(t.constraintAge>0) System.out.println("!!!A Node " + t.value + " with constraint " + t.constraintAge);
//System.out.println("At node " + t.value + " and blasted the number of fossils is " + t.numFossils);
//if (t.numFossils>0) System.out.println("b" + t.value);
//if(t.numFossils>0) fos += "b" + t.value;
		if( t.nextSister !=null)
			s += "(";
//System.out.println("The tree is " + s);
		while(t.ancestor!=null)
			{
			if(t.firstDaughter!=null && up)
				{
				if(t.firstDaughter.nextSister!=null)
					s += "(";
				t=t.firstDaughter;
//if(t.constraintAge>0) System.out.println("!!!B Node " + t.value + " with constraint " + t.constraintAge);
//if (t.numFossils>0) System.out.println("c" + t.value);
//if(t.numFossils>0) fos += "c" + t.value;
//System.out.println("At node " + t.value + " and the number of fossils is " + t.numFossils);
				}
			else
				{
				//if(t.firstDaughter==null) { s += "a" + tot + ":" + t.time; tot++; }
				if(t.firstDaughter==null) 
					{ 	
					s += "a" + t.value + ":" + t.time; 
					tot++; 
					}
				if(t.nextSister!=null)
					{
					up=true;
					t=t.nextSister;
//if(t.constraintAge>0) System.out.println("!!!C Node " + t.value + " with constraint " + t.constraintAge);
//if (t.numFossils>0) System.out.println("d" + t.value);
//if(t.numFossils>0) fos += "d" + t.value;
					s += ",";
					}
				else if(t.nextSister == null)
					{
					if(t.ancestor!=null)
					if(t.ancestor.firstDaughter != t)
						{
						//if(t.ancestor.firstDaughter.numFossils>0)
							//fos += t.ancestor.firstDaughter.value + "FD";
						//if(t.ancestor.firstDaughter.nextSister.numFossils>0)
							//fos += t.ancestor.firstDaughter.nextSister.value + "NS";
						//if(t.ancestor.numFossils>0)
							//fos += t.ancestor.value + "A";
					
						if(t.ancestor.constraintAge>0)
							{
							//if(writeCon) s += ")C" + (((double)Math.round((len-t.ancestor.constraintAge)*100))/100.00) + ":" + t.ancestor.time;
							if(writeCon) s += ")a" + t.ancestor.value + "C" + (((double)Math.round((t.ancestor.constraintAge)*100))/100.00) + ":" + t.ancestor.time;
							//else s += ")a" + t.ancestor.value + ":" + t.ancestor.time;
							else s += "):" + t.ancestor.time;
//System.out.println("Writing tree label for constraint: " + t.ancestor.value);
							}
						else s += "):" + t.ancestor.time;
						//s += ")" + t.ancestor.value + ":" + t.ancestor.time;
						//s += "):" + t.ancestor.time;
						//s += "):" + t.ancestor.time;
						fos = "";
						}
					if(t.ancestor!=null)
						t=t.ancestor;
					up=false;
					}
				}
			}
		}
	return s + ";";
	}

public static String treeToStringTime(Tree T)
	{
	Tree t = new Tree();
	t = T;
	int ns = 0;
	String s = new String();
	boolean up=true;

	if(t.firstDaughter==null)
		return"";
	else
		{
		t = t.firstDaughter;
		if( t.nextSister !=null)
			s += "(";
//System.out.println("The tree is " + s);
		while(t.ancestor!=null)
			{
			if(t.firstDaughter!=null && up)
				{
				if(t.firstDaughter.nextSister!=null)
					s += "(";
				t=t.firstDaughter;
				}
			else
				{
				if(t.firstDaughter==null) { s += t.value + ":" + t.time; }
				if(t.nextSister!=null)
					{
					up=true;
					t=t.nextSister;
					s += ",";
					}
				else if(t.nextSister == null)
					{
					if(t.ancestor!=null)
					if(t.ancestor.firstDaughter != t)
						{
						s += "):" + t.ancestor.time;
						}
					if(t.ancestor!=null)
						t=t.ancestor;
					up=false;
					}
				}
			}
		}
	return s;
	}

public static String treeToStringLength(Tree T)
	{
	Tree t = new Tree();
	t = T;
	int ns = 0;
	String s = new String();
	boolean up=true;

	if(t.firstDaughter==null)
		return"";
	else
		{
		t = t.firstDaughter;
		if( t.nextSister !=null)
			s += "(";
//System.out.println("The tree is " + s);
		while(t.ancestor!=null)
			{
			if(t.firstDaughter!=null && up)
				{
				if(t.firstDaughter.nextSister!=null)
					s += "(";
				t=t.firstDaughter;
				}
			else
				{
				if(t.firstDaughter==null) { s += t.value + ":" + t.time; }
				if(t.nextSister!=null)
					{
					up=true;
					t=t.nextSister;
					s += ",";
					}
				else if(t.nextSister == null)
					{
					if(t.ancestor!=null)
					if(t.ancestor.firstDaughter != t)
						{
						s += "):" + t.ancestor.time;
						}
					if(t.ancestor!=null)
						t=t.ancestor;
					up=false;
					}
				}
			}
		}
	return s;
	}
}
