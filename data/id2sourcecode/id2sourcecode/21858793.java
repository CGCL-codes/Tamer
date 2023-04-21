    public int[][] binaryzation(int[][] pixels) {
        long minCenter = 0;
        long maxCenter = 255 * 255 * 255;
        long newMinCenter = -1;
        long newMaxCenter = -1;
        int computeTimes = 0;
        while (minCenter != maxCenter && (minCenter != newMinCenter || maxCenter != newMaxCenter) && computeTimes++ < maxComputeTimes) {
            double minSum = 0;
            double maxSum = 0;
            int minCount = 0;
            int maxCount = 0;
            if (newMinCenter != -1) minCenter = newMinCenter; else newMinCenter = minCenter;
            if (newMaxCenter != -1) maxCenter = newMaxCenter; else newMaxCenter = maxCenter;
            for (int w = 0; w < pixels.length; w++) {
                for (int h = 0; h < pixels[w].length; h++) {
                    if (Math.abs(minCenter - pixels[w][h]) > Math.abs(maxCenter - pixels[w][h])) {
                        maxSum += pixels[w][h];
                        maxCount++;
                    } else {
                        minSum += pixels[w][h];
                        minCount++;
                    }
                }
            }
            if (minCount == 0) newMinCenter = (minCenter + maxCenter) / 2; else if (maxCount == 0) newMaxCenter = (minCenter + maxCenter) / 2; else {
                newMinCenter = Math.round(minSum / minCount);
                newMaxCenter = Math.round(maxSum / maxCount);
            }
        }
        for (int w = 0; w < pixels.length; w++) {
            for (int h = 0; h < pixels[w].length; h++) {
                if (Math.abs(minCenter - pixels[w][h]) > Math.abs(maxCenter - pixels[w][h])) {
                    pixels[w][h] = 0xFFFFFF;
                } else {
                    pixels[w][h] = 0;
                }
            }
        }
        return pixels;
    }
