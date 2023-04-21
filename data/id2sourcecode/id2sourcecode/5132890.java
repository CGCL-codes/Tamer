    public static String mdString(String _value) throws Exception {
        StringBuffer hexValue = new StringBuffer();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String value = format.format(date);
        if (_value != null) {
            value += _value;
        }
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte b[] = md.digest(value.getBytes());
        for (int i = 0; i < b.length; i++) {
            int val = ((int) b[i]) & 0xff;
            if (val < 16) hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
