    public void authorize(final String username, final String password, final int operation) throws PermissionException, SQLException {
        try {
            Connection c = dataStore.getConnection();
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] passBytes = sha.digest(password.getBytes());
            String passwordDigest = HexDump.byteArrayToHexString(passBytes).toLowerCase().replaceAll(" ", "");
            PreparedStatement ps = c.prepareStatement("select passwordDigest from users where userDN=?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs != null) {
                if (rs.next()) {
                    String passD = rs.getString(1);
                    if (passwordDigest.equals(passD)) return;
                }
            }
            throw new PermissionException("authorize failed for username " + username);
        } catch (java.security.NoSuchAlgorithmException e) {
        }
    }
