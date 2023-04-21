    public void register(String name) throws UserDBException, SQLException {
        con.setAutoCommit(false);
        try {
            queryRegUserPS.setString(1, name);
            rs = queryRegUserPS.executeQuery();
            _found = rs.next();
            if (!_found) {
                insertNewRegUserPS.setString(1, name);
                if (insertNewRegUserPS.executeUpdate() != 1) {
                    throw new UserDBException("insertRegNewUser had no effect");
                }
            } else {
                throw new UserDBException("no such user found");
            }
        } catch (SQLException sqle) {
            con.rollback();
            con.setAutoCommit(true);
            throw new UserDBException(sqle.getMessage());
        }
        con.commit();
        con.setAutoCommit(true);
    }
