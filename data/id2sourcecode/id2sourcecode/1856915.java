    public static void loadMovieAverages(String completePath) {
        try {
            File infile = new File(completePath + fSep + "SmartGRAPE" + fSep + "movieAverageData.txt");
            FileChannel inC = new FileInputStream(infile).getChannel();
            int size = (int) inC.size();
            ByteBuffer map = inC.map(FileChannel.MapMode.READ_ONLY, 0, size);
            movieAverages = new TShortFloatHashMap(17770, 1);
            inC.close();
            while (map.hasRemaining()) {
                movieAverages.put(map.getShort(), map.getFloat());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
