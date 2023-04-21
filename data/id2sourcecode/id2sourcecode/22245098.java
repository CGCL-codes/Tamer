    @Override
    public String hashString(String toHash) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            byte[] buffer = toHash.getBytes();
            digest.update(buffer);
            byte[] sha1Hash = digest.digest();
            BigInteger converter = new BigInteger(sha1Hash);
            return converter.toString(16);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SecurityProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
