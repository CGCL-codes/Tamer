    public static String getSignature(String alg, InputStream is) {
        String sig = "";
        try {
            MessageDigest digest = MessageDigest.getInstance(alg);
            byte[] buffer = new byte[8192];
            int read = 0;
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] hash = digest.digest();
            BigInteger bigInt = new BigInteger(1, hash);
            sig = bigInt.toString(16);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (is != null) try {
                is.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return sig;
    }
