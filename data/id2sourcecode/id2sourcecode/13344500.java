    public static void testOtsdbLock3(String otsdbCfg) {
        final Otsdb db1 = new Otsdb();
        db1.loadConfig(otsdbCfg);
        try {
            db1.executeUpdate(create(table1));
            db1.executeUpdate(insert(table1));
            db1.executeUpdate(create(table2));
            db1.executeUpdate(insert(table2));
            Pair<Statement, ResultSet> result1 = db1.executeQuery(select(table2));
            boolean read = false;
            while (result1.second.next()) {
                if (read) {
                    String[] read1 = {};
                    String[] write1 = { table1 };
                    db1.lock(read1, write1);
                }
                int id = result1.second.getInt("testId");
                String name = result1.second.getString("testName");
                System.out.println("read: " + id + ", " + name);
                read = true;
            }
            db1.unlock();
            db1.executeUpdate(drop(table1));
            db1.executeUpdate(drop(table2));
        } catch (RequestException e) {
            Assert.fail("at create: " + e);
        } catch (SQLException e) {
            Assert.fail("at request: " + e);
        }
    }
