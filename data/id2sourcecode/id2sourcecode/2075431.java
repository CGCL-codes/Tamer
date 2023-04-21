    @Test
    public void cleanup() {
        db.checkTransEmpty();
        Transaction t1 = db.readonlyTran();
        getFirst("tables", t1);
        t1.ck_complete();
        db.checkTransEmpty();
        makeTable(3);
        db.checkTransEmpty();
        Transaction t2 = db.readwriteTran();
        t2.ck_complete();
        db.checkTransEmpty();
        Transaction t3 = db.readwriteTran();
        getFirst("test", t3);
        t3.ck_complete();
        db.checkTransEmpty();
    }
