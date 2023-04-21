    public static void benchmarkRealForward_2D_input_1D(int init_exp) {
        int[] sizes = new int[nsize];
        double[] times = new double[nsize];
        float[] x;
        for (int i = 0; i < nsize; i++) {
            int exponent = init_exp + i;
            int N = (int) Math.pow(2, exponent);
            sizes[i] = N;
            System.out.println("Real forward FFT 2D (input 1D) of size 2^" + exponent + " x 2^" + exponent);
            FloatFft2D fft2 = new FloatFft2D(N, N);
            x = new float[N * 2 * N];
            if (doWarmup) {
                IoUtils.fillMatrix_2D(N, N, x);
                fft2.realForwardFull(x);
                IoUtils.fillMatrix_2D(N, N, x);
                fft2.realForwardFull(x);
            }
            float av_time = 0;
            long elapsedTime = 0;
            for (int j = 0; j < niter; j++) {
                IoUtils.fillMatrix_2D(N, N, x);
                elapsedTime = System.nanoTime();
                fft2.realForwardFull(x);
                elapsedTime = System.nanoTime() - elapsedTime;
                av_time = av_time + elapsedTime;
            }
            times[i] = (double) av_time / 1000000.0 / (double) niter;
            System.out.println("\tAverage execution time: " + String.format("%.2f", av_time / 1000000.0 / (float) niter) + " msec");
            x = null;
            fft2 = null;
            System.gc();
        }
        IoUtils.writeFFTBenchmarkResultsToFile("benchmarkFloatRealForwardFFT_2D_input_1D.txt", nthread, niter, doWarmup, doScaling, sizes, times);
    }
