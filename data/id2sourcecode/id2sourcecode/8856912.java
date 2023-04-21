    public static void adaptiveThreshold(IplImage srcImage, final IplImage sumImage, final IplImage sqSumImage, final IplImage dstImage, final boolean invert, final int windowMax, final int windowMin, final double varMultiplier, final double k) {
        final int w = srcImage.width();
        final int h = srcImage.height();
        final int srcChannels = srcImage.nChannels();
        final int srcDepth = srcImage.depth();
        final int dstDepth = dstImage.depth();
        if (srcChannels > 1 && dstDepth == IPL_DEPTH_8U) {
            cvCvtColor(srcImage, dstImage, srcChannels == 4 ? CV_RGBA2GRAY : CV_BGR2GRAY);
            srcImage = dstImage;
        }
        final ByteBuffer srcBuf = srcImage.getByteBuffer();
        final ByteBuffer dstBuf = dstImage.getByteBuffer();
        final DoubleBuffer sumBuf = sumImage.getDoubleBuffer();
        final DoubleBuffer sqSumBuf = sqSumImage.getDoubleBuffer();
        final int srcStep = srcImage.widthStep();
        final int dstStep = dstImage.widthStep();
        final int sumStep = sumImage.widthStep();
        final int sqSumStep = sqSumImage.widthStep();
        cvIntegral(srcImage, sumImage, sqSumImage, null);
        double totalMean = sumBuf.get((h - 1) * sumStep / 8 + (w - 1)) - sumBuf.get((h - 1) * sumStep / 8) - sumBuf.get(w - 1) + sumBuf.get(0);
        totalMean /= w * h;
        double totalSqMean = sqSumBuf.get((h - 1) * sqSumStep / 8 + (w - 1)) - sqSumBuf.get((h - 1) * sqSumStep / 8) - sqSumBuf.get(w - 1) + sqSumBuf.get(0);
        totalSqMean /= w * h;
        double totalVar = totalSqMean - totalMean * totalMean;
        final double targetVar = totalVar * varMultiplier;
        Parallel.loop(0, h, new Parallel.Looper() {

            public void loop(int from, int to, int looperID) {
                for (int y = from; y < to; y++) {
                    for (int x = 0; x < w; x++) {
                        double var = 0, mean = 0, sqMean = 0;
                        int upperLimit = windowMax;
                        int lowerLimit = windowMin;
                        int window = upperLimit;
                        while (upperLimit - lowerLimit > 2) {
                            int x1 = Math.max(x - window / 2, 0);
                            int x2 = Math.min(x + window / 2 + 1, w);
                            int y1 = Math.max(y - window / 2, 0);
                            int y2 = Math.min(y + window / 2 + 1, h);
                            mean = sumBuf.get(y2 * sumStep / 8 + x2) - sumBuf.get(y2 * sumStep / 8 + x1) - sumBuf.get(y1 * sumStep / 8 + x2) + sumBuf.get(y1 * sumStep / 8 + x1);
                            mean /= window * window;
                            sqMean = sqSumBuf.get(y2 * sqSumStep / 8 + x2) - sqSumBuf.get(y2 * sqSumStep / 8 + x1) - sqSumBuf.get(y1 * sqSumStep / 8 + x2) + sqSumBuf.get(y1 * sqSumStep / 8 + x1);
                            sqMean /= window * window;
                            var = sqMean - mean * mean;
                            if (window == upperLimit && var < targetVar) {
                                break;
                            }
                            if (var > targetVar) {
                                upperLimit = window;
                            } else {
                                lowerLimit = window;
                            }
                            window = lowerLimit + (upperLimit - lowerLimit) / 2;
                            window = (window / 2) * 2 + 1;
                        }
                        double value = 0;
                        if (srcDepth == IPL_DEPTH_8U) {
                            value = srcBuf.get(y * srcStep + x) & 0xFF;
                        } else if (srcDepth == IPL_DEPTH_32F) {
                            value = srcBuf.getFloat(y * srcStep + 4 * x);
                        } else if (srcDepth == IPL_DEPTH_64F) {
                            value = srcBuf.getDouble(y * srcStep + 8 * x);
                        } else {
                            assert false;
                        }
                        if (invert) {
                            double threshold = 255 - (255 - mean) * k;
                            dstBuf.put(y * dstStep + x, (value < threshold ? (byte) 0xFF : (byte) 0x00));
                        } else {
                            double threshold = mean * k;
                            dstBuf.put(y * dstStep + x, (value > threshold ? (byte) 0xFF : (byte) 0x00));
                        }
                    }
                }
            }
        });
    }
