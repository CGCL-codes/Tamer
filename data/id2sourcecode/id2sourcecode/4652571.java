    public void delete(DbHelper db) throws SQLException {
        try {
            db.executeUpdate("DELETE FROM event WHERE session = ?", this.getId());
            db.executeUpdate("DELETE FROM message WHERE session = ?", this.getId());
            db.executeUpdate("DELETE FROM agent WHERE session = ?", this.getId());
            db.executeUpdate("DELETE FROM session WHERE id = ?", this.getId());
            db.commit();
            setChanged();
            notifyObservers("delete");
        } catch (SQLException ex) {
            db.rollback();
            Logger.getLogger(SessionRecord.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
    }
