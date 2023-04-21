    private void refreshCssDigest() throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digestBytes = md.digest(css.getBytes());
        cssDigest = Base64.encodeBase64String(digestBytes);
    }
