    public void run() {
        try {
            DataBase db = new DataBase(ht.get("DbDriver").toString(), ht.get("DbUrl").toString());
            AuthInfo table = new AuthInfo(db);
            while (true) {
                for (int i = (reads > writes ? reads : writes); i > 0; i--) {
                    Row row = null;
                    if (i <= reads) {
                        if (i <= writes) {
                            row = table.getRow(acct);
                            int c = ((Number) row.get(1)).intValue();
                            c++;
                            row.set(1, new Integer(c));
                            row.update();
                            synchronized (this) {
                                readcount++;
                                writecount++;
                            }
                        } else {
                            row = table.getRow(acct);
                            synchronized (this) {
                                readcount++;
                            }
                        }
                    }
                }
            }
        } catch (Exception ignore) {
            Code.fail(ignore.toString());
        }
    }
