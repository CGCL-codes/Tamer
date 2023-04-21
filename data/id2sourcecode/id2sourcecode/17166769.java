    private String getDigest(String password) {
        String key = apopChallenge + password;
        byte[] digest;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            digest = md.digest(key.getBytes("iso-8859-1"));
        } catch (NoSuchAlgorithmException nsae) {
            return null;
        } catch (UnsupportedEncodingException uee) {
            return null;
        }
        return toHex(digest);
    }
