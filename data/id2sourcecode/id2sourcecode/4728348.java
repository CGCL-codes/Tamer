    private String getMD5Hash(String timestamp) {
        try {
            byte[] i1 = timestamp.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(i1);
            byte[] digest = md5.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                String token = Integer.toHexString(0xFF & digest[i]);
                String zero = "";
                if (token.length() < 2) zero = "0";
                hexString.append(zero + token);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
            return null;
        }
    }
