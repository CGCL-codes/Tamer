    public String getDigest(String arg) {
        try {
            digest.reset();
            digest.update(arg.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] hash = digest.digest();
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            int currentByte = 0x000000FF & hash[i];
            result.append(hexDigits[currentByte / 16]);
            result.append(hexDigits[currentByte % 16]);
        }
        return result.toString();
    }
