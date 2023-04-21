    @Override
    public String hashString(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] bytesOfMessage = input.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        return bytesToHex(md.digest(bytesOfMessage));
    }
