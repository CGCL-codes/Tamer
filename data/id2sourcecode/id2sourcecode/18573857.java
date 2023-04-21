    public static void exportSolverCode(Solver s, File f) throws NoConnectionToDBException, SQLException, FileNotFoundException, IOException {
        PreparedStatement ps = DatabaseConnector.getInstance().getConn().prepareStatement("SELECT `code` FROM " + table + " WHERE idSolver=?");
        ps.setInt(1, s.getId());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            InputStream in = rs.getBinaryStream("code");
            if (in == null) {
                return;
            }
            new File("tmp").mkdir();
            File tmp = new File("tmp" + System.getProperty("file.separator") + s.getId() + ".zip.tmp");
            FileOutputStream out = new FileOutputStream(tmp);
            byte[] buffer = new byte[8192];
            int read;
            while (-1 != (read = in.read(buffer))) {
                out.write(buffer, 0, read);
            }
            out.close();
            in.close();
            Util.unzip(tmp, f);
            tmp.delete();
        }
        rs.close();
    }
