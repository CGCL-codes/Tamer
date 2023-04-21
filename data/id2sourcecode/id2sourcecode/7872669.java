    private static boolean prepareSubmissionFile(String completePath, String QualifyingPredictionFileName) {
        try {
            TShortObjectHashMap qualMap = new TShortObjectHashMap(17770, 1);
            File custratings = new File(completePath + fSep + "SmartGRAPE" + fSep + QualifyingPredictionFileName);
            FileChannel in = new FileInputStream(custratings).getChannel();
            int filesize = (int) in.size();
            ByteBuffer mappedfile = in.map(FileChannel.MapMode.READ_ONLY, 0, filesize);
            while (mappedfile.hasRemaining()) {
                short movie = mappedfile.getShort();
                int customer = mappedfile.getInt();
                float prediction = mappedfile.getFloat();
                if (qualMap.containsKey(movie)) {
                    TIntFloatHashMap custPredictions = (TIntFloatHashMap) qualMap.get(movie);
                    custPredictions.put(customer, prediction);
                    qualMap.put(movie, custPredictions);
                } else {
                    TIntFloatHashMap custPredictions = new TIntFloatHashMap();
                    custPredictions.put(customer, prediction);
                    qualMap.put(movie, custPredictions);
                }
            }
            short movie;
            TShortObjectIterator itr = qualMap.iterator();
            System.out.println("Populated custratings hashmap");
            File inFile = new File(completePath + fSep + "SmartGRAPE" + fSep + "qualifying.txt");
            BufferedReader br = new BufferedReader(new FileReader(inFile));
            File outFile = new File(completePath + fSep + "SmartGRAPE" + fSep + "SubmissionFile.txt");
            BufferedWriter bufW = new BufferedWriter(new FileWriter(outFile));
            boolean endOfFile = true;
            short movieName = 0;
            int customer = 0;
            TIntFloatHashMap currentMoviePredictions = null;
            int decimalPlaces = 4;
            BigDecimal bd;
            while (endOfFile) {
                String line = br.readLine();
                if (line != null) {
                    if (line.indexOf(":") >= 0) {
                        movieName = new Short(line.substring(0, line.length() - 1)).shortValue();
                        bufW.write(line);
                        bufW.newLine();
                        currentMoviePredictions = (TIntFloatHashMap) qualMap.get(movieName);
                    } else {
                        customer = new Integer(line.substring(0, line.indexOf(','))).intValue();
                        float prediction = (float) currentMoviePredictions.get(customer);
                        if (prediction == prediction) {
                            bd = new BigDecimal(prediction);
                            bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_UP);
                            bufW.write(new Float(bd.floatValue()).toString());
                        } else System.out.println("got a Nan");
                        bufW.newLine();
                    }
                } else endOfFile = false;
            }
            br.close();
            bufW.close();
            return true;
        } catch (IOException e) {
            System.err.println(e);
            return false;
        }
    }
