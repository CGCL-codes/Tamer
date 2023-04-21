    private String getMD5hash(String code) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException!");
        }
        StringBuffer sb = new StringBuffer();
        byte[] array = md.digest(code.getBytes());
        String bytestring = "";
        for (byte b : array) {
            String hexString = Integer.toHexString(b);
            switch(hexString.length()) {
                case 1:
                    bytestring = "0" + hexString;
                    break;
                case 2:
                    bytestring = hexString;
                    break;
                case 8:
                    bytestring = hexString.substring(6, 8);
                    break;
            }
            sb.append(bytestring);
        }
        return sb.toString();
    }
