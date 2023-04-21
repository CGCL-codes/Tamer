    public static String hash(byte[] bytes, String algorithm) {
        synchronized (algorithm.intern()) {
            MessageDigest digest = digests.get(algorithm);
            if (digest == null) {
                try {
                    digest = MessageDigest.getInstance(algorithm);
                    digests.put(algorithm, digest);
                } catch (NoSuchAlgorithmException nsae) {
                    nsae.printStackTrace();
                    return null;
                }
            }
            digest.update(bytes);
            return encodeHex(digest.digest());
        }
    }
