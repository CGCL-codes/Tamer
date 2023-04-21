    public void demoTransaction() throws SQLException, ClassNotFoundException, FileNotFoundException {
        TransSQLExecutor tse = new TransSQLExecutor();
        DBRow dr = new DBRow("Customer", "seqNo");
        dr.setColumn("name", "Winwell");
        dr.setColumn("phone", "0988168168");
        dr.setColumn("address", "Hsinchu City, Taiwan");
        tse.executeUpdate(dr.toInsertString());
        tse.rollback();
        dr = new DBRow("Customer", "seqNo");
        dr.setColumn("name", "FFEH");
        dr.setColumn("phone", "0968168168");
        dr.setColumn("address", "Hsinchu City, Taiwan");
        tse.executeUpdate(dr.toInsertString());
        tse.commit();
        tse.close();
    }
