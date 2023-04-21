    private static String hash(String input, String algorithm) throws NoSuchAlgorithmException {
        String hashedInput = null;
        byte[] bytes = input.getBytes();
        MessageDigest md;
        md = MessageDigest.getInstance(algorithm);
        md.update(bytes);
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(Integer.toHexString(b & 0xff));
        }
        hashedInput = sb.toString();
        sb.setLength(0);
        return hashedInput;
    }
