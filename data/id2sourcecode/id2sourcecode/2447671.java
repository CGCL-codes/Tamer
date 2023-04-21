    @Test
    public void test2() {
        Request.execute("create lib " + "(group, lib_committed, lib_modified, name, num, parent, text) " + "key (name,group) " + "key (num) " + "index (parent) " + "index (parent,name)");
        for (int i = 0; i < 10; ++i) {
            Record rec = mkrec(i);
            Transaction t = db.readwriteTran();
            t.addRecord("lib", rec);
            t.ck_complete();
        }
        checkCount();
        Transaction t = db.readwriteTran();
        t.ck_complete();
        checkCount();
    }
