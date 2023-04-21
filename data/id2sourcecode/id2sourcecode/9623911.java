    public static String createCheckSum(String input, String algorithm) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] md5Bytes = null;
        FileInputStream fis = new FileInputStream(input);
        byte[] fileBytes = new byte[1000];
        while (fis.read(fileBytes) > 0) {
            md.update(fileBytes);
        }
        md5Bytes = md.digest();
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
