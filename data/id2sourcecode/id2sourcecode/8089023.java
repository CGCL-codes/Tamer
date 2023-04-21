    public void storeUsageDataBatch(InputStream in) throws java.io.IOException, java.sql.SQLException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int k;
        while ((k = in.read()) != -1) bout.write(k);
        bout.flush();
        DBObject dbo = new DBObject();
        System.out.println(bout.toString());
        dbo.executeBuffer(bout.toString());
    }
