    private synchronized int readCount() {
        int count = 0;
        try {
            ResultSet rs = mSelectStmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            mUpdateStmt.setInt(1, count + 1);
            mUpdateStmt.executeUpdate();
            mConnection.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                mConnection.rollback();
            } catch (Exception ex1) {
            }
            count = 0;
        }
        return count;
    }
