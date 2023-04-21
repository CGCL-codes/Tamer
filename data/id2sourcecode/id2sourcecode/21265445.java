    private static String hashPassword(String password) {
        byte digest[] = Security.digest(password.getBytes());
        StringBuffer hash = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            String c = Integer.toHexString((0xFFFFFFFF & digest[i]) & 0xFF);
            if (c.length() == 1) {
                c = '0' + c;
                hash = hash.append("0").append(c);
            } else {
                hash = hash.append(c);
            }
        }
        System.out.println("Hash:" + hash.toString());
        return hash.toString().toLowerCase();
    }
