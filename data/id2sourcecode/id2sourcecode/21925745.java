    public static TShortObjectHashMap InitializeCustomerRatingsForMovieHashMap(String completePath, TShortObjectHashMap MovieLimitsTHash) {
        try {
            TShortObjectHashMap returnMap = new TShortObjectHashMap(MovieLimitsTHash.size(), 1);
            int totalMovies = MovieLimitsTHash.size();
            File movieMMAPDATAFile = new File(completePath + fSep + "SmartGRAPE" + fSep + CustomerRatingFileName);
            FileChannel inC = new FileInputStream(movieMMAPDATAFile).getChannel();
            short[] itr = MovieLimitsTHash.keys();
            int startIndex = 0;
            int endIndex = 0;
            TIntArrayList a = null;
            TIntByteHashMap result;
            ByteBuffer buf;
            for (int i = 0; i < totalMovies; i++) {
                short currentMovie = itr[i];
                a = (TIntArrayList) MovieLimitsTHash.get(currentMovie);
                startIndex = a.get(0);
                endIndex = a.get(1);
                if (endIndex > startIndex) {
                    result = new TIntByteHashMap(endIndex - startIndex + 1, 1);
                    buf = ByteBuffer.allocate((endIndex - startIndex + 1) * 5);
                    inC.read(buf, (startIndex - 1) * 5);
                } else {
                    result = new TIntByteHashMap(1, 1);
                    buf = ByteBuffer.allocate(5);
                    inC.read(buf, (startIndex - 1) * 5);
                }
                buf.flip();
                int bufsize = buf.capacity() / 5;
                for (int q = 0; q < bufsize; q++) {
                    result.put(buf.getInt(), buf.get());
                }
                returnMap.put(currentMovie, result.clone());
                buf.clear();
                buf = null;
                a.clear();
                a = null;
            }
            inC.close();
            return returnMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
