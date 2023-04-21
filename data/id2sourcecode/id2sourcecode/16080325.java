    private byte[] nextBlock() throws LimitReachedException {
        int count = pad - 'A' + 1;
        if (count > 26) throw new LimitReachedException();
        for (int i = 0; i < count; i++) sha.update(pad);
        sha.update(secret, 0, secret.length);
        sha.update(seed, 0, seed.length);
        byte[] b = sha.digest();
        md5.update(secret, 0, secret.length);
        md5.update(b, 0, b.length);
        idx = 0;
        pad++;
        return md5.digest();
    }
