    public static TShortObjectHashMap loadMovieDiffStats(String completePath, short movieToProcess, String CFDataFolderName) {
        try {
            File inFile = new File(completePath + fSep + "SmartGRAPE" + fSep + CFDataFolderName + fSep + "Movie-" + movieToProcess + "-MatrixData.txt");
            FileChannel inC = new FileInputStream(inFile).getChannel();
            TShortObjectHashMap returnMap = new TShortObjectHashMap(17770, 1);
            int size = (int) inC.size();
            ByteBuffer mapped = inC.map(FileChannel.MapMode.READ_ONLY, 0, size);
            short otherMovie;
            int count;
            float diffRating, sumXY, sumX, sumY, sumX2, sumY2, pearsonCorr, adjustedCosineCorr, cosineCorr;
            while (mapped.hasRemaining()) {
                otherMovie = mapped.getShort();
                count = mapped.getInt();
                diffRating = mapped.getFloat();
                sumXY = mapped.getFloat();
                sumX = mapped.getFloat();
                sumY = mapped.getFloat();
                sumX2 = mapped.getFloat();
                sumY2 = mapped.getFloat();
                pearsonCorr = mapped.getFloat();
                adjustedCosineCorr = mapped.getFloat();
                cosineCorr = mapped.getFloat();
                PearsonSlopeOneStats newD = new PearsonSlopeOneStats(count, diffRating, pearsonCorr, adjustedCosineCorr, 0);
                returnMap.put(otherMovie, newD);
            }
            inC.close();
            return returnMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
