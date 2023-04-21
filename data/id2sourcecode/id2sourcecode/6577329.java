    public static void benchmarkDoubleMRNSD3D(int threads) {
        ConcurrencyUtils.setNumberOfThreads(threads);
        Opener o = new Opener();
        ImagePlus blurImage = o.openImage(path + blur_image);
        ImagePlus[][][] psfImage = new ImagePlus[1][1][1];
        psfImage[0][0][0] = o.openImage(path + psf_image);
        double av_time_deblur = 0;
        long elapsedTime_deblur = 0;
        System.out.println("Benchmarking DoubleMRNSD3D using " + threads + " threads");
        MRNSDOptions options = new MRNSDOptions(false, 0, false, 0, false);
        for (int i = 0; i < NITER; i++) {
            elapsedTime_deblur = System.nanoTime();
            MRNSDDoubleIterativeDeconvolver3D mrnsd = new MRNSDDoubleIterativeDeconvolver3D(blurImage, psfImage, PreconditionerType.NONE, PREC_TOL, boundary, resizing, output, MAXITER, false, options);
            ImagePlus imX = mrnsd.deconvolve();
            elapsedTime_deblur = System.nanoTime() - elapsedTime_deblur;
            av_time_deblur = av_time_deblur + elapsedTime_deblur;
            mrnsd = null;
            imX = null;
        }
        blurImage = null;
        psfImage = null;
        System.out.println("Average execution time: " + String.format(format, av_time_deblur / 1000000000.0 / (double) NITER) + " sec");
        writeResultsToFile("DoubleMRNSD3D_" + threads + "_threads.txt", (double) av_time_deblur / 1000000000.0 / (double) NITER);
    }
