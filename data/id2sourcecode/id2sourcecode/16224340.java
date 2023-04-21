    public String calcHash(String value) {
        md.reset();
        try {
            md.update(value.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new UnexpectedErrorException(e);
        }
        byte[] digest = md.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            String hex = Integer.toHexString(0xFF & digest[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
