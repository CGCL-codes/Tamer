    public byte[] digest(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance(SHA1, PROVIDER);
            md.update(data);
            return md.digest();
        } catch (java.security.NoSuchAlgorithmException e) {
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }
