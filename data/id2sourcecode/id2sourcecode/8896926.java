    private String h(String data) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(algorithm.toString());
        } catch (NoSuchAlgorithmException e) {
        }
        byte[] digestedData = digest.digest(data.getBytes());
        return toHexString(digestedData);
    }
