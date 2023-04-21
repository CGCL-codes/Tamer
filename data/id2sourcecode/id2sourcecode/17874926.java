    public static TShortObjectHashMap InitializeCustomerRatingsForMovieHashMap() {
        try {
            File inputFile = new File(completePath + fSep + "SmartGRAPE" + fSep + MovieIndexFileName);
            FileChannel inC = new FileInputStream(inputFile).getChannel();
            int filesize = (int) inC.size();
            ByteBuffer mappedfile = inC.map(FileChannel.MapMode.READ_ONLY, 0, filesize);
            MovieLimitsTHash = new TShortObjectHashMap(17770, 1);
            int i = 0, totalcount = 0;
            short movie;
            int startIndex, endIndex;
            TIntArrayList a;
            while (mappedfile.hasRemaining()) {
                movie = mappedfile.getShort();
                startIndex = mappedfile.getInt();
                endIndex = mappedfile.getInt();
                a = new TIntArrayList(2);
                a.add(startIndex);
                a.add(endIndex);
                MovieLimitsTHash.put(movie, a);
            }
            inC.close();
            mappedfile.clear();
            mappedfile = null;
            System.out.println("Loaded movie index hash");
            a = null;
            TShortObjectHashMap returnMap = new TShortObjectHashMap(MovieLimitsTHash.size(), 1);
            int totalMovies = MovieLimitsTHash.size();
            File movieMMAPDATAFile = new File(completePath + fSep + "SmartGRAPE" + fSep + "CustomerRatingBinaryFile.txt");
            inC = new FileInputStream(movieMMAPDATAFile).getChannel();
            short[] itr = MovieLimitsTHash.keys();
            Arrays.sort(itr);
            TIntByteHashMap result;
            ByteBuffer buf;
            for (i = 1610; i < totalMovies; i++) {
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
