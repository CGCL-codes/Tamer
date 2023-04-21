    synchronized byte[] genKey(byte[] passphrase, byte[] iv) {
        if (cipher == null) cipher = genCipher();
        if (hash == null) hash = genHash();
        byte[] key = new byte[cipher.getBlockSize()];
        int hsize = hash.getBlockSize();
        byte[] hn = new byte[key.length / hsize * hsize + (key.length % hsize == 0 ? 0 : hsize)];
        try {
            byte[] tmp = null;
            if (vendor == VENDOR_OPENSSH) {
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
            } else if (vendor == VENDOR_FSECURE) {
                for (int index = 0; index + hsize <= hn.length; ) {
                    if (tmp != null) {
                        hash.update(tmp, 0, tmp.length);
                    }
                    hash.update(passphrase, 0, passphrase.length);
                    tmp = hash.digest();
                    System.arraycopy(tmp, 0, hn, index, tmp.length);
                    index += tmp.length;
                }
                System.arraycopy(hn, 0, key, 0, key.length);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return key;
    }
