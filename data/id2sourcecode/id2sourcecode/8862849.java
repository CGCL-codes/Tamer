        public NextNum(String tablename) {
            this.tablename = tablename;
            Request.execute("create " + tablename + " (num) key()");
            Transaction t = theDB.readwriteTran();
            t.addRecord(tablename, rec(1));
            t.ck_complete();
        }
