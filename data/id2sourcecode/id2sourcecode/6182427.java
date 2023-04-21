    public String calculateClassesMD5() throws Exception {
        File[] subFolders = projectClassesPath.listFiles();
        MessageDigest md = MessageDigest.getInstance("MD5");
        for (File file : subFolders) {
            if (file.getName().equals("sut")) {
                continue;
            }
            if (file.getName().equals("scenarios")) {
                continue;
            }
            FileUtils.updateMessageDigest(file, md);
        }
        byte[] hash = md.digest();
        BigInteger result = new BigInteger(hash);
        String rc = result.toString(16);
        return rc;
    }
