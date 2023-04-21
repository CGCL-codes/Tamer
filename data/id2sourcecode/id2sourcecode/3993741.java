    public static String getMD5(String src) {
        byte[] buf = md.digest(src.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buf.length; i++) {
            sb.append(Integer.toString(new Byte(buf[i]).intValue(), Character.MAX_RADIX));
        }
        return sb.toString();
    }
