    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String context = request.getContextPath();
        String servlet = request.getServletPath();
        String reqURI = request.getRequestURI();
        reqURI = reqURI.replaceAll("\\+", "%2B");
        reqURI = URLDecoder.decode(reqURI, "UTF-8");
        String reqResource = reqURI.substring(servlet.length() + context.length());
        String resource_path = this.upload_dir + reqResource;
        File resource = new File(resource_path);
        mLogger.debug("Resource requested [" + reqURI + "]");
        mLogger.debug("Real path is [" + resource.getAbsolutePath() + "]");
        if (!resource.exists() || !resource.canRead() || resource.isDirectory()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        File uploadDir = new File(this.upload_dir);
        if (!resource.getCanonicalPath().startsWith(uploadDir.getCanonicalPath())) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Date ifModDate = new Date(request.getDateHeader("If-Modified-Since"));
        Date lastMod = new Date(resource.lastModified());
        if (lastMod.compareTo(ifModDate) <= 0) {
            mLogger.debug("Resource unmodified ... sending 304");
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }
        response.addDateHeader("Last-Modified", (new Date()).getTime());
        response.setContentType(this.context.getMimeType(resource.getAbsolutePath()));
        byte[] buf = new byte[8192];
        int length = 0;
        OutputStream out = response.getOutputStream();
        InputStream resource_file = new FileInputStream(resource);
        while ((length = resource_file.read(buf)) > 0) out.write(buf, 0, length);
        out.close();
        resource_file.close();
    }
