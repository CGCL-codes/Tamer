    public static void testOtsdbLock(String otsdbCfg) {
        final Otsdb db1 = new Otsdb();
        db1.loadConfig(otsdbCfg);
        Otsdb db2 = new Otsdb();
        try {
            db1.executeUpdate(create(table1));
            db1.executeUpdate(insert(table1));
            Pair<Statement, ResultSet> result1 = db1.executeQuery(select(table1));
            while (result1.second.next()) {
                Pair<Statement, ResultSet> result2 = db2.executeQuery(select(table1));
                if (result2.second.next()) {
                    int id = result2.second.getInt("testId");
                    Assert.assertEquals(id, 1);
                    String name = result2.second.getString("testName");
                    Assert.assertEquals(name, "test1");
                }
                int id = result1.second.getInt("testId");
                String name = result1.second.getString("testName");
                System.out.println("read: " + id + ", " + name);
            }
        } catch (RequestException e) {
            Assert.fail("at create: " + e);
        } catch (SQLException e) {
            Assert.fail("at request: " + e);
            return;
        }
        String[] read = {};
        String[] write = { table1 };
        try {
            db1.lock(read, write);
        } catch (RequestException e) {
            Assert.fail("at create: " + e);
        }
        try {
            db1.lock(read, write);
        } catch (RequestException e) {
            if (!e.getReason().equals(Reason.LOCKED)) {
                Assert.fail("at create: " + e);
            }
        }
        new Thread() {

            public void run() {
                System.out.println("db1 is waiting: ");
                try {
                    for (int i = 0; i < 20; i++) {
                        sleep(100);
                        System.out.print(".");
                    }
                    System.out.println();
                    System.out.println("db1 release the lock");
                    db1.unlock();
                } catch (Exception e) {
                }
            }
        }.start();
        try {
            System.out.println("db2 request the lock");
            db2.lock(read, write);
            System.out.println("db2 got the lock");
        } catch (RequestException e) {
            Assert.fail("at second connection's lock: " + e);
        }
        try {
            db1.executeUpdate(create(table2));
            db1.executeUpdate(insert(table2));
            Pair<Statement, ResultSet> result1 = db1.executeQuery(select(table2));
            if (result1.second.next()) {
                int id = result1.second.getInt("testId");
                Assert.assertEquals(id, 1);
                String name = result1.second.getString("testName");
                Assert.assertEquals(name, "test1");
            }
        } catch (RequestException e) {
            Assert.fail("at create: " + e);
        } catch (SQLException e) {
            Assert.fail("at request: " + e);
        }
        try {
            String[] read2 = {};
            String[] write2 = { table2 };
            db1.lock(read2, write2);
            db1.executeUpdate(insert(table2));
            Pair<Statement, ResultSet> result1 = db1.executeQuery(select(table2));
            if (result1.second.next()) {
                int id = result1.second.getInt("testId");
                Assert.assertEquals(id, 1);
                String name = result1.second.getString("testName");
                Assert.assertEquals(name, "test1");
            }
            db1.unlock();
        } catch (RequestException e) {
            Assert.fail("at create: " + e);
        } catch (SQLException e) {
            Assert.fail("at request: " + e);
        }
        Pair<Statement, ResultSet> result2;
        try {
            result2 = db2.executeQuery(select(table2));
            if (result2.second.next()) {
                int id = result2.second.getInt("testId");
                Assert.assertEquals(id, 1);
                String name = result2.second.getString("testName");
                Assert.assertEquals(name, "test1");
            }
        } catch (RequestException e) {
            if (!e.getReason().equals(Reason.LOCKED)) {
                Assert.fail("at create: " + e);
            }
        } catch (SQLException e) {
            Assert.fail("at request: " + e);
        }
        try {
            db2.executeUpdate(drop(table1));
            db2.unlock();
            db2.executeUpdate(drop(table2));
        } catch (RequestException e) {
            Assert.fail("at create: " + e);
        }
    }
