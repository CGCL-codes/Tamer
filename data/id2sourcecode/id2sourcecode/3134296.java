    private void serveFile(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = getRelativePath(request);
        if (path.matches(".*\\.(?i)(?:jsp|jstl|jspx)$")) {
            ServletContext context = filterConfig.getServletContext();
            RequestDispatcher disp = context.getRequestDispatcher(path);
            disp.forward(request, response);
            return;
        }
        URL url = null;
        File file;
        ServletContext context = filterConfig.getServletContext();
        try {
            url = context.getResource(path);
            String realpath = context.getRealPath(path);
            if (realpath == null) {
                return;
            }
            file = new File(realpath);
            if (!file.canRead()) {
                return;
            }
        } catch (MalformedURLException e) {
            return;
        }
        if (url == null) {
            return;
        }
        Date lastModified = new Date(file.lastModified());
        SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        String lastModifiedStr = df.format(lastModified);
        String eTag = "\"" + lastModifiedStr.replaceAll(" ", "") + "\"";
        response.setHeader("Last-Modified", lastModifiedStr);
        response.setHeader("ETag", eTag);
        String requestedIfModified = request.getHeader("If-Modified-Since");
        if ((requestedIfModified != null) && requestedIfModified.equals(lastModifiedStr)) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }
        String requestedNoneMatch = request.getHeader("If-None-Match");
        if ((requestedNoneMatch != null) && requestedNoneMatch.equals(eTag)) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }
        response.setStatus(HttpServletResponse.SC_OK);
        String mimeType = context.getMimeType(path);
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }
        ServletOutputStream os = null;
        InputStream is = null;
        try {
            os = response.getOutputStream();
            is = url.openStream();
            response.setContentType(mimeType);
            response.setContentLength(is.available());
            byte b[] = new byte[8192];
            int c;
            while ((c = is.read(b)) > -1) {
                os.write(b);
                os.flush();
            }
        } finally {
            if (os != null) {
                os.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }
