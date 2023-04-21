    public static char[] digest(char[] target, String algorithm) {
        return DigestUtils.digest(new String(target), algorithm).toCharArray();
    }
