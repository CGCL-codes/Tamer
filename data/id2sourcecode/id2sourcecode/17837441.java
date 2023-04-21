    public static String createKeyHash(byte[] key) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA1", "BC");
            final String res = new String(Hex.encode(md.digest(key)));
            md.reset();
            return res;
        } catch (NoSuchProviderException ex) {
            final String message = "Nu such provider trying to hash public key";
            throw new RuntimeException(message, ex);
        } catch (NoSuchAlgorithmException ex) {
            final String message = "Nu such algorithm trying to hash public key";
            throw new RuntimeException(message, ex);
        }
    }
