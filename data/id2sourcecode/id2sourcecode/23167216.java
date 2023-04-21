    public synchronized void setUser(String username, String password) {
        md5.reset();
        try {
            md5.update(username.getBytes("UTF-8"));
            md5.update((byte) ':');
            md5.update(VWDIFF_REALM.getBytes("UTF-8"));
            md5.update((byte) ':');
            md5.update(password.getBytes("UTF-8"));
            users.put(username, md5.digest());
        } catch (java.io.UnsupportedEncodingException shouldNotHappen) {
        }
    }
