    String calculateMD5(String in) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] pre_md5 = md.digest(in.getBytes("LATIN1"));
            String md5 = "";
            for (int i = 0; i < 16; i++) {
                if (pre_md5[i] < 0) {
                    md5 += Integer.toHexString(256 + pre_md5[i]);
                } else if (pre_md5[i] > 15) {
                    md5 += Integer.toHexString(pre_md5[i]);
                } else {
                    md5 += "0" + Integer.toHexString(pre_md5[i]);
                }
            }
            return md5;
        } catch (UnsupportedEncodingException ex) {
            logError("Unsupported encoding.", ex);
            return "";
        } catch (NoSuchAlgorithmException ex) {
            logError("No such algorithm.", ex);
            return "";
        }
    }
