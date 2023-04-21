    static void multi_write_test(boolean withSubDirs, int threads, int total_iterations) {
        MultiWriteThread t[] = new MultiWriteThread[threads];
        int iterations;
        File f;
        for (int i = 0; i < 100; i++) {
            f = new File("tmp/blobs/dir" + i + "/");
            if (!f.exists()) f.mkdir();
        }
        iterations = (total_iterations + threads - 1) / threads;
        for (int i = 0; i < threads; i++) t[i] = new MultiWriteThread(withSubDirs, i * iterations, iterations);
        try {
            f = new File("tmp");
            if (!f.exists()) f.mkdir();
            f = new File("tmp/blobs");
            if (!f.exists()) f.mkdir();
            if (withSubDirs) {
                for (int i = 0; i < 100; i++) {
                    f = new File("tmp/blobs/dir" + i + "/");
                    if (!f.exists()) f.mkdir();
                }
            }
            long start = System.currentTimeMillis();
            for (int i = 0; i < threads; i++) t[i].start();
            for (int i = 0; i < threads; i++) t[i].join();
            long stop = System.currentTimeMillis();
            printTime(start, stop, iterations * threads);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
