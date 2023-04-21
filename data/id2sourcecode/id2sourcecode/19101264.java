    private byte[] computeSessionId(byte[] cookie, BigInteger hostKeyN, BigInteger sessionKeyN) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(hostKeyN.abs().toByteArray());
            md.update(sessionKeyN.abs().toByteArray());
            md.update(cookie, 0, 8);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.getLogInstance().fatal("MD5 not supported by security provider.");
            return null;
        }
    }
