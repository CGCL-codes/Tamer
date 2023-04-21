    public static String hash(String raw) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] data = md.digest(raw.getBytes("UTF-8"));
            return Base64Utils.toBase64(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No MD5 algorithm", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("No UTF-8", e);
        }
    }
