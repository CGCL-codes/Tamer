    public static void benchmarkComplexForward_1D(int init_exp) {
        int[] sizes = new int[nsize];
        double[] times = new double[nsize];
        float[] x;
        for (int i = 0; i < nsize; i++) {
            int exponent = init_exp + i;
            int N = (int) Math.pow(2, exponent);
            sizes[i] = N;
            System.out.println("Complex forward FFT 1D of size 2^" + exponent);
            FloatFft1D fft = new FloatFft1D(N);
            x = new float[2 * N];
            if (doWarmup) {
                IoUtils.fillMatrix_1D(2 * N, x);
                fft.complexForward(x);
                IoUtils.fillMatrix_1D(2 * N, x);
                fft.complexForward(x);
            }
            float av_time = 0;
            long elapsedTime = 0;
            for (int j = 0; j < niter; j++) {
                IoUtils.fillMatrix_1D(2 * N, x);
                elapsedTime = System.nanoTime();
                fft.complexForward(x);
                elapsedTime = System.nanoTime() - elapsedTime;
                av_time = av_time + elapsedTime;
            }
            times[i] = (double) av_time / 1000000.0 / (double) niter;
            System.out.println("\tAverage execution time: " + String.format("%.2f", av_time / 1000000.0 / (float) niter) + " msec");
            x = null;
            fft = null;
            System.gc();
        }
        IoUtils.writeFFTBenchmarkResultsToFile("benchmarkFloatComplexForwardFFT_1D.txt", nthread, niter, doWarmup, doScaling, sizes, times);
    }
