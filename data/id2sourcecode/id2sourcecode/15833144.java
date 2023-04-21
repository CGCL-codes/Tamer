    public boolean checkPassword(byte[] key, byte[] response, boolean def) {
        String password = (String) getConfig().get(passwordKey, null);
        if (password == null) return def;
        password = password.trim();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsa) {
            Log.error("DataNode: Can't get MD5 implementation " + nsa);
            return false;
        }
        md.update(key);
        byte[] hash = md.digest(password.getBytes());
        if (hash.length != response.length) return false;
        for (int i = 0; i < hash.length; i++) {
            if (hash[i] != response[i]) return false;
        }
        return true;
    }
