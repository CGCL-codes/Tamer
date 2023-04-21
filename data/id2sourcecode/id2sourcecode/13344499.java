    public static void testOtsdbLock2(String otsdbCfg) {
        final Otsdb db1 = new Otsdb();
        db1.loadConfig(otsdbCfg);
        Otsdb db2 = new Otsdb();
        try {
            db1.executeUpdate(create(table1));
            db1.executeUpdate(insert(table1));
            db1.executeUpdate(create(table2));
            db1.executeUpdate(insert(table2));
            String[] read1 = {};
            String[] write1 = { table1 };
            db1.lock(read1, write1);
            Pair<Statement, ResultSet> result1 = db2.executeQuery(select(table2));
            if (result1.second.next()) {
                int id = result1.second.getInt("testId");
                Assert.assertEquals(id, 1);
                String name = result1.second.getString("testName");
                Assert.assertEquals(name, "test1");
            }
            db1.executeUpdate(drop(table1));
            db1.executeUpdate(drop(table2));
        } catch (RequestException e) {
            Assert.fail("at create: " + e);
        } catch (SQLException e) {
            Assert.fail("at request: " + e);
        }
    }
