    public static int[][] getBinImage(BufferedImage image) {
        Color bgColor = OCRUtility.nearestCommonColor(image);
        int[][] distanceMap = new int[image.getHeight()][image.getWidth()];
        int[] distanceHistogram = new int[768];
        for (int row = 0; row < image.getHeight(); row++) {
            for (int col = 0; col < image.getWidth(); col++) {
                distanceMap[row][col] = OCRUtility.colorDistance(bgColor, image.getRGB(col, row));
                distanceHistogram[distanceMap[row][col]]++;
            }
        }
        int highestHistogramPosition = 0;
        int histogramValue = 0;
        for (int i = 1; i < 768; i++) {
            if (distanceHistogram[i] > histogramValue) {
                highestHistogramPosition = i;
                histogramValue = distanceHistogram[highestHistogramPosition];
            }
        }
        int higherHistogramPosition = 0;
        histogramValue = 0;
        for (int i = highestHistogramPosition * 8; i < 768; i++) {
            if (distanceHistogram[i] > histogramValue && distanceHistogram[i] < distanceHistogram[highestHistogramPosition]) {
                higherHistogramPosition = i;
                histogramValue = distanceHistogram[higherHistogramPosition];
            }
        }
        int threshold = (highestHistogramPosition + higherHistogramPosition) / 2;
        for (int row = 0; row < image.getHeight(); row++) {
            for (int col = 0; col < image.getWidth(); col++) {
                if (distanceMap[row][col] < threshold) {
                    distanceMap[row][col] = 0;
                } else {
                    distanceMap[row][col] = 1;
                }
            }
        }
        int yBegin = 0;
        lab1: for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                if (distanceMap[i][j] == 1) {
                    yBegin = i;
                    break lab1;
                }
            }
        }
        int yEnd = 0;
        lab1a: for (int i = image.getHeight() - 1; i >= 0; i--) {
            for (int j = 0; j < image.getWidth(); j++) {
                if (distanceMap[i][j] == 1) {
                    yEnd = i;
                    break lab1a;
                }
            }
        }
        int xBegin = 0;
        lab2: for (int j = 0; j < image.getWidth(); j++) {
            for (int i = 0; i < image.getHeight(); i++) {
                if (distanceMap[i][j] == 1) {
                    xBegin = j;
                    break lab2;
                }
            }
        }
        int xEnd = 0;
        lab2a: for (int j = image.getWidth() - 1; j >= 0; j--) {
            for (int i = 0; i < image.getHeight(); i++) {
                if (distanceMap[i][j] == 1) {
                    xEnd = j;
                    break lab2a;
                }
            }
        }
        int[][] binImage = new int[yEnd - yBegin + 1][xEnd - xBegin + 1];
        for (int i = 0; i < binImage.length; i++) {
            for (int j = 0; j < binImage[0].length; j++) {
                binImage[i][j] = distanceMap[i + yBegin][j + xBegin];
            }
        }
        return binImage;
    }
