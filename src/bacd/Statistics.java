/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bacd;

import java.util.Random;

/**
 *
 * @author David
 */
public class Statistics {

public static double factln(double n)
{
  double x = n + 1;
  double tmp = x + 5.5;
  tmp -= (x + .5) * Math.log(tmp);
  double ser = 1.000000000190015 + 76.18009172947146/(++x) - 86.50532032941677/(++x) + 24.01409824083091/ (++x) -  1.231739572450155/(++x) + 0.12086509738661e-2/(++x) - 0.5395239384953e-5/(++x);
  return Math.log(2.5066282746310005*ser/(x-6)) - tmp;
}

public static int sampleFromBinomial(int N, double p, Random rng)
{
    double cum = rng.nextDouble();
    double totNext = Statistics.binom(0,N,p);
    double tot = 0.0;
    for(int i=0; i<=N; i++)
    {
        totNext = tot+Statistics.binom(i, N, p);
        if(tot<cum && cum<=totNext) return i;
        tot = totNext;
    }
    return N;
}

public static double binom(double suc, double fail, double pSuc)
	{
	double n = suc+fail;

	if(n<20)
		{
		double num = factorial(n);
		double denom = factorial(suc)*factorial(fail);
//System.out.println("Numerator " + num + " Denominator: " + denom);
		return (num/denom)*Math.pow(pSuc,suc)*Math.pow(1.00-pSuc,fail);
		}

	double logn = factln(n);
	double logd = factln(fail) + factln(suc);
	return (Math.exp(logn-logd)*Math.pow(pSuc,suc)*Math.pow(1-pSuc,fail));
	}



public static double binomialSignTestP(double suc, double n, double pSuc)
	{
	double tot = 0.0;
	for(int i=0; i<=n-suc; i++) 
		{
//System.out.println("Successes: " + (suc+i) + " Failures: " + (n-(suc+i)));
		tot+= binom(suc+i,n-(suc+i),pSuc);
		}
	return tot;
	}

public static double factorial(double n)
	{
//System.out.println(n);
	if(n==1) return 1.0;
	else if(n==0) return 1.0;
	else return n*factorial(n-1);
	}

public static double hypergeom(double n, double m, double N, double i)
{
/*
   # There are m "bad" and n "good" balls in an urn.
   # Pick N of them. The probability of i or more successful selections:
   # (m!n!N!(m+n-N)!)/(i!(n-i)!(m+i-N)!(N-i)!(m+n)!)
*/
   double loghyp1;
   double loghyp2;
   loghyp1 = factln(m)+factln(n)+factln(N)+factln(m+n-N);
   loghyp2 = factln(i)+factln(n-i)+factln(m+i-N)+factln(N-i)+factln(m+n);
   return Math.exp(loghyp1 - loghyp2);
}

public static double mean(int[] data)
        {
        return Statistics.sum(data)/(double)data.length;
        }

public static double mean(double[] data)
        {
        return Statistics.sum(data)/(double)data.length;
        }

public static double deviation(double[] data, double mean, int i)
        {
        return (data[i]-mean)*(data[i]-mean);
        }

public static double sampleCovariance(double[] y, double meanY, double[] x, double meanX) throws Exception
	{
	double tot = 0.0;
	int n = y.length;
	if(y.length!=x.length) throw new Exception("Unequal data sizes");
	for(int i=0; i<n; i++) tot+=(y[i]-meanY)*(x[i]-meanX);
	return tot/(n-1);
	//return tot/(n);

	}

public static double sampleCorrelationCoefficient(double[] y, double meanY, double[] x, double meanX) throws Exception
	{
	double sx = sampleStandardDeviation(x,meanX);
	double sy = sampleStandardDeviation(y,meanY);
	double sxy = sampleCovariance(y,meanY,x,meanX);
	double r = sxy / (sx*sy);
	return r;
	}

//t-statistic for correlation coefficient for testing with t-test
public static double correlationTValue(double r, double N)
	{
	return r*Math.sqrt((N-2.0)/(1.0-r*r));
	}

public static double sampleStandardDeviation(double[] data, double mean)
        {
        double tot=0.0;
        int n = data.length;
        for(int i=0; i<n; i++) tot+=Statistics.deviation(data, mean, i);
        tot/=(n-1);
        return Math.sqrt(tot);
	}

public static double standardError(double[] data, double mean)
	{
	return sampleStandardDeviation(data, mean)/Math.sqrt(data.length);
	}

public static double sum(int[] data)
        {
        double tot=0.0;
        for(int i=0; i<data.length; i++) tot+=data[i];
        return tot;
        }

public static double sum(double[] data)
        {
        double tot=0.0;
        for(int i=0; i<data.length; i++) tot+=data[i];
        return tot;
        }

public static double sign(double d)
	{
	if(d<0) return -1;
	else return 1;
	}

public static boolean wilcoxonTest(double[] x, int n, double alpha) throws Exception
	{
//from http://comp9.psych.cornell.edu/Darlington/wilcoxon/wilcox51.htm
	double[] sig = {-1,-1,-1,-1,-1,0,1,3,5,8,10,13,17,21,25,30,35,41,47,53,60,67,75,83,91,100,110,119,130,140,151};
	if(n>30 || n<0) throw new Exception("Used for n between 0 and 30 inclusive.");
	if(alpha!=0.05) throw new Exception("Only alpha = 0.05 is implemented currently.");
	double[] y = new double[x.length];
	for(int i=0; i<x.length; i++) 
		{
		y[i] = Math.abs(x[i]);
		//System.out.println(y[i] + " " + x[i]);
		}
	int n1 = x.length;
	Utilities.cosort(y,x,1,n1);
	//System.out.println("Cosorted:" );
	//for(int i=0; i<x.length; i++) 
		//System.out.println(y[i] + " " + x[i]);
	double minus = 0;
	double plus = 0;
	for(int i=0; i<x.length; i++) 
		{
		if(x[i]<0) minus+=i+1;
		else plus+=i+1;
		}
	double min = (minus < plus) ? minus : plus;
//System.out.println("Wilcox: " + min + " " + n);
	if(sig[n]>min) return true;
	return false;
	}

public static double oneSampleT(double sampMean, double nullMean, double sampStDev, double n)
	{
	return (sampMean-nullMean)/(sampStDev/Math.sqrt(n));
	}


public static double oneSampleT(double sampMean, double nullMean, double stdErrorMean)
	{
	return (sampMean-nullMean)/stdErrorMean;
	}

public static boolean t_testSignificance(double t_value, double df, double alpha, int tail)
        {
        if(alpha != 0.05)
                {
                System.out.println("t-test only accepting alpha=0.05 for now.");
                return false;
                }
//from: http://www.statsoft.com/textbook/sttable.html#t
        double[][] table = { {1.00, 0.324920 , 1.000000 , 3.077684 , 6.313752 , 12.70620 , 31.82052 , 63.65674 , 636.6192}, {2.00, 0.288675, 0.816497, 1.885618, 2.919986, 4.30265, 6.96456, 9.92484, 31.5991}, {3.00, 0.276671, 0.764892, 1.637744, 2.353363, 3.18245, 4.54070, 5.84091, 12.9240}, {4.00, 0.270722, 0.740697, 1.533206, 2.131847, 2.77645, 3.74695, 4.60409, 8.6103}, {5.00, 0.267181, 0.726687, 1.475884, 2.015048, 2.57058, 3.36493, 4.03214, 6.8688}, {6.00, 0.264835, 0.717558, 1.439756, 1.943180, 2.44691, 3.14267, 3.70743, 5.9588}, {7.00, 0.263167, 0.711142, 1.414924, 1.894579, 2.36462, 2.99795, 3.49948, 5.4079}, {8.00, 0.261921, 0.706387, 1.396815, 1.859548, 2.30600, 2.89646, 3.35539, 5.0413}, {9.00, 0.260955, 0.702722, 1.383029, 1.833113, 2.26216, 2.82144, 3.24984, 4.7809}, {10.00, 0.260185, 0.699812, 1.372184, 1.812461, 2.22814, 2.76377, 3.16927, 4.5869}, {11.00, 0.259556, 0.697445, 1.363430, 1.795885, 2.20099, 2.71808, 3.10581, 4.4370}, {12.00, 0.259033, 0.695483, 1.356217, 1.782288, 2.17881, 2.68100, 3.05454, 4.3178}, {13.00, 0.258591, 0.693829, 1.350171, 1.770933, 2.16037, 2.65031, 3.01228, 4.2208}, {14.00, 0.258213, 0.692417, 1.345030, 1.761310, 2.14479, 2.62449, 2.97684, 4.1405}, {15.00, 0.257885, 0.691197, 1.340606, 1.753050, 2.13145, 2.60248, 2.94671, 4.0728}, {16.00, 0.257599, 0.690132, 1.336757, 1.745884, 2.11991, 2.58349, 2.92078, 4.0150}, {17.00, 0.257347, 0.689195, 1.333379, 1.739607, 2.10982, 2.56693, 2.89823, 3.9651}, {18.00, 0.257123, 0.688364, 1.330391, 1.734064, 2.10092, 2.55238, 2.87844, 3.9216}, {19.00, 0.256923, 0.687621, 1.327728, 1.729133, 2.09302, 2.53948, 2.86093, 3.8834}, {20.00, 0.256743, 0.686954, 1.325341, 1.724718, 2.08596, 2.52798, 2.84534, 3.8495}, {21.00, 0.256580, 0.686352, 1.323188, 1.720743, 2.07961, 2.51765, 2.83136, 3.8193}, {22.00, 0.256432, 0.685805, 1.321237, 1.717144, 2.07387, 2.50832, 2.81876, 3.7921}, {23.00, 0.256297, 0.685306, 1.319460, 1.713872, 2.06866, 2.49987, 2.80734, 3.7676}, {24.00, 0.256173, 0.684850, 1.317836, 1.710882, 2.06390, 2.49216, 2.79694, 3.7454}, {25.00, 0.256060, 0.684430, 1.316345, 1.708141, 2.05954, 2.48511, 2.78744, 3.7251}, {26.00, 0.255955, 0.684043, 1.314972, 1.705618, 2.05553, 2.47863, 2.77871, 3.7066}, {27.00, 0.255858, 0.683685, 1.313703, 1.703288, 2.05183, 2.47266, 2.77068, 3.6896}, {28.00, 0.255768, 0.683353, 1.312527, 1.701131, 2.04841, 2.46714, 2.76326, 3.6739}, {29.00, 0.255684, 0.683044, 1.311434, 1.699127, 2.04523, 2.46202, 2.75639, 3.6594}, {30.00, 0.255605, 0.682756, 1.310415, 1.697261, 2.04227, 2.45726, 2.75000, 3.6460}, {Double.MAX_VALUE, 0.253347, 0.674490, 1.281552, 1.644854, 1.95996, 2.32635, 2.57583, 3.2905}} ;
        for(int i=0; i<table.length; i++)
                {
                if(i==0)
                        {
                        if(df == table[i][0])
                                {
                                if(tail*t_value>=table[i][4]) return true;
                                else return false;
                                }
                        }
                else
                        {
                        if(df>table[i-1][0] && df<=table[i][0])
                                {
                                if(tail*t_value>=table[i][4]) return true;
                                else return false;
                                }
                        }
                }
        return false;
        }


}
