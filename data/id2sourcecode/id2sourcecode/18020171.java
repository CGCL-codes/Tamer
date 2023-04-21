    public static String getMD5String(String filepath) {
        File f = new File(filepath);
        String md5Str = null;
        try {
            InputStream is = new FileInputStream(f);
            byte[] buffer = new byte[8192];
            int read = 0;
            while ((read = is.read(buffer)) > 0) {
                m_digest.update(buffer, 0, read);
            }
            byte[] md5sum = m_digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            md5Str = bigInt.toString(16);
        } catch (IOException e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        }
        return md5Str;
    }
