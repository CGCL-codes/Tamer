    public static void download(Connection conn, Integer fileid, String filename, HttpServletResponse response) throws SQLException, IOException, Exception {
        String prefix = getDirectory(fileid);
        String file = Configuration.diskStorage + prefix + "/" + fileid;
        FileInputStream fsi = new FileInputStream(file);
        response.setHeader("Content-disposition", "attachment; filename=" + filename.replaceAll(" ", "_"));
        response.setHeader("Content-Type", ContentTypes.getContentType(filename));
        try {
            OutputStream os = response.getOutputStream();
            byte[] content = new byte[8192];
            int n = 0;
            while ((n = fsi.read(content)) > 0) os.write(content, 0, n);
            os.flush();
        } catch (Exception e) {
        }
        fsi.close();
    }
