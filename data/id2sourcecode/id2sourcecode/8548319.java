    public static String calculateDigest(String plain) {
        try {
            byte[] bytes = getDigest().digest(plain.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02d", ((int) b) & 0xff));
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
