    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        boolean docrootMode;
        String path = req.getPathInfo();
        if (path != null) {
            docrootMode = false;
        } else {
            path = req.getServletPath();
            docrootMode = true;
        }
        if (docrootMode && this.defaultpath != null && (path == null || path.length() == 0 || path.equals("/"))) {
            res.sendRedirect(req.getContextPath() + this.defaultpath);
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
        try {
            if (docrootMode && (base == null || base.length() == 0)) {
                docrootMode = false;
            }
            InputStream in = null;
            if (docrootMode) {
                if (passthroughPaths != null) {
                    for (String prefix : this.passthroughPaths) {
                        if (path.startsWith(prefix)) {
                            in = ResourceUtil.getFileResourceFromDocroot(path).getInputStream();
                        }
                    }
                }
                if (in == null) {
                    File file = new File(base, path);
                    in = new BufferedInputStream(new FileInputStream(file));
                }
            } else {
                in = getServletContext().getResourceAsStream(path);
                if (in == null) {
                    throw new FileNotFoundException();
                }
            }
            String type = getServletContext().getMimeType(path);
            if (type == null) {
                type = "application/octet-stream";
            }
            res.setContentType(type);
            OutputStream out = new BufferedOutputStream(res.getOutputStream());
            int bytes_read;
            byte[] buffer = new byte[8];
            while ((bytes_read = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytes_read);
            }
            out.flush();
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND, path);
        }
    }
