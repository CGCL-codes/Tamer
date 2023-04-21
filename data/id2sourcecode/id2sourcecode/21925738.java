    public static void buildDiffStatistics(String completePath, String slopeOneDataFolderName, String slopeOneDataFileName) {
        try {
            File outfile = new File(completePath + fSep + "SmartGRAPE" + fSep + slopeOneDataFolderName + fSep + slopeOneDataFileName);
            FileChannel outC = new FileOutputStream(outfile, true).getChannel();
            short[] movies = CustomersAndRatingsPerMovie.keys();
            Arrays.sort(movies);
            int noMovies = movies.length;
            for (int i = 0; i < noMovies - 1; i++) {
                short movie1 = movies[i];
                System.out.println("Processing movie: " + movie1);
                TIntByteHashMap testMovieCustAndRatingsMap = (TIntByteHashMap) CustomersAndRatingsPerMovie.get(movie1);
                int[] customers1 = testMovieCustAndRatingsMap.keys();
                Arrays.sort(customers1);
                for (int j = i + 1; j < noMovies; j++) {
                    float diffRating = 0;
                    int count = 0;
                    short movie2 = movies[j];
                    TIntByteHashMap otherMovieCustAndRatingsMap = (TIntByteHashMap) CustomersAndRatingsPerMovie.get(movie2);
                    int[] customers2 = otherMovieCustAndRatingsMap.keys();
                    TIntArrayList intersectSet = CustOverLapForTwoMoviesCustom(customers1, customers2);
                    if ((intersectSet.size() == 0) || (intersectSet == null)) {
                        count = 0;
                        diffRating = 0;
                    } else {
                        count = intersectSet.size();
                        for (int l = 0; l < count; l++) {
                            int commonCust = intersectSet.getQuick(l);
                            diffRating += testMovieCustAndRatingsMap.get(commonCust);
                            diffRating -= otherMovieCustAndRatingsMap.get(commonCust);
                        }
                    }
                    ByteBuffer buf = ByteBuffer.allocate(12);
                    buf.putShort(movie1);
                    buf.putShort(movie2);
                    buf.putInt(count);
                    buf.putFloat(diffRating);
                    buf.flip();
                    outC.write(buf);
                    buf.clear();
                }
            }
            outC.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
