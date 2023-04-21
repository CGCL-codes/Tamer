    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (Config.CONTEXT == null) {
            Logger.debug(this, "Asset not Found");
            response.sendError(404, "Asset not Found");
            return;
        }
        File f;
        boolean PREVIEW_MODE = false;
        boolean EDIT_MODE = false;
        HttpSession session = request.getSession(false);
        if (session != null) {
            PREVIEW_MODE = ((session.getAttribute(com.dotmarketing.util.WebKeys.PREVIEW_MODE_SESSION) != null));
            EDIT_MODE = ((session.getAttribute(com.dotmarketing.util.WebKeys.EDIT_MODE_SESSION) != null));
        }
        User user = null;
        try {
            if (session != null) user = (com.liferay.portal.model.User) session.getAttribute(com.dotmarketing.util.WebKeys.CMS_USER);
        } catch (Exception nsue) {
            Logger.warn(this, "Exception trying to getUser: " + nsue.getMessage(), nsue);
        }
        try {
            if (request.getParameter("path") == null) {
                StringTokenizer _st = new StringTokenizer(request.getRequestURI(), "/");
                Logger.debug(this, "Requesting by url: " + request.getRequestURI());
                String _fileName = null;
                while (_st.hasMoreElements()) {
                    _fileName = _st.nextToken();
                }
                Logger.debug(this, "Parsed filename: " + _fileName);
                String identifier = UtilMethods.getFileName(_fileName);
                Identifier ident = null;
                Logger.debug(SpeedyAssetServlet.class, "Loading identifier: " + identifier);
                try {
                    ident = IdentifierCache.getIdentifierFromIdentifierCache(new Long(identifier));
                } catch (Exception ex) {
                    Logger.debug(SpeedyAssetServlet.class, "Identifier not found going to try as a File Asset", ex);
                }
                if (ident != null && ident.getURI() != null && !ident.getURI().equals("")) {
                    if (PREVIEW_MODE || EDIT_MODE) {
                        String uri = WorkingCache.getPathFromCache(ident.getURI(), ident.getHostInode());
                        if (!UtilMethods.isSet(realPath)) {
                            f = new File(Config.CONTEXT.getRealPath(assetPath + uri));
                        } else {
                            f = new File(realPath + uri);
                        }
                        if (uri == null || !f.exists() || !f.canRead()) {
                            response.sendError(404);
                            return;
                        }
                    } else {
                        String uri = LiveCache.getPathFromCache(ident.getURI(), ident.getHostInode());
                        if (!UtilMethods.isSet(realPath)) {
                            f = new File(Config.CONTEXT.getRealPath(assetPath + uri));
                        } else {
                            f = new File(realPath + uri);
                        }
                        if (uri == null || !f.exists() || !f.canRead()) {
                            response.sendError(404);
                            return;
                        }
                    }
                } else {
                    Logger.warn(this, "Invalid identifier passed: url = " + request.getRequestURI());
                    return;
                }
            } else {
                String relativePath = request.getParameter("path");
                f = new File(FileFactory.getRealAssetsRootPath() + relativePath);
                if (!f.exists() || !f.canRead()) {
                    Logger.warn(this, "Invalid path passed: path = " + relativePath + ", file doesn't exists.");
                    response.sendError(404);
                    return;
                }
            }
            String inode = UtilMethods.getFileName(f.getName());
            com.dotmarketing.portlets.files.model.File file = FileCache.getFileByInode(inode);
            Identifier identifier = IdentifierCache.getIdentifierFromIdentifierCache(file);
            String mimeType = FileFactory.getMimeType(f.getName());
            if (mimeType == null) mimeType = "application/octet-stream";
            response.setContentType(mimeType);
            if (!permissionAPI.doesUserHavePermission(identifier, PERMISSION_READ, user)) {
                if (user == null) response.sendError(401, "The requested file is unauthorized"); else response.sendError(403, "The requested file is forbidden");
                return;
            }
            response.setHeader("Content-Disposition", "filename=" + file.getFileName());
            if (request.getParameter("dotcms_force_download") != null) {
                String url = request.getRequestURL().toString();
                String filename = url.substring(url.lastIndexOf("/") + 1, url.length());
                filename = file.getFileName();
                response.setHeader("Content-Type", "application/force-download");
                response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            } else {
                boolean _adminMode = false;
                try {
                    _adminMode = (request.getSession(false).getAttribute(com.dotmarketing.util.WebKeys.ADMIN_MODE_SESSION) != null);
                } catch (Exception e) {
                }
                if (!_adminMode) {
                    int _daysCache = 30;
                    GregorianCalendar expiration = new GregorianCalendar();
                    expiration.add(java.util.Calendar.DAY_OF_MONTH, _daysCache);
                    int seconds = (_daysCache * 24 * 60 * 60);
                    long _lastModified = f.lastModified();
                    if (_lastModified < 0) _lastModified = 0;
                    _lastModified = _lastModified / 1000;
                    _lastModified = _lastModified * 1000;
                    Date _lastModifiedDate = new java.util.Date(_lastModified);
                    long _fileLength = f.length();
                    String _eTag = "dot:" + inode + ":" + _lastModified + ":" + _fileLength;
                    response.setHeader("Expires", httpDate.format(expiration.getTime()));
                    response.setHeader("Cache-Control", "public, max-age=" + seconds);
                    String ifModifiedSince = request.getHeader("If-Modified-Since");
                    String ifNoneMatch = request.getHeader("If-None-Match");
                    if (ifNoneMatch != null) {
                        if (_eTag.equals(ifNoneMatch) || ifNoneMatch.equals("*")) {
                            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                            f = null;
                            return;
                        }
                    }
                    if (ifModifiedSince != null) {
                        try {
                            Date ifModifiedSinceDate = httpDate.parse(ifModifiedSince);
                            if (_lastModifiedDate.getTime() <= ifModifiedSinceDate.getTime()) {
                                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                                f = null;
                                return;
                            }
                        } catch (Exception e) {
                        }
                    }
                    response.setHeader("Last-Modified", httpDate.format(_lastModifiedDate));
                    response.setHeader("Content-Length", String.valueOf(_fileLength));
                    response.setHeader("ETag", _eTag);
                } else {
                    GregorianCalendar expiration = new GregorianCalendar();
                    expiration.add(java.util.Calendar.MONTH, -1);
                    response.setHeader("Expires", httpDate.format(expiration.getTime()));
                    response.setHeader("Cache-Control", "max-age=-1");
                }
            }
            ServletOutputStream out = null;
            FileChannel from = null;
            ByteBuffer bb = null;
            try {
                out = response.getOutputStream();
                from = new FileInputStream(f).getChannel();
                bb = ByteBuffer.allocateDirect(10);
                int numRead = 0;
                while (numRead >= 0) {
                    bb.rewind();
                    numRead = from.read(bb);
                    bb.rewind();
                    for (int i = 0; i < numRead; i++) {
                        out.write(bb.get());
                    }
                }
            } catch (Exception e) {
                Logger.warn(this, "Error occurred serving asset = " + request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : ""), e);
            } finally {
                if (bb != null) bb.clear();
                if (from != null) from.close();
                if (out != null) out.close();
            }
        } catch (Exception e) {
            Logger.debug(this, "General Error occurred serving asset = " + request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : ""), e);
        }
    }
