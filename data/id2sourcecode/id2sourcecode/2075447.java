    @Test
    public void nested() {
        makeTable("test1");
        makeTable("test2");
        Transaction t1 = db.readwriteTran();
        t1.addRecord("test1", record(123));
        Transaction t2 = db.readwriteTran();
        t2.addRecord("test2", record(456));
        t2.ck_complete();
        t1.ck_complete();
        check("test1", 123);
        check("test2", 456);
        db.checkTransEmpty();
    }
