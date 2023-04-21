    public static synchronized String calcHash(byte[] data) {
        byte[] buf = new byte[data.length];
        for (int i = 0; i < data.length; i++) buf[i] = data[i];
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(buf);
            return convertToHex(md.digest()) + buf.length;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
