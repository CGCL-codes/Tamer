    public static String getNextID() {
        try {
            String str = String.valueOf(secureRnd.nextLong());
            digest.update(str.getBytes("UTF8"));
            byte[] hash = digest.digest();
            return Base64.encodeBytes(hash);
        } catch (UnsupportedEncodingException e) {
            throw new UndeclaredThrowableException(e);
        }
    }
