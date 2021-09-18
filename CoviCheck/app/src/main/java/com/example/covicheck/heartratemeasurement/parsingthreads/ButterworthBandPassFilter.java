package com.example.covicheck.heartratemeasurement.parsingthreads;


import com.github.psambit9791.jdsp.filter.Butterworth;

public class ButterworthBandPassFilter implements Runnable {

    private final double[] signal;
    private double[] result;

    public ButterworthBandPassFilter (double[] signal) {
        this.signal = signal;
    }

    @Override
    public void run () {

        int Fs = 30; //Sampling Frequency in Hz
        int order = 4; //order of the filter
        int lowCutOff = 12; //Lower Cut-off Frequency
        int highCutOff = 18; //Higher Cut-off Frequency
        Butterworth flt = new Butterworth(signal, Fs); //signal is of type double[]
        result = flt.bandPassFilter(order, lowCutOff, highCutOff); //get the result after filtering
    }

    public double[] returnSignalResult() {
        return signal; //filter OFF
        //return result; //filter ON
    }

}
