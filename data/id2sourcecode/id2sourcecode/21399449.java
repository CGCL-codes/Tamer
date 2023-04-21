    @ManagedOperation(description = "Change password")
    public boolean changePassword(String userName, String oldPassword, String newPassword) {
        if (log.isDebugEnabled()) log.debug("begin changePassword mbean");
        Connection conn = null;
        boolean valid = false;
        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup(datasource);
            conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement(atSqlGetUser);
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                byte[] hash = MessageDigest.getInstance(hashAlgorithm).digest(newPassword.getBytes());
                String newPasswd = Base64.encodeBytes(hash);
                ps.close();
                PreparedStatement psu = conn.prepareStatement(atSqlUpdateUser);
                psu.setString(2, userName);
                psu.setString(1, newPasswd);
                psu.execute();
                psu.close();
            }
            rs.close();
        } catch (Exception exe) {
            log.error("Error occured", exe);
        } finally {
            try {
                conn.close();
            } catch (Exception exe) {
            }
        }
        if (log.isDebugEnabled()) log.debug("end changePassword mbean");
        return valid;
    }
