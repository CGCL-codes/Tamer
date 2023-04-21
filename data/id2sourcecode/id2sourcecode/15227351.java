    public static UUID Combine(UUID first, UUID second) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        byte[] input = new byte[32];
        first.ToBytes(input, 0);
        second.ToBytes(input, 16);
        return new UUID(md.digest(input));
    }
