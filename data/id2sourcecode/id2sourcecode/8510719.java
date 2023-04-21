    private void initSymCiphers(String algorithm, SecretKey secret) throws Exception {
        if (log.isInfoEnabled()) log.info(" Initializing symmetric ciphers");
        symEncodingCipher = Cipher.getInstance(algorithm);
        symDecodingCipher = Cipher.getInstance(algorithm);
        symEncodingCipher.init(Cipher.ENCRYPT_MODE, secret);
        symDecodingCipher.init(Cipher.DECRYPT_MODE, secret);
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.reset();
        digest.update(secret.getEncoded());
        symVersion = new String(digest.digest(), "UTF-8");
        if (log.isInfoEnabled()) {
            log.info(" Initialized symmetric ciphers with secret key (" + symVersion.length() + " bytes)");
        }
    }
