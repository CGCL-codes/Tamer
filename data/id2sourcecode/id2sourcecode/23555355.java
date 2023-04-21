    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doOptions(request, response);
        long date = getLastModified(request);
        if (date > -1) response.setDateHeader("Last-Modified", date);
        InputStream is;
        OutputStream os;
        String path = request.getPathInfo();
        ServletContext context = getServletContext();
        URL url = context.getResource(path);
        URLConnection resource = url.openConnection();
        is = resource.getInputStream();
        String contentType = context.getMimeType(path);
        if (contentType == null) contentType = resource.getContentType();
        String encoding = resource.getContentEncoding();
        if (encoding != null) contentType += "; charset=" + encoding;
        response.setContentType(contentType);
        int contentLength = resource.getContentLength();
        if (contentLength > 0) response.setContentLength(contentLength);
        os = response.getOutputStream();
        int bytesRead = 0;
        byte buffer[] = new byte[512];
        while ((bytesRead = is.read(buffer)) != -1) os.write(buffer, 0, bytesRead);
        is.close();
    }
