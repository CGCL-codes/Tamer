    public String getMD5(File f) {
        String output = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            InputStream is = new FileInputStream(f);
            byte[] buffer = new byte[8192];
            int read = 0;
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            output = bigInt.toString(16);
            is.close();
        } catch (Exception e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        }
        return output;
    }
