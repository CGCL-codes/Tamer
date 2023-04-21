    public byte[] hash(byte[] datas) {
        algorithm.reset();
        algorithm.update(datas);
        return algorithm.digest();
    }
