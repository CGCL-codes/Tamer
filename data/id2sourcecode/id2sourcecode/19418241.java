    public static String devuelveHash(String txtOrigen) {
        String devuelve = "";
        txtOrigen = txtOrigen.trim();
        txtOrigen = txtOrigen.toUpperCase();
        try {
            BASE64Encoder encode = new BASE64Encoder();
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] input = txtOrigen.getBytes("UTF8");
            sha.update(input);
            byte[] myhash = sha.digest();
            devuelve = encode.encode(myhash);
        } catch (Exception e) {
            System.out.println("error : " + e);
        }
        return devuelve;
    }
