    private void sendFile(InputStream file, String filename, String contentType) throws IOException {
        log.info("sending file " + filename);
        InputStream in = null;
        OutputStream out = null;
        try {
            if (filename != null) {
                if (contentType == null) {
                    if (filename.endsWith(".jpg")) contentType = "image/jpeg"; else if (filename.endsWith(".html")) contentType = "text/html"; else if (filename.endsWith(".css")) contentType = "text/css"; else {
                        contentType = "application/unknown";
                        this.resp.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
                    }
                    this.resp.setContentType(contentType);
                }
            }
            out = new BufferedOutputStream(this.resp.getOutputStream());
            in = new BufferedInputStream(file);
            int c;
            while ((c = in.read()) != -1) out.write(c);
            return;
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
