    protected String getMAC(String s) {
        int i = 0;
        for (int j = 0; j < s.length(); j++) {
            i += (byte) s.charAt(j);
        }
        s = String.valueOf(i);
        s += getSecret();
        String s2 = "";
        byte b[];
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            b = md.digest(s.getBytes());
            for (i = 0; i < b.length; i++) {
                if ((b[i] & 0xFF) < 16) s2 += "0";
                s2 += Integer.toHexString(b[i] & 0xFF).toUpperCase();
            }
        } catch (Exception e) {
            log.error(e);
        }
        return s2;
    }
