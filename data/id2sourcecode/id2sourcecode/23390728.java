    protected void setupSubscriptions() {
        try {
            Iterator iter = getParameters().iterator();
            String uuid = "";
            if (iter.hasNext()) {
                uuid = (String) iter.next();
            } else {
                return;
            }
            myBBService = getBlackboardService();
            File uuidDir = new File(storage_directory + File.separator + uuid.trim());
            uuidDir.mkdir();
            Properties issuanceProps = new Properties();
            myRandom = new Random();
            int firstInt = -1;
            int secondInt = -1;
            while (true) {
                firstInt = myRandom.nextInt(MAX_RFC);
                secondInt = myRandom.nextInt(MAX_RFC);
                if ((firstInt > MIN_RFC) && (secondInt > MIN_RFC)) break;
            }
            String rfcUrlOne = "" + rfcPrefix + firstInt + ".txt";
            String rfcUrlTwo = "" + rfcPrefix + secondInt + ".txt";
            issuanceProps.setProperty("trustStore", keystore_file);
            issuanceProps.setProperty("certification_endpoint", cert_endpoint);
            System.setProperty("javax.net.ssl.trustStore", keystore_file);
            int x = 0;
            File[] tmpFileArray = uuidDir.listFiles();
            int numSeries = 0;
            for (x = 0; x < tmpFileArray.length; x++) {
                try {
                    InformationCurrencySeries ics = new InformationCurrencySeries(new FileInputStream(tmpFileArray[x]));
                    myBBService.publishAdd(ics);
                } catch (Exception e) {
                }
            }
            File keypairFile = new File(uuidDir.getCanonicalPath() + File.separator + "keypair.obj");
            myKeyPair = null;
            if (keypairFile.exists()) {
                myKeyPair = (KeyPair) new ObjectInputStream(new FileInputStream(keypairFile)).readObject();
            }
            if (myKeyPair == null) {
                try {
                    KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
                    kpg.initialize(2048);
                    myKeyPair = kpg.generateKeyPair();
                    FileOutputStream fos = new FileOutputStream(keypairFile);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(myKeyPair);
                    oos.flush();
                    fos.flush();
                } catch (Exception e) {
                }
            }
            if (tmpFileArray.length < 6) {
                try {
                    InformationCurrencySeries retICSOne = URLIssuanceClient.generateIC(new URL(rfcUrlOne), myKeyPair.getPrivate(), issuanceProps);
                    File tmpFileOne = File.createTempFile("ics-out-", ".xml", uuidDir);
                    retICSOne.toString(new FileOutputStream(tmpFileOne));
                    myBBService.publishAdd(retICSOne);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    InformationCurrencySeries retICSTwo = URLIssuanceClient.generateIC(new URL(rfcUrlTwo), myKeyPair.getPrivate(), issuanceProps);
                    File tmpFileTwo = File.createTempFile("ics-out-", ".xml", uuidDir);
                    retICSTwo.toString(new FileOutputStream(tmpFileTwo));
                    myBBService.publishAdd(retICSTwo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            myBBService.publishAdd(myKeyPair);
            System.out.println("created myKeyPair");
            myStringSub = (CollectionSubscription) myBBService.subscribe(myStringPredicate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
