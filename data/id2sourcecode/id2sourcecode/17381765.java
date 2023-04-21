    public static void benchmarkForward_1D(int init_exp) {
        int[] sizes = new int[nsize];
        double[] times = new double[nsize];
        double[] x;
        for (int i = 0; i < nsize; i++) {
            int exponent = init_exp + i;
            int N = (int) Math.pow(2, exponent);
            sizes[i] = N;
            System.out.println("Forward DHT 1D of size 2^" + exponent);
            DoubleDht1D dht = new DoubleDht1D(N);
            x = new double[N];
            if (doWarmup) {
                IoUtils.fillMatrix_1D(N, x);
                dht.forward(x);
                IoUtils.fillMatrix_1D(N, x);
                dht.forward(x);
            }
            double av_time = 0;
            long elapsedTime = 0;
            for (int j = 0; j < niter; j++) {
                IoUtils.fillMatrix_1D(N, x);
                elapsedTime = System.nanoTime();
                dht.forward(x);
                elapsedTime = System.nanoTime() - elapsedTime;
                av_time = av_time + elapsedTime;
            }
            times[i] = (double) av_time / 1000000.0 / (double) niter;
            System.out.println("Average execution time: " + String.format("%.2f", av_time / 1000000.0 / (double) niter) + " msec");
            x = null;
            dht = null;
            System.gc();
        }
        IoUtils.writeFFTBenchmarkResultsToFile("benchmarkDoubleForwardDHT_1D.txt", nthread, niter, doWarmup, doScaling, sizes, times);
    }
