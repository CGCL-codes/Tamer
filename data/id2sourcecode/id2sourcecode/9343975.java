    public void threadThis() {
        try {
            DBF writer = new DBF("testfiles/temp.dbf");
            for (int i = 0; i < writer.getRecordCount(); i++) {
                writer.read(true);
                Field str_field = writer.getField(1);
                System.out.println(str_field.get());
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
