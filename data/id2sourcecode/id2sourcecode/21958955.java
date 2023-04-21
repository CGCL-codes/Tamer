    public void queryGetFile(String table, int id) {
        Statement st;
        ResultSet rs = null;
        String filename = null;
        try {
            st = conn.createStatement();
            if (table == "stl") {
                String stlQuery = "select file_name, file_content from stl where id=" + id;
                rs = st.executeQuery(stlQuery);
            }
            if (table == "dxf") {
                String dxfQuery = "select file_name, file_content from dxf where id=" + id;
                rs = st.executeQuery(dxfQuery);
            }
            if (table == "x3d") {
                String x3dQuery = "select file_name, file_content from x3d where id=" + id;
                rs = st.executeQuery(x3dQuery);
            }
            if (table == "vfc") {
                String vfcQuery = "select file_name, file_content from vfc where id=" + id;
                rs = st.executeQuery(vfcQuery);
            }
            while (rs.next()) {
                Blob blob = rs.getBlob("file_content");
                filename = rs.getString("file_name");
                OutputStream fwriter;
                try {
                    fwriter = new FileOutputStream("temp/" + filename + ".zip");
                    readFromBlob(blob, fwriter);
                    fwriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        File zipFile = new File("temp/" + filename + ".zip");
        ZIPfile decompress = new ZIPfile(zipFile);
        File getTempFile = new File("temp/" + filename + "." + table);
    }
