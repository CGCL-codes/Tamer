    public boolean executeCommand(String cmd) throws Exception {
        if (cmd == null) return false;
        if (cmd.length() == 0) return true;
        if (cmd.toLowerCase().startsWith("assert")) {
            if (cmd.toLowerCase().startsWith("assertrecords")) {
                int checkLen = Integer.parseInt(cmd.substring("assertrecords".length() + 1));
                results.beforeFirst();
                int count = 0;
                while (results.next()) count++;
                if (count != checkLen) throw new Error("Assertion failed: " + checkLen + " != " + count); else println("Assertion passed: " + checkLen + " == " + count);
            } else {
                println("UNKNWON ASSERTION TYPE: " + cmd);
            }
        } else if (cmd.toLowerCase().startsWith("desc tables")) {
            printResultSet(getConnection().getMetaData().getTables(null, null, null, null), 0);
        } else if (cmd.toLowerCase().equals("transaction")) {
            getConnection().setAutoCommit(false);
            println("Begin transaction");
        } else if (cmd.toLowerCase().equals("reconnect")) {
            print("reconnecting...");
            conn = null;
            getConnection();
            println("done");
        } else if (cmd.toLowerCase().equals("commit")) {
            getConnection().commit();
            println("commit complete");
        } else if (cmd.toLowerCase().equals("rollback")) {
            getConnection().rollback();
            println("rollback complete");
        } else if (cmd.toLowerCase().equals("exit")) {
            println("exiting");
            return false;
        } else if (cmd.toLowerCase().startsWith("select")) {
            long t = System.currentTimeMillis();
            results = getConnection().createStatement().executeQuery(cmd);
            printResultSet(results, (System.currentTimeMillis() - t));
            return true;
        } else {
            int count = getConnection().createStatement().executeUpdate(cmd);
            println(count + " rows affected");
        }
        return true;
    }
