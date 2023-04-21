    public static void benchmarkFloatPWPL3D(int threads) {
        ConcurrencyUtils.setNumberOfThreads(threads);
        Opener o = new Opener();
        ImagePlus blurImage = o.openImage(path + blur_image);
        ImagePlus[][][] psfImage = new ImagePlus[1][1][1];
        psfImage[0][0][0] = o.openImage(path + psf_image);
        double av_time_deblur = 0;
        long elapsedTime_deblur = 0;
        System.out.println("Benchmarking FloatPWPL3D using " + threads + " threads");
        WPLOptions options = new WPLOptions(0.01, 1.0, 1.0, true, false, false, 0, false, false, false, 0);
        for (int i = 0; i < NITER; i++) {
            elapsedTime_deblur = System.nanoTime();
            WPLFloatIterativeDeconvolver3D wpl = new WPLFloatIterativeDeconvolver3D(blurImage, psfImage[0][0][0], boundary, resizing, output, MAXITER, false, options);
            ImagePlus imX = wpl.deconvolve();
            elapsedTime_deblur = System.nanoTime() - elapsedTime_deblur;
            av_time_deblur = av_time_deblur + elapsedTime_deblur;
            wpl = null;
            imX = null;
        }
        blurImage = null;
        psfImage = null;
        System.out.println("Average execution time (tol =  " + PREC_TOL + "): " + String.format(format, av_time_deblur / 1000000000.0 / (double) NITER) + " sec");
        writeResultsToFile("FloatPWPL3D_" + threads + "_threads.txt", (double) av_time_deblur / 1000000000.0 / (double) NITER, PREC_TOL);
    }
