    static String toMd5(byte[] bytes) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(bytes);
            return toHexString(algorithm.digest(), "");
        } catch (NoSuchAlgorithmException e) {
            Log.w(TAG, "toMd5(): " + e);
            throw new RuntimeException(e);
        }
    }
