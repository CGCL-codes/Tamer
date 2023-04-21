    public static void benchmarkForward_3D_input_3D(int init_exp) {
        int[] sizes = new int[nsize];
        double[] times = new double[nsize];
        double[][][] x;
        for (int i = 0; i < nsize; i++) {
            int exponent = init_exp + i;
            int N = (int) Math.pow(2, exponent);
            sizes[i] = N;
            System.out.println("Forward DST 3D (input 3D) of size 2^" + exponent + " x 2^" + exponent + " x 2^" + exponent);
            DoubleDst3D dst3 = new DoubleDst3D(N, N, N);
            x = new double[N][N][N];
            if (doWarmup) {
                IoUtils.fillMatrix_3D(N, N, N, x);
                dst3.forward(x, false);
                IoUtils.fillMatrix_3D(N, N, N, x);
                dst3.forward(x, false);
            }
            double av_time = 0;
            long elapsedTime = 0;
            for (int j = 0; j < niter; j++) {
                IoUtils.fillMatrix_3D(N, N, N, x);
                elapsedTime = System.nanoTime();
                dst3.forward(x, false);
                elapsedTime = System.nanoTime() - elapsedTime;
                av_time = av_time + elapsedTime;
            }
            times[i] = (double) av_time / 1000000.0 / (double) niter;
            System.out.println("Average execution time: " + String.format("%.2f", av_time / 1000000.0 / (double) niter) + " msec");
            x = null;
            dst3 = null;
            System.gc();
        }
        IoUtils.writeFFTBenchmarkResultsToFile("benchmarkDoubleForwardDST_3D_input_3D.txt", nthread, niter, doWarmup, doScaling, sizes, times);
    }
