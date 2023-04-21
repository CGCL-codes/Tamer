    private static String encrypt(String algorithm, String encoding, String password) {
        if (password == null) return null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] digest = md.digest(password.getBytes(encoding));
            return algorithm + SEPERATOR + toHexString(digest);
        } catch (UnsupportedEncodingException err) {
            ErrorService.error(err);
            return null;
        } catch (NoSuchAlgorithmException err) {
            ErrorService.error(err);
            return null;
        }
    }
