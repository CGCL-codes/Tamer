    public static TShortObjectHashMap loadMovieDiffStats(String completePath, short movieToProcess, String slopeOneDataFolderName) {
        try {
            File inFile = new File(completePath + fSep + "SmartGRAPE" + fSep + slopeOneDataFolderName + fSep + "Movie-" + movieToProcess + "-MatrixData.txt");
            FileChannel inC = new FileInputStream(inFile).getChannel();
            TShortObjectHashMap returnMap = new TShortObjectHashMap(17770, 1);
            int size = (int) inC.size();
            ByteBuffer mapped = inC.map(FileChannel.MapMode.READ_ONLY, 0, size);
            short otherMovie;
            int count;
            float diffRating;
            while (mapped.hasRemaining()) {
                otherMovie = mapped.getShort();
                count = mapped.getInt();
                diffRating = mapped.getFloat();
                slopeOneStats newSOS = new slopeOneStats(count, diffRating);
                returnMap.put(otherMovie, newSOS);
            }
            return returnMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
