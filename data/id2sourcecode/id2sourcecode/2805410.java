    public static String getMD5(String aMsg) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] lBufSource = aMsg.getBytes("UTF-8");
            byte[] lBufTarget = md.digest(lBufSource);
            Formatter formatter = new Formatter();
            for (byte b : lBufTarget) {
                formatter.format("%02x", b);
            }
            return (formatter.toString());
        } catch (Exception ex) {
            System.out.println("getMD5: " + ex.getMessage());
        }
        return null;
    }
