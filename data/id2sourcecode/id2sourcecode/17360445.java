    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        readHandle = CCNHandle.open();
        writeHandle = CCNHandle.open();
        CCNTime version = new CCNTime();
        ContentName stringName = testHelper.getClassChildName(STRING_VALUE_NAME);
        for (int i = 0; i < data.length; ++i) {
            data[i] = new CCNStringObject(stringName, "Value " + i, SaveType.REPOSITORY, writeHandle);
            System.out.println("Saving as version " + version);
            data[i].save(version);
            version.increment(1);
        }
        gone = new CCNStringObject(testHelper.getClassChildName(GONE_VALUE_NAME), GONE_VALUE_NAME, SaveType.REPOSITORY, writeHandle);
        gone.saveAsGone();
        bigData = testHelper.getClassChildName(BIG_VALUE_NAME);
        bigDataContent = new byte[bigDataLength];
        Random rand = new Random();
        rand.nextBytes(bigDataContent);
        bigValueDigest = CCNDigestHelper.digest(bigDataContent);
        CCNRepositoryWriter writer = new CCNRepositoryWriter(writeHandle);
        writer.newVersion(bigData, bigDataContent);
    }
