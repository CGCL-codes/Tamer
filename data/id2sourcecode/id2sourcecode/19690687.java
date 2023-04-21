    private void sendFile(String filePath, HttpServletResponse response) throws IOException {
        String filename = FileUtils.getFilename(filePath);
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(filePath);
            if (in != null) {
                out = new BufferedOutputStream(response.getOutputStream());
                in = new BufferedInputStream(in);
                response.setContentType("application/unknown");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
                int c;
                while ((c = in.read()) != -1) out.write(c);
                return;
            }
        } finally {
            if (in != null) try {
                in.close();
            } catch (Exception e) {
            }
            if (out != null) try {
                out.close();
            } catch (Exception e) {
            }
        }
    }
