    protected void processPdf(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        String tmpDir = System.getProperty("java.io.tmpdir");
        String path = request.getParameter("id");
        if (log.isDebugEnabled()) log.debug("path===>" + path);
        File file = new File(tmpDir + File.separator + path);
        if (file == null || !file.exists()) {
            log.error("Download of file " + (file == null ? "NULL" : file.getAbsolutePath()) + " failed.");
            throw new ServletException("Download Failed, File not available");
        }
        if (log.isDebugEnabled()) log.debug("Downloading file " + file.getAbsolutePath());
        response.setContentType("application/pdf");
        Boolean attachment = Parser.parseBoolean(request.getParameter("attachment"));
        if (attachment != null && attachment.booleanValue()) response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\""); else response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
        try {
            response.setBufferSize(2048);
        } catch (IllegalStateException e) {
        }
        InputStream in = new FileInputStream(file);
        ServletOutputStream out = response.getOutputStream();
        try {
            byte[] buffer = new byte[1000];
            while (in.available() > 0) out.write(buffer, 0, in.read(buffer));
            out.flush();
        } catch (IOException e) {
            log.error("Problem Serving Resource " + file.getAbsolutePath(), e);
        } finally {
            out.close();
            in.close();
        }
        if (log.isDebugEnabled()) log.debug("Downloaded file " + file.getAbsolutePath());
        if (log.isDebugEnabled()) log.debug("Deleting file " + file.getAbsolutePath());
        if (!file.delete()) log.error("Failed to delete file " + file.getAbsolutePath());
        return;
    }
