    public String getAvatarHash() {
        byte[] bytes = getAvatar();
        if (bytes == null) {
            return null;
        }
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        digest.update(bytes);
        return StringUtils.encodeHex(digest.digest());
    }
