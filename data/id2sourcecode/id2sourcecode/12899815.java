    public void getSM(String smID, OutputStream out) throws Exception, java.sql.SQLException {
        System.out.println("SR: Get SM with id=" + smID);
        DBObject dbo = new DBObject();
        dbo.createNewStatement("SELECT SERVICEMANIFEST FROM DBESM.SERVICEMANIFEST WHERE SMID=?");
        dbo.setInt(1, Integer.parseInt(smID));
        ResultSet rs = dbo.executeQuery();
        if (rs.next() == false) {
            System.out.println("SR: SM not found .. Returning empty SM.");
            dbo.close();
            return;
        }
        InputStream in = rs.getAsciiStream(1);
        int k;
        while ((k = in.read()) != -1) out.write(k);
        out.flush();
        dbo.close();
    }
