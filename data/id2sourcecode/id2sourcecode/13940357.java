    public static String md5(File f) throws IOException {
        byte[] buffer = new byte[8192];
        int read = 0;
        InputStream is = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            is = new BufferedInputStream(new FileInputStream(f));
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            return prepad(output, 32, '0');
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("Can't find md5 algorithm");
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
