    public static final String getMessageDigest(String text, String algorithm) {
        try {
            byte[] digesta = MessageDigest.getInstance(algorithm).digest(text.getBytes());
            StringBuilder sb = new StringBuilder();
            String str = null;
            for (int i = 0; i < digesta.length; i++) {
                str = Integer.toHexString(0xFF & digesta[i]);
                sb.append((str.length() == 1) ? StringConstants.ZERO + str : str);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new CustomRuntimeException(e);
        }
    }
