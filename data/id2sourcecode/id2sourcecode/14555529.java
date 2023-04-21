    @Override
    public String sh1withRSA(InputStream is) throws FileNotFoundException, IOException {
        byte fileContent[] = IOUtils.toByteArray(is);
        MessageDigest sha1 = null;
        try {
            sha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger hash = new BigInteger(1, sha1.digest(fileContent));
        String sen = hash.toString(16);
        return sen;
    }
