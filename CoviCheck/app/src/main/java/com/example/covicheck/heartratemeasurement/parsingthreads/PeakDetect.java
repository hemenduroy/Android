package com.example.covicheck.heartratemeasurement.parsingthreads;

import java.util.ArrayList;
import java.util.Collections;
import java.lang.Math;

public class PeakDetect implements Runnable{
    private final ArrayList<Double> RawECG;
    private final ArrayList<Double> Rpeak_values = new ArrayList<>();
    public PeakDetect(ArrayList<Double> RawECG) {
        this.RawECG=RawECG;
    }

    public ArrayList<Double> RPeakDetection() {
        return Rpeak_values;
    }

    @Override
    public void run() {
        //System.out.println("RawECG size " + RawECG.size());

        ArrayList<Double> LPFECG = new ArrayList<>();
        ArrayList<Double> HPFECG = new ArrayList<>();

        for (int i=12; i<=44; i++) {
            int index=i-12;
            if(index<2)
                LPFECG.add(index, (0.5*(RawECG.get(i) - 2*RawECG.get(i-6) + RawECG.get(i - 12))));
            else
                LPFECG.add(index, (0.5*(2*LPFECG.get(index - 1) - LPFECG.get(index - 2) + RawECG.get(i) - 2*RawECG.get(i - 6) + RawECG.get(i - 12))));
            //System.out.println("LPFECG : " + LPFECG.get(index));
        }

        for (int i = 45; i<RawECG.size(); i++) {
            int index = i - 12;
            int index2 = i - 45;
            LPFECG.add(index, (0.5 * (2 * LPFECG.get(index - 1) - LPFECG.get(index - 2) + RawECG.get(i) - 2 * RawECG.get(i - 6) + RawECG.get(i - 12))));
            if (index2 < 1)
                HPFECG.add(index2,(1.0 / 32.0) * (32.0 * LPFECG.get(index - 16) + (LPFECG.get(index) - LPFECG.get(index - 32))));
            else
                HPFECG.add(index2,(1.0 / 32.0) * (32.0 * LPFECG.get(index - 16) - (HPFECG.get(index2 - 1) + LPFECG.get(index) - LPFECG.get(index - 32))));
            //System.out.println("HPFECG : " + HPFECG.get(index2));
        }

        double BaseLine=0; //average
        for (int i=0; i<HPFECG.size();i++)
            BaseLine+=HPFECG.get(i);
        BaseLine/=HPFECG.size();
        //System.out.println("BaseLine : " + BaseLine);

        double DynamicRangeUp = (-1* Collections.min(HPFECG)) - BaseLine;
        double DynamicRangeDown = BaseLine - (-1*Collections.max(HPFECG));
        double thresholdUp = 0.002*DynamicRangeUp;
        double thresholdR = 0.5*DynamicRangeUp;
        double thresholdDown = 0.000002*DynamicRangeDown;
        //double thresholdQ = 0.1*DynamicRangeDown;

        double up=1.0,maximum=-1000.0,minimum=1000.0,PeakType = 0.0; //Speak = 0.0,Rpeak = 0.0

        double PreviousPeak=-1*HPFECG.get(0);
        ArrayList<Integer> Rpeak_index = new ArrayList<>();
        //ArrayList<Double> Qpeak_index = new ArrayList<>();
        //ArrayList<Double> Speak_index = new ArrayList<>();
        ArrayList<Integer> peak_index = new ArrayList<>();

        int i=0,k=-1,possiblePeak=0;
        while (i<HPFECG.size()) {
            if ((-1 * HPFECG.get(i)) > maximum)
                maximum = -1 * HPFECG.get(i);
            if ((-1 * HPFECG.get(i)) < minimum)
                minimum = -1 * HPFECG.get(i);
            if (up == 1) {
                if ((-1 * HPFECG.get(i)) < maximum) {
                    if (possiblePeak == 0)
                        possiblePeak = i;
                    if ((-1 * HPFECG.get(i)) < (maximum - thresholdUp)) {
                        k++;
                        peak_index.add(k, (possiblePeak - 1));
                        minimum = -1 * HPFECG.get(i);
                        up = 0.0;
                        possiblePeak = 0;
                        if (PeakType == 0)
                            if (-1 * (HPFECG.get(peak_index.get(k))) > BaseLine + thresholdR) {
                                //Rpeak++;
                                Rpeak_index.add(peak_index.get(k));
                                PreviousPeak = -1 * HPFECG.get(peak_index.get(k));
                            } else {
                                if ((Math.abs((-1 * HPFECG.get(peak_index.get(k)) - PreviousPeak) / PreviousPeak) > 1.5) && (-1 * HPFECG.get(peak_index.get(k)) > BaseLine + thresholdR)) {
                                    //Rpeak++;
                                    Rpeak_index.add(peak_index.get(k));//USING Rpeak instead of Rpeak_index
                                    PreviousPeak = -1 * HPFECG.get(peak_index.get(k));
                                    PeakType = 2.0;
                                }
                            }//end else
                    }//end if1
                }//end if2
            }
            else {
                if (-1 * HPFECG.get(i) > minimum) {
                    if (possiblePeak == 0)
                        possiblePeak = i;
                    if (-1 * HPFECG.get(i) > (minimum + thresholdDown)) {
                        k++;
                        peak_index.add(k, (possiblePeak - 1));
                        maximum = -1 * HPFECG.get(i);

                        up = 1.0;
                        possiblePeak = 0;
                    }//end if
                }
            }
            i++;
        }//while end
        //ArrayList<Double> peak_values = new ArrayList<>();
        //for(int c=0; c<peak_index.size();c++)
        //    peak_values.add((-1*HPFECG.get(peak_index.get(c))));
        //System.out.println("Rpeak_values : ");
        if(Rpeak_index.size()>0) {
            for (int d = 0; d < Rpeak_index.size(); d++) {
                Rpeak_values.add(RawECG.get(Rpeak_index.get(d)));
                //System.out.println(RawECG.get(Rpeak_index.get(d)));
            }
        }
    }
}
