    @Test
    public void duplicate_key_update() {
        makeTable(3);
        Transaction t = db.readwriteTran();
        try {
            t.updateRecord("test", "a", new Record().add(1), record(2));
            fail("expected exception");
        } catch (SuException e) {
            assertTrue(e.toString().contains("update record: duplicate key: a"));
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
