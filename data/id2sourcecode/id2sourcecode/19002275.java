    private void addPlainUser(String user, String password) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        users.put(user, md5.digest(password.getBytes()));
    }
