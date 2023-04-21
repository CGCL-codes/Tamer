    public void handleRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || path.length() == 0 || (path.equals("/") && !defaultpath.equals("/"))) {
            StringBuilder sb = new StringBuilder();
            sb.append(req.getScheme()).append("://").append(getServerName(req));
            if (!(req.getServerPort() == 80 || req.getServerPort() == 443)) sb.append(":" + req.getServerPort());
            sb.append(req.getContextPath()).append(defaultpath);
            if (req.getQueryString() != null && !req.getQueryString().equals("")) sb.append("?" + req.getQueryString());
            res.sendRedirect(sb.toString());
            return;
        }
        if (path.contains("..") || path.startsWith("/WEB-INF")) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND, path);
            return;
        }
        if (path.endsWith("/")) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, path);
            return;
        }
        Resource inputResource = null;
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (passthroughPaths != null) {
            for (String prefix : this.passthroughPaths) {
                if (path.startsWith(prefix)) {
                    Resource resource = null;
                    if (path.startsWith("modules/") && !extractedPaths.contains(prefix)) {
                        String moduleUri = "module://" + path.substring(8);
                        resource = ResourceUtil.getResource(moduleUri);
                    } else {
                        resource = ResourceUtil.getFileResourceFromDocroot(path);
                    }
                    if (resource.exists()) {
                        inputResource = resource;
                        break;
                    }
                }
            }
            if (inputResource == null) {
                FileResource baseResource = ResourceUtil.getFileResource(base);
                FileResource resource = ResourceUtil.getFileResource(baseResource, path);
                if (resource.exists()) {
                    inputResource = resource;
                }
            }
        }
        if (inputResource == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND, path);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Resource doesn't exist -> send 'not found': " + path);
            }
            return;
        }
        if (!inputResource.isFile()) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, path);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Resource isn't a normal file -> send 'forbidden': " + path);
            }
            return;
        }
        long contentLength = inputResource.length();
        long lastModified = inputResource.lastModified();
        String reqETag = req.getHeader("If-None-Match");
        if (reqETag != null) {
            String etag = createETag(path, contentLength, lastModified);
            if (etag.equals(reqETag)) {
                res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                res.flushBuffer();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("ETag didn't change -> send 'not modified' for resource: " + path);
                }
                return;
            }
        }
        long reqMod = req.getDateHeader("If-Modified-Since");
        if (reqMod != -1) {
            if (lastModified < reqMod + 1000) {
                res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                res.flushBuffer();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Modification time didn't change -> send 'not modified' for resource: " + path);
                }
                return;
            }
        }
        String type = getServletContext().getMimeType(path);
        if (type == null) {
            type = "application/octet-stream";
        }
        res.setContentType(type);
        if (contentLength > -1 && contentLength < Integer.MAX_VALUE) {
            res.setContentLength((int) contentLength);
        }
        if (lastModified > -1) {
            res.setDateHeader("Last-Modified", lastModified);
        }
        String etag = MD5Utils.hex_md5(path + contentLength + lastModified);
        res.setHeader("ETag", etag);
        if (mode == null || mode.equals("") || mode.equals("prod")) {
            res.setHeader("Cache-Control", "max-age=3600");
        } else {
            res.setHeader("Cache-Control", "max-age=3, must-revalidate");
        }
        OutputStream out = new BufferedOutputStream(res.getOutputStream());
        InputStream in = inputResource.getInputStream();
        int bytes_read;
        byte[] buffer = new byte[8];
        while ((bytes_read = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytes_read);
        }
        out.flush();
        in.close();
        out.close();
    }
