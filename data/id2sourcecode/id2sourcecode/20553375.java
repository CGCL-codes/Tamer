    public synchronized boolean loginChecker(String uid, String password) {
        boolean out = false;
        String result = "";
        String encryptedString = uid + password;
        int i;
        java.security.MessageDigest md = null;
        try {
            md = java.security.MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        byte[] pw = encryptedString.getBytes();
        for (i = 0; i < pw.length; i++) {
            int vgl = pw[i];
            if (vgl < 0) vgl += 256;
            if (32 < vgl) md.update(pw[i]);
        }
        byte[] bresult = md.digest();
        result = "";
        for (i = 0; i < bresult.length; i++) {
            int counter = bresult[i];
            if (counter < 0) counter += 256;
            String counterStr = Integer.toString(counter, 16);
            while (counterStr.length() < 2) counterStr = '0' + counterStr;
            result += counterStr;
        }
        try {
            Object ret;
            ResultSet rs;
            Statement stmt;
            String sql = "SELECT uid FROM users WHERE (uid='" + uid + "'AND password='" + result + "'AND allowed=true)";
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                rs.close();
                stmt.close();
                out = true;
            } else {
                rs.close();
                stmt.close();
                out = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return out;
    }
