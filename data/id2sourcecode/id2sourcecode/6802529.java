    public static void benchmarkForward_3D_input_3D(int init_exp) {
        int[] sizes = new int[nsize];
        double[] times = new double[nsize];
        float[][][] x;
        for (int i = 0; i < nsize; i++) {
            int exponent = init_exp + i;
            int N = (int) Math.pow(2, exponent);
            sizes[i] = N;
            System.out.println("Forward DHT 3D (input 3D) of size 2^" + exponent + " x 2^" + exponent + " x 2^" + exponent);
            FloatDht3D dht3 = new FloatDht3D(N, N, N);
            x = new float[N][N][N];
            if (doWarmup) {
                IoUtils.fillMatrix_3D(N, N, N, x);
                dht3.forward(x);
                IoUtils.fillMatrix_3D(N, N, N, x);
                dht3.forward(x);
            }
            float av_time = 0;
            long elapsedTime = 0;
            for (int j = 0; j < niter; j++) {
                IoUtils.fillMatrix_3D(N, N, N, x);
                elapsedTime = System.nanoTime();
                dht3.forward(x);
                elapsedTime = System.nanoTime() - elapsedTime;
                av_time = av_time + elapsedTime;
            }
            times[i] = (float) av_time / 1000000.0 / (float) niter;
            System.out.println("Average execution time: " + String.format("%.2f", av_time / 1000000.0 / (float) niter) + " msec");
            x = null;
            dht3 = null;
            System.gc();
        }
        IoUtils.writeFFTBenchmarkResultsToFile("benchmarkFloatForwardDHT_3D_input_3D.txt", nthread, niter, doWarmup, doScaling, sizes, times);
    }
