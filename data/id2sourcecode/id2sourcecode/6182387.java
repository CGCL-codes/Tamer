    private static final void expand(MessageDigest digest, int hmacSize, byte[] secret, int secOff, int secLen, byte[] label, byte[] seed, byte[] output) throws DigestException {
        byte[] pad1 = HMAC_ipad.clone();
        byte[] pad2 = HMAC_opad.clone();
        for (int i = 0; i < secLen; i++) {
            pad1[i] ^= secret[i + secOff];
            pad2[i] ^= secret[i + secOff];
        }
        byte[] tmp = new byte[hmacSize];
        byte[] aBytes = null;
        int remaining = output.length;
        int ofs = 0;
        while (remaining > 0) {
            digest.update(pad1);
            if (aBytes == null) {
                digest.update(label);
                digest.update(seed);
            } else {
                digest.update(aBytes);
            }
            digest.digest(tmp, 0, hmacSize);
            digest.update(pad2);
            digest.update(tmp);
            if (aBytes == null) {
                aBytes = new byte[hmacSize];
            }
            digest.digest(aBytes, 0, hmacSize);
            digest.update(pad1);
            digest.update(aBytes);
            digest.update(label);
            digest.update(seed);
            digest.digest(tmp, 0, hmacSize);
            digest.update(pad2);
            digest.update(tmp);
            digest.digest(tmp, 0, hmacSize);
            int k = Math.min(hmacSize, remaining);
            for (int i = 0; i < k; i++) {
                output[ofs++] ^= tmp[i];
            }
            remaining -= k;
        }
    }
