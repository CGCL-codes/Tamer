    private String getCheckSum(String fileName) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        InputStream is = new DigestInputStream(new FileInputStream(scriptsDirName + "/test_scripts/" + fileName), digest);
        while (is.read() != -1) ;
        return getHexPresentation(digest.digest());
    }
