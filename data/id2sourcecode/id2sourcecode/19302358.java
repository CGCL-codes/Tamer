    private String getHashFileName(String pageName) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] dig;
        try {
            dig = digest.digest(pageName.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new InternalWikiException("AAAAGH!  UTF-8 is gone!  My eyes!  It burns...!");
        }
        return TextUtil.toHexString(dig) + ".cache";
    }
