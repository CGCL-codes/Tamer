    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ContentName namespace = testHelper.getTestNamespace("benchmarkTest");
        testName = ContentName.fromNative(namespace, "BenchmarkObject");
        testName = VersioningProfile.addVersion(testName);
        shortPayload = ("this is sample segment content").getBytes();
        longPayload = new byte[LONG_LENGTH];
        Random rnd = new Random();
        rnd.nextBytes(longPayload);
        veryLongPayload = new byte[VERY_LONG_LENGTH];
        rnd.nextBytes(veryLongPayload);
        payloads = new byte[][] { shortPayload, longPayload, veryLongPayload };
        contentObjects = new ContentObject[payloads.length];
        unsignedContentObjects = new ContentObject[payloads.length];
        ContentName segmentName = SegmentationProfile.segmentName(testName, 0);
        for (int i = 0; i < payloads.length; ++i) {
            contentObjects[i] = ContentObject.buildContentObject(segmentName, payloads[i], null, null, SegmentationProfile.getSegmentNumberNameComponent(0));
            unsignedContentObjects[i] = new ContentObject(contentObjects[i].name(), contentObjects[i].signedInfo(), contentObjects[i].content(), (Signature) null);
        }
        final KeyPairGenerator kpg = KeyPairGenerator.getInstance(UserConfiguration.defaultKeyAlgorithm());
        for (int i = 0; i < keyLengths.length; ++i) {
            kpg.initialize(keyLengths[i]);
            keyPairs[i] = kpg.generateKeyPair();
        }
        format.setMaximumFractionDigits(3);
        handle = CCNHandle.open();
        System.out.println("Benchmark Test starting on " + System.getProperty("os.name"));
    }
