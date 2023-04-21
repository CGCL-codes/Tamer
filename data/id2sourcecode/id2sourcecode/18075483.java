    public String Encripto(String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] b = md.digest(password.getBytes());
            int size = b.length;
            StringBuffer h = new StringBuffer(size);
            for (int i = 0; i < size; i++) {
                int u = b[i] & 255;
                if (u < 16) {
                    h.append("0" + Integer.toHexString(u));
                } else {
                    h.append(Integer.toHexString(u));
                }
            }
            return h.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(convertir_md5.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("murio" + ex);
            return "";
        }
    }
