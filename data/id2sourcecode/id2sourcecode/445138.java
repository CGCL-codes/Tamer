    public static final void getFileFromDB(Connection con, String SQLQuery, String filePath, String type) {
        InputStream is;
        FileOutputStream fos;
        byte[] buff;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQLQuery);
            if (rs.next()) {
                is = rs.getBinaryStream(type);
                fos = new FileOutputStream(filePath);
                buff = new byte[8192];
                int len;
                while (0 < (len = is.read(buff))) fos.write(buff, 0, len);
                fos.close();
                is.close();
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
    }
