    public void insert(InetAddress addr) {
        byte[] hash = sha1.digest(addr.getAddress());
        int index1 = (hash[0] & 0xFF) | (hash[1] & 0xFF) << 8;
        int index2 = (hash[2] & 0xFF) | (hash[3] & 0xFF) << 8;
        index1 %= m;
        index2 %= m;
        filter.set(index1);
        filter.set(index2);
    }
