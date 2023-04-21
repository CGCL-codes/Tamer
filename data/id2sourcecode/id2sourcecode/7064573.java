    protected void copyTrustFile(String fileName) throws IOException {
        File origFile = new File(getRootForTrustFile() + File.separator + fileName);
        FileUtils.copyFile(origFile, new File(PeerConfiguration.TRUSTY_COMMUNITIES_FILENAME));
    }
