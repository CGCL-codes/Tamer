    private synchronized byte[] getHash(byte[] id, byte[] data) {
        d.reset();
        if (id != null) put00Data(d, id, data); else d.update(data);
        return d.digest();
    }
