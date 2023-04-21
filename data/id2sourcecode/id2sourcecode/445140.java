    public static final MyHashtable getLabelsFromDB(Connection con, int orderid) {
        MyHashtable response = new MyHashtable();
        InputStream is;
        FileOutputStream fos;
        byte[] buff;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from ups_labels where orderid=" + orderid);
            while (rs.next()) {
                String fileName = "label" + orderid + "_" + System.currentTimeMillis() + ".gif";
                String filePath = System.getProperty("java.io.tmpdir") + "/" + fileName;
                is = rs.getBinaryStream("label");
                fos = new FileOutputStream(filePath);
                buff = new byte[8192];
                int len;
                while (0 < (len = is.read(buff))) fos.write(buff, 0, len);
                fos.close();
                is.close();
                response.put(fileName, filePath);
            }
            rs.close();
            stmt.close();
            con.close();
            fos = null;
            buff = null;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            new ErrorHandler(ex);
        }
        return response;
    }
