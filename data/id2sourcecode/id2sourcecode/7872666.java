    public static float[] loadMovieAverages(String completePath, double K, String MovieAveragesRawDataFileName) {
        try {
            TShortObjectHashMap movieAveragesRawData = new TShortObjectHashMap(17771, 1);
            File inputFile = new File(completePath + fSep + "SmartGRAPE" + fSep + MovieAveragesRawDataFileName);
            FileChannel inC = new FileInputStream(inputFile).getChannel();
            int filesize = (int) inC.size();
            ByteBuffer mappedfile = inC.map(FileChannel.MapMode.READ_ONLY, 0, filesize);
            short movie;
            int startIndex, endIndex;
            double globalAvg = 0;
            double count = 0, sumrating = 0;
            TDoubleArrayList a;
            for (short i = 0; i <= 17770; i++) {
                movie = mappedfile.getShort();
                count = mappedfile.getDouble();
                sumrating = mappedfile.getDouble();
                a = new TDoubleArrayList(2);
                a.add(count);
                a.add(sumrating);
                movieAveragesRawData.put(i, a);
            }
            inC.close();
            mappedfile = null;
            float[] movieAverages = new float[17771];
            TShortObjectIterator iterator = movieAveragesRawData.iterator();
            TDoubleArrayList arr;
            iterator.advance();
            arr = (TDoubleArrayList) iterator.value();
            movieAverages[0] = new Double(arr.getQuick(1) / arr.getQuick(0)).floatValue();
            System.out.println("MOVIE GLOBAL AVERAGE IS: " + movieAverages[0]);
            for (int i = 1; i <= 17770; i++) {
                iterator.advance();
                arr = (TDoubleArrayList) iterator.value();
                movieAverages[i] = new Double((movieAverages[0] * K + arr.getQuick(1)) / (K + arr.getQuick(0))).floatValue();
            }
            System.out.println("Loaded movie averages raw data");
            return movieAverages;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
