    private void returnDownloadFile(HttpServletRequest request, HttpServletResponse response, File file, String relName) throws IOException {
        String contentType = filterConfig.getServletContext().getMimeType(relName);
        if (contentType != null) response.setContentType(contentType);
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
            log.error("Problem Serving Resource " + relName, e);
        } finally {
            out.close();
        }
    }
