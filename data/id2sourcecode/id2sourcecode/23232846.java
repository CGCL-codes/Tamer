    public int digest(String string) {
        md5.update(string.getBytes());
        byte[] digest = md5.digest();
        int result = 0;
        for (int q = 0; q < digest.length; q++) {
            result = result ^ digest[q] << ((3 - (q % 4)) << 3);
        }
        return result;
    }
