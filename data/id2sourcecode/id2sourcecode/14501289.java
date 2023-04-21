    private static void addOrUpdateAccount(String account, String password, String level) throws IOException, SQLException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA");
        byte[] newpass;
        newpass = password.getBytes("UTF-8");
        newpass = md.digest(newpass);
        java.sql.Connection con = null;
        con = L2DatabaseFactory.getInstance().getConnection();
        PreparedStatement statement = con.prepareStatement("REPLACE	accounts (login, password, access_level) VALUES (?,?,?)");
        statement.setString(1, account);
        statement.setString(2, Base64.encodeBytes(newpass));
        statement.setString(3, level);
        statement.executeUpdate();
        statement.close();
    }
