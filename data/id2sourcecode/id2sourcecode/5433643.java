    public static void benchmarkFloatPCGLS2D(int threads) {
        ConcurrencyUtils.setNumberOfThreads(threads);
        Opener o = new Opener();
        ImagePlus blurImage = o.openImage(path + blur_image);
        ImagePlus[][] psfImage = new ImagePlus[1][1];
        psfImage[0][0] = o.openImage(path + psf_image);
        double av_time_deblur = 0;
        long elapsedTime_deblur = 0;
        System.out.println("Benchmarking FloatPCGLS2D using " + threads + " threads");
        CGLSOptions options = new CGLSOptions(false, 0, false, 0, false);
        for (int i = 0; i < NITER; i++) {
            elapsedTime_deblur = System.nanoTime();
            CGLSFloatIterativeDeconvolver2D cgls = new CGLSFloatIterativeDeconvolver2D(blurImage, psfImage, PreconditionerType.FFT, (float) PREC_TOL, boundary, resizing, output, MAXITER, false, options);
            ImagePlus imX = cgls.deconvolve();
            elapsedTime_deblur = System.nanoTime() - elapsedTime_deblur;
            av_time_deblur = av_time_deblur + elapsedTime_deblur;
            cgls = null;
            imX = null;
        }
        blurImage = null;
        psfImage = null;
        System.out.println("Average execution time (tol =  " + PREC_TOL + "): " + String.format(format, av_time_deblur / 1000000000.0 / (double) NITER) + " sec");
        writeResultsToFile("FloatPCGLS2D_" + threads + "_threads.txt", (double) av_time_deblur / 1000000000.0 / (double) NITER, PREC_TOL);
    }
