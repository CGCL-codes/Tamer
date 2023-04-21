    @Test
    public void write_skew() {
        makeTable(1000);
        Transaction t1 = db.readwriteTran();
        Transaction t2 = db.readwriteTran();
        getFirst("test", t1);
        getLast("test", t1);
        t1.updateRecord("test", "a", key(1), record(1));
        getFirst("test", t2);
        getLast("test", t2);
        try {
            t2.updateRecord("test", "a", key(999), record(999));
            fail();
        } catch (SuException e) {
            assertTrue(e.toString().contains("conflict"));
        }
        assert (t2.isAborted());
        t1.ck_complete();
        assertNotNull(t2.complete());
        assertStartsWith("write-read conflict", t2.conflict());
        db.checkTransEmpty();
    }
