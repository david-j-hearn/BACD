/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bacd;

/**
 *
 * @author David
 */
public class CEMethods {
	Tree tree;
	CEMethods(Tree t)
		{
		tree = t;
		}

	public static double[] sort(double[] characters, int l, int r)
		{
		int l1, r1;
		double tempf=0.0;
		double temp;
		if(l<r)
			{
			l1=l;
			r1=r;
			while (l1<r1)
				{
				while(l1 < r && characters[l1-1] <= characters[l-1])
					l1++;
				while(l < r1 && characters[r1-1] >= characters[l-1])
					r1--;
				if(l1 < r1)
					{
					temp = characters[l1-1];
					characters[l1-1]=characters[r1-1];
					characters[r1-1]=temp;
					}
				} 
			
			temp = characters[l-1];
			characters[l-1]=characters[r1-1];
			characters[r1-1]=temp;
			
			characters = sort(characters, l, r1-1);
			characters = sort(characters, r1+1, r);
			}
		return characters;
		}


	public static int round(double d)
		{
		if((int)d - d >= .5)
			return (int)d+1;	
		return (int)d;
		}

	public static double findThreshold(double[] characters, double frequency)
		{
		double[] temp = new double[characters.length];
		System.arraycopy(characters, 0, temp, 0, characters.length);
		temp = sort(temp, 1, temp.length);
//System.out.println("The sorted characters are:");
//for(int s=0;s<temp.length;s++) System.out.println(temp[s]);
		int cut = 0;
		if(round(temp.length - frequency*temp.length)==0)
			cut = temp.length-1;
		else	
			cut = round(temp.length - frequency*temp.length);
		return temp[cut];
		}

	public static double lengthToMRCAWithCharacter(Tree t1, String label, boolean[] targetChar)
		{
		//String[] labels = Tree.getTaxonLabels(t1,targetChar.length);
		String[] labels = Tree.getTaxonLabels(t1);
		double length = Double.MAX_VALUE;
			for(int j=0; j<targetChar.length; j++) if(targetChar[j])
				{
				double temp = lengthToMRCA(t1, label, labels[j]);
				if(temp<length)
					length=temp;
				if(length==0.0)
					return length;
				}
		return length;
		}


	public static double lengthToMRCA(Tree t, String taxonLabel1, String taxonLabel2)
		{
		Tree tree = new Tree(t);
		Tree copy = new Tree(t);
		Tree copy1 = Tree.findTaxon(copy, taxonLabel1, true);
		tree = Tree.findTaxon(tree, taxonLabel2, true);
		if(copy1.value.equals(tree.value))
			return 0.0;
		double length = 0.0;
		while(tree.ancestor!=null)
			{
			tree = tree.ancestor;
			copy = copy1;
			length = 0.0;
			while(copy.ancestor!=null)	
				{
				length+=copy.length;
				copy = copy.ancestor;
//System.out.println("The target node label is " + tree.value + " whereas the 2nd character node label is " + copy.value);
				if(copy.value.equals(tree.value))
					return length;
				}
			}
		return -1.0;
		}
}
