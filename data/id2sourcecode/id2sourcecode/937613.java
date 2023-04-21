    public void setPassword(String passwd) {
        try {
            MessageDigest sha = MessageDigest.getInstance("MD5");
            byte[] tmp = passwd.getBytes();
            sha.update(tmp);
            password = new String(sha.digest());
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println("MD5 doesn't exist");
            System.out.println(e.toString());
        }
    }
