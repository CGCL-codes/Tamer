    public String md5(String input) {
        StringBuffer sb = new StringBuffer();
        String s = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte digest[] = md.digest(input.getBytes());
            for (int i = 0; i < digest.length; i++) {
                s = Integer.toHexString(digest[i] & 0xFF);
                if (s.length() == 1) {
                    sb.append("0");
                    sb.append(s);
                } else sb.append(s);
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error("md5(String)", e);
            return null;
        }
        return sb.toString();
    }
