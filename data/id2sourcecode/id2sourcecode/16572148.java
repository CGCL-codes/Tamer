    public String heshData(String data) {
        String heshData = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] encodedData = md.digest(data.getBytes());
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < encodedData.length; i++) {
                hexString.append(Integer.toHexString(0xFF & encodedData[i]));
            }
            heshData = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
        }
        return heshData;
    }
