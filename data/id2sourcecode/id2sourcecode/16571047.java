    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String req_uri = request.getRequestURI();
        String this_path = request.getContextPath() + request.getServletPath();
        String file = req_uri.substring(this_path.length());
        file = URLDecoder.decode(file, "UTF-8");
        if (file == null || file.length() < 1) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        DBPersistenceToken token = (DBPersistenceToken) PersistenceManager.tokenLookup(getServletContext());
        DBFilesPersMan pers_man = DBFilesPersMan.getManager(token);
        response.setContentType(DBFilesPersMan.getContentTypeForFile(file));
        OutputStream os = response.getOutputStream();
        Connection con = token.getCFService().getConnection(token.getPool_name());
        try {
            PreparedStatement ps = con.prepareStatement("SELECT bdata FROM project_files WHERE r_path=?");
            ps.setString(1, file);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                InputStream is = rs.getBinaryStream(1);
                byte[] buffer = new byte[4096];
                while (true) {
                    int read = is.read(buffer);
                    if (read == -1) break;
                    os.write(buffer, 0, read);
                    os.flush();
                }
                is.close();
            }
            rs.close();
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException sqlex) {
                sqlex.printStackTrace();
            }
        }
        os.close();
    }
