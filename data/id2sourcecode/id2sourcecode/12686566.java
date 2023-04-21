    public byte[] getPasswordHash(byte[] password, byte[] rgs) {
        byte[] pwdrgs = ArrayUtils.addAll(password, rgs);
        try {
            MessageDigest digester = MessageDigest.getInstance("SHA-1");
            digester.update(pwdrgs);
            return digester.digest();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Algorithm could not be found.", e);
        }
        return null;
    }
