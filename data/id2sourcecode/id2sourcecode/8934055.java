    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        File file;
        String fileName;
        String contentType;
        String UUID = request.getParameter(PARAM_UUID);
        if (UUID != null) {
            String sessionAttribute = SESSION_ATTR_FILE_PREFIX + UUID;
            file = (File) request.getSession().getAttribute(sessionAttribute);
            request.getSession().removeAttribute(sessionAttribute);
            if (file == null || !file.exists()) {
                String s = "Download of file " + (file == null ? "NULL" : file.getAbsolutePath()) + " failed.";
                log.error(s);
                throw new ServletException(s);
            }
            if (log.isDebugEnabled()) log.debug("Downloading file cached in the session: " + file.getAbsolutePath());
            fileName = file.getName();
            contentType = determineContentType(fileName);
        } else {
            file = (File) request.getAttribute(ATTR_FILE);
            request.removeAttribute(ATTR_FILE);
            if (file == null || !file.exists()) {
                String s = "Download of file " + (file == null ? "NULL" : file.getAbsolutePath()) + " failed.";
                log.error(s);
                throw new ServletException(s);
            }
            if (log.isDebugEnabled()) log.debug("Downloading file " + file.getAbsolutePath());
            fileName = (String) request.getAttribute(ATTR_FILE_NAME);
            request.removeAttribute(ATTR_FILE_NAME);
            if (fileName != null) {
                int pos = fileName.lastIndexOf('/');
                if (pos >= 0) fileName = fileName.substring(pos + 1); else {
                    pos = fileName.lastIndexOf('\\');
                    if (pos >= 0) fileName = fileName.substring(pos + 1);
                }
            } else fileName = file.getName();
            contentType = (String) request.getAttribute(ATTR_CONTENT_TYPE);
            request.removeAttribute(ATTR_CONTENT_TYPE);
            if (contentType == null) determineContentType(fileName);
        }
        if (log.isDebugEnabled()) {
            log.debug("Downloading file as " + fileName);
            log.debug("ContentType is " + contentType);
        }
        response.setContentType(contentType);
        try {
            response.setBufferSize(2048);
        } catch (IllegalStateException e) {
        }
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
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
    }
