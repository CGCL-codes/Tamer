    public Vector<VisionResult> pick(BufferedImage image) {
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
        Vector<VisionResult> v = new Vector<VisionResult>();
        for (int row = 0; row < image.getHeight(); row++) {
            for (int col = 0; col < image.getWidth(); col++) {
                if (distanceMap[row][col] > 0) {
                    row = findMiddleLine(distanceMap, row, col);
                    break;
                }
            }
            int lastX = -1;
            int nextPos = -1;
            for (int col = 0; col < image.getWidth(); col++) {
                if (distanceMap[row][col] > 0) {
                    Rectangle rect = paintFill(distanceMap, row, col);
                    if (rect.x >= 0 && rect.x + rect.width < image.getWidth() && rect.y >= 0 && rect.y + rect.getHeight() < image.getHeight() && rect.getHeight() + rect.getWidth() > 12) {
                        if (lastX == -1 || rect.x - lastX > rect.height / 3) v.add(null);
                        int[][] binImage = new int[rect.height][rect.width];
                        for (int i = 0; i < rect.height; i++) {
                            for (int j = 0; j < rect.width; j++) {
                                if (distanceMap[i + rect.y][j + rect.x] == -1) binImage[i][j] = 1; else binImage[i][j] = 0;
                            }
                        }
                        v.add(new VisionResult(image.getSubimage(rect.x, rect.y, rect.width, rect.height), binImage));
                        lastX = rect.x + rect.width;
                        nextPos = rect.y + rect.height;
                    }
                }
            }
            if (nextPos != -1) row = nextPos;
        }
        return v;
    }
