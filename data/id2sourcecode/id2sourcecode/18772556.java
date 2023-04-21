    @Test
    public void foreign_key_cascade_updates() {
        makeTable(3);
        db.addTable("test2");
        db.addColumn("test2", "a");
        db.addColumn("test2", "f");
        db.addIndex("test2", "a", true);
        db.addIndex("test2", "f", false, false, false, "test", "a", Index.CASCADE_UPDATES);
        Table table = db.getTable("test");
        ForeignKey fk = table.getIndex("a").fkdsts.get(0);
        assertEquals(db.getTable("test2").num, fk.tblnum);
        assertEquals("f", fk.columns);
        assertEquals(Index.CASCADE_UPDATES, fk.mode);
        Transaction t1 = db.readwriteTran();
        t1.addRecord("test2", record(10, 1));
        t1.addRecord("test2", record(11, 1));
        t1.ck_complete();
        Transaction t2 = db.readwriteTran();
        t2.updateRecord("test", "a", key(1), record(111));
        t2.ck_complete();
        List<Record> recs = get("test2");
        assertEquals(2, recs.size());
        assertEquals(record(10, 111), recs.get(0));
        assertEquals(record(11, 111), recs.get(1));
    }
