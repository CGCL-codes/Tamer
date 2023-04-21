    public static String hex_md5(String message, String charset) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] raw = md.digest(new String(message).getBytes(charset));
            result = byteToHex(raw);
        } catch (NoSuchAlgorithmException ex) {
            logger.error("this should not happen!", ex);
            throw new RuntimeException("No Such Algorithm", ex);
        } catch (UnsupportedEncodingException ex) {
            logger.error("this should not happen!", ex);
            throw new RuntimeException("Unsupported Charset", ex);
        }
        return result;
    }
