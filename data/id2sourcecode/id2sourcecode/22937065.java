    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        CCNTestBase.setUpBeforeClass();
        byte[] testID = CCNDigestHelper.digest(testName.getBytes());
        tcn = ContentName.fromURI(testName);
        pubID = new PublisherID(testID, PublisherID.PublisherType.ISSUER_KEY);
    }
