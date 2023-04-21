    public static String getTextHash(List<String> stackLines) {
        final String ret;
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            for (String stackLine : stackLines) {
                final byte[] lineBytes = stackLine.getBytes("UTF-8");
                messageDigest.update(lineBytes);
            }
            final byte[] bytes = messageDigest.digest();
            final BigInteger bigInt = new BigInteger(1, bytes);
            ret = bigInt.toString(36);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return ret;
    }
