    public static String hashMD5(String s) {
        byte[] bytearr = s.getBytes();
        MessageDigest md;
        String hash = "";
        try {
            md = MessageDigest.getInstance("md5");
            md.reset();
            md.update(bytearr);
            byte[] result = md.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                hexString.append(Integer.toHexString((0xFF & result[i]) | 0x100).substring(1, 3));
            }
            hash = hexString.toString();
        } catch (NoSuchAlgorithmException nsa) {
            System.out.println(nsa.getMessage());
        }
        return hash;
    }
