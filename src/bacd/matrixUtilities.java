/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bacd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Vector;

/**
 *
 * @author David
 */
public class matrixUtilities {

//display a matrix to the standard output
	public static void write(double[][] m)
		{
		for(int i=0; i<m.length; i++)
			{
			System.out.print("[ ");
			for(int j=0;j<m[0].length; j++) { System.out.print(m[i][j] + " " ); }
			System.out.print("]\n");
			}
		System.out.println();
		}

//write matrix to a file with a header line
	public static void write(String file, Vector m, String header)
		{
		try{
                FileWriter fw = new FileWriter(file);
		fw.write(header + "\n");
		for(int i=0; i<m.size(); i++)
			{
			fw.write((String)m.elementAt(i) + "\n");
			}
		fw.close();
		}
		catch(Exception e) {e.printStackTrace();}
		}
	public static void write(String file, double[][] m, String header)
		{
		try{
                FileWriter fw = new FileWriter(file);
		fw.write(header + "\n");
		for(int i=0; i<m.length; i++)
			{
			for(int j=0; j<m[0].length; j++) fw.write("\t" + m[i][j]);
			fw.write("\n");
			}
		fw.close();
		}
		catch(Exception e) {e.printStackTrace();}
		}

//write a matrix to a file
	public static void write(String file, double[][] m)
		{
		try{
                FileWriter fw = new FileWriter(file);
		for(int i=0; i<m.length; i++)
			{
			for(int j=0; j<m[0].length; j++) fw.write("\t" + m[i][j]);
			fw.write("\n");
			}
		fw.close();
		}
		catch(Exception e) {e.printStackTrace();}
		}

//read a matrix from a file
	public static double[][] read(String file, boolean header)
		{
		double out[][] = null;
		try {
		BufferedReader d = new BufferedReader(new FileReader(file));
        	String line;
		int rows=0;
		int cols=0;
		if(header) line = d.readLine();
        	while((line=d.readLine())!=null)
			{
			if(cols<=0)
				{
				String[] cn = line.split("\\s+");
				cols = cn.length;
				}
			rows++;
			}
		d.close();
		
		d = new BufferedReader(new FileReader(file));
		out = new double[rows][cols];
		rows=0;
		if(header) line = d.readLine();
        	while((line=d.readLine())!=null)
			{
			String[] t = line.split("\\s+");
			for(int i=0; i<cols; i++) out[rows][i] = Double.parseDouble(t[i]);
			rows++;
			}
		d.close();
		}
		catch(Exception e) {e.printStackTrace();}
		
		return(out);
		}

//make a copy of a matrix
	public static double[][] copy(double[][] m)
		{
		double[][] out = new double[m.length][m[0].length];
		for(int i=0;i<m.length;i++) for(int j=0; j<m[0].length;j++) out[i][j]=m[i][j];
		return out;
		}

//multiply two matrices
	public static double[][] multiply(double[][] m1, double[][] m2)
		{
		if(m1[0].length != m2.length) 
			{ System.out.println("Matrix dimentions do not match for multiplication."); System.exit(1); }

		double[][] out = new double[m1.length][m2[0].length];
		for(int i=0; i<m1.length; i++) for(int j=0; j<m2[0].length; j++) out[i][j]=0.0;

//System.out.println("m1");
		//write(m1);
//System.out.println("m2");
		//write(m2);

		int m1r = m1.length;
		int m2c = m2[0].length;
		int cr = m2.length;
		for(int i=0; i<m1r; i++) 
			for(int j=0; j<m2c; j++) 
				for(int k=0; k<cr; k++)
					out[i][j] = out[i][j] + m1[i][k]*m2[k][j];
		return out;
		}

//transpose a matrix
	public static double[][] transpose(double[][] m1)
		{
		double[][] out = new double[m1[0].length][m1.length];
		for(int i=0; i<m1.length; i++) for(int j=0; j<m1[0].length; j++) out[i][j] = m1[j][i];
		return out;
		}

//add two matrices
	public static double[][] add(double[][] m1, double[][] m2)
		{
		double[][] m3 = new double[m1.length][m1[0].length];
		if(m1.length!=m2.length || m1[0].length!=m2[0].length) { System.out.println("Matrix addition requires equal dimensional matrices."); System.exit(0); }
		for(int i=0; i<m2.length; i++) for(int j=0; j<m2[0].length; j++) m3[i][j] = m1[i][j]+m2[i][j];
		return m3;
		}

//multiply a matrix by a scalar
	public static double[][] scalarMultiply(double s, double[][] m1)
		{
		double[][] m3 = new double[m1.length][m1[0].length];
		for(int i=0; i<m1.length;i++) for(int k=0; k<m1[0].length; k++) m3[i][k] = m1[i][k]*s;
		return m3;
		}

//invert a matrix numerically
	public static double[][] invert(double input[][])
		{
		double[][] a=copy(input);
		int n = a.length;
		if(n!=a[0].length) { System.out.println("Method invert() requires a square matrix"); System.exit(1); }
		int irow=0, icol=0;
		int[] ipiv = new int[n];
		int[] indxc = new int[n];
		int[] indxr = new int[n];
		double pivinv=0;
		for(int i=0; i<n; i++) ipiv[i]=indxc[i]=indxr[i]=0;
		for(int i=0; i<n; i++)
			{
			double big = 0.0;
			for(int j=0; j<n; j++) if(ipiv[j]!=1) for(int k=0;k<n;k++)
				{
				if(ipiv[k]==0)
					{
					if(Math.abs(a[j][k])>=big)
						{
						big = Math.abs(a[j][k]);
						irow = j;
						icol = k;
						}
					}
				else if(ipiv[k]>1) { System.out.println("Matrix is singular-1."); System.exit(1); }
				}
			++(ipiv[icol]);
			if(irow!=icol) for(int l=0;l<n;l++) {double temp=a[irow][l];a[irow][l]=a[icol][l];a[icol][l]=temp;}
			indxr[i]=irow;
			indxc[i]=icol;
			if(a[icol][icol]==0.0) {System.out.println("Matrix is singular-2."); System.exit(1); }
			pivinv = 1.0/a[icol][icol];
			a[icol][icol]=1.0;
			for(int l=0;l<n;l++) a[icol][l] *= pivinv;
			for(int ll=0;ll<n;ll++) if(ll != icol)
				{
				double dum = a[ll][icol];
				a[ll][icol]=0.0;
				for(int l=0;l<n;l++) a[ll][l] -= a[icol][l]*dum;
				}
			}
		for(int l=n-1; l>=0; l--) if(indxr[l] != indxc[l]) for(int k=0; k<n; k++)
				{ double temp=a[k][indxr[l]]; a[k][indxr[l]]=a[k][indxc[l]]; a[k][indxc[l]]=temp; }
		return a;
		}

	public static double[][] exp(double[][] m, double t, int k)
		{
		//estimate the exponential of the matrix. n=2^k.
		//e^(xt) = lim(n->infinity)(1-xt/n)^-n from Ross, Introduction to Probability Models, 6th ed, p. 340
		double[][] exm = copy(m);	
		exm = invert(add(scalarMultiply(-1*t/Math.pow(2,k),exm),makeI(m.length)));
		for(int i=0; i<k; i++) exm = multiply(exm,exm);
		return exm;
		}

	public static double[][] exp1(double[][] m, double t, int k)
		{
		//estimate the exponential of the matrix. n=2^k.
		//e^(xt) = lim(n->infinity)(1+xt/n)^n from Ross, Introduction to Probability Models, 6th ed, p. 340
		double[][] exm = copy(m);	
		exm = add(scalarMultiply(t/Math.pow(2,k),exm),makeI(m.length));
		for(int i=0; i<k; i++) exm = multiply(exm,exm);
		return exm;
		}

//calculate the stationary distribution associated with rate matrix Q.
	public static double[] pi(double[][] Q)
		{
		//pi*Q=0
		//pi*P=pi
		double[][] q = copy(Q);
		q = exp(q, 1000, 30);
		double[] pi = new double[q.length];
		for(int i=0; i<q[0].length; i++)
			pi[i]=q[0][i];
		return(pi);
		}

	//public static double[][] pi(double[][] Q)
		//{
		////pi*Q=0
		////pi*P=pi
		//double[][] q = copy(Q);
		//return(scalarMultiply(Math.E,invert(add(q,makeE(q.length)))));
		//}

	public static double[][] makeI(int sqDim)
		{
		double[][] I = new double[sqDim][sqDim];
		for(int i=0; i<sqDim; i++)
			for(int j=0; j<sqDim; j++)
				if(i!=j) I[i][j]=0;
				else I[i][j]=1;
		return I;
		}

	public static double[][] makeE(int sqDim)
		{
		double[][] I = new double[sqDim][sqDim];
		for(int i=0; i<sqDim; i++) for(int j=0; j<sqDim; j++) I[i][j]=1.0;
		return I;
		}
}
