    private String cryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] raw = password.getBytes("UTF-8");
            byte[] hash = md.digest(raw);
            return Base64.encodeBytes(hash);
        } catch (NoSuchAlgorithmException e) {
            _log.severe("[SecondaryPasswordAuth]Unsupported Algorythm");
        } catch (UnsupportedEncodingException e) {
            _log.severe("[SecondaryPasswordAuth]Unsupported Encoding");
        }
        return null;
    }
