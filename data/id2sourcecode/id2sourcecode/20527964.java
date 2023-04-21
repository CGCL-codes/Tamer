    public void blur1Direction(final FloatProcessor ip, final double sigma, final double accuracy, final boolean xDirection, final int extraLines) {
        final int UPSCALE_K_RADIUS = 2;
        final double MIN_DOWNSCALED_SIGMA = 4.;
        final float[] pixels = (float[]) ip.getPixels();
        final int width = ip.getWidth();
        final int height = ip.getHeight();
        final Rectangle roi = ip.getRoi();
        final int length = xDirection ? width : height;
        final int pointInc = xDirection ? 1 : width;
        final int lineInc = xDirection ? width : 1;
        final int lineFromA = (xDirection ? roi.y : roi.x) - extraLines;
        final int lineFrom;
        if (lineFromA < 0) lineFrom = 0; else lineFrom = lineFromA;
        final int lineToA = (xDirection ? roi.y + roi.height : roi.x + roi.width) + extraLines;
        final int lineTo;
        if (lineToA > (xDirection ? height : width)) lineTo = (xDirection ? height : width); else lineTo = lineToA;
        final int writeFrom = xDirection ? roi.x : roi.y;
        final int writeTo = xDirection ? roi.x + roi.width : roi.y + roi.height;
        final int inc = Math.max((lineTo - lineFrom) / (100 / (nPasses > 0 ? nPasses : 1) + 1), 20);
        pass++;
        if (pass > nPasses) pass = 1;
        final int numThreads = Math.min(Prefs.getThreads(), lineTo - lineFrom);
        final Thread[] lineThreads = new Thread[numThreads];
        final boolean doDownscaling = sigma > 2 * MIN_DOWNSCALED_SIGMA + 0.5;
        final int reduceBy = doDownscaling ? Math.min((int) Math.floor(sigma / MIN_DOWNSCALED_SIGMA), length) : 1;
        final double sigmaGauss = doDownscaling ? Math.sqrt(sigma * sigma / (reduceBy * reduceBy) - 1. / 3. - 1. / 4.) : sigma;
        final int maxLength = doDownscaling ? (length + reduceBy - 1) / reduceBy + 2 * (UPSCALE_K_RADIUS + 1) : length;
        final float[][] gaussKernel = makeGaussianKernel(sigmaGauss, accuracy, maxLength);
        final int kRadius = gaussKernel[0].length * reduceBy;
        final int readFrom = (writeFrom - kRadius < 0) ? 0 : writeFrom - kRadius;
        final int readTo = (writeTo + kRadius > length) ? length : writeTo + kRadius;
        final int newLength = doDownscaling ? (readTo - readFrom + reduceBy - 1) / reduceBy + 2 * (UPSCALE_K_RADIUS + 1) : length;
        final int unscaled0 = readFrom - (UPSCALE_K_RADIUS + 1) * reduceBy;
        final float[] downscaleKernel = doDownscaling ? makeDownscaleKernel(reduceBy) : null;
        final float[] upscaleKernel = doDownscaling ? makeUpscaleKernel(reduceBy) : null;
        for (int t = 0; t < numThreads; ++t) {
            final int ti = t;
            final float[] cache1 = new float[newLength];
            final float[] cache2 = doDownscaling ? new float[newLength] : null;
            final Thread thread = new Thread(new Runnable() {

                public final void run() {
                    long lastTime = System.currentTimeMillis();
                    boolean canShowProgress = Thread.currentThread() == lineThreads[0];
                    int pixel0 = (lineFrom + ti) * lineInc;
                    for (int line = lineFrom + ti; line < lineTo; line += numThreads, pixel0 += numThreads * lineInc) {
                        long time = System.currentTimeMillis();
                        if (time - lastTime > 110) {
                            if (canShowProgress) showProgress((double) (line - lineFrom) / (lineTo - lineFrom));
                            if (Thread.currentThread().isInterrupted()) return;
                            lastTime = time;
                        }
                        if (doDownscaling) {
                            downscaleLine(pixels, cache1, downscaleKernel, reduceBy, pixel0, unscaled0, length, pointInc, newLength);
                            convolveLine(cache1, cache2, gaussKernel, 0, newLength, 1, newLength - 1, 0, 1);
                            upscaleLine(cache2, pixels, upscaleKernel, reduceBy, pixel0, unscaled0, writeFrom, writeTo, pointInc);
                        } else {
                            int p = pixel0 + readFrom * pointInc;
                            for (int i = readFrom; i < readTo; i++, p += pointInc) cache1[i] = pixels[p];
                            convolveLine(cache1, pixels, gaussKernel, readFrom, readTo, writeFrom, writeTo, pixel0, pointInc);
                        }
                    }
                }
            }, "GaussianBlur-" + t);
            thread.setPriority(Thread.currentThread().getPriority());
            lineThreads[ti] = thread;
            thread.start();
        }
        try {
            for (final Thread thread : lineThreads) if (thread != null) thread.join();
        } catch (InterruptedException e) {
            for (final Thread thread : lineThreads) thread.interrupt();
            try {
                for (final Thread thread : lineThreads) thread.join();
            } catch (InterruptedException f) {
            }
            Thread.currentThread().interrupt();
        }
        showProgress(1.0);
        return;
    }
