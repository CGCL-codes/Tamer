    public String getDigest(Object[] hashes) throws Exception {
        MessageDigest md = MessageDigest.getInstance(hashName);
        for (int x = 0; x < hashes.length; x++) {
            md.update((byte[]) hashes[x]);
        }
        byte[] dig = md.digest();
        return Base16.toHexString(dig);
    }
