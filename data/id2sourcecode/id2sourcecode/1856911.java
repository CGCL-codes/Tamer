    public static void main(String[] args) {
        try {
            String completePath = null;
            String predictionFileName = null;
            String CFDataFolderName = null;
            String submissionFileName = null;
            if (args.length == 4) {
                completePath = args[0];
                predictionFileName = args[1];
                CFDataFolderName = args[2];
                submissionFileName = args[3];
            } else {
                System.out.println("Please provide complete path to training_set parent folder as an argument. EXITING");
                System.exit(0);
            }
            loadMovieAverages(completePath);
            File inputFile = new File(completePath + fSep + "SmartGRAPE" + fSep + CustIndexFileName);
            FileChannel inC = new FileInputStream(inputFile).getChannel();
            int filesize = (int) inC.size();
            ByteBuffer mappedfile = inC.map(FileChannel.MapMode.READ_ONLY, 0, filesize);
            CustomerLimitsTHash = new TIntObjectHashMap(480189, 1);
            int startIndex, endIndex;
            TIntArrayList a;
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
                System.out.println("Populated MoviesAndRatingsPerCustomer hashmap: ");
            }
            CustomerLimitsTHash.clear();
            CustomerLimitsTHash = null;
            boolean success = true;
            success = predictDataSet(completePath, "Probe", predictionFileName, CFDataFolderName);
            if (success) {
                System.out.println("Binary probe prediction file successfully created");
            } else {
                System.out.println("Binary probe prediction file creation failed");
            }
            DataUtilities.computeProbeRMSE(completePath, predictionFileName);
            success = predictDataSet(completePath, "Qualifying", predictionFileName, CFDataFolderName);
            if (success) {
                System.out.println("Binary qualifying prediction file successfully created");
            } else {
                System.out.println("Binary qualifying prediction file creation failed");
            }
            DataUtilities.computeProbeRMSE(completePath, predictionFileName);
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
