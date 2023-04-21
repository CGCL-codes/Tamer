    public void setClassDefinition(String name, ByteBuffer def) {
        Connection con = null;
        try {
            con = ds.getConnection();
            con.setAutoCommit(false);
            int snapshot = getSnapshotIndex(con);
            byte[] bytes = new byte[def.remaining()];
            def.get(bytes);
            MessageDigest alg;
            try {
                alg = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                DbUtil.rollback(con);
                throw new UnexpectedException(e);
            }
            byte[] digest = alg.digest(bytes);
            String sql = "SELECT COUNT(1) " + "FROM ParentClasses " + "WHERE Name = ? " + "  AND SnapshotIndex = ?";
            if (DbUtil.queryInt(con, 0, sql, name, snapshot) > 0) {
                DbUtil.update(con, "UPDATE ParentClasses " + "SET " + "  Definition = ?, " + "  MD5 = ? " + "WHERE Name = ? " + "  AND SnapshotIndex = ?", bytes, digest, name, snapshot);
            } else {
                DbUtil.update(con, "INSERT INTO ParentClasses " + "  (SnapshotIndex, Name, Definition, MD5) " + "VALUES (?, ?, ?, ?)", snapshot, name, bytes, digest);
            }
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            DbUtil.rollback(con);
            logger.error("Unable to persist class definition to database.", e);
            throw new RuntimeException(e);
        } finally {
            DbUtil.close(con);
        }
    }
