    public static String hash(String _passwd, String _szAuthNo) {
        byte[] msg = null;
        byte[] msgSunDigest = null;
        MessageDigest md5Sun = null;
        _passwd += _szAuthNo;
        try {
            msg = _passwd.getBytes("ISO-8859-1");
            md5Sun = MessageDigest.getInstance("MD5");
            md5Sun.update(msg);
            msgSunDigest = md5Sun.digest(msg);
            return new String(msgSunDigest, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
