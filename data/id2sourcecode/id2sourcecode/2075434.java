    private Transaction add_remove() {
        Transaction t = db.readwriteTran();
        checkBefore(t);
        t.addRecord("test", record(9999));
        t.removeRecord("test", "a", key(1));
        checkAfter(t);
        return t;
    }
