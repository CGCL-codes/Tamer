    public static void viewFile(Connection conn, String filename, HttpServletResponse response) throws SQLException, IOException, Exception {
        response.setHeader("Content-Type", ContentTypes.getContentType(filename));
        File f = new File(filename);
        if (!f.exists() || !f.isFile()) throw new IOException("DiskStorage.viewFile(): File not found: " + filename);
        response.setHeader("Content-disposition", "inline; filename=" + f.getName().replaceAll(" ", "_"));
        FileInputStream fis = new FileInputStream(f);
        OutputStream os = response.getOutputStream();
        byte[] content = new byte[8192];
        int n = 0;
        while ((n = fis.read(content)) > 0) os.write(content, 0, n);
        os.flush();
        fis.close();
        return;
    }
