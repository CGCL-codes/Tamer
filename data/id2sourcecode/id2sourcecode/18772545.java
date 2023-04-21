    @Test
    public void test() {
        Table tbl = db.getTable("indexes");
        assertEquals("indexes", tbl.name);
        makeTable();
        tbl = db.getTable("test");
        assertEquals(2, tbl.columns.size());
        assertEquals(2, tbl.indexes.size());
        Record r = new Record().add(12).add(34);
        Transaction t = db.readwriteTran();
        t.addRecord("test", r);
        t.ck_complete();
        assertEquals(1, db.getNrecords("test"));
        List<Record> recs = get("test");
        assertEquals(1, recs.size());
        assertEquals(r, recs.get(0));
        reopen();
        assertEquals(1, db.getNrecords("test"));
        recs = get("test");
        assertEquals(1, recs.size());
        assertEquals(r, recs.get(0));
        t = db.readwriteTran();
        t.removeRecord("test", "a", new Record().add(12));
        t.ck_complete();
        assertEquals(0, get("test").size());
    }
