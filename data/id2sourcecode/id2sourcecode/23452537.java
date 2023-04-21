    private long problemId(final String problem) throws SQLException, IOException {
        MessageDigest msgDigest = null;
        try {
            msgDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        final InputStream istr = cspom.Problem.problemInputStream(problem);
        final byte[] buffer = new byte[8192];
        int read;
        while ((read = istr.read(buffer)) > 0) {
            msgDigest.update(buffer, 0, read);
        }
        final String md5sum = Concrete.md5(msgDigest.digest());
        Statement stmt = connection.createStatement();
        final ResultSet rst = stmt.executeQuery("SELECT problemId FROM problems WHERE md5 = '" + md5sum + "'");
        final long problemId;
        if (rst.next()) {
            problemId = rst.getInt(1);
        } else {
            stmt.executeUpdate("INSERT INTO problems(name, md5) VALUES ('" + new File(problem).getName() + "', '" + md5sum + "')");
            final ResultSet aiRst = stmt.getGeneratedKeys();
            if (aiRst.next()) {
                problemId = aiRst.getInt(1);
            } else {
                throw new SQLException("Could not retrieve generated key");
            }
        }
        return problemId;
    }
