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
