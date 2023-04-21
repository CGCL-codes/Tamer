    public static void benchmarkForward_3D_input_1D(int init_exp) {
        int[] sizes = new int[nsize];
        double[] times = new double[nsize];
        float[] x;
        for (int i = 0; i < nsize; i++) {
            int exponent = init_exp + i;
            int N = (int) Math.pow(2, exponent);
            sizes[i] = N;
            System.out.println("Forward DST 3D (input 1D) of size 2^" + exponent + " x 2^" + exponent + " x 2^" + exponent);
            FloatDst3D dst3 = new FloatDst3D(N, N, N);
            x = new float[N * N * N];
            if (doWarmup) {
                IoUtils.fillMatrix_3D(N, N, N, x);
                dst3.forward(x, false);
                IoUtils.fillMatrix_3D(N, N, N, x);
                dst3.forward(x, false);
            }
            float av_time = 0;
            long elapsedTime = 0;
            for (int j = 0; j < niter; j++) {
                IoUtils.fillMatrix_3D(N, N, N, x);
                elapsedTime = System.nanoTime();
                dst3.forward(x, false);
                elapsedTime = System.nanoTime() - elapsedTime;
                av_time = av_time + elapsedTime;
            }
            times[i] = (double) av_time / 1000000.0 / (double) niter;
            System.out.println("Average execution time: " + String.format("%.2f", av_time / 1000000.0 / (float) niter) + " msec");
            x = null;
            dst3 = null;
            System.gc();
        }
        IoUtils.writeFFTBenchmarkResultsToFile("benchmarkFloatForwardDST_3D_input_1D.txt", nthread, niter, doWarmup, doScaling, sizes, times);
    }
