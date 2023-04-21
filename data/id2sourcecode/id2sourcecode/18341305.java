    public static String Hash(byte[][] bss) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            for (byte[] bs : bss) {
                md.update(bs);
            }
            byte[] digest = md.digest();
            String hash = HexUtil.PrintHexDigits(digest);
            return hash;
        } catch (Throwable th) {
            throw new RuntimeException("Failed to hash data:" + th.getMessage(), th);
        }
    }
