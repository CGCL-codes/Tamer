    private synchronized byte[] bhash(byte[] src) {
        _md.update(src);
        return _md.digest();
    }
