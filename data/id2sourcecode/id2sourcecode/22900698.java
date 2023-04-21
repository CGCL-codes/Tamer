    private static int dumpTableImp(String tablename) throws Throwable {
        FileChannel fout = new FileOutputStream(tablename + ".su").getChannel();
        try {
            Transaction t = theDB.readonlyTran();
            try {
                writeFileHeader(fout);
                return dump1(fout, t, tablename, false);
            } finally {
                t.complete();
            }
        } finally {
            fout.close();
        }
    }
