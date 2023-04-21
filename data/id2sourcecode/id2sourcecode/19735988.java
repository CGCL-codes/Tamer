    public static void benchmarkForward_1D(int init_exp) {
        int[] sizes = new int[nsize];
        double[] times = new double[nsize];
        double[] x;
        for (int i = 0; i < nsize; i++) {
            int exponent = init_exp + i;
            int N = (int) Math.pow(2, exponent);
            sizes[i] = N;
            System.out.println("Forward DST 1D of size 2^" + exponent);
            DoubleDst1D dst = new DoubleDst1D(N);
            x = new double[N];
            if (doWarmup) {
                IoUtils.fillMatrix_1D(N, x);
                dst.forward(x, false);
                IoUtils.fillMatrix_1D(N, x);
                dst.forward(x, false);
            }
            double av_time = 0;
            long elapsedTime = 0;
            for (int j = 0; j < niter; j++) {
                IoUtils.fillMatrix_1D(N, x);
                elapsedTime = System.nanoTime();
                dst.forward(x, false);
                elapsedTime = System.nanoTime() - elapsedTime;
                av_time = av_time + elapsedTime;
            }
            times[i] = (double) av_time / 1000000.0 / (double) niter;
            System.out.println("Average execution time: " + String.format("%.2f", av_time / 1000000.0 / (double) niter) + " msec");
            x = null;
            dst = null;
            System.gc();
        }
        IoUtils.writeFFTBenchmarkResultsToFile("benchmarkDoubleForwardDST_1D.txt", nthread, niter, doWarmup, doScaling, sizes, times);
    }
