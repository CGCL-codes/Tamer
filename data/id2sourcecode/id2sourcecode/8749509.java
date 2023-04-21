    public void performSendFile(String mimeType, String filename, InputStream is) throws ServletException {
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        try {
            byte buffer[] = new byte[1024];
            int nread = -1;
            while ((nread = is.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, nread);
            }
        } catch (IOException e) {
            throw new ServletException("IO error returning file stream", e);
        }
    }
