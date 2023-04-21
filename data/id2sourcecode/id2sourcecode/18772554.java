    @Test
    public void foreign_key_addIndex2() {
        makeTable(3);
        db.addTable("test2");
        db.addColumn("test2", "a");
        db.addColumn("test2", "f1");
        db.addColumn("test2", "f2");
        db.addIndex("test2", "a", true);
        Transaction t1 = db.readwriteTran();
        t1.addRecord("test2", record(10, 1, 5));
        t1.ck_complete();
        db.addIndex("test2", "f1", false, false, false, "test", "a", Index.BLOCK);
        try {
            db.addIndex("test2", "f2", false, false, false, "test", "a", Index.BLOCK);
            fail("expected exception");
        } catch (SuException e) {
            assertTrue(e.toString().contains("blocked by foreign key"));
        }
    }
