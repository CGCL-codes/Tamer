    public boolean verify(byte[] hash, byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance(SHA1, PROVIDER);
            md.update(data);
            return Arrays.equals(hash, md.digest());
        } catch (java.security.NoSuchAlgorithmException e) {
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return false;
    }
