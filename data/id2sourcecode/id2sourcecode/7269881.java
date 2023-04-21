    private void sendResourceFile(HttpServletRequest req, HttpServletResponse resp, ResourceFile rf) throws ServletException, IOException {
        if (log.isDebugEnabled()) {
            log.debug("Opening data of ResourceFile: " + rf);
        }
        InputStream in = new BufferedInputStream(getResourceFileInputStream(rf));
        try {
            if (log.isDebugEnabled()) {
                log.debug("Sending data of ResourceFile: " + rf);
            }
            resp.setContentType(rf.getContentType());
            resp.setHeader("Content-Disposition", "attachment; filename=" + rf.getName());
            resp.setContentLength(null != rf.getSize() ? rf.getSize().intValue() : 0);
            OutputStream out = new BufferedOutputStream(resp.getOutputStream());
            final byte[] READ_BUF = new byte[65536];
            int read = 0;
            do {
                read = in.read(READ_BUF);
                if (read > 0) {
                    out.write(READ_BUF, 0, read);
                }
            } while (read > 0);
            out.flush();
            out.close();
        } finally {
            try {
                in.close();
            } catch (Exception ex) {
            }
        }
    }
