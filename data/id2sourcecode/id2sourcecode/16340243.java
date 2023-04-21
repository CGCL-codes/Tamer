    public static final int DigestLength(String algorithm) {
        Object test = DIGLEN.get(algorithm);
        if (test instanceof Integer) return ((Integer) test).intValue(); else {
            try {
                java.security.MessageDigest md = java.security.MessageDigest.getInstance(algorithm);
                byte[] digest = md.digest();
                int digest_len = digest.length;
                DIGLEN.put(algorithm, new Integer(digest_len));
                return digest_len;
            } catch (java.security.NoSuchAlgorithmException exc) {
                throw new alto.sys.Error.Argument(algorithm, exc);
            }
        }
    }
