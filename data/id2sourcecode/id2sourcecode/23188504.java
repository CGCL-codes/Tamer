    protected String createPasswordHash(String password) throws ProvisioningException {
        if (getHashAlgorithm() == null && getHashEncoding() == null) {
            return password;
        }
        if (logger.isDebugEnabled()) logger.debug("Creating password hash for [" + password + "] with algorithm/encoding [" + getHashAlgorithm() + "/" + getHashEncoding() + "]");
        byte[] passBytes;
        String passwordHash = null;
        try {
            if (hashCharset == null) passBytes = password.getBytes(); else passBytes = password.getBytes(hashCharset);
        } catch (UnsupportedEncodingException e) {
            logger.error("charset " + hashCharset + " not found. Using platform default.");
            passBytes = password.getBytes();
        }
        try {
            byte[] hash;
            if (hashAlgorithm != null) hash = getDigest().digest(passBytes); else hash = passBytes;
            if ("BASE64".equalsIgnoreCase(hashEncoding)) {
                passwordHash = CipherUtil.encodeBase64(hash);
            } else if ("HEX".equalsIgnoreCase(hashEncoding)) {
                passwordHash = CipherUtil.encodeBase16(hash);
            } else if (hashEncoding == null) {
                logger.error("You must specify a hashEncoding when using hashAlgorithm");
            } else {
                logger.error("Unsupported hash encoding format " + hashEncoding);
            }
        } catch (Exception e) {
            logger.error("Password hash calculation failed : \n" + e.getMessage() != null ? e.getMessage() : e.toString(), e);
        }
        return passwordHash;
    }
