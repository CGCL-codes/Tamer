    public static String md5(String value) {
        String ret = "";
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            byte[] bytes = value.getBytes();
            int orig = bytes.length;
            int dest = orig / 16;
            if (bytes.length % 16 != 0) {
                dest += 16;
            }
            byte[] msg = new byte[dest];
            instance.update(bytes, 0, orig);
            int size = instance.digest(msg, 0, dest);
            ret = bytesToHex(msg, size);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ret;
    }
