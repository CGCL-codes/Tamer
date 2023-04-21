    @Test
    public void duplicate_key_add() {
        makeTable(3);
        Transaction t = db.readwriteTran();
        try {
            t.addRecord("test", record(1));
            fail("expected exception");
        } catch (SuException e) {
            assertTrue(e.toString().contains("duplicate key: a"));
        } finally {
            t.ck_complete();
        }
        t = db.readonlyTran();
        try {
            assertEquals(3, t.getTableData(t.getTable("test").num).nrecords);
        } finally {
            t.ck_complete();
        }
    }
