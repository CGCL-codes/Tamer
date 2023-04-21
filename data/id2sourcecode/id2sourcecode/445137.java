    public static final boolean dbFileSaveTo(Connection con, String savePath, String type, int id) {
        String SQLQuery = "select " + type + " from documents where documentid= " + id + "";
        boolean success = true;
        InputStream is;
        FileOutputStream fos;
        byte[] buff;
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQLQuery);
            if (rs.next()) {
                is = rs.getBinaryStream(type);
                fos = new FileOutputStream(savePath);
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
            success = false;
            ex.printStackTrace();
            new ErrorHandler(ex);
        }
        return success;
    }
