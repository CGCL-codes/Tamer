    public static byte[] createDigest(InputStream is) throws Exception {
        try {
            MessageDigest shaDigester = createSHADigester();
            DigestInputStream dis = new DigestInputStream(is, shaDigester);
            while (dis.read() >= 0) {
            }
            return dis.getMessageDigest().digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to create digest.", e);
        } catch (Exception e) {
            throw new Exception("Unable to create digest.", e);
        }
    }
