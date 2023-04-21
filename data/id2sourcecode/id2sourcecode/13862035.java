    public static TIntObjectHashMap InitializeMovieRatingsForCustomerHashMap(String completePath, TIntObjectHashMap custList) {
        try {
            TIntObjectHashMap returnMap = new TIntObjectHashMap(custList.size(), 1);
            int totalCusts = custList.size();
            File movieMMAPDATAFile = new File(completePath + fSep + "SmartGRAPE" + fSep + "MovieRatingBinaryFile.txt");
            FileChannel inC = new FileInputStream(movieMMAPDATAFile).getChannel();
            int[] itr = custList.keys();
            int startIndex = 0;
            int endIndex = 0;
            TIntArrayList a = null;
            TShortByteHashMap result;
            ByteBuffer buf;
            for (int i = 0; i < totalCusts; i++) {
                int currentCust = itr[i];
                a = (TIntArrayList) custList.get(currentCust);
                startIndex = a.get(0);
                endIndex = a.get(1);
                if (endIndex > startIndex) {
                    result = new TShortByteHashMap(endIndex - startIndex + 1, 1);
                    buf = ByteBuffer.allocate((endIndex - startIndex + 1) * 3);
                    inC.read(buf, (startIndex - 1) * 3);
                } else {
                    result = new TShortByteHashMap(1, 1);
                    buf = ByteBuffer.allocate(3);
                    inC.read(buf, (startIndex - 1) * 3);
                }
                buf.flip();
                int bufsize = buf.capacity() / 3;
                for (int q = 0; q < bufsize; q++) {
                    result.put(buf.getShort(), buf.get());
                }
                returnMap.put(currentCust, result.clone());
                buf.clear();
                buf = null;
                a.clear();
                a = null;
            }
            return returnMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
