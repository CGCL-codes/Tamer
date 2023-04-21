    public static void benchmarkFloatHyBR2D(int threads) {
        ConcurrencyUtils.setNumberOfThreads(threads);
        Opener o = new Opener();
        ImagePlus blurImage = o.openImage(path + blur_image);
        ImagePlus[][] psfImage = new ImagePlus[1][1];
        psfImage[0][0] = o.openImage(path + psf_image);
        double av_time_deblur = 0;
        long elapsedTime_deblur = 0;
        System.out.println("Benchmarking FloatHyBR2D using " + threads + " threads");
        HyBROptions options = new HyBROptions(HyBRInnerSolver.TIKHONOV, HyBRRegularizationMethod.ADAPTWGCV, 0, 0, false, 2, 0, false, false, 0);
        for (int i = 0; i < NITER; i++) {
            elapsedTime_deblur = System.nanoTime();
            HyBRFloatIterativeDeconvolver2D hybr = new HyBRFloatIterativeDeconvolver2D(blurImage, psfImage, PreconditionerType.NONE, (float) PREC_TOL, boundary, resizing, output, MAXITER, false, options);
            ImagePlus imX = hybr.deconvolve();
            elapsedTime_deblur = System.nanoTime() - elapsedTime_deblur;
            av_time_deblur = av_time_deblur + elapsedTime_deblur;
            hybr = null;
            imX = null;
        }
        blurImage = null;
        psfImage = null;
        System.out.println("Average execution time: " + String.format(format, av_time_deblur / 1000000000.0 / (double) NITER) + " sec");
        writeResultsToFile("FloatHyBR2D_" + threads + "_threads.txt", (double) av_time_deblur / 1000000000.0 / (double) NITER);
    }
