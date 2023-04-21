    public int createAccount(final String username, final String password) throws SQLException {
        int userID = -1;
        try {
            Connection conn = dataStore.getConnection();
            SecureRandom sr = new SecureRandom();
            Connection connTwo = dataStore.getConnection();
            Statement st = connTwo.createStatement();
            PreparedStatement userIns = conn.prepareStatement("insert into users values (?,?,?)");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] passBytes = sha.digest(password.getBytes());
            String passwordDigest = HexDump.byteArrayToHexString(passBytes).toLowerCase().replaceAll(" ", "");
            while (true) {
                userID = sr.nextInt(Integer.MAX_VALUE);
                userIns.setInt(1, userID);
                userIns.setString(2, username);
                userIns.setString(3, passwordDigest);
                int updated = userIns.executeUpdate();
                if (updated == 1) {
                    break;
                }
            }
            dataStore.releaseConnection(conn);
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return userID;
    }
