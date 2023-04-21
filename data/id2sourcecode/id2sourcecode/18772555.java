    @Test
    public void foreign_key_cascade_deletes() {
        makeTable(3);
        db.addTable("test2");
        db.addColumn("test2", "a");
        db.addColumn("test2", "f");
        db.addIndex("test2", "a", true);
        db.addIndex("test2", "f", false, false, false, "test", "a", Index.CASCADE_DELETES);
        Transaction t1 = db.readwriteTran();
        t1.addRecord("test2", record(10, 1));
        t1.addRecord("test2", record(11, 1));
        t1.ck_complete();
        Transaction t2 = db.readwriteTran();
        t2.removeRecord("test", "a", key(1));
        t2.ck_complete();
        assertEquals(0, get("test2").size());
    }
