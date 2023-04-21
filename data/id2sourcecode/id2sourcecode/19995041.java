    public static String getEncodedWithMD5(String texto) {
        StringBuffer output = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = md.digest(texto.getBytes());
            int size = b.length;
            output = new StringBuffer(size);
            for (int i = 0; i < size; i++) {
                int u = b[i] & 255;
                if (u < 16) {
                    output.append("0" + Integer.toHexString(u));
                } else {
                    output.append(Integer.toHexString(u));
                }
            }
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return output.toString();
    }
