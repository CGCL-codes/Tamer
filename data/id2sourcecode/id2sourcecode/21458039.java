    public void testWrite() {
        String src = "e:\\test\\jdbc.xls";
        String dest = "e:\\test\\export.xml";
        try {
            Reader reader = new SimpleExcelReader(src);
            Writer writer = new SimpleXmlWriter(reader.getHeader(), new File(dest));
            new SimpleTractor(reader, writer).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
