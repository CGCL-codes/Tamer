    public static String upperEnglish(String source) {
        if (source == null) return "";
        source = source.trim();
        byte[] chars = source.getBytes();
        StringBuffer sb = new StringBuffer(chars.length);
        byte[] chinese = new byte[2];
        int count = 0;
        int line = 0;
        int i = 0;
        for (; i < chars.length; i++) {
            if (chars[i] < 0) {
                chinese[0] = chars[i];
                chinese[1] = chars[i + 1];
                sb.append(new String(chinese));
                i++;
            } else {
                String tmpstr = (char) chars[i] + "";
                tmpstr = tmpstr.toUpperCase();
                sb.append(tmpstr);
            }
        }
        return sb.toString();
    }
