    private static byte[] digest(byte[] input) {
        md.reset();
        md.update(input);
        return md.digest();
    }
