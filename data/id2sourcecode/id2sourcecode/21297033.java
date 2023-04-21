    public void longProc2(int param) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] temp = new byte[param];
        temp = md.digest(temp);
    }
