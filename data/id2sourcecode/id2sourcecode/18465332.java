    private byte[] generateKey(MessageDigest hash, byte[] passphrase, byte[] iv) {
        byte[] key = new byte[24];
        int hsize = hash.getDigestLength();
        byte[] hn = new byte[key.length / hsize * hsize + (key.length % hsize == 0 ? 0 : hsize)];
        try {
            byte[] tmp = null;
            for (int index = 0; index + hsize <= hn.length; ) {
                if (tmp != null) {
                    hash.update(tmp, 0, tmp.length);
                }
                hash.update(passphrase, 0, passphrase.length);
                hash.update(iv, 0, iv.length);
                tmp = hash.digest();
                System.arraycopy(tmp, 0, hn, index, tmp.length);
                index += tmp.length;
            }
            System.arraycopy(hn, 0, key, 0, key.length);
        } catch (Exception e) {
            Activator.getServices().error("generateKey()", e);
        }
        return key;
    }
