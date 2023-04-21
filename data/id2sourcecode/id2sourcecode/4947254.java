    public void testWrite() {
        String file = "e:\\test\\jdbc.xls";
        try {
            Reader<Row> reader = new SimpleExcelReader(file);
            Writer writer = new SimpleJdbcWriter("cross_test", reader.getHeader());
            Tractor tractor = new SimpleTractor(reader, writer);
            tractor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
