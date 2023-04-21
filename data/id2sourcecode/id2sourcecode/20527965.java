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
