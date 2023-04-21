    public void deleteFilter(long filterid) {
        DBConnection con = getConnection(false);
        try {
            PreparedStatement query = con.prepareStatement("delete msg_FilterRedirect where filterID=?");
            query.setLong(1, filterid);
            con.executeUpdate(query, null);
            query = con.prepareStatement("delete msg_Filter where ID=? and user_oid=?");
            query.setLong(1, filterid);
            query.setBigDecimal(2, user_oid);
            if (con.executeUpdate(query, null) == 0) {
                throw new HamboFatalException("Tried to delete bogus filter #" + filterid);
            }
            con.commit();
        } catch (Exception e) {
            try {
                con.rollback();
            } catch (Exception err2) {
                logError("Failed to rollback after " + e, err2);
            }
            if (e instanceof RuntimeException) throw (RuntimeException) e; else throw new HamboFatalException("Failed to delete filter", e);
        } finally {
            releaseConnection(con);
        }
    }
