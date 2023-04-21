    @Override
    protected synchronized char[] getCacheFileName(String key) {
        if ((key == null) || (key.length() == 0)) {
            throw new IllegalArgumentException("Invalid key '" + key + "' specified to getCacheFile.");
        }
        byte[] digest = md.digest(key.getBytes());
        return byteArrayToHexString(digest).toCharArray();
    }
