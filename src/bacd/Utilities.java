/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bacd;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author David
 */
public class Utilities {

     public static void writeFile(String file, String text)
                {
                try{
                FileWriter fw = new FileWriter(file);
                fw.write(text);
                fw.close();
                }
                catch(Exception e) {e.printStackTrace();}
                }

    
public static double[] VectorToDoubleArray(Vector v)
	{
	if(v==null) return null;
	if(v.size()==0) return null;
	Double[] d = new Double[v.size()];
	double[] out = new double[d.length];
	v.copyInto(d);
	for(int i=0; i<d.length; i++) out[i] = d[i].doubleValue();
	return out;
	}

public static double[] StringArrayToDoubleArray(String[] stringArray) throws Exception
	{
	//System.out.println("The string array has length " + stringArray.length);
	double[] doubleArray = new double[stringArray.length];
	for(int i=0; i<stringArray.length; i++)
		{
//System.out.println(i + " " + stringArray[i]);
		doubleArray[i] = Double.parseDouble(stringArray[i]);
		}
	return doubleArray;
	}

public static int[] StringArrayToIntArray(String[] stringArray) throws Exception
	{
	int[] intArray = new int[stringArray.length];
	for(int i=0; i<stringArray.length; i++)
		intArray[i] = Integer.parseInt(stringArray[i]);
	return intArray;
	}

public static int[] StringBooleanArrayToIntArray(String[] stringArray) throws Exception
	{
	int[] intArray = new int[stringArray.length];
	for(int i=0; i<stringArray.length; i++)
		{
		if(stringArray[i].toLowerCase().equals("false")) intArray[i] = 0;
		else intArray[i] = 1;
		}
	return intArray;
	}


public static double dichCharFreq(double[] vals)
	{
	double tot = 0.0;
	for(int i=0;i<vals.length;i++) if(vals[i]!=0.0) tot++;
	return tot/(double)vals.length;
	}

public static double findMin(double[] vals)
	{
	double t=vals[0];
	for(int i=0; i<vals.length; i++) if(vals[i]<t) t=vals[i];
	return t;
	}

public static double findMax(double[] vals)
	{
	double t=vals[0];
	for(int i=0; i<vals.length; i++) if(vals[i]>t) t=vals[i];
	return t;
	}

public static Vector vIntersect(Vector v1, Vector v2)
	{
	if(v1==null || v2==null) return null;
	int size1 = v1.size();
	int size2 = v2.size();
	Vector out = new Vector();

	for(int i=0; i<size1; i++)
		if(v2.contains(v1.elementAt(i)) && !out.contains(v1.elementAt(i)) ) out.add(v1.elementAt(i));

	if(out.size()<=0) return null;
	else return out;
	}

public static Vector vUnion(Vector v1, Vector v2)
	{
	Vector out = new Vector();
	
	if(v1!=null)
		{
		int size1 = v1.size();
		for(int i=0;i<size1;i++) if(!out.contains(v1.elementAt(i))) out.add(v1.elementAt(i));
		}
	if(v2!=null)
		{
		int size2 = v2.size();
		for(int i=0;i<size2;i++) if(!out.contains(v2.elementAt(i))) out.add(v2.elementAt(i));
		}
	if(out.size()<=0) return null;
	else return out;
	}

        public static int sampleState(double[][] P, Random r)
                throws Exception
                {
                double totc = 0.0;
                double choice = r.nextDouble();
		int cur=0;
                for(int i=0;i<P[0].length;i++)
                        if(choice>totc && choice<=totc+P[cur][i]) return(i);
                        else { totc+=P[cur][i]; }
                throw new Exception("State not found");
                }


public static int sampleState(double[] P, Random r)
	throws Exception
        {
        double pos = r.nextDouble();
        double cur = 0;
        for(int i=0; i<P.length;i++)
                {
                if(pos > cur && pos<= cur + P[i]) return i;
                cur += P[i];
                }
                throw new Exception("State not found");
        }

public static double min(double x, double y)
	{
	if(x>y) return y;
	else return x;
	}

public static double max(double x, double y)
	{
	if(x>y) return x;
	else return y;
	}


//enter the mean and variance of the lognormal distribution
public static double nextLogNormal(Random r, double mean, double var)
	{
	//mean = exp(m+s/2)
	//var = exp(s + 2m)(exp(s/2)-1)
	//s = var for Gaussian
	//m = mean for Gaussian
	return Math.exp(r.nextGaussian()*Math.sqrt(Math.log(var/(mean*mean)+1)) + Math.log(mean/Math.sqrt(var/(mean*mean)+1)));
	//return Math.log(r.nextGaussian()*var + mean);
	}

public static double nextExponential(Random r, double lambda)
        {
        return (-1*Math.log(r.nextDouble())/lambda);
        }

public static int nextPoisson(Random r, double lambda, double totTime)
	{
	//estimates the poisson distribution as the number of events that happen in totTime 
	//for which the wait time between events is exponentially distributed with rate lambda.
	int cnt = 0;
	double time = 0.0;
	while(time<=totTime)
		{
		time += nextExponential(r,lambda);
		cnt++;
		}
	return(cnt-1);
	}

public static int nextBinomial(Random r, double p, int totE)
	{
	//estimates the binomial distribution as the sum of totE Bernouli random variables with p probability of success
	int totS = 0;
	for(int i=0;i<totE;i++) if(r.nextDouble()<=p) totS++;
	return totS;
	}

public static double nextGamma(Random r, double lambda, double numEvents)
	{
	//estimates the gamma distribution as a sum of numEvents exponential random variables of rate lambda.
	double time = 0.0;
	for(int i=0; i<numEvents; i++) time += nextExponential(r,lambda);
	return(time);
	}

public static String makeRandSeq(int seqLen, Random r)
        {
        String[] lets = {"A", "T", "G", "C"};
        String out = new String();
        for(int i=0; i<seqLen; i++) out += lets[(int)(r.nextDouble()*(double)4)];
        return out;
        }

        public static void cosort(double[] characters, double[] characters1, int l, int r)
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
					temp = characters1[l1-1];
                                        characters1[l1-1]=characters1[r1-1];
                                        characters1[r1-1]=temp;
                                        }
                                }
                                                                                                                             
                        temp = characters[l-1];
                        characters[l-1]=characters[r1-1];
                        characters[r1-1]=temp;
                        temp = characters1[l-1];
                        characters1[l-1]=characters1[r1-1];
                        characters1[r1-1]=temp;
                                                                                                                             
                        cosort(characters, characters1, l, r1-1);
                        cosort(characters, characters1, r1+1, r);
                        }
                }

        public static double[] quicksort(double[] characters, int l, int r)
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
                                                                                                                             
                        characters = quicksort(characters, l, r1-1);
                        characters = quicksort(characters, r1+1, r);
                        }
                return characters;
                }

        public static int round(double d)
                {
                if((int)d - d >= .5)
                        return (int)d+1;
                return (int)d;
                }

    static String runSystemCommand(String command) {

                String output = new String();
        String s = new String();
        try {
            Process p = Runtime.getRuntime().exec(command);

            BufferedReader stdInput = new BufferedReader(new
                 InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                 InputStreamReader(p.getErrorStream()));

            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                output+= s;
            }

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                output+= s;
            }

        }
        catch (IOException e) {
                System.out.println("exception happened - here's what I know: ");
                e.printStackTrace();
                System.exit(-1);
                }
        return output;

    }


			
}
