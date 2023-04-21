    @Override
    public void removeRow(int row) {
        synchronized (this) {
            Checks.inRange("row", row, 0, getRowCount() - 1);
            try {
                deleteRowStmt.setInt(1, row);
                deleteRowStmt.executeUpdate();
                decrementOrdinalsStmt.setInt(1, row + 1);
                decrementOrdinalsStmt.executeUpdate();
                connection.commit();
            } catch (Throwable xp) {
                try {
                    connection.rollback();
                } catch (Throwable xp1) {
                }
                throw new AnnoneException(Text.get("Can''t add row."), xp);
            }
        }
    }
