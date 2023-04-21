    public static void copyTrustFile(String fileName) throws IOException {
        File origFile = new File(TEST_FILES_PATH + File.separator + fileName);
        FileUtils.copyFile(origFile, new File(PeerConfiguration.TRUSTY_COMMUNITIES_FILENAME));
    }
