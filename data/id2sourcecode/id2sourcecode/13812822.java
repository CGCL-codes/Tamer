    public static String doPasswordDigest(String nonce, String created, String password) {
        String passwdDigest = null;
        try {
            byte[] b1 = Base64.decode(nonce);
            byte[] b2 = created.getBytes("UTF-8");
            byte[] b3 = password.getBytes("UTF-8");
            byte[] b4 = new byte[b1.length + b2.length + b3.length];
            int i = 0;
            int offset = 0;
            System.arraycopy(b1, 0, b4, offset, b1.length);
            offset += b1.length;
            System.arraycopy(b2, 0, b4, offset, b2.length);
            offset += b2.length;
            System.arraycopy(b3, 0, b4, offset, b3.length);
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            sha.reset();
            sha.update(b4);
            passwdDigest = Base64.encode(sha.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return passwdDigest;
    }
