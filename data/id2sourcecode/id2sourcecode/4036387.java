    private byte nextByte() {
        _digest.update(_rootSeed);
        byte[] hash = _digest.digest(_iterativeSeed);
        _digest.reset();
        System.arraycopy(hash, 0, _iterativeSeed, 0, _iterativeSeed.length);
        return hash[hash.length - 1];
    }
