    public static String calculateHash(String algorithm, byte[] content) {
        try {
            MessageDigest digester = MessageDigest.getInstance(algorithm);
            digester.update(content);
            byte[] data = digester.digest();
            StringBuffer result = new StringBuffer(algorithm);
            result.append(":");
            for (int i = 0; i < data.length; i++) {
                result.append(Integer.toHexString(data[i]));
            }
            return result.toString().toUpperCase();
        } catch (NoSuchAlgorithmException ex) {
            throw new InvalidStateException(ex);
        }
    }
