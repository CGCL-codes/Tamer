    public synchronized String nextId(HttpServletRequest request) {
        if (_digest == null) {
            _digest = getDigest();
        }
        if (_random == null) {
            _random = getRandom();
        }
        byte[] bytes = new byte[SESSION_ID_BYTES];
        _random.nextBytes(bytes);
        bytes = _digest.digest(bytes);
        return encode(bytes);
    }
