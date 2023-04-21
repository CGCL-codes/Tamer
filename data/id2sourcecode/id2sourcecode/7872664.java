    public static void computeProbeRMSE(String completePath, String ProbeDataAndPredictionFileName) {
        try {
            File custratings = new File(completePath + fSep + "SmartGRAPE" + fSep + ProbeDataAndPredictionFileName);
            FileChannel in = new FileInputStream(custratings).getChannel();
            int filesize = (int) in.size();
            ByteBuffer mappedfile = in.map(FileChannel.MapMode.READ_ONLY, 0, filesize);
            in.close();
            double rmse = 0;
            ;
            double squaredvalue = 0;
            double delta = 0;
            int numvalues = 0;
            while (mappedfile.hasRemaining()) {
                double prediction, actual;
                actual = new Byte(mappedfile.get()).doubleValue();
                prediction = new Float(mappedfile.getFloat()).doubleValue();
                delta = prediction - actual;
                squaredvalue += delta * delta;
                numvalues++;
            }
            rmse = Math.sqrt(squaredvalue / numvalues);
            System.out.println("The rmse for the probe data set is: " + rmse);
        } catch (IOException e) {
            System.err.println(e);
        }
    }
