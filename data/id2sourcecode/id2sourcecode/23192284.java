    private byte[] md5(String string) {
        md5.reset();
        md5.update(string.getBytes());
        return md5.digest();
    }
