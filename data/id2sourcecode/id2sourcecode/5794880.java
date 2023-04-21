    public final byte[] computeEncryptedKey(byte[] password, byte[] o, int permissions, byte[] id, int encRevision, int length) throws CryptographyException {
        byte[] result = new byte[length];
        try {
            byte[] padded = truncateOrPad(password);
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(padded);
            md.update(o);
            byte zero = (byte) (permissions >>> 0);
            byte one = (byte) (permissions >>> 8);
            byte two = (byte) (permissions >>> 16);
            byte three = (byte) (permissions >>> 24);
            md.update(zero);
            md.update(one);
            md.update(two);
            md.update(three);
            md.update(id);
            byte[] digest = md.digest();
            if (encRevision == 3 || encRevision == 4) {
                for (int i = 0; i < 50; i++) {
                    md.reset();
                    md.update(digest, 0, length);
                    digest = md.digest();
                }
            }
            if (encRevision == 2 && length != 5) {
                throw new CryptographyException("Error: length should be 5 when revision is two actual=" + length);
            }
            System.arraycopy(digest, 0, result, 0, length);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptographyException(e);
        }
        return result;
    }
