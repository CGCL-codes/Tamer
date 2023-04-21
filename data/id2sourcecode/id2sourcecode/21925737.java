    public static void main(String[] args) {
        try {
            String completePath = null;
            String predictionFileName = null;
            String slopeOneStatsFolderName = null;
            String submissionFileName = null;
            if (args.length == 4) {
                completePath = args[0];
                predictionFileName = args[1];
                slopeOneStatsFolderName = args[2];
                submissionFileName = args[3];
            } else {
                System.out.println("Please provide complete path to training_set parent folder as an argument. EXITING");
                System.exit(0);
            }
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
            mappedfile = null;
            System.out.println("Loaded movie index hash");
            if ((CustomersAndRatingsPerMovie == null) || CustomersAndRatingsPerMovie.size() <= 0) {
                CustomersAndRatingsPerMovie = InitializeCustomerRatingsForMovieHashMap(completePath, MovieLimitsTHash);
                System.out.println("Populated CustomersAndRatingsPerMovie hashmap");
            }
            buildDiffStatistics(completePath, "SlopeOneData", "Movie2MovieSlopeOneData.txt");
            buildPerMovieDiffBinary(completePath, "SlopeOneData", "Movie2MovieSlopeOneData.txt");
            CustomersAndRatingsPerMovie.clear();
            CustomersAndRatingsPerMovie = null;
            MovieLimitsTHash.clear();
            MovieLimitsTHash = null;
            inputFile = new File(completePath + fSep + "SmartGRAPE" + fSep + CustIndexFileName);
            inC = new FileInputStream(inputFile).getChannel();
            filesize = (int) inC.size();
            mappedfile = inC.map(FileChannel.MapMode.READ_ONLY, 0, filesize);
            CustomerLimitsTHash = new TIntObjectHashMap(480189, 1);
            int custid;
            while (mappedfile.hasRemaining()) {
                custid = mappedfile.getInt();
                startIndex = mappedfile.getInt();
                endIndex = mappedfile.getInt();
                a = new TIntArrayList(2);
                a.add(startIndex);
                a.add(endIndex);
                CustomerLimitsTHash.put(custid, a);
            }
            inC.close();
            mappedfile = null;
            System.out.println("Loaded customer index hash");
            if ((MoviesAndRatingsPerCustomer == null) || MoviesAndRatingsPerCustomer.size() <= 0) {
                MoviesAndRatingsPerCustomer = InitializeMovieRatingsForCustomerHashMap(completePath, CustomerLimitsTHash);
                System.out.println("Populated CustomersAndRatingsPerMovie hashmap: ");
            }
            boolean success = true;
            success = predictDataSet(completePath, "Qualifying", predictionFileName, slopeOneStatsFolderName);
            if (success) {
                System.out.println("Binary prediction file successfully created");
            } else {
                System.out.println("Binary prediction file creation failed");
            }
            success = DataUtilities.prepareSubmissionFile(completePath, predictionFileName, submissionFileName);
            if (success) {
                System.out.println("Prediction file for submission to Netflix successfully created");
            } else {
                System.out.println("Prediction file creation for submission to Netflix failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
