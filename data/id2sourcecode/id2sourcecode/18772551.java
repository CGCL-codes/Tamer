    @Test
    public void foreign_key_addRecord() {
        makeTable(3);
        db.addTable("test2");
        db.addColumn("test2", "b");
        db.addColumn("test2", "f1");
        db.addColumn("test2", "f2");
        db.addIndex("test2", "b", true);
        db.addIndex("test2", "f1", false, false, false, "test", "a", Index.BLOCK);
        db.addIndex("test2", "f2", false, false, false, "test", "a", Index.BLOCK);
        Table test2 = db.getTable("test2");
        Index f1 = test2.indexes.get("f1");
        assertEquals("f1", f1.columns);
        assertEquals(1, (int) f1.colnums.get(0));
        ForeignKey fk = f1.fksrc;
        assertEquals("test", fk.tablename);
        assertEquals("a", fk.columns);
        assertEquals(0, fk.mode);
        Index f2 = test2.indexes.get("f2");
        assertEquals("f2", f2.columns);
        assertEquals(2, (int) f2.colnums.get(0));
        fk = f2.fksrc;
        assertEquals("test", fk.tablename);
        assertEquals("a", fk.columns);
        assertEquals(0, fk.mode);
        Transaction t1 = db.readwriteTran();
        t1.addRecord("test2", record(10, 1, 2));
        shouldBlock(t1, record(11, 5, 1));
        shouldBlock(t1, record(11, 1, 5));
        try {
            t1.removeRecord("test", "a", key(1));
            fail("expected exception");
        } catch (SuException e) {
            assertTrue(e.toString().contains("blocked by foreign key"));
        }
        t1.ck_complete();
    }
