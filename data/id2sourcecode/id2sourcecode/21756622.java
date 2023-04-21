    public static String createMD5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(input.getBytes("UTF-8"));
            return StringHelper.toHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Failed to find MD5 codec", e);
        } catch (UnsupportedEncodingException e) {
            LOGGER.log(Level.SEVERE, "Failed to find UTF-8 encoding", e);
        }
        assert false : "We failed to MD5 encode a string.";
        return null;
    }
