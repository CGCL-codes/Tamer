    @Override
    public void close() {
        if (digest == null) {
            digest = hash.digest();
        }
    }
