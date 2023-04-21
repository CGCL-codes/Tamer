    public static String calcMd5(String arg) {
        byte[] source = arg.getBytes();
        MessageDigest digest = getMd5Digest();
        digest.update(source);
        byte[] target = digest.digest();
        return toHex(target);
    }
