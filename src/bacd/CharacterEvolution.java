/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bacd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author David
 */
public class CharacterEvolution {

    double x, y;
    static int BROWNIAN = 1;
    static int UNIFORM = 2;
    static int BROWNIAN_CUTOFF = 3;
    static int UNIFORM_CUTOFF = 4;
    Random rng;
    double rate = 1.0;

    CharacterEvolution(double r) {
        rate = r;
        rng = new Random(System.currentTimeMillis());
    }

    CharacterEvolution() {
        rng = new Random(System.currentTimeMillis());
    }

    CharacterEvolution(Random r) {
        rng = r;
    }

    /*.................................................................................................................*/
    /** randomly evolve a character under Brownian motion and variance equal to branchlength * rate*/
    public static void summarizeComparativeTests(String[] files, int nTrees, String outfile, boolean append) {

//tree_num        pos_act pos_rand        n_act   n_rand  diff    pos     act_bin_pval    rand_bin_pval   act_pval        rand_pval       act_p   rand_p  both_p
//
//file	mean_pos_act	stdev_pos_act	mean_pos_rand	stdev_pos_rand	mean_n_act	mean_n_rand	mean_diff	percent_pos	percent_act_sig	percent_rand_sig
//public static int[] StringArrayToIntArray(String[] stringArray) throws Exception
//public static int[] StringBooleanArrayToIntArray(String[] stringArray) throws Exception
//public static double[] StringArrayToDoubleArray(String[] stringArray) throws Exception

        try {

            File f = new File(outfile);
            boolean exists = f.exists();
            FileWriter fw = new FileWriter(outfile, append);
//file	mean_pos_act	stdev_pos_act	mean_pos_rand	stdev_pos_rand	mean_n_act	mean_n_rand	mean_diff	percent_pos	percent_act_sig	percent_rand_sig
            if (!append || !exists) {
                fw.write("file\tmean_pos_act\tstdev_pos_act\tmean_pos_rand\tstdev_pos_rand\tmean_n_act\tmean_n_rand\tmean_diff\tpercent_pos\tpercent_act_sig\tpercent_rand_sig\tdiff_sig\tind_tree_sig\n");
            }
            for (int i = 0; i < files.length; i++) {
                System.out.println("Summarizing " + files[i]);
//System.out.println("1A");
                Hashtable hash = characterHashFromTabText(files[i], nTrees - 1);
//System.out.println("2A");
                if (hash != null) {
//System.out.println("A");
                    double[] n_act = Utilities.StringArrayToDoubleArray((String[]) hash.get("n_act"));
//System.out.println("B");
                    double[] n_rand = Utilities.StringArrayToDoubleArray((String[]) hash.get("n_rand"));
//System.out.println("C");
                    double[] diff = Utilities.StringArrayToDoubleArray((String[]) hash.get("diff"));
//System.out.println("D");
                    double[] pos = Utilities.StringArrayToDoubleArray((String[]) hash.get("pos"));
//System.out.println("E");
                    double[] pos_act = Utilities.StringArrayToDoubleArray((String[]) hash.get("pos_act"));
//System.out.println("F");
                    double mean_pos_act = Statistics.mean(pos_act);
//System.out.println("G");
                    double[] pos_rand = Utilities.StringArrayToDoubleArray((String[]) hash.get("pos_rand"));
//System.out.println("H");
                    double mean_pos_rand = Statistics.mean(pos_rand);
//System.out.println("I");
                    int[] act_sig = Utilities.StringBooleanArrayToIntArray((String[]) hash.get("act_pval"));
//System.out.println("J");
                    int[] rand_sig = Utilities.StringBooleanArrayToIntArray((String[]) hash.get("rand_pval"));
                    String diff_sig = "NO";
                    double percent_pos = Statistics.mean(pos);
                    if (percent_pos >= 0.95 || percent_pos <= 0.05) {
                        diff_sig = "YES";
                    }
                    String ind_tree_sig = "no";
                    double percent_act_sig = Statistics.mean(act_sig);
                    if (percent_act_sig >= 0.95) {
                        ind_tree_sig = "yes";
                    }

                    fw.write(files[i] + "\t" + mean_pos_act + "\t" + (Statistics.sampleStandardDeviation(pos_act, mean_pos_act)) + "\t" + mean_pos_rand + "\t" + (Statistics.sampleStandardDeviation(pos_rand, mean_pos_rand)) + "\t" + (Statistics.mean(n_act)) + "\t" + (Statistics.mean(n_rand)) + "\t" + (Statistics.mean(diff)) + "\t" + percent_pos + "\t" + percent_act_sig + "\t" + (Statistics.mean(rand_sig)) + "\t" + diff_sig + "\t" + ind_tree_sig + "\n");
                } else {
                    System.out.println("\tCould not summarize " + files[i]);
                }
            }
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ouch");
        }
    }

    public static void summarizeContrastData(String file, boolean exactNull, Random r) {

        try {
            BufferedReader d = new BufferedReader(new FileReader(new File(file)));
            FileWriter fw = new FileWriter(file + ".Summary");
            fw.write("tree_num\tpos_act\tpos_rand\tn_act\tn_rand\tdiff\tpos\tact_bin_pval\trand_bin_pval\tact_pval\trand_pval\tact_mean_pos\tact_t_pos\tact_stdev_pos\trand_mean_pos\trand_t_pos\trand_stdev_pos\tact_p\trand_p\tboth_p\n");



            String line;
            line = d.readLine();
            int treeNum = -1;
            double ppR = 0.0; //
            double ppA = 0.0; //
            double corA = 0.0;
            double corR = 0.0;
            double aveR = 0.0; //
            double aveA = 0.0; //
            double stdevR = 0.0;
            double stdevA = 0.0;
            double tR = 0.0;
            double tA = 0.0;
            double binPR = 0.0; //
            double binPA = 0.0; //
            boolean rPR = false; //
            boolean rPA = false; //
            boolean aSig = false; //
            boolean rSig = false; //
            Vector rD = new Vector();
            Vector rD1 = new Vector();
            Vector aD = new Vector();
            Vector aD1 = new Vector();
            int nR = 0; //
            int nA = 0; //
            int cnt = 0;
            while ((line = d.readLine()) != null) {
                String[] stuff = line.split("\\t");
                String t1 = stuff[3];
                String t2 = stuff[4];
                String ra = stuff[5];
                int tnum = Integer.parseInt(stuff[6]);
                double cN1 = Double.parseDouble(stuff[9]);
                double cN2 = Double.parseDouble(stuff[10]);
                //if (cnt == 0 && !exactNull && t1.equals("cont") && t2.equals("cont")) {
                //  fw.write("tree_num\tpos_act\tpos_rand\tn_act\tn_rand\tdiff\tpos\tact_corr\trand_corr\tact_sog\trand_sig\tact_mean_pos\tact_t_pos\tact_stdev_pos\trand_mean_pos\trand_t_pos\trand_stdev_pos\tact_p\trand_p\tboth_p\n");
                //} else if (cnt == 0 && !exactNull) {
                //  fw.write("tree_num\tpos_act\tpos_rand\tn_act\tn_rand\tdiff\tpos\tact_bin_pval\trand_bin_pval\tact_pval\trand_pval\tact_mean_pos\tact_t_pos\tact_stdev_pos\trand_mean_pos\trand_t_pos\trand_stdev_pos\tact_p\trand_p\tboth_p\n");
                //} else if (cnt == 0 && exactNull && t1.equals("cont") && t2.equals("cont")) {
                //  fw.write("tree_num\tpos_act\tpos_rand\tn_act\tn_rand\tdiff\tpos\tact_bin_pval\trand_bin_pval\tt_test_p_act\tt_test_p_rand\tact_corr\tact_t\tact_sig\tact_p\trand_p\tboth_p\n");
                //} else if (cnt == 0 && exactNull) {
                //  fw.write("tree_num\tpos_act\tpos_rand\tn_act\tn_rand\tdiff\tpos\tact_bin_pval\trand_bin_pval\tact_pval\trand_pval\tact_mean_pos\tact_t_pos\tact_stdev_pos\trand_mean_pos\trand_t_pos\trand_stdev_pos\tact_p\trand_p\tboth_p\n");
                //}
                //cnt++;
//System.out.println("t1: " + t1 + " t2: " + t2 + " ra: " + ra + " tnum: " + tnum + " cn1: " + cN1 + " cn2: " + cN2);
                if (tnum != treeNum) {
                    if (nR > 0 && nA > 0 && t1.equals("cont") && t2.equals("cont")) {
//System.out.println("tnum: " + tnum + " treeNum: " + treeNum + " nR: " + nR + " nA: " + nA + " ppA: " + ppA);

                        //both exact and simulated, cont-cont

                        double[] dataA1 = Utilities.VectorToDoubleArray(aD);
                        double[] dataA2 = Utilities.VectorToDoubleArray(aD1);
                        corA = Statistics.sampleCorrelationCoefficient(dataA1, Statistics.mean(dataA1), dataA2, Statistics.mean(dataA2));
                        tA = Statistics.correlationTValue(corA, nA);
                        aSig = Statistics.t_testSignificance(Math.abs(tA), nA - 2, 0.05, 1);
                        
                        if (!exactNull) { //simulated, cont-cont
                            double[] dataR1 = Utilities.VectorToDoubleArray(rD);
                            double[] dataR2 = Utilities.VectorToDoubleArray(rD1);
                            rPA = aSig;
                            corR = Statistics.sampleCorrelationCoefficient(dataR1, Statistics.mean(dataR1), dataR2, Statistics.mean(dataR2));
                            tR = Statistics.correlationTValue(corR, nR);
                            rPR = Statistics.t_testSignificance(Math.abs(tR), nR - 2, 0.05, 1);
                            ppR /= nR;
                            ppA /= nA;
//System.out.println("OK ppR: " + ppR);
                        } else { //exact, cont-cont
                            nR = nA;
                            ppR = Utilities.nextBinomial(r, 0.5, nA);
                            binPA = Statistics.binomialSignTestP(ppA, nA, 0.5);
                            binPR = Statistics.binomialSignTestP(ppR, nR, 0.5);
                            ppR /= nR;
                            ppA /= nA;
                            if (binPA <= 0.05) {
                                rPA = true;
                            } else if (binPA >= 0.95) {
                                rPA = true;
                            }
                            if (binPR <= 0.05) {
                                rPR = true;
                            } else if (binPR >= 0.95) {
                                rPR = true;
                            }
                        }
                    } else if (nA > 0 && nR > 0) {

                        //both exact and simulated, dich-cont

                        double[] dataA1 = Utilities.VectorToDoubleArray(aD1);
                        aveA = Statistics.mean(dataA1);
                        stdevA = Statistics.sampleStandardDeviation(dataA1, aveA);
                        tA = Statistics.oneSampleT(aveA, 0.0, stdevA, nA);
                        binPA = Statistics.binomialSignTestP(ppA, nA, 0.5);
                        ppA /= nA;
                        aSig = Statistics.t_testSignificance(Math.abs(tA), nA - 1, 0.05, 1);
                        //aSig = Statistics.wilcoxonTest(dataA1, dataA1.length, 0.05);


                        if (!exactNull) { //simulated, dich-cont
                            double[] dataR1 = Utilities.VectorToDoubleArray(rD1);
                            aveR = Statistics.mean(dataR1);
                            stdevR = Statistics.sampleStandardDeviation(dataR1, aveR);
                            tR = Statistics.oneSampleT(aveR, 0.0, stdevR, nR);
                            binPR = Statistics.binomialSignTestP(ppR, nR, 0.5);
                            ppR /= nR;
                            rSig = Statistics.t_testSignificance(Math.abs(tR), nR - 1, 0.05, 1);
                            //rSig = Statistics.wilcoxonTest(dataR1, dataR1.length, 0.05);
                        } else { //exact, dich-cont
                            nR = nA;
                            ppR = Utilities.nextBinomial(r, 0.5, nA);
                            binPR = Statistics.binomialSignTestP(ppR, nR, 0.5);
                            ppR /= nR;
                            if (binPA <= 0.05) {
                                rPA = true;
                            } else if (binPA >= 0.95) {
                                rPA = true;
                            }
                            if (binPR <= 0.05) {
                                rPR = true;
                            } else if (binPR >= 0.95) {
                                rPR = true;
                            }
                        }
                    }
                    if (nR > 0 && nA > 0) {
                        double diff = ppA - ppR;
                        int pos = 0;
                        if (diff > 0) {
                            pos = 1;
                        }
                        
                        //all
                        fw.write(tnum + "\t" + ppA + "\t" + ppR + "\t" + nA + "\t" + nR + "\t" + diff + "\t" + pos + "\t");



                        if (!exactNull) {

                            if (t1.equals("cont") && t2.equals("cont")) { //simulated, cont - cont
//System.out.println("OK ppR in printing: " + ppR);
                                fw.write(corA + "\t" + corR + "\t" + rPA + "\t" + rPR + "\t");
                                fw.write("NA\t" + tA + "\tNA\tNA\t" + tR + "\tNA\t");
                                if (rPA) {
                                    fw.write(diff + "\t");
                                } else {
                                    fw.write("NA\t");
                                }
                                if (rPR) {
                                    fw.write(diff + "\t");
                                } else {
                                    fw.write("NA\t");
                                }
                                if (rPA && rPR) {
                                    fw.write(diff + "\n");
                                } else {
                                    fw.write("NA\n");
                                }
                            } else { // simulated, dich - cont
                                fw.write(binPA + "\t" + binPR + "\t" + aSig + "\t" + rSig + "\t");
                                fw.write(aveA + "\t" + tA + "\t" + stdevA + "\t" + aveR + "\t" + tR + "\t" + stdevR + "\t");
                                if (aSig) {
                                    fw.write(diff + "\t");
                                } else {
                                    fw.write("NA\t");
                                }
                                if (rSig) {
                                    fw.write(diff + "\t");
                                } else {
                                    fw.write("NA\t");
                                }
                                if (aSig && rSig) {
                                    fw.write(diff + "\n");
                                } else {
                                    fw.write("NA\n");
                                }
                            }
                        } else {

                            if (t1.equals("cont") && t2.equals("cont")) { //exact, cont-cont

                                fw.write(binPA + "\t" + binPR + "\t" + rPA + "\t" + rPR + "\t");
                                fw.write(corA + "\t" + tA + "\t" + aSig + "\tNA\tNA\tNA\t");
                                if (rPA) {
                                    fw.write(diff + "\t");
                                } else {
                                    fw.write("NA\t");
                                }
                                if (rPR) {
                                    fw.write(diff + "\t");
                                } else {
                                    fw.write("NA\t");
                                }
                                if (rPA && rPR) {
                                    fw.write(diff + "\n");
                                } else {
                                    fw.write("NA\n");
                                }
                            } else { //exact, dich-cont

                                fw.write(binPA + "\t" + binPR + "\t" + rPA + "\t" + rPR + "\t");
                                fw.write(aveA + "\t" + tA + "\t" + stdevA + "\tNA\tNA\tNA\t");
                                if (rPA) {
                                    fw.write(diff + "\t");
                                } else {
                                    fw.write("NA\t");
                                }
                                if (rPR) {
                                    fw.write(diff + "\t");
                                } else {
                                    fw.write("NA\t");
                                }
                                if (rPA && rPR) {
                                    fw.write(diff + "\n");
                                } else {
                                    fw.write("NA\n");
                                }
                            }
                        }
                    }
                    ppR = 0.0; ////
                    ppA = 0.0; ////
                    aveR = 0.0; //
                    aveA = 0.0; //
                    stdevR = 0.0;
                    stdevA = 0.0;
                    tR = 0.0;
                    tA = 0.0;
                    binPR = 0.0; //
                    binPA = 0.0; //
                    rPR = false; //
                    rPA = false; //
                    aSig = false; //
                    rSig = false; //
                    rD = new Vector();
                    rD1 = new Vector();
                    aD = new Vector();
                    aD1 = new Vector();
                    nR = 0; //
                    nA = 0; //
                    treeNum = tnum;
                }
                if (ra.equals("Actual")) {
//System.out.println("\tData are real\n");
//System.out.println("Previous: ppA: " + ppA);
                    if (t1.equals("cont") && t2.equals("cont")) {
                        if (cN2 > 0.0 && cN1 > 0.0) {
                            ppA++;
//System.out.println("A");
                        } else if (cN1 < 0.0 && cN2 < 0.0) {
                            ppA++;
//System.out.println("B");
                        }
                    } else if (cN2 > 0.0) {
//System.out.println("C");
                        ppA++;
                    }
                    aD1.add(new Double(cN2));
                    aD.add(new Double(cN1));
                    nA++;
//System.out.println("New: C1: " + cN1 + " C2: " + cN2 + " ppA: " + ppA + " nA: " + nA);
                    if (exactNull) {
                        nR++;
                    }

                } else if (ra.equals("Random") && !exactNull) {
//System.out.println("\tData are random\n");
                    if (t1.equals("cont") && t2.equals("cont")) {
                        if (cN2 > 0.0 && cN1 > 0.0) {
                            ppR++;
//System.out.println("RA");
                        } else if (cN1 < 0.0 && cN2 < 0.0) {
                            ppR++;
//System.out.println("RB");
                        }
                    } else if (cN2 > 0.0) {
                        ppR++;
//System.out.println("RC");
                    }
                    rD1.add(new Double(cN2));
                    rD.add(new Double(cN1));
                    nR++;
                }
            }
            d.close();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//estimates the rate matrix for uncorrelated characters DEPRICATED
    public static double[][] estimateIndCharQMatrix(double[][] c1m, double[][] c2m) {
//p(t) = e^(Rt)
//log(p(t))/t = R
//log(x) = (x-1)-(x-1)*(x-1)/2+(x-1)*(x-1)*(x-1)/3-...
//log(p(t))/t = R
//R = (P-1)/t - (P-1)(P-1)/2t + (P-1)(P-1)(P-1)/3t - ...
//P =
//[ Pc10->0*Pc20->0 Pc10->0*Pc20->1 ... ]
//[ Pc10->0*Pc21->0 Pc10->0*Pc21->1 ... ]
//[ .				      . ]
//[ ...                             ... ]
        double t = 1000;
        int iter = 100;
        double[][] p1 = transitionMatrix(c1m, t);
        double[][] p2 = transitionMatrix(c2m, t);
        double[][] R = new double[4][4];
        double[][] P = new double[4][4];
        double[][] temp = new double[4][4];
        double[][] temp1 = new double[4][4];
        double[][] E = matrixUtilities.makeE(4);
        P[0][0] = p1[0][0] * p2[0][0];
        P[0][1] = p1[0][0] * p2[0][1];
        P[0][2] = p1[0][1] * p2[0][0];
        P[0][3] = p1[0][1] * p2[0][1];
        P[1][0] = p1[0][0] * p2[1][0];
        P[1][1] = p1[0][0] * p2[1][1];
        P[1][2] = p1[0][1] * p2[1][0];
        P[1][3] = p1[0][1] * p2[1][1];
        P[2][0] = p1[1][0] * p2[0][0];
        P[2][1] = p1[1][0] * p2[0][1];
        P[2][2] = p1[1][1] * p2[0][0];
        P[2][3] = p1[1][1] * p2[0][1];
        P[3][0] = p1[1][0] * p2[1][0];
        P[3][1] = p1[1][0] * p2[1][1];
        P[3][2] = p1[1][1] * p2[1][0];
        P[3][3] = p1[1][1] * p2[1][1];

//R = (P-1)/t - (P-1)(P-1)/2t + (P-1)(P-1)(P-1)/3t - ...
        temp = matrixUtilities.add(P, matrixUtilities.scalarMultiply(-1, E));
        temp1 = matrixUtilities.copy(temp);
        R = matrixUtilities.copy(temp);
        for (int i = 1; i < iter; i++) {
            temp1 = matrixUtilities.multiply(temp1, temp);
            if (i % 2 != 0) {
                R = matrixUtilities.add(R, matrixUtilities.scalarMultiply(-1 / (i + 1), temp1));
            } else {
                R = matrixUtilities.add(R, matrixUtilities.scalarMultiply(1 / (i + 1), temp1));
            }
        }
        R = matrixUtilities.scalarMultiply(1 / t, R);
        return (R);
    }

//Returns the index of a randomly selected species with a state of interest. states[i] is true if the taxon has the state.
//Assumes left to right tree ordering
    public static String pickRandomTaxonWithState(String[] taxa, boolean[] presence, Random r)
            throws Exception {
        boolean test = false;
        Vector withS = new Vector();
        for (int i = 0; i < presence.length; i++) {
            if (presence[i]) {
                test = true;
                withS.add(Integer.toString(i));
            }
        }
        if (!test) {
            throw new Exception("None of the taxa have the indicated character states.");
        }
        //Random r = new Random();
        return (taxa[Integer.parseInt((String) withS.elementAt((int) (r.nextDouble() * (double) withS.size())))]);
    }

    public static Tree calculateBrunchContrasts(Tree tree, String[] taxaDisc, String[] taxaCont, String[] disc1, double[] cont2, FileWriter writer, String preText) {

//System.out.println("Assigning continuous states.\n");
        tree = assignICStates(tree, null, taxaCont, null, cont2, null, null);
//System.out.println("Assigning discrete states.\n");
        tree = assignDiscreteTerminalStates(tree, taxaDisc, disc1, 1);

        //Tree tree = t;
//System.out.println("Parsing contrasts.\n");

        boolean descending = false;
        try {
            int numT = 0;
            while (tree.ancestor != null) {
                tree = tree.ancestor;
            }

            while (tree.firstDaughter != null) {
                descending = false;
                if (tree.ancestor != null) {
                    System.out.println("Failure to traverse tree back to root. Quitting.");
                    System.exit(1);
                }
                if (tree.firstDaughter != null) {
                    numT++;
//System.out.println("\nTraversal " + numT + "\n");
//System.out.println("Node " + tree.value);
                    tree = tree.firstDaughter;
//here!!
//System.out.println("Node " + tree.value);
                    if (tree.firstDaughter == null && tree.nextSister != null) {
//System.out.println("\tA1 with node state " + tree.disc1 + " for taxon " + tree.value);
                        if (tree.nextSister.disc1.compareTo(tree.disc1) != 0 && tree.nextSister.firstDaughter == null) {
                            //display contrast
                            if (tree.disc1.compareTo("1") == 0) {
                                //System.out.print("Contrast c between " + tree.value + " and " + tree.nextSister.value + " " );
                                //System.out.println(tree.cont2 - tree.nextSister.cont2);
                                if (writer != null && preText != null) {
                                    writer.write(preText + tree.value + "\t" + tree.nextSister.value + "\t1\t" + (tree.cont2 - tree.nextSister.cont2) + "\n");
                                }
                            } else {
                                //System.out.print("Contrast d between " + tree.nextSister.value + " and " + tree.value + " " );
                                //System.out.println(tree.nextSister.cont2 - tree.cont2);
                                if (writer != null && preText != null) {
                                    writer.write(preText + tree.nextSister.value + "\t" + tree.value + "\t1\t" + (tree.nextSister.cont2 - tree.cont2) + "\n");
                                }
                            }
                            //remove node
                            if (tree.ancestor.ancestor == null) {
                                return tree; //case zero - contrast across root node
                            } else if (tree.ancestor.ancestor.ancestor == null) //case one - chop to root
                            {
                                if (tree.ancestor.ancestor.firstDaughter == tree.ancestor) {
                                    tree = tree.ancestor.nextSister;
                                    tree.ancestor = null;
                                } else {
                                    tree = tree.ancestor.ancestor.firstDaughter;
                                    tree.ancestor = null;
                                }
                            } else //case two - chop off branch and reset ancestors, sisters
                            {
                                if (tree.ancestor == tree.ancestor.ancestor.firstDaughter) {
                                    tree = tree.ancestor.nextSister;
                                } else {
                                    tree = tree.ancestor.ancestor.firstDaughter;
                                }
                                if (tree.ancestor.ancestor.firstDaughter == tree.ancestor) {
                                    tree.nextSister = tree.ancestor.nextSister;
                                    tree.ancestor.ancestor.firstDaughter = tree;
                                } else {
                                    tree.ancestor.ancestor.firstDaughter.nextSister = tree;
                                }
                                tree.ancestor = tree.ancestor.ancestor;
                            }
                        } else {
                            //collapse tree to node with all descendants with same discrete state
                            String state = tree.disc1;
                            try {
                                while (percentTrait(tree.ancestor, state, 1) == 1) {
                                    if (tree.ancestor != null) {
                                        tree = tree.ancestor;
                                    } else {
//System.out.println("Reached the root.");
                                        break;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.exit(0);
                            } //catch null pointer exception
                            tree.firstDaughter = null;
                            tree.disc1 = state;
                        }
                    }
                } else {
                    return null;
                }

                while (tree.ancestor != null) {
//System.out.println("Node " + tree.value);
                    if (tree.firstDaughter != null && !descending) {
                        tree = tree.firstDaughter;
//here!!
//System.out.println("\tA1");
                        if (tree.firstDaughter == null && tree.nextSister != null) {
//System.out.println("\tA2 with node state " + tree.disc1 + " for taxon " + tree.value);
                            if (tree.nextSister.disc1.compareTo(tree.disc1) != 0 && tree.nextSister.firstDaughter == null) {
                                //display contrast
//System.out.println("Displaying contrast");
                                if (tree.disc1.compareTo("1") == 0) {
                                    //System.out.print("Contrast a between " + tree.value + " and " + tree.nextSister.value + " " );
                                    //System.out.println(tree.cont2 - tree.nextSister.cont2);
                                    if (writer != null && preText != null) {
                                        writer.write(preText + tree.value + "\t" + tree.nextSister.value + "\t1\t" + (tree.cont2 - tree.nextSister.cont2) + "\n");
                                    }
                                } else {
                                    //System.out.print("Contrast b between " + tree.nextSister.value + " and " + tree.value + " " );
                                    //System.out.println(tree.nextSister.cont2 - tree.cont2);
                                    if (writer != null && preText != null) {
                                        writer.write(preText + tree.nextSister.value + "\t" + tree.value + "\t1\t" + (tree.nextSister.cont2 - tree.cont2) + "\n");
                                    }
                                }
                                //remove node
                                if (tree.ancestor.ancestor == null) {
//System.out.println("Root node reached");
                                    return tree; //case zero - contrast across root node
                                } else if (tree.ancestor.ancestor.ancestor == null) //case one - chop to root
                                {
//System.out.println("Chopping off to root");
                                    if (tree.ancestor.ancestor.firstDaughter == tree.ancestor) {
                                        tree = tree.ancestor.nextSister;
                                        tree.ancestor = null;
                                    } else {
                                        tree = tree.ancestor.ancestor.firstDaughter;
                                        tree.ancestor = null;
                                    }
                                } else //case two - chop off branch and reset ancestors, sisters
                                {
//System.out.println("Chopping off branch");
                                    if (tree.ancestor == tree.ancestor.ancestor.firstDaughter) {
//System.out.println("Setting tree to " + tree.ancestor.nextSister.value + ", the next sister of " + tree.ancestor.value);
                                        tree = tree.ancestor.nextSister;
                                    } else {
//System.out.println("Setting tree to " + tree.ancestor.ancestor.firstDaughter.value + ", the first daughter of " + tree.ancestor.ancestor.value);
                                        tree = tree.ancestor.ancestor.firstDaughter;
                                    }
                                    if (tree.ancestor.ancestor.firstDaughter == tree.ancestor) {
//System.out.println("Resetting next sister of " + tree.value + " to " + tree.ancestor.nextSister.value);
                                        tree.nextSister = tree.ancestor.nextSister;
//System.out.println("Resetting first daughter of " + tree.ancestor.ancestor.value + " to " + tree.value);
                                        tree.ancestor.ancestor.firstDaughter = tree;
                                    } else {
//System.out.println("Resetting next sister of " + tree.value + " to null");
                                        tree.nextSister = null;
//System.out.println("Resetting next sister of " + tree.ancestor.ancestor.firstDaughter.value + " to " + tree.value);
                                        tree.ancestor.ancestor.firstDaughter.nextSister = tree;
                                    }
//System.out.println("Resetting the ancestor of " + tree.value + " to " + tree.ancestor.ancestor.value);
                                    tree.ancestor = tree.ancestor.ancestor;
                                }
                            } else {
//System.out.println("Collapsing node " + tree.value);
                                //collapse tree to node with all descendants with same discrete state
                                String state = tree.disc1;
                                try {
//System.out.println("The percent of trait " + state + " is " + percentTrait(tree.ancestor, state, 1) + " for " + tree.ancestor.value);
//System.out.println("The percent of trait " + state + " is " + percentTrait(tree, state, 1) + " for " + tree.value);
                                    while (percentTrait(tree.ancestor, state, 1) == 1) {
                                        if (tree.ancestor != null) {
                                            tree = tree.ancestor;
                                        } else {
//System.out.println("Reached the root.");
                                            break;
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    System.exit(0);
                                } //catch null pointer exception
//System.out.println("\t\tdown to node " + tree.value);
//System.out.println("\tSetting " + tree.value + "'s first daughter to null");
                                tree.firstDaughter = null;
                                tree.disc1 = state;
                            }
                        }
                    } else if (tree.nextSister != null) {
                        tree = tree.nextSister;
                        descending = false;
                    } else {
                        descending = true;
                        tree = tree.ancestor;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tree;
    }

//returns an array of length equal to the number of taxa in the tree listing (T/F) whether the taxon has one of the
//Sets internal states and adjusts branch lengths according to rules in Felsenstein 1985
//assumes fully bifurcating tree
//assumes branch lengths are all positive or 0, and if 0, adds a tiny value to make positive
    public static void printIC(Tree tree, FileWriter writer, String preText) {
        if (writer == null || preText == null) {
            return;
        }
        boolean descending = false;
        try {
            while (tree.ancestor != null) {
                tree = tree.ancestor;
            }

            descending = false;
            if (tree.ancestor != null) {
                System.out.println("Failure to traverse tree back to root. Quitting.");
                System.exit(1);
            }
//here if tree.statesSet		
            //if(tree.statesSet)
            //{
            //if(tree.cont1>=tree.nextSister.cont1)
            //writer.write(preText + tree.value + "\t" + tree.nextSister.value + "\t" + tree.contrastCont1 + "\t" + tree.contrastCont2 + "\n");
            //else
            //writer.write(preText + tree.nextSister.value + "\t" + tree.value + "\t" + tree.contrastCont1 + "\t" + tree.contrastCont2 + "\n");
            //}


            if (tree.firstDaughter != null) {
                tree = tree.firstDaughter;
//here if tree.statesSet		
                if (tree.statesSet) {
                    if (tree.cont1 >= tree.nextSister.cont1) {
                        writer.write(preText + tree.value + "\t" + tree.nextSister.value + "\t" + tree.contrastCont1 + "\t" + tree.contrastCont2 + "\n");
                    } else {
                        writer.write(preText + tree.nextSister.value + "\t" + tree.value + "\t" + tree.contrastCont1 + "\t" + tree.contrastCont2 + "\n");
                    }
                }

            } else {
                return;
            }

            while (tree.ancestor != null) {
                if (tree.firstDaughter != null && !descending) {
                    tree = tree.firstDaughter;
//here if tree.statesSet		
                    if (tree.statesSet) {
                        if (tree.cont1 >= tree.nextSister.cont1) {
                            writer.write(preText + tree.value + "\t" + tree.nextSister.value + "\t" + tree.contrastCont1 + "\t" + tree.contrastCont2 + "\n");
                        } else {
                            writer.write(preText + tree.nextSister.value + "\t" + tree.value + "\t" + tree.contrastCont1 + "\t" + tree.contrastCont2 + "\n");
                        }
                    }
                } else if (tree.nextSister != null) {
                    tree = tree.nextSister;
                    descending = false;
                } else {
                    descending = true;
                    tree = tree.ancestor;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    public static Tree assignICStates(Tree tree, String[] taxaCont1, String[] taxaCont2, double[] cont1, double[] cont2, FileWriter writer, String preText) {
        double tol = 0.0000001;
//System.out.println("Tree is A " + tree.value);
        tree = assignContinuousTerminalStates(tree, taxaCont1, cont1, 1);
//System.out.println("Tree is B " + tree.value);
        tree = assignContinuousTerminalStates(tree, taxaCont2, cont2, 2);
//System.out.println("Tree is C " + tree.value);

        //Tree tree = new Tree();
        //tree = t;

        boolean descending = false;
        try {
            int numT = 0;
            while (tree.ancestor != null) {
                tree = tree.ancestor;
            }

            while (!tree.statesSet) {
                descending = false;
                numT++;
                //System.out.println("The number of traversals is " + numT);
                if (tree.ancestor != null) {
                    System.out.println("Failure to traverse tree back to root. Quitting.");
                    System.exit(1);
                }
                if (tree.firstDaughter != null) {
//System.out.println("Tree is " + tree.value);
                    tree = tree.firstDaughter;
                    if ((tree.firstDaughter == null || tree.statesSet) && tree.nextSister != null) {
                        tree.statesSet = true;
                        if (tree.nextSister.firstDaughter == null || tree.nextSister.statesSet) {


                            if (tree.length < 0) {
                                tree.length = Math.abs(tree.length);
                            } else if (tree.length == 0) {
                                tree.length = tol;
                            }
                            if (tree.nextSister.length < 0) {
                                tree.nextSister.length = Math.abs(tree.nextSister.length);
                            } else if (tree.nextSister.length == 0) {
                                tree.nextSister.length = tol;
                            }
                            if (tree.ancestor.length < 0) {
                                tree.ancestor.length = Math.abs(tree.ancestor.length);
                            } else if (tree.ancestor.length == 0) {
                                tree.ancestor.length = tol;
                            }

                            if (tree.cont1 != -9999 && tree.nextSister.cont1 != -9999 && !tree.ancestor.statesSet) {
                                tree.ancestor.cont1 = ((1 / tree.length) * tree.cont1 + (1 / tree.nextSister.length) * tree.nextSister.cont1) / (1 / tree.length + 1 / tree.nextSister.length);
                                if (tree.cont1 >= tree.nextSister.cont1) {
                                    tree.contrastCont1 = (tree.cont1 - tree.nextSister.cont1) / Math.sqrt(tree.length + tree.nextSister.length);
                                } else {
                                    tree.contrastCont1 = (tree.nextSister.cont1 - tree.cont1) / Math.sqrt(tree.length + tree.nextSister.length);
                                }
                                //if(print) System.out.println("\nD Contrast value in cont 1 between " + tree.value + " and " + tree.nextSister.value + " is " + tree.contrastCont1 + "\n");

                                //System.out.println("Setting cont1 of " + tree.ancestor.value + " to " + tree.ancestor.cont1);
                            }
                            if (tree.cont2 != -9999 && tree.nextSister.cont2 != -9999 && !tree.ancestor.statesSet) {
                                tree.ancestor.cont2 = ((1 / tree.length) * tree.cont2 + (1 / tree.nextSister.length) * tree.nextSister.cont2) / (1 / tree.length + 1 / tree.nextSister.length);
//cont1 is independent variable
                                if (tree.cont1 >= tree.nextSister.cont1) {
                                    tree.contrastCont2 = (tree.cont2 - tree.nextSister.cont2) / Math.sqrt(tree.length + tree.nextSister.length);
                                } else {
                                    tree.contrastCont2 = (tree.nextSister.cont2 - tree.cont2) / Math.sqrt(tree.length + tree.nextSister.length);
                                }
                                //if(print) System.out.println("\nA Contrast value in cont 2 between " + tree.value + " and " + tree.nextSister.value + " is " + tree.contrastCont2 + "\n");
                                //System.out.println("Setting cont2 of " + tree.ancestor.value + " to " + tree.ancestor.cont2);
                            }


                            tree.nextSister.statesSet = true;
                            if (!tree.ancestor.statesSet) {
                                tree.ancestor.length = tree.ancestor.length + tree.length * tree.nextSister.length / (tree.nextSister.length + tree.length);
                                //System.out.println("Setting branch length of " + tree.ancestor.value + " to " + tree.ancestor.length);
                            }
                            if (!tree.ancestor.statesSet) {
                                tree.ancestor.statesSet = true;
                                //if(writer!=null && preText!=null) if(tree.cont1>=tree.nextSister.cont1)
                                //writer.write(preText + tree.value + "\t" + tree.nextSister.value + "\t" + tree.contrastCont1 + "\t" + tree.contrastCont2 + "\n");
                            }
                            if (tree.ancestor.value.compareTo("root") == 0) {
                                //System.out.println("Root has been found and is being set!");
                                //tree=tree.ancestor;
                                //return tree;
                            }

                        }
                    }
                } else {
                    return null;
                }

                while (tree.ancestor != null) {
//System.out.println("Tree is " + tree.value);
                    if (tree.firstDaughter != null && !descending) {
                        tree = tree.firstDaughter;
                        if ((tree.firstDaughter == null || tree.statesSet) && tree.nextSister != null) {
                            tree.statesSet = true;
                            if (tree.nextSister.firstDaughter == null || tree.nextSister.statesSet) {
//System.out.println("Matched " + tree.value);
                                if (tree.length < 0) {
                                    tree.length = Math.abs(tree.length);
                                } else if (tree.length == 0) {
                                    tree.length = tol;
                                }
                                if (tree.nextSister.length < 0) {
                                    tree.nextSister.length = Math.abs(tree.nextSister.length);
                                } else if (tree.nextSister.length == 0) {
                                    tree.nextSister.length = tol;
                                }
                                if (tree.ancestor.length < 0) {
                                    tree.ancestor.length = Math.abs(tree.ancestor.length);
                                } else if (tree.ancestor.length == 0) {
                                    tree.ancestor.length = tol;
                                }

                                if (tree.cont1 != -9999 && tree.nextSister.cont1 != -9999 && !tree.ancestor.statesSet) {
                                    tree.ancestor.cont1 = ((1 / tree.length) * tree.cont1 + (1 / tree.nextSister.length) * tree.nextSister.cont1) / (1 / tree.length + 1 / tree.nextSister.length);
                                    if (tree.cont1 >= tree.nextSister.cont1) {
                                        tree.contrastCont1 = (tree.cont1 - tree.nextSister.cont1) / Math.sqrt(tree.length + tree.nextSister.length);
                                    } else {
                                        tree.contrastCont1 = (tree.nextSister.cont1 - tree.cont1) / Math.sqrt(tree.length + tree.nextSister.length);
                                    }
                                    //if(print) System.out.println("\nB Contrast value in cont 1 between " + tree.value + " and " + tree.nextSister.value + " is " + tree.contrastCont1 + "\n");
                                    //System.out.println("Setting cont1 of " + tree.ancestor.value + " to " + tree.ancestor.cont1);
                                }
                                if (tree.cont2 != -9999 && tree.nextSister.cont2 != -9999 && !tree.ancestor.statesSet) {
                                    tree.ancestor.cont2 = ((1 / tree.length) * tree.cont2 + (1 / tree.nextSister.length) * tree.nextSister.cont2) / (1 / tree.length + 1 / tree.nextSister.length);
                                    if (tree.cont1 >= tree.nextSister.cont1) {
                                        tree.contrastCont2 = (tree.cont2 - tree.nextSister.cont2) / Math.sqrt(tree.length + tree.nextSister.length);
                                    } else {
                                        tree.contrastCont2 = (tree.nextSister.cont2 - tree.cont2) / Math.sqrt(tree.length + tree.nextSister.length);
                                    }
                                    //if(print) System.out.println("\nC Contrast value in cont 2 between " + tree.value + " and " + tree.nextSister.value + " is " + tree.contrastCont2 + "\n");
                                    //System.out.println("Setting cont2 of " + tree.ancestor.value + " to " + tree.ancestor.cont2);
                                }
                                //if(writer!=null && preText!=null) if(tree.cont1>=tree.nextSister.cont1)
                                //writer.write(preText + tree.value + "\t" + tree.nextSister.value + "\t" + tree.contrastCont1 + "\t" + tree.contrastCont2 + "\n");

                                tree.nextSister.statesSet = true;
                                if (!tree.ancestor.statesSet) {
                                    tree.ancestor.length = tree.ancestor.length + tree.length * tree.nextSister.length / (tree.nextSister.length + tree.length);
                                    //System.out.println("Setting branch length of " + tree.ancestor.value + " to " + tree.ancestor.length);
                                }
                                //tree.ancestor.statesSet=true;
                                if (!tree.ancestor.statesSet) {
                                    tree.ancestor.statesSet = true;
                                    //if(writer!=null && preText!=null) if(tree.cont1>=tree.nextSister.cont1)
                                    //writer.write(preText + tree.value + "\t" + tree.nextSister.value + "\t" + tree.contrastCont1 + "\t" + tree.contrastCont2 + "\n");
                                }
                            }
                        }
                    } else if (tree.nextSister != null) {
                        tree = tree.nextSister;
                        descending = false;
                    } else {
                        descending = true;
                        tree = tree.ancestor;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (writer != null && preText != null) {
            printIC(tree, writer, preText);
        }
        return tree;
    }

//returns an array of length equal to the number of taxa in the tree listing (T/F) whether the taxon has one of the
//states listed in states. The actual character states of the taxa in the tree (left to right order) are stored in
//the characters array
    public static boolean[] hasStates(String[] states, String[] characters) {
        boolean[] out = new boolean[characters.length];
        for (int i = 0; i < characters.length; i++) {
            out[i] = false;
        }
        for (int i = 0; i < characters.length; i++) {
            for (int j = 0; j < states.length; j++) {
//System.out.println(characters[i] + "\n\t" + states[j]);
                if (characters[i].equals(states[j])) {
                    out[i] = true;
                    j = states.length + 1;
                }
            }
        }
        return out;
    }

    public static double percentTrait(Tree t, String state, int pos) {
        if (t == null) {
            return -1;
        }
        String[] states = getDiscreteTaxonStates(t, pos);
        int num = 0;
        //System.out.println("There are " + states.length + " states.");
        for (int i = 0; i < states.length; i++) {
            if (states[i].compareTo(state) == 0) {
                num++;
            }
        }
        return (double) num / (double) states.length;
    }

    public static String[] getDiscreteTaxonStates(Tree tree, int pos) {
        boolean descending = false;
        Tree t = tree;
        //while(t.ancestor!=null)
        //t=t.ancestor;
        Vector taxa = new Vector();
        int cnt = 0;
        if (t.firstDaughter != null) {
            t = t.firstDaughter;
            cnt++;
            //if(t.firstDaughter==null)
            //{
            //if(pos==1) taxa.add(t.disc1);
            //else if(pos==2) taxa.add(t.disc1);
            //}
        } else {
            String[] out1 = {tree.disc1};
            if (pos == 2) {
                out1[0] = tree.disc2;
            }
            return out1;
        }
        //while(t.ancestor!=null)
        while (cnt > 0) {
            if (t.firstDaughter != null && !descending) {
                t = t.firstDaughter;
                cnt++;
            } else if (t.nextSister != null) {
                if (t.firstDaughter == null) {
                    if (pos == 1) {
                        taxa.add(t.disc1);
                    } else if (pos == 2) {
                        taxa.add(t.disc1);
                    }
                    //cnt++;
                }
                t = t.nextSister;
                descending = false;
            } else {
                if (t.firstDaughter == null) {
                    if (pos == 1) {
                        taxa.add(t.disc1);
                    } else if (pos == 2) {
                        taxa.add(t.disc1);
                    }
                    //cnt++;
                }
                descending = true;
                t = t.ancestor;
                cnt--;
            }
        }
        String[] out = new String[taxa.size()];
        taxa.copyInto(out);
        return out;
    }

    public static String[] getTaxonStates(Tree tree) {
        boolean descending = false;
        Tree t = tree;
        while (t.ancestor != null) {
            t = t.ancestor;
        }
        Vector taxa = new Vector();
        int cnt = 0;
        if (t.firstDaughter != null) {
            t = t.firstDaughter;
        } else {
            return null;
        }
        while (t.ancestor != null) {
            if (t.firstDaughter != null && !descending) {
                t = t.firstDaughter;
            } else if (t.nextSister != null) {
                if (t.firstDaughter == null) {
                    taxa.add(Integer.toString((int) t.state));
                    cnt++;
                }
                t = t.nextSister;
                descending = false;
            } else {
                if (t.firstDaughter == null) {
                    taxa.add(Integer.toString((int) t.state));
                    cnt++;
                }
                descending = true;
                t = t.ancestor;
            }
        }
        String[] out = new String[taxa.size()];
        taxa.copyInto(out);
        return out;
    }

    public static double[] scaleStates(double[] states, double min, double max) {
        double maxa = Utilities.findMax(states);
        double mina = Utilities.findMin(states);
        for (int i = 0; i < states.length; i++) {
            states[i] = min + (max - min) * (states[i] - mina) / (maxa - mina);
        }
        return states;
    }

    public double evolveBrownianState(double ancState, double branchLength, double rate) {
        return ancState + rng.nextGaussian() * Math.sqrt(branchLength * rate);
        //return ancState + rng.nextGaussian()*branchLength*rate;
    }

    /** randomly evolve a character under uniform motion*/
    public double evolveUniformState(double ancState, double branchLength, double rate) {
        return ancState + (rng.nextDouble() - 0.5) * branchLength * rate;
        //return ancState + (rng.nextDouble()-0.5)*Math.sqrt(branchLength*rate);
    }

//get the state evolving from current state, where cur is the index of the 'from' state in the transition matrix
//  a b c d
//a - - - -
//b - - - - 
//c - - - -
//d - - - -
// so cur=0 is starting from character state a, cur=2 starts from c, and so on
// R is the stochastic rate matrix, and t is the time the continuous time process goes
    public static int evolveState(double[][] R, double t, int cur, Random r)
            throws Exception {
        double[][] P = transitionMatrix(R, t);
        double totc = 0.0;
        double choice = r.nextDouble();
        for (int i = 0; i < P[0].length; i++) {
            if (choice > totc && choice <= totc + P[cur][i]) {
                return (i);
            } else {
                totc += P[cur][i];
            }
        }
        throw new Exception("Character not found");
    }

//evolve numbers of substitutions along branches of tree under continuous time Markov Chain
//the branch length that is recorded is the number of substitutions per site per time unit
    public static Tree evolveBranchLengths(Tree t, int numTaxa, int seqLength, double lambda, Random r) {
        boolean descending = false;
        Tree tree = t;
        try {
            while (tree.ancestor != null) {
                tree = tree.ancestor;
            }
            //tree.length = tree.time;
            //tree.length = seqLength*lambda*tree.time;
            //tree.length = Utilities.nextPoisson(r, seqLength*lambda, tree.time)/(seqLength*tree.time);
            tree.length = Utilities.nextPoisson(r, seqLength * lambda, tree.time);

            if (tree.firstDaughter != null) {
                tree = tree.firstDaughter;
                //tree.length = tree.time;
                //tree.length = seqLength*lambda*tree.time;
                //tree.length = Utilities.nextPoisson(r, seqLength*lambda, tree.time)/(seqLength*tree.time);
                tree.length = Utilities.nextPoisson(r, seqLength * lambda, tree.time);
            } else {
                return null;
            }

            while (tree.ancestor != null) {
                if (tree.firstDaughter != null && !descending) {
                    tree = tree.firstDaughter;
                    //tree.length = tree.time;
                    //tree.length = seqLength*lambda*tree.time;
                    //tree.length = Utilities.nextPoisson(r, seqLength*lambda, tree.time)/(seqLength*tree.time);
                    tree.length = Utilities.nextPoisson(r, seqLength * lambda, tree.time);
                } else if (tree.nextSister != null) {
                    tree = tree.nextSister;
                    //tree.length = tree.time;
                    //tree.length = seqLength*lambda*tree.time;
                    //tree.length = Utilities.nextPoisson(r, seqLength*lambda, tree.time)/(seqLength*tree.time);
                    tree.length = Utilities.nextPoisson(r, seqLength * lambda, tree.time);
                    descending = false;
                } else {
                    descending = true;
                    tree = tree.ancestor;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tree;
    }

//evolves one character up provided tree under the stochastic matrix Q
    public static String[] evolveCharacterMatrix(Tree t, int numTaxa, double[][] Q, Random r) {
        double characters[][] = evolveCharacterMatrix(t, numTaxa, 1, Q, r);
        String out[] = new String[numTaxa];
        for (int i = 0; i < numTaxa; i++) {
            out[i] = Integer.toString((int) characters[i][0]);
        }
        return out;
    }

    public static Tree evolveIndCharIndTaxMatrix(Tree t, double[][] Q1, double[][] Q2, Random r) {
        boolean descending = false;
        int posn = 0;
        Tree tree = t;
        double[] P1 = matrixUtilities.pi(Q1);
        double[] P2 = matrixUtilities.pi(Q2);
        try {
            posn = 0;
            descending = false;

            while (tree.ancestor != null) {
                tree = tree.ancestor;
            }

            int c1 = Utilities.sampleState(P1, r);
            int c2 = Utilities.sampleState(P2, r);
            if (c1 == 0 && c2 == 0) {
                tree.state = 0.0;
                tree.discreteState = "0,0";
            } else if (c1 == 0 && c2 == 1) {
                tree.state = 1.0;
                tree.discreteState = "0,1";
            } else if (c1 == 1 && c2 == 0) {
                tree.state = 2.0;
                tree.discreteState = "1,0";
            } else {
                tree.state = 3.0;
            }
            {
                tree.discreteState = "1,1";
            }

            if (tree.firstDaughter != null) {
                tree = tree.firstDaughter;

                c1 = Utilities.sampleState(P1, r);
                c2 = Utilities.sampleState(P2, r);
                if (c1 == 0 && c2 == 0) {
                    tree.state = 0.0;
                    tree.discreteState = "0,0";
                } else if (c1 == 0 && c2 == 1) {
                    tree.state = 1.0;
                    tree.discreteState = "0,1";
                } else if (c1 == 1 && c2 == 0) {
                    tree.state = 2.0;
                    tree.discreteState = "1,0";
                } else if (c1 == 1 && c2 == 1) {
                    tree.state = 3.0;
                    tree.discreteState = "1,1";
                }

            } else {
                return null;
            }
            while (tree.ancestor != null) {
                if (tree.firstDaughter != null && !descending) {
                    tree = tree.firstDaughter;

                    c1 = Utilities.sampleState(P1, r);
                    c2 = Utilities.sampleState(P2, r);
                    if (c1 == 0 && c2 == 0) {
                        tree.state = 0.0;
                        tree.discreteState = "0,0";
                    } else if (c1 == 0 && c2 == 1) {
                        tree.state = 1.0;
                        tree.discreteState = "0,1";
                    } else if (c1 == 1 && c2 == 0) {
                        tree.state = 2.0;
                        tree.discreteState = "1,0";
                    } else if (c1 == 1 && c2 == 1) {
                        tree.state = 3.0;
                        tree.discreteState = "1,1";
                    }

                } else if (tree.nextSister != null) {
                    if (tree.firstDaughter == null) {
                        posn++; //node is terminal
                    }
                    tree = tree.nextSister;

                    c1 = Utilities.sampleState(P1, r);
                    c2 = Utilities.sampleState(P2, r);
                    if (c1 == 0 && c2 == 0) {
                        tree.state = 0.0;
                        tree.discreteState = "0,0";
                    } else if (c1 == 0 && c2 == 1) {
                        tree.state = 1.0;
                        tree.discreteState = "0,1";
                    } else if (c1 == 1 && c2 == 0) {
                        tree.state = 2.0;
                        tree.discreteState = "1,0";
                    } else if (c1 == 1 && c2 == 1) {
                        tree.state = 3.0;
                        tree.discreteState = "1,1";
                    }

                    descending = false;
                } else {
                    if (tree.firstDaughter == null) {
                        posn++; //node is terminal
                    }
                    descending = true;
                    tree = tree.ancestor;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tree;
    }

//ASSUMES TREE IS SCALED APPROPRIATELY FOR RATE AND FOR UNIFORM FOSSIL PLACEMENT
    public static Tree evolveFossils(Tree t, double rate, Random r) {
        double totTime = 0;
        //double depth = Tree.maxTreeTime(t, true);
        double totTLen = Tree.getTotalLength(t, true);
        t = Tree.setNodeAges(t, true);
        t = Tree.setCumulativeNodeAge(t, true);
        boolean descending = false;
        boolean good = true;
        while (t.ancestor != null) {
            t = t.ancestor;
        }
        while (totTime < totTLen) {
            if (good) {
                totTime += Utilities.nextExponential(r, rate);
            }
            if (t.cumulativeTime > totTime) //we're at the correct node
            {
                //double ta = depth - (t.nodeAge - (t.cumulativeTime-totTime));
                double ta = (t.nodeAge - (t.cumulativeTime - totTime));
                if (ta < t.fossilAge || t.fossilAge == 0.0) {
                    if (t.ancestor != null) {
                        t.fossilAge = ta;
                        t.numFossils++;
//System.out.println("*Evolving fossil at node " + t.value + " with ancestor " + t.ancestor.value + ":");
//System.out.println("\tNode age: " + t.nodeAge);
//System.out.println("\tCumulative time: " + t.cumulativeTime);
//System.out.println("\tPoisson time: " + totTime);
//System.out.println("\tFossil age: " + t.fossilAge);
//System.out.println("\tTerminal node?: " + (t.firstDaughter==null));
//System.out.println("\tExtinct?: " + t.extinct);
                    }
                }
                //t.fossilAge.add(new Double(t.nodeAge - (t.cumulativeTime-totTime)));
                if (t.nodeAge - (t.cumulativeTime - totTime) < 0) {
                    System.out.println("Found negative fossil placement time!! Quitting.");
                    System.exit(0);
                }
                good = true;
            } else //need to find the correct node
            {
                if (t.firstDaughter != null && !descending) {
                    t = t.firstDaughter;
                } else if (t.nextSister != null) {
                    t = t.nextSister;
                    descending = false;
                } else {
                    descending = true;
                    if (t.ancestor == null) {
                        System.out.println("Returning early from evolving fossils.");
                        return t;
                    }
                    t = t.ancestor;
                }
                good = false;
            }
        }
        return Tree.findRoot(t);
    }

//assumes the root is already at stationarity
//continuous time model
//evolves characters independently according to the 2x2 rate matrices Q1 and Q2
    public static Tree evolveIndependentCharacterMatrix(Tree t, double[][] Q1, double[][] Q2, Random r) {
        boolean descending = false;
        //double[][] chars = new double[numTaxa][numChars];
        //System.out.println(ts + " string length: " + ts.length());
        int posn = 0;
        Tree tree = t;
        try {
            //for(int i=0;i<numChars;i++)
            //{
//System.out.println("\nEvolving the " + i + " character state.\n");
            //if(i!=0 && posn!=numTaxa)
            //{
            //System.out.println("Did not correctly traverse tree! Expecting " + numTaxa + " taxa. Found " + posn + " taxa.");
            //System.exit(0);
            //}
            posn = 0;
            descending = false;

//System.out.println(ts + " string length: " + ts.length());
            while (tree.ancestor != null) {
                tree = tree.ancestor;
            }
            //tree.state = (double)evolveState(Q, 1000, 0, r);
            int tc1 = 0;
            int tc2 = 0;
            int c1 = evolveState(Q1, 1000, 0, r);
            int c2 = evolveState(Q2, 1000, 0, r);
//System.out.println("state1: " + c1 + " state2: " + c2);
            if (c1 == 0 && c2 == 0) {
                tree.state = 0.0;
                tree.discreteState = "0,0";
            } else if (c1 == 0 && c2 == 1) {
                tree.state = 1.0;
                tree.discreteState = "0,1";
            } else if (c1 == 1 && c2 == 0) {
                tree.state = 2.0;
                tree.discreteState = "1,0";
            } else {
                tree.state = 3.0;
            }
            {
                tree.discreteState = "1,1";
            }
//System.out.println("The root state is " + tree.state + " with label " + tree.value);
            if (tree.firstDaughter != null) {
                tree = tree.firstDaughter;
                tc1 = 0;
                tc2 = 0;
                if ((int) tree.ancestor.state >= 2) {
                    tc1 = 1;
                }
                if (((int) tree.ancestor.state) % 2 == 1) {
                    tc2 = 1;
                }
                c1 = evolveState(Q1, tree.ancestor.time, tc1, r);
                c2 = evolveState(Q2, tree.ancestor.time, tc2, r);
                if (c1 == 0 && c2 == 0) {
                    tree.state = 0.0;
                    tree.discreteState = "0,0";
                } else if (c1 == 0 && c2 == 1) {
                    tree.state = 1.0;
                    tree.discreteState = "0,1";
                } else if (c1 == 1 && c2 == 0) {
                    tree.state = 2.0;
                    tree.discreteState = "1,0";
                } else if (c1 == 1 && c2 == 1) {
                    tree.state = 3.0;
                    tree.discreteState = "1,1";
                }
//System.out.println("X state1: " + c1 + " state2: " + c2 + " tree state: " + Integer.toString((int)tree.state));

                //tree.state = (double)evolveState(Q1, tree.ancestor.time, (int)tree.ancestor.state, r);
                //System.out.println("Ancestor time " + tree.ancestor.time + " Label: " + tree.ancestor.value);
                //System.out.println("a");
            } //else { System.out.println("b"); return null;}
            else {
                return null;
            }
            while (tree.ancestor != null) {
                if (tree.firstDaughter != null && !descending) {
                    tree = tree.firstDaughter;
                    //tree.state = (double)evolveState(Q, tree.ancestor.time, (int)tree.ancestor.state, r);
                    tc1 = 0;
                    tc2 = 0;
                    if ((int) tree.ancestor.state >= 2) {
                        tc1 = 1;
                    }
                    if (((int) tree.ancestor.state) % 2 == 1) {
                        tc2 = 1;
                    }
                    c1 = evolveState(Q1, tree.ancestor.time, tc1, r);
                    c2 = evolveState(Q2, tree.ancestor.time, tc2, r);
                    if (c1 == 0 && c2 == 0) {
                        tree.state = 0.0;
                        tree.discreteState = "0,0";
                    } else if (c1 == 0 && c2 == 1) {
                        tree.state = 1.0;
                        tree.discreteState = "0,1";
                    } else if (c1 == 1 && c2 == 0) {
                        tree.state = 2.0;
                        tree.discreteState = "1,0";
                    } else if (c1 == 1 && c2 == 1) {
                        tree.state = 3.0;
                        tree.discreteState = "1,1";
                    }
//System.out.println("A state1: " + c1 + " state2: " + c2 + " tree state: " + Integer.toString((int)tree.state));
                    //System.out.println("Ancestor time " + tree.ancestor.time + " Label: " + tree.ancestor.value + " with current state " + tree.state);
                    //System.out.println("c");
                } else if (tree.nextSister != null) {
                    if (tree.firstDaughter == null) {
                        //chars[posn][i]=tree.state;
                        posn++; //node is terminal
                        //System.out.println("d");
                    }
                    tree = tree.nextSister;
                    tc1 = 0;
                    tc2 = 0;
                    //tree.state = (double)evolveState(Q, tree.ancestor.time, (int)tree.ancestor.state, r);
                    if ((int) tree.ancestor.state >= 2) {
                        tc1 = 1;
                    }
                    if (((int) tree.ancestor.state) % 2 == 1) {
                        tc2 = 1;
                    }
                    c1 = evolveState(Q1, tree.ancestor.time, tc1, r);
                    c2 = evolveState(Q2, tree.ancestor.time, tc2, r);
                    if (c1 == 0 && c2 == 0) {
                        tree.state = 0.0;
                        tree.discreteState = "0,0";
                    } else if (c1 == 0 && c2 == 1) {
                        tree.state = 1.0;
                        tree.discreteState = "0,1";
                    } else if (c1 == 1 && c2 == 0) {
                        tree.state = 2.0;
                        tree.discreteState = "1,0";
                    } else if (c1 == 1 && c2 == 1) {
                        tree.state = 3.0;
                        tree.discreteState = "1,1";
                    }
//System.out.println("B state1: " + c1 + " state2: " + c2 + " tree state: " + Integer.toString((int)tree.state));
                    //System.out.println("Ancestor time " + tree.ancestor.time + " Label: " + tree.ancestor.value + " with current state " + tree.state);
                    descending = false;
                    //System.out.println("e");
                } else {
                    if (tree.firstDaughter == null) {
                        //chars[posn][i]=tree.state;
                        posn++; //node is terminal
                        //System.out.println("f");
                    }
                    descending = true;
                    tree = tree.ancestor;
                    //System.out.println("g");
                }
                //System.out.println("h");
            }
            //System.out.println("i");
            //}
            //System.out.println("j");
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return chars;
        return tree;
    }

//assumes the root is already at stationarity
//continuous time model
    public static double[][] evolveCharacterMatrix(Tree t, int numTaxa, int numChars, double[][] Q, Random r) {
        boolean descending = false;
        double[][] chars = new double[numTaxa][numChars];
        //System.out.println(ts + " string length: " + ts.length());
        int posn = 0;
        try {
            //Random r = new Random();
            for (int i = 0; i < numChars; i++) {
//System.out.println("\nEvolving the " + i + " character state.\n");
                if (i != 0 && posn != numTaxa) {
                    System.out.println("Did not correctly traverse tree! Expecting " + numTaxa + " taxa. Found " + posn + " taxa.");
                    System.exit(0);
                }
                posn = 0;
                descending = false;

                Tree tree = t;
//System.out.println(ts + " string length: " + ts.length());
                while (tree.ancestor != null) {
                    tree = tree.ancestor;
                }
                tree.state = (double) evolveState(Q, 1000, 0, r); // this is incorrect with certain stochastic matrices!!!
//System.out.println("The root state is " + tree.state + " with label " + tree.value);
                if (tree.firstDaughter != null) {
                    tree = tree.firstDaughter;
                    tree.state = (double) evolveState(Q, tree.ancestor.time, (int) tree.ancestor.state, r);
                    //System.out.println("Ancestor time " + tree.ancestor.time + " Label: " + tree.ancestor.value);
                    //System.out.println("a");
                } //else { System.out.println("b"); return null;}
                else {
                    return null;
                }
                while (tree.ancestor != null) {
                    if (tree.firstDaughter != null && !descending) {
                        tree = tree.firstDaughter;
                        tree.state = (double) evolveState(Q, tree.ancestor.time, (int) tree.ancestor.state, r);
                        //System.out.println("Ancestor time " + tree.ancestor.time + " Label: " + tree.ancestor.value + " with current state " + tree.state);
                        //System.out.println("c");
                    } else if (tree.nextSister != null) {
                        if (tree.firstDaughter == null) {
                            chars[posn][i] = tree.state;
                            posn++; //node is terminal
                            //System.out.println("d");
                        }
                        tree = tree.nextSister;
                        tree.state = (double) evolveState(Q, tree.ancestor.time, (int) tree.ancestor.state, r);
                        //System.out.println("Ancestor time " + tree.ancestor.time + " Label: " + tree.ancestor.value + " with current state " + tree.state);
                        descending = false;
                        //System.out.println("e");
                    } else {
                        if (tree.firstDaughter == null) {
                            chars[posn][i] = tree.state;
                            posn++; //node is terminal
                            //System.out.println("f");
                        }
                        descending = true;
                        tree = tree.ancestor;
                        //System.out.println("g");
                    }
                    //System.out.println("h");
                }
                //System.out.println("i");
            }
            //System.out.println("j");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chars;
    }

    public static double[][] transitionMatrix(double[][] R, double time) {
        return matrixUtilities.exp(R, time, 30);
    }

    /** return a character matrix of dichotomous values in which a value is 1 if >= threshold and 0 otherwise*/
    public int[][] thresholdMatrix(double[][] matrix, double threshold) {
        int[][] out = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                out[i][j] = threshold(matrix[i][j], threshold);
            }
        }
        return out;
    }

    // evolve correlated character in which
    // -character has "p" chance of being 1 if the other character is 1 and
    // -character has "1-p" chance of being 1 if the other character is 0
    public int[][] evolveCutoffCorrelatedCharacters(int[][] dichChars, double p) {
        int[][] out = new int[dichChars.length][];
        for (int i = 0; i < dichChars.length; i++) {
            out[i] = new int[dichChars[i].length];
            for (int j = 0; j < dichChars[i].length; j++) {
                if (dichChars[i][j] == 1) {
                    if (p >= rng.nextDouble()) {
                        out[i][j] = 1;
                    } else {
                        out[i][j] = 0;
                    }
                } else if ((1 - p) >= rng.nextDouble()) {
                    out[i][j] = 1;
                } else {
                    out[i][j] = 0;
                }
            }
        }
        return out;
    }

    public double[][] evolveCorrelatedCharacters(double[][] characters, double rate, int model) {
        double[][] out = new double[characters.length][];
        for (int i = 0; i < characters.length; i++) {
            out[i] = new double[characters[i].length];
            for (int j = 0; j < characters[i].length; j++) {
                if (model == BROWNIAN) {
                    out[i][j] = evolveBrownianState(characters[i][j], 1.0, rate);
                } else if (model == UNIFORM) {
                    out[i][j] = evolveUniformState(characters[i][j], 1.0, rate);
                }
            }
        }
        return out;
    }

    /** evolved a character matrix under specified model with variance = branch length * rate/2 */
    public double[][] evolveCharacterMatrix(Tree tree, int numTaxa, double rootState, double rate, int model, int numChars) {
        //boolean descending = false;
        double[][] chars = new double[numChars][];
        for (int i = 0; i < numChars; i++) {
            boolean descending = false;
            while (tree.ancestor != null) {
                tree = tree.ancestor;
            }
            chars[i] = new double[numTaxa];
//System.out.println("There are " + tree.numLeaves + " taxa. Number of characters: " + numChars);

            int posn = 0;
            if (tree.firstDaughter != null) {
                tree = tree.firstDaughter;
                if (model == UNIFORM) {
                    tree.state = evolveUniformState(tree.ancestor.state, tree.length, rate);
//System.out.println("Setting the state of " + tree.value + " with branchlength " + tree.length + " to " + tree.state);
                } else if (model == BROWNIAN) {
                    tree.state = evolveBrownianState(tree.ancestor.state, tree.length, rate);
//System.out.println("Setting the state of " + tree.value + " with branchlength " + tree.length + " to " + tree.state);
                }
                //System.out.println(tree.value + " is the first daughter of " + tree.ancestor.value);
            } else {
                //System.out.println("Tree is null");
                return null;
            }
            while (tree.ancestor != null) {
                if (tree.firstDaughter != null && !descending) {
                    //System.out.println(tree.value + " is the mother of " + tree.firstDaughter.value);
                    tree = tree.firstDaughter;
                    if (model == UNIFORM) {
                        tree.state = evolveUniformState(tree.ancestor.state, tree.length, rate);
//System.out.println("Setting the state of " + tree.value + " with branchlength " + tree.length + " to " + tree.state);
                    } else if (model == BROWNIAN) {
                        tree.state = evolveBrownianState(tree.ancestor.state, tree.length, rate);
//System.out.println("Setting the state of " + tree.value + " with branchlength " + tree.length + " to " + tree.state);
                    }
                } else if (tree.nextSister != null) {
                    //System.out.println(tree.value + " is a sister of " + tree.nextSister.value);
                    if (tree.firstDaughter == null) {
                        chars[i][posn] = tree.state;
                        posn++; //node is terminal
                    }
                    tree = tree.nextSister;
                    if (model == UNIFORM || model == UNIFORM_CUTOFF) {
                        tree.state = evolveUniformState(tree.ancestor.state, tree.length, rate);
//System.out.println("Setting the state of " + tree.value + " with branchlength " + tree.length + " to " + tree.state);
                    } else if (model == BROWNIAN || model == BROWNIAN_CUTOFF) {
                        tree.state = evolveBrownianState(tree.ancestor.state, tree.length, rate);
//System.out.println("Setting the state of " + tree.value + " with branchlength " + tree.length + " to " + tree.state);
                    }
                    descending = false;
                } else {
                    if (tree.firstDaughter == null) {
//System.out.println("i: " + i + " posn: " + posn);
                        chars[i][posn] = tree.state;
                        posn++; //node is terminal
                    }
                    descending = true;
                    tree = tree.ancestor;
                }
            }
        }
        return chars;
    }

    /** returns 1 if the continuous character state is above a threshold*/
    public int threshold(double state, double threshold) {
        if (state >= threshold) {
            return 1;
        }
        return 0;
    }

//states are integer valued from 0, 1, ..., n; values set as String
//requires a fully bifurcating tree to work
    public static Tree setDownpass(Tree tree, boolean fromRoot) {
        Vector temp = null;
        if (fromRoot) {
            tree = Tree.findRoot(tree);
            if (tree.firstDaughter == null) {
                System.out.println("There are no descendants in tree");
                return null;
            }
        }
        if (tree.numSis(tree) > 2) {
            System.out.println("Polytomies present");
            return null;
        }
        tree.downpass = new Vector();
        tree.right = new Vector();
        tree.left = new Vector();
        if (tree.nextSister != null) {
            tree.nextSister = setDownpass(tree.nextSister, false);
        }
        if (tree.firstDaughter != null) {
            tree.firstDaughter = setDownpass(tree.firstDaughter, false);

            if (tree.firstDaughter.nextSister == null) {
                System.out.println("Tree not bifurcating at every internal node");
                return null;
            }
            //tree.firstDaughter.nextSister = setDownpass(tree.firstDaughter.nextSister, false);
            //else {System.out.println("Tree not bifurcating at every internal node"); return null;}

            tree.left = tree.firstDaughter.downpass;
            tree.right = tree.firstDaughter.nextSister.downpass;
//System.out.println("left pass of " + tree.value + " is " + makeDiscreteState(tree.left,temp,temp,2));
//System.out.println("right of " + tree.value + " is " + makeDiscreteState(tree.right,temp,temp,2));

            if (tree.left == null || tree.right == null) {
                System.out.println("Downpass not set for nodes");
                return null;
            }
            if ((tree.downpass = Utilities.vIntersect(tree.right, tree.left)) == null) {
                tree.downpass = Utilities.vUnion(tree.right, tree.left);
            }
//System.out.println("\tdownpass of " + tree.value + " is " + makeDiscreteState(tree.downpass,temp,temp,2));
        } else if (tree.discreteState != null) {
            tree.downpass.add(tree.discreteState);
            tree.left.add(tree.discreteState);
            tree.right.add(tree.discreteState);
            return tree;
        } else {
            System.out.println("No character state set for terminal node.");
            return null;
        }
        return tree;
    }

    public static Tree setUppass(Tree tree, boolean fromRoot) {
        Vector temp = null;
        if (fromRoot) {
            tree = Tree.findRoot(tree);
            if (tree.firstDaughter == null) {
                System.out.println("There are no descendants in tree");
                return null;
            }
        }
        if (tree.numSis(tree) > 2) {
            System.out.println("Polytomies present");
            return null;
        }
        tree.uppass = new Vector();
        if (tree.firstDaughter != null) {
            if (tree.ancestor == null) {
            } else if (tree.ancestor.ancestor != null && tree.ancestor.uppass == null) {
                System.out.println("Ancestor uppass not set");
                return null;
            } else if (tree.ancestor.uppass == null) //immediate descendant of root
            {
                if (tree.nextSister != null && tree.ancestor.firstDaughter == tree) //first daughter
                {
                    if (tree.nextSister.downpass == null) {
                        System.out.println("downpass not set.");
                        return null;
                    }
                    tree.uppass = tree.nextSister.downpass;
//System.out.println("\tuppass of " + tree.value + " is " + makeDiscreteState(tree.uppass,temp,temp,2));
                } else if (tree.ancestor.firstDaughter.nextSister == null) {
                    System.out.println("Tree not fully bifurcating.");
                    return null;
                } else if (tree.ancestor.firstDaughter.nextSister == tree) //next sister
                {
                    if (tree.ancestor.firstDaughter.downpass == null) {
                        System.out.println("downpass not set.");
                        return null;
                    }
                    tree.uppass = tree.ancestor.firstDaughter.downpass;
//System.out.println("\tuppass of " + tree.value + " is " + makeDiscreteState(tree.uppass,temp,temp,2));
                }
            } else {
                if (tree.nextSister != null && tree.ancestor.firstDaughter == tree) {
//first daughter
                    if (tree.nextSister.downpass == null) {
                        System.out.println("downpass not set.");
                        return null;
                    }
                    if ((tree.uppass = Utilities.vIntersect(tree.nextSister.downpass, tree.ancestor.uppass)) == null) {
                        tree.uppass = Utilities.vUnion(tree.nextSister.downpass, tree.ancestor.uppass);
                    }
//System.out.println("\tuppass of " + tree.value + " is " + makeDiscreteState(tree.uppass,temp,temp,2));
                } else if (tree.ancestor.firstDaughter.nextSister == null) {
                    System.out.println("Tree not fully bifurcating.");
                    return null;
                } //next sister
                else if (tree.ancestor.firstDaughter.nextSister == tree) {
                    if (tree.ancestor.firstDaughter.downpass == null) {
                        System.out.println("downpass not set.");
                        return null;
                    }
                    if ((tree.uppass = Utilities.vIntersect(tree.ancestor.firstDaughter.downpass, tree.ancestor.uppass)) == null) {
                        tree.uppass = Utilities.vUnion(tree.ancestor.firstDaughter.downpass, tree.ancestor.uppass);
                    }
//System.out.println("\tuppass of " + tree.value + " is " + makeDiscreteState(tree.uppass,temp,temp,2));
                }
            }


            tree.firstDaughter = setUppass(tree.firstDaughter, false);
            if (tree.firstDaughter.nextSister == null) {
                System.out.println("Tree not fully bifurcating");
                return null;
            }
            if (tree.nextSister != null) {
                tree.nextSister = setUppass(tree.nextSister, false);
            }
            //tree.firstDaughter.nextSister = setUppass(tree.firstDaugther.nextSister,false);
            //else {System.out.println("Tree not fully bifurcating"); return null;}
        } else if (tree.discreteState != null) {
            if (tree.nextSister != null) {
                tree.nextSister = setUppass(tree.nextSister, false);
            }
            tree.uppass.add(tree.discreteState);
            return tree;
        } else {
            System.out.println("No character state set for terminal node.");
            return null;
        }
        return tree;
    }

    public static Tree setFinalPass(Tree t, int numStates, boolean fromRoot) {
        Vector temp = null;
        if (fromRoot) {
            t = Tree.findRoot(t);
            if (t.firstDaughter == null) {
                System.out.println("There are no descendants in tree");
                return null;
            }
        }
        if (t.discreteState != null) {
            if (t.nextSister != null) {
                t.nextSister = setFinalPass(t.nextSister, numStates, false);
            }
            return t;
        }
        if (t.firstDaughter == null) {
            System.out.println("Not all terminal nodes set");
            return null;
        }
        if (t.left == null || t.right == null) {
            System.out.println("Tree not fully bifurcating, or downpass failed.");
            return null;
        }
        if (t.uppass == null && t.ancestor == null) {
            System.out.println("Uppass failed.");
            return null;
        } else if (t.ancestor == null) {
            t.discreteState = makeDiscreteState(t.downpass, temp, temp, numStates);
        } else {
            t.discreteState = makeDiscreteState(t.left, t.right, t.uppass, numStates);
        }

//System.out.println();
//System.out.println("\t\tFinal state for " + t.value + " is " + t.discreteState);
//System.out.println("\t\tdownpass for " + t.value + " is " + t.downpass);
//System.out.println("\t\tuppass for " + t.value + " is " + t.uppass);
//System.out.println("\t\tleft for " + t.value + " is " + t.left);
//System.out.println("\t\tright for " + t.value + " is " + t.right);

        if (t.nextSister != null) {
            t.nextSister = setFinalPass(t.nextSister, numStates, false);
        }
        if (t.firstDaughter != null) {
            t.firstDaughter = setFinalPass(t.firstDaughter, numStates, false);
        }
        return t;
    }

//requires:
//	fully bifurcating tree (no polytomies, no unbranched nodes)
//	no ambiguity or polymorphism in terminal node state
    public static Tree reconstructParsimonyStates(Tree t, int numStates, String[] taxa, String[] stateList) {
        t = assignDiscreteTerminalStates(t, taxa, stateList, 0);
        t = setDownpass(t, true);
        t = setUppass(t, true);
        t = setFinalPass(t, numStates, true);
        return t;
    }

    public static String[] simulateStates(double freqState1, int numStates, Random r) {
        String[] out = new String[numStates];
        for (int i = 0; i < numStates; i++) {
            double rnd = r.nextDouble();
            if (rnd < freqState1) {
                out[i] = "1";
            } else {
                out[i] = "0";
            }
        }
        return out;
    }

    public static boolean isState(String state, String derivedState) {
        if (state == null) {
            return false;
        }
        state = state.replaceAll("\\{", "");
        state = state.replaceAll("\\}", "");
        String[] states = state.split(",");
        for (int i = 0; i < states.length; i++) {
            if (states[i].equals(derivedState)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEquivocal(String state) {
        if (state == null) {
            return false;
        }
        String[] states = state.split(",");
        if (states.length > 1) {
            return true;
        }
        return false;
    }

    public static boolean origin(String ancestralState, String derivedState, String toMatch, boolean cntEquiv) {
        if (ancestralState == null || derivedState == null || toMatch == null) {
            return false;
        }
//matches if ancestral state is not 'toMatch' and derivedState is 'toMatch'

//cntEquiv
//	equivA equivD, derived matches, anc doesn't, then count
//	equivA !equivD, derived matches, anc doesn't, then count
//	!equivA equivD, derived matches, anc doesn't, then count
//	!equivA !equivD, derived matches, anc doesn't, then count
//	summary: cntEquiv, derived matches, anc doesn't, then count
//!cntEquiv
//	equivA equivD, never count
//	equivA !equivD, derived matches, anc matches, then count
//	equivA !equivD, derived matches, anc doesn't, then count
//	!equivA equivD, never count
//	!equivA !equivD, derived matches, anc doesn't, then count

        String state;
        boolean ancMatches = isState(ancestralState, toMatch);
        boolean derMatches = isState(derivedState, toMatch);
        boolean ancEquiv = isEquivocal(ancestralState);
        boolean derEquiv = isEquivocal(derivedState);

        if (cntEquiv && derMatches && !ancMatches) {
            return true;
        }
        if (!cntEquiv) {
            if (ancEquiv && !derEquiv && ancMatches && derMatches) {
                return true;
            }
            if (ancEquiv && !derEquiv && !ancMatches && derMatches) {
                return true;
            }
            if (!ancEquiv && !derEquiv && !ancMatches && derMatches) {
                return true;
            }
        }
        return false;
    }

//public static boolean derivedWithin(String ancestralState, String derivedState, String toMatchAnc, String toMatchDer, boolean cntEquiv)
    //{
////matches if ancestral state is 'toMatchAnc' and current node state is 'toMatchDer'
    //if(isState(ancestralState, toMatchAnc, !cntEquiv) && isState(derivedState, toMatchDer, cntEquiv)) return true;
    //return false;
    //}
//counts the number of times the derived state is derived within a lineage with ancestral state 'ancestral'
//	equivocal states are set to the ancestral state counted if 'cntEquiv' is true (what is wanted for analyses)
//public static int countReversions(Tree t, String ancestral, String derived, boolean cntEquiv)
    //{
    //int tot = 0;
    //boolean descending = false;
    //while(t.ancestor!=null) t=t.ancestor;
    //if(t.firstDaughter!=null)
    //{
    //t=t.firstDaughter;
    //if(derivedWithin(t.ancestor.discreteState,t.discreteState,ancestral,derived,cntEquiv)) tot++;
    //}
    //else return 0;
    //while(t.ancestor!=null)
    //{
    //if(t.firstDaughter!=null && !descending)
    //{
    //t=t.firstDaughter;
    //if(derivedWithin(t.ancestor.discreteState,t.discreteState,ancestral,derived,cntEquiv)) tot++;
    //}
    //else if(t.nextSister!=null)
    //{
    //t=t.nextSister;
    //if(derivedWithin(t.ancestor.discreteState,t.discreteState,ancestral,derived,cntEquiv)) tot++;
    //descending=false;
    //}
    //else
    //{
    //descending = true;
    //t=t.ancestor;
    //}
    //}
    //if(t.ancestor==null)
    //return tot;
    //else
    //return 0;
    //}
//counts the number of separate origins of 'state' 
//	equivocal ancestral states are counted if 'cntEquiv' is true (what is wanted for analyses)
//Make cntRoot false if only reversions are to be counted
    public static int countOrigins(Tree t, String state, boolean cntEquiv, boolean cntRoot) {
        int tot = 0;
        boolean descending = false;
        while (t.ancestor != null) {
            t = t.ancestor;
        }
//System.out.println("The state for " + t.value + " is " + t.discreteState);
        if (cntRoot) {
            if (isState(t.discreteState, state) && cntEquiv) {
                tot++;
            } else if (isState(t.discreteState, state) && !isEquivocal(t.discreteState) && !cntEquiv) {
                tot++;
            }
        }


        if (t.firstDaughter != null) {
            t = t.firstDaughter;
            if (origin(t.ancestor.discreteState, t.discreteState, state, cntEquiv)) {
                tot++;
            }
        } else {
            return 0;
        }
        while (t.ancestor != null) {
            if (t.firstDaughter != null && !descending) {
                t = t.firstDaughter;
                if (origin(t.ancestor.discreteState, t.discreteState, state, cntEquiv)) {
                    tot++;
                }
            } else if (t.nextSister != null) {
                t = t.nextSister;
                if (origin(t.ancestor.discreteState, t.discreteState, state, cntEquiv)) {
                    tot++;
                }
                descending = false;
            } else {
                descending = true;
                t = t.ancestor;
            }
        }
        if (t.ancestor == null) {
            return tot;
        } else {
            return 0;
        }
    }

    public static String makeDiscreteState(Vector v1, Vector v2, Vector v3, int numStates) {

        try {
            String out = new String();
            if (v1 == null) {
                return null;
            }
            int size1 = v1.size();

            if (v2 == null && v3 == null) {
                out = "{";
                boolean first = true;
                for (int i = 0; i < size1; i++) {
                    if (((String) v1.elementAt(i)).length() > 0) {
                        if (!first) {
                            out += ",";
                        }
                        out += (String) v1.elementAt(i);
                        first = false;
                    }
                }
                out += "}";
                return out;
            }

            if (v2 == null || v3 == null) {
                return null;
            }

            int size2 = v2.size();
            int size3 = v3.size();

            int[] statesCnt = new int[numStates];
            int max = 0;
            for (int i = 0; i < size1; i++) {
                if (((String) v1.elementAt(i)).length() > 0) {
                    try {
                        int t = Integer.parseInt((String) v1.elementAt(i));
                        statesCnt[t]++;
                        if (statesCnt[t] > max) {
                            max = statesCnt[t];
                        }
                    } catch (Exception e) {
                        System.out.println("NUMBER FORMAT EXCEPTION!!!");
                    }
                }
            }
            for (int i = 0; i < size2; i++) {
                if (((String) v2.elementAt(i)).length() > 0) {
                    try {
                        int t = Integer.parseInt((String) v2.elementAt(i));
                        statesCnt[t]++;
                        if (statesCnt[t] > max) {
                            max = statesCnt[t];
                        }
                    } catch (Exception e) {
                        System.out.println("NUMBER FORMAT EXCEPTION!!!");
                    }
                }
            }
            for (int i = 0; i < size3; i++) {
                if (((String) v3.elementAt(i)).length() > 0) {
                    try {
                        int t = Integer.parseInt((String) v3.elementAt(i));
                        statesCnt[t]++;
                        if (statesCnt[t] > max) {
                            max = statesCnt[t];
                        }
                    } catch (Exception e) {
                        System.out.println("NUMBER FORMAT EXCEPTION!!!");
                    }
                }
            }
            out = "{";
            boolean first = true;
            for (int i = 0; i < numStates; i++) {
                if (statesCnt[i] == max) {
                    if (!first) {
                        out += ",";
                    }
                    out += i;
                    first = false;
                }
            }
            out += "}";
            return out;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//this method and the assignDiscreteTerminalStates methods assume the termTaxLables and the states are in the same order.
//That is, termTaxLabel[i] holds the name of a taxon whose state is held by states[i]
    public static Tree assignContinuousTerminalStates(Tree t, String[] termTaxLabels, double[] states, int character) {
        //System.out.println("Assigning continuous terminal states");
        if (states == null) {
            return t;
        }
        //System.out.println("a");
        boolean descending = false;
        while (t.ancestor != null) {
            t = t.ancestor;
        }
        if (t.firstDaughter != null) {
            t = t.firstDaughter;
        } else {
            int l = Tree.matchTaxon(t.value, termTaxLabels);
            if (l >= 0) {
//System.out.println("Setting " + t.value + " to " + states[l]);
                if (character == 1) {
                    t.cont1 = states[l];
                } else if (character == 2) {
                    t.cont2 = states[l];
                } else {
                    t.state = states[l];
                }
            }
            return t;
        }
        while (t.ancestor != null) {
            if (t.firstDaughter != null && !descending) {
                t = t.firstDaughter;
            } else if (t.nextSister != null) {
                if (t.firstDaughter == null) {
                    int l = Tree.matchTaxon(t.value, termTaxLabels);
                    if (l >= 0) {
//System.out.println("Setting " + t.value + " to " + states[l]);
                        if (character == 1) {
                            t.cont1 = states[l];
                        } else if (character == 2) {
                            t.cont2 = states[l];
                        } else {
                            t.state = states[l];
                        }
                    }
                }
                t = t.nextSister;
                descending = false;
            } else {
                if (t.firstDaughter == null) {
                    int l = Tree.matchTaxon(t.value, termTaxLabels);
                    if (l >= 0) {
//System.out.println("Setting " + t.value + " to " + states[l]);
                        if (character == 1) {
                            t.cont1 = states[l];
                        } else if (character == 2) {
                            t.cont2 = states[l];
                        } else {
                            t.state = states[l];
                        }
                    }
                }
                descending = true;
                t = t.ancestor;
            }
        }
        if (t.ancestor == null) {
            return t;
        } else {
            return null;
        }
    }

    public static Tree assignDiscreteTerminalStates(Tree t, String[] termTaxLabels, String[] states, int character) {
        boolean descending = false;
        while (t.ancestor != null) {
            t = t.ancestor;
        }
        if (t.firstDaughter != null) {
            t = t.firstDaughter;
        } else {
            int l = Tree.matchTaxon(t.value, termTaxLabels);
            if (l >= 0) {
                //System.out.println(t.value + "\t" + states[l]);
                if (character == 1) {
                    t.disc1 = states[l];
                } else if (character == 2) {
                    t.disc1 = states[l];
                } else {
                    t.discreteState = states[l];
                }
            }
            return t;
        }
        while (t.ancestor != null) {
            if (t.firstDaughter != null && !descending) {
                t = t.firstDaughter;
            } else if (t.nextSister != null) {
                if (t.firstDaughter == null) {
                    int l = Tree.matchTaxon(t.value, termTaxLabels);
                    //if(l>=0) t.discreteState = states[l];
                    if (l >= 0) {
                        //System.out.println(t.value + "\t" + states[l]);
                        if (character == 1) {
                            t.disc1 = states[l];
                        } else if (character == 2) {
                            t.disc1 = states[l];
                        } else {
                            t.discreteState = states[l];
                        }
                    }
                }
                t = t.nextSister;
                descending = false;
            } else {
                if (t.firstDaughter == null) {
                    int l = Tree.matchTaxon(t.value, termTaxLabels);
                    if (l >= 0) {
                        //System.out.println(t.value + "\t" + states[l]);
                        if (character == 1) {
                            t.disc1 = states[l];
                        } else if (character == 2) {
                            t.disc1 = states[l];
                        } else {
                            t.discreteState = states[l];
                        }
                    }
                }
                descending = true;
                t = t.ancestor;
            }
        }
        if (t.ancestor == null) {
            return t;
        } else {
            return null;
        }
    }

    public static void printStates(Tree t) {
        boolean descending = false;
        while (t.ancestor != null) {
            t = t.ancestor;
        }
        if (t.firstDaughter != null) {
            Tree.describeNode(t);
            t = t.firstDaughter;
            Tree.describeNode(t);
        } else {
            Tree.describeNode(t);
            return;
        }
        while (t.ancestor != null) {
            if (t.firstDaughter != null && !descending) {
                t = t.firstDaughter;
                Tree.describeNode(t);
            } else if (t.nextSister != null) {
                t = t.nextSister;
                Tree.describeNode(t);
                descending = false;
            } else {
                descending = true;
                t = t.ancestor;
            }
        }
        return;
    }

//taxa are rows and characters are columns in charM
    public static boolean charactersToNexusFile(String file, String[] taxa, double[][] charM) {
        try {
            FileWriter fw = new FileWriter(file);
            fw.write("#NEXUS\n\nBegin data;\n");
            fw.write("Dimensions ntax=" + taxa.length + " nchar=" + charM[0].length + ";\n");
            fw.write("Format datatype=standard gap=- missing=? symbols=\" 0 1 2 3 4 5 6 7 8 9\";\n");
            fw.write("matrix\n");
            for (int i = 0; i < taxa.length; i++) {
                fw.write(taxa[i] + "    ");
                for (int j = 0; j < charM[0].length; j++) {
                    fw.write(Integer.toString((int) charM[i][j]));
                }
                fw.write("\n");
            }
            fw.write(";\nend;\n");
            fw.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

//returns a hash of string arrays referenced by the name of the variable
    public static Hashtable characterHashFromTabText(String file, int numTax) {
        String[][] chars = new String[numTax][3];
        Hashtable out = new Hashtable();
        BufferedReader d = null;
        try {
//System.out.println("a");
            d = new BufferedReader(new FileReader(new File(file)));
            int cnt = 0;
            String line;
            String header = d.readLine();
            String[] columns = header.split("\\t");
//System.out.println("b");
            while ((line = d.readLine()) != null) {
                String[] temp = line.split("\\t");
                if (cnt == 0) {
                    chars = new String[numTax][temp.length];
                }
                System.arraycopy(temp, 0, chars[cnt], 0, temp.length);
                cnt++;
            }
            d.close();
//System.out.println("c");
            if (cnt != numTax) {
                return null;
            }
//System.out.println("d");
            for (int i = 0; i < columns.length; i++) {
//System.out.println("1");
                String[] temp1 = new String[numTax];
//System.out.println("2");
                for (int j = 0; j < numTax; j++) {
//System.out.println("\t" + j);
                    temp1[j] = chars[j][i];
                }
//System.out.println("3");
                out.put(columns[i], temp1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Problem in characterHashFromTabText");
        }
        return out;
    }

//returns a character matrix with the first column having the species labels
    public static String[][] charactersFromTabText(String file, int numTax) {
        String[][] chars = new String[numTax][3];
        BufferedReader d = null;
        try {
            d = new BufferedReader(new FileReader(new File(file)));
            int cnt = 0;
            String line;
            while ((line = d.readLine()) != null) {
                String[] temp = line.split("\\t");
                if (cnt == 0) {
                    chars = new String[numTax][temp.length];
                }
                System.arraycopy(temp, 0, chars[cnt], 0, temp.length);
                cnt++;
            }
            d.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chars;
    }

    public static char[][] charactersFromNexusFile(String file, String[] taxa) {
        try {
            BufferedReader d = new BufferedReader(new FileReader(new File(file)));
            char[][] chars = null;
            boolean inCharacters = false;
            boolean inMatrix = false;
            boolean haveCharNum = false;
            boolean initialized = false;
            int numTax = taxa.length;
            int numChar = 0;
            int totAdded = 0;
            int psn = 0;
            int t = 0;
            String line;
            while ((line = d.readLine()) != null) {
                if (inCharacters && inMatrix && haveCharNum && line.matches(".*;.*")) {
//System.out.println("Closing on " + line);
                    d.close();
                    return chars;
                }

                if (inCharacters && inMatrix && haveCharNum) {
//System.out.println("There are " + numTax + " taxa and " + numChar + " characters.");
                    if (!initialized) {
                        chars = new char[numTax][numChar];
                        initialized = true;
                    }
                    if (line.length() > 0) {
                        char[] tempc;
                        if (line.matches(".*\\s+.*")) {
//System.out.println(line);
                            psn = 0;
                            t = 0;
                            line = line.replaceAll("^\\s+", "");
                            String[] stuff = line.split("\\s+");
                            for (int i = 0; i < numTax; i++) {
                                //if(stuff[0].matches(".*" + taxa[i] + ".*"))
                                if (stuff[0].equals(taxa[i])) {
                                    tempc = stuff[1].replaceAll("\\s", "").toCharArray();
                                    //chars[i] = stuff[1].replaceAll("\\s","").toCharArray();
                                    System.arraycopy(tempc, 0, chars[i], 0, tempc.length);
//System.out.println(stuff[0] + "MATCH'" + stuff[1] + "'" + chars[i][0]) ;
                                    t = i;
                                    i = numTax;
                                    psn = tempc.length;
                                }
                            }
                        } else {
                            tempc = line.toCharArray();
                            System.arraycopy(tempc, 0, chars[t], psn, tempc.length);
                            psn += tempc.length;
                        }
//System.out.println(stuff[0] + "MATCH1'" + stuff[1] + "'" + chars[t][0] + " at index " + t) ;
//System.out.println("There are " + chars[i].length + " characters in '" + stuff[1] + "'");
//System.out.println("The first character of taxon " + totAdded + " is " + chars[totAdded][0]);
                        //System.arraycopy(tchars,0,chars[totAdded],0,tchars.length);
                        //System.arraycopy(tchars,1,chars[totAdded],0,tchars.length-1);
                    }
                }

                if (line.toLowerCase().matches(".*begin characters;.*")) {
                    inCharacters = true;
                } else if (line.toLowerCase().matches(".*dimensions.*")) {
                    line = line.replaceAll(";", "");
                    String[] stuff = line.split("\\s+");
                    for (int i = 0; i < stuff.length; i++) {
                        if (stuff[i].toLowerCase().matches(".*nchar.*")) {
                            stuff = stuff[i].split("\\s*=\\s*");
                            numChar = Integer.parseInt(stuff[1]);
                            haveCharNum = true;
                            System.out.println("The number of characters is " + numChar);
                        }
                    }
                } else if (line.toLowerCase().matches(".*matrix.*")) {
                    inMatrix = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean makeRandomData(int i, Tree tree, String outputFile, String[] C1, String[] C2, String c1, String c2) {
//System.out.println("Making random data");
//c1, c2 holds whether characters are dichotomous or continuous, either "dich" or "cont" for dichotomous or continuous
        //The first character is now ignored, and filled by the values of the second character... speed things up...
        try {
            if (tree == null) {
                System.out.println("Tree on which to evolve random data is null. Quitting.");
                System.exit(0);
            }
            //else { System.out.println("The tree is " + Tree.treeToStringTimeOrder(tree,false)); }
            int model = CharacterEvolution.BROWNIAN;

            int[][] cm1 = null, cm2 = null;
            double[] cc1 = null, cc2 = null;
            String[] taxa = Tree.getTaxonLabels(tree);
            if (taxa == null) {
                System.out.println("There are no taxa in the provided tree when making random data. Quitting.");
                System.exit(0);
            }
//System.out.println("There are " + taxa.length + " taxa.");
            //randomly evolve data on tree

            double[][] characters0 = evolveCharacterMatrix(tree, C1.length, 0, 1, model, 1);
            double[][] characters1 = evolveCharacterMatrix(tree, C1.length, 0, 1, model, 1);
            //scale data appropriately
            double[] temp1 = Utilities.StringArrayToDoubleArray(C1);
            double[] temp2 = Utilities.StringArrayToDoubleArray(C2);
            if (c1.matches("dich")) {
                double fc1 = Utilities.dichCharFreq(temp1);
                double th1 = CEMethods.findThreshold(characters0[0], fc1);
                cm1 = thresholdMatrix(characters0, th1);
            } else if (c1.matches("cont")) {
                double max = Utilities.findMax(temp1);
                double min = Utilities.findMin(temp1);
                cc1 = CharacterEvolution.scaleStates(characters0[0], min, max);
            }
            if (c2.matches("dich")) {
                System.out.println("Dichotomous character in the second place should never be reached");
                System.exit(0);
//System.out.println("Setting c2 dich");
                //double fc2 = Utilities.dichCharFreq(temp2);
                //double th2 = CEMethods.findThreshold(characters1[0], fc2);
                //cm2 = thresholdMatrix(characters1, th2);
            } //scaling continuous characters not really necessary... all that is measured is whether contrast is positive or negative
            else if (c2.matches("cont")) {
//System.out.println("Setting c2 cont");
                double max = Utilities.findMax(temp2);
                double min = Utilities.findMin(temp2);
                cc2 = CharacterEvolution.scaleStates(characters1[0], min, max);
            }
            printData(i, outputFile, taxa, c1, c2, cm1, cm2, cc1, cc2, true);
        } catch (Exception e) {
            System.out.println("Exception caught: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

//Generates a file with randomly evolved characters (brownian motion)
//takes:
// a file with trees,
// a file with two characters for each taxon,
// two strings specifying whether characters are dichotomous (dich) or continuous (cont)
// the number of trees to read in from the tree file
    public void makeRandomData(String nexTreeFile, String tabTextCharsFile, String c1, String c2, int numTrees) {
//c1, c2 holds whether characters are dichotomous or continuous, either "dich" or "cont" for dichotomous or continuous
        try {
            int model = CharacterEvolution.BROWNIAN;

//read in trees
            Tree[] trees = Tree.treesFromNexusFile(new File(nexTreeFile), numTrees);
            String[] taxa = Tree.getTaxonLabels(trees[0]);
//read in character state data
            String[][] data = CharacterEvolution.charactersFromTabText(tabTextCharsFile, taxa.length);
            String[] names = new String[taxa.length];
            double[] cf0 = new double[taxa.length];
            double[] cf1 = new double[taxa.length];
            for (int i = 0; i < taxa.length; i++) {
                names[i] = data[i][0];
                cf0[i] = Double.parseDouble(data[i][1]);
                cf1[i] = Double.parseDouble(data[i][2]);
            }
//for each tree
            for (int i = 0; i < trees.length; i++) {
                taxa = Tree.getTaxonLabels(trees[i]);
                System.out.println("\n\nReplicate " + i);
                int[][] cm1 = null, cm2 = null;
                double[] cc1 = null, cc2 = null;
                //randomly evolve data on tree
                double[][] characters0 = evolveCharacterMatrix(trees[i], taxa.length, 0, 1, model, 1);
                double[][] characters1 = evolveCharacterMatrix(trees[i], taxa.length, 0, 1, model, 1);
                //scale data appropriately
                if (c1.matches("dich")) {
                    double fc1 = Utilities.dichCharFreq(cf0);
                    System.out.println("The frequency is " + fc1);
                    double th1 = CEMethods.findThreshold(characters0[0], fc1);
                    cm1 = thresholdMatrix(characters0, th1);
                    System.out.println("The frequency is " + fc1 + ". The threshold is " + th1);
                } else if (c1.matches("cont")) {
                    double max = Utilities.findMax(cf0);
                    double min = Utilities.findMin(cf0);
                    cc1 = CharacterEvolution.scaleStates(characters0[0], min, max);
                    System.out.println("C1 Min: " + min + " max: " + max + " New min: " + Utilities.findMin(cc1) + " New max: " + Utilities.findMax(cc1));
                }
                if (c2.matches("dich")) {
                    double fc2 = Utilities.dichCharFreq(cf1);
                    System.out.println("The frequency is " + fc2);
                    double th2 = CEMethods.findThreshold(characters1[0], fc2);
                    cm2 = thresholdMatrix(characters1, th2);
                    System.out.println("The frequency is " + fc2 + ". The threshold is " + th2);
                } else if (c2.matches("cont")) {
                    double max = Utilities.findMax(cf1);
                    double min = Utilities.findMin(cf1);
                    cc2 = CharacterEvolution.scaleStates(characters1[0], min, max);
                    System.out.println("C2 Min: " + min + " max: " + max + " New min: " + Utilities.findMin(cc2) + " New max: " + Utilities.findMax(cc2));
                }
                //print data to datafile
                printData(i, tabTextCharsFile, taxa, c1, c2, cm1, cm2, cc1, cc2);
            }
        } catch (Exception e) {
            System.out.println("Exception caught: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void printData(int number, String datafile, String[] taxa, String c1, String c2, int[][] cm1, int[][] cm2, double[] cc1, double[] cc2, boolean printHeader) {
        try {
            FileWriter fw = new FileWriter(datafile + "_R_" + number);
            if (printHeader) {
                fw.write("species\tchar1\tchar2\n");
            }
            for (int k = 0; k < taxa.length; k++) {
                String out = taxa[k];
                if (c1.matches("dich")) {
                    out += "\t" + cm1[0][k];
                } else if (c1.matches("cont")) {
                    out += "\t" + cc1[k];
                }
                if (c2.matches("dich")) {
                    out += "\t" + cm2[0][k];
                } else if (c2.matches("cont")) {
                    out += "\t" + cc2[k];
                }
                out += "\n";
                fw.write(out);
                //System.out.print(out);
            }
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printData(int number, String datafile, String[] taxa, String c1, String c2, int[][] cm1, int[][] cm2, double[] cc1, double[] cc2) {
        try {
            FileWriter fw = new FileWriter(datafile + "_R_" + number);
            for (int k = 0; k < taxa.length; k++) {
                String out = taxa[k];
                if (c1.matches("dich")) {
                    out += "\t" + cm1[0][k];
                } else if (c1.matches("cont")) {
                    out += "\t" + cc1[k];
                }
                if (c2.matches("dich")) {
                    out += "\t" + cm2[0][k];
                } else if (c2.matches("cont")) {
                    out += "\t" + cc2[k];
                }
                out += "\n";
                fw.write(out);
                //System.out.print(out);
            }
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
