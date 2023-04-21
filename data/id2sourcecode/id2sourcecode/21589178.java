    public void testReader() {
        String file = "e:\\test\\crossdata.xml";
        String dest = "e:\\test\\crosssql.sql";
        try {
            Reader reader = new SimpleXmlReader(new File(file));
            Writer writer = new SimpleSQLWriter(reader.getHeader(), new File(dest), "test_table");
            Tractor tractor = new SimpleTractor(reader, writer);
            tractor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
