    public byte[] getLoadFileDataHash(boolean includeDebug) {
        try {
            return MessageDigest.getInstance("SHA1").digest(getRawCode(includeDebug));
        } catch (NoSuchAlgorithmException e) {
            GPUtil.debug("Not possible?");
            return null;
        }
    }
