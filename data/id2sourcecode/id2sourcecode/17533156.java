    protected String md5(File file) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        FileInputStream fis = new FileInputStream(file);
        byte data[] = new byte[BUFFERSIZE];
        int count;
        while ((count = fis.read(data, 0, BUFFERSIZE)) != -1) {
            md.update(data, 0, count);
        }
        byte[] digest = md.digest();
        BigInteger bigInt = new BigInteger(1, digest);
        String hashtext = bigInt.toString(16);
        return hashtext;
    }
