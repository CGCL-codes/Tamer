    public String encryptPassword(String passwd) throws SukuException {
        byte[] tunnus = passwd.getBytes();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new SukuException(e);
        }
        md.update(tunnus);
        byte digest[] = md.digest();
        StringBuilder pw = new StringBuilder();
        for (byte b : digest) {
            String a = "00" + Integer.toHexString(b & 0xff);
            pw.append(a.substring(a.length() - 2));
        }
        return pw.toString();
    }
