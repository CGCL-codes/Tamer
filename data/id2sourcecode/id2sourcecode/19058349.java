    @Override
    public int addRow(int row) {
        synchronized (this) {
            Checks.inRange("row", row, -1, getRowCount());
            try {
                if (row < 0) row = getRowCount();
                incrementOrdinalsStmt.setInt(1, row);
                incrementOrdinalsStmt.executeUpdate();
                insertRowStmt.setInt(1, row);
                insertRowStmt.executeUpdate();
                connection.commit();
                return row;
            } catch (Throwable xp) {
                try {
                    connection.rollback();
                } catch (Throwable xp1) {
                }
                throw new AnnoneException(Text.get("Can''t add row."), xp);
            }
        }
    }
