    protected String createDigestedPassword(String algorithm, byte[] password) throws IllegalArgumentException {
        try {
            if (LdapSecurityConstants.HASH_METHOD_CRYPT.getName().equalsIgnoreCase(algorithm)) {
                String saltWithCrypted = UnixCrypt.crypt(StringTools.utf8ToString(password), "");
                String crypted = saltWithCrypted.substring(2);
                return '{' + algorithm + '}' + Arrays.toString(StringTools.getBytesUtf8(crypted));
            } else {
                MessageDigest digest = MessageDigest.getInstance(algorithm);
                byte[] fingerPrint = digest.digest(password);
                char[] encoded = Base64.encode(fingerPrint);
                return '{' + algorithm + '}' + new String(encoded);
            }
        } catch (NoSuchAlgorithmException nsae) {
            LOG.error("Cannot create a digested password for algorithm '{}'", algorithm);
            throw new IllegalArgumentException(nsae.getMessage());
        }
    }
