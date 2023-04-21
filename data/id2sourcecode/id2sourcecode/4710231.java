    public static String getMD5FromFile(String filename) {
        InputStream is = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            File f = new File(filename);
            is = new FileInputStream(f);
            byte[] buffer = new byte[8192];
            int read = 0;
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            return output;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Could not get MD5 from file: " + filename);
    }
