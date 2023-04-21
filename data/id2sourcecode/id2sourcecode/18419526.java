        public void setClassDefinition(String name, ByteBuffer def) {
            check();
            Connection con = null;
            try {
                con = ds.getConnection();
                con.setAutoCommit(false);
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
                String sql = "SELECT COUNT(1) " + "FROM ChildClasses " + "WHERE ChildID = ? " + "  AND Name = ?";
                if (DbUtil.queryInt(con, 0, sql, id, name) > 0) {
                    DbUtil.update(con, "UPDATE ChildClasses " + "SET " + "  Definition = ?, " + "  MD5 = ? " + "WHERE ChildID = ? " + "  AND Name = ?", bytes, digest, id, name);
                } else {
                    DbUtil.update(con, "INSERT INTO ChildClasses " + "  (ChildID, Name, Definition, MD5) " + "VALUES (?, ?, ?, ?)", id, name, bytes, digest);
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
