    public static String md5(String _encrypt) {
        try {
            byte[] _digest = MessageDigest.getInstance("MD5").digest(_encrypt.getBytes());
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < _digest.length; i++) {
                String hexDigitStr = Integer.toHexString(0xFF & _digest[i]);
                if (hexDigitStr.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hexDigitStr);
            }
            return (hexString.toString());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Every VM should have MD5.");
        }
    }
