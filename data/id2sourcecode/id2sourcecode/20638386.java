    final byte[] ripemd160Hash(String p) {
        final RipeMD160 ripemd160 = new RipeMD160();
        final byte[] data = p.getBytes();
        ripemd160.update(data, 0, data.length);
        final byte[] hash = ripemd160.digest();
        return hash;
    }
