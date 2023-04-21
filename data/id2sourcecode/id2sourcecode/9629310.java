    protected byte[] calculateChecksum(byte[] data, int size) throws KrbCryptoException {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new KrbCryptoException("JCE provider may not be installed. " + e.getMessage());
        }
        try {
            md5.update(data);
            return (md5.digest());
        } catch (Exception e) {
            throw new KrbCryptoException(e.getMessage());
        }
    }
