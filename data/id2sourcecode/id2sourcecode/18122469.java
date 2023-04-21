    private void sendFile(File fileToSend, HttpServletResponse response) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(fileToSend);
            if (in != null) {
                out = new BufferedOutputStream(response.getOutputStream());
                in = new BufferedInputStream(in);
                response.setContentType("application/unknown");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileToSend.getName() + "\"");
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
