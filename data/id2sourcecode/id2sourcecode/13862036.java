    public static TShortObjectHashMap InitializeCustomerRatingsForMovieHashMap(String completePath, short movie) {
        try {
            TShortObjectHashMap returnMap = new TShortObjectHashMap(MovieLimitsTHash.size(), 1);
            File movieMMAPDATAFile = new File(completePath + fSep + "SmartGRAPE" + fSep + "CustomerRatingBinaryFile.txt");
            FileChannel inC = new FileInputStream(movieMMAPDATAFile).getChannel();
            TIntByteHashMap result;
            ByteBuffer buf;
            TIntArrayList a = (TIntArrayList) MovieLimitsTHash.get(movie);
            int startIndex = a.get(0);
            int endIndex = a.get(1);
            if (endIndex > startIndex) {
                result = new TIntByteHashMap(endIndex - startIndex + 1, 1);
                buf = ByteBuffer.allocate((endIndex - startIndex + 1) * 5);
                inC.read(buf, (startIndex - 1) * 5);
            } else {
                result = new TIntByteHashMap(1);
                buf = ByteBuffer.allocate(5);
                inC.read(buf, (startIndex - 1) * 5);
            }
            buf.flip();
            int bufsize = buf.capacity() / 5;
            for (int q = 0; q < bufsize; q++) {
                result.put(buf.getInt(), buf.get());
            }
            returnMap.put(movie, result);
            buf.clear();
            buf = null;
            a.clear();
            a = null;
            inC.close();
            return returnMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
