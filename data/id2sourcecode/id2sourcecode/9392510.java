    public void doGetEditor(String path, WebletRequest request, WebletResponse response) throws IOException {
        ClassLoader cl = this.getClass().getClassLoader();
        response.setLastModified(System.currentTimeMillis());
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (path.endsWith(".html")) {
            response.setContentType("text/html");
        } else if (path.endsWith(".xml")) {
            response.setContentType("text/xml");
        } else if (path.endsWith(".css")) {
            response.setContentType("text/css");
        } else if (path.endsWith(".js")) {
            response.setContentType("text/javascript");
        } else if (path.endsWith(".gif")) {
            response.setContentType("image/gif");
        } else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
            response.setContentType("image/jpeg");
        } else if (path.endsWith(".png")) {
            response.setContentType("image/png");
        }
        URL url = cl.getResource(path);
        if (url == null) {
            if (log.isWarnEnabled()) {
                log.warn("FCKeditorWeblet: resource not found: " + path);
            }
            return;
        }
        URLConnection conn = url.openConnection();
        if (conn == null) {
            if (log.isWarnEnabled()) {
                log.warn("FCKeditorWeblet: resource not found: " + path);
            }
            return;
        }
        response.setLastModified(conn.getLastModified());
        if (request.getIfModifiedSince() < conn.getLastModified()) {
            response.setContentLength(conn.getContentLength());
            InputStream is = conn.getInputStream();
            OutputStream out = response.getOutputStream();
            byte[] buffer = new byte[2048];
            BufferedInputStream bis = new BufferedInputStream(is);
            int read = 0;
            read = bis.read(buffer);
            while (read != -1) {
                out.write(buffer, 0, read);
                read = bis.read(buffer);
            }
            bis.close();
            out.flush();
            out.close();
        } else {
            response.setStatus(WebletResponse.SC_NOT_MODIFIED);
        }
    }
