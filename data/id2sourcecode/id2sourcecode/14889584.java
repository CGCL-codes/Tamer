    protected void doGet(HttpServletRequest req, HttpServletResponse resp, boolean includeBody) throws ServletException, IOException {
        String lockOwner = "doGet" + System.currentTimeMillis() + req.toString();
        String path = getRelativePath(req);
        if (!isInRepositoryVisibleToCurrentUser(path)) {
            resp.sendError(resp.SC_FORBIDDEN);
            return;
        }
        if (fResLocks.lock(path, lockOwner, false, 0)) {
            try {
                if (fStore.isResource(path)) {
                    if (path.endsWith("/") || (path.endsWith("\\"))) {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND, req.getRequestURI());
                    } else {
                        long lastModified = fStore.getLastModified(path).getTime();
                        resp.setDateHeader("last-modified", lastModified);
                        long resourceLength = fStore.getResourceLength(path);
                        if (resourceLength > 0) {
                            if (resourceLength <= Integer.MAX_VALUE) {
                                resp.setContentLength((int) resourceLength);
                            } else {
                                resp.setHeader("content-length", "" + resourceLength);
                            }
                        }
                        String mimeType = getServletContext().getMimeType(path);
                        if (mimeType != null) {
                            resp.setContentType(mimeType);
                        }
                        if (includeBody) {
                            OutputStream out = resp.getOutputStream();
                            InputStream in = fStore.getResourceContent(path);
                            try {
                                int read = -1;
                                byte[] copyBuffer = new byte[BUF_SIZE];
                                while ((read = in.read(copyBuffer, 0, copyBuffer.length)) != -1) {
                                    out.write(copyBuffer, 0, read);
                                }
                            } finally {
                                in.close();
                                out.flush();
                                out.close();
                            }
                        }
                    }
                } else {
                    if (includeBody && fStore.isFolder(path)) {
                        OutputStream out = resp.getOutputStream();
                        String[] children = fStore.getChildrenNames(path);
                        StringBuffer childrenTemp = new StringBuffer();
                        childrenTemp.append("Contents of this Folder:\n");
                        for (int i = 0; i < children.length; i++) {
                            childrenTemp.append(children[i]);
                            childrenTemp.append("\n");
                        }
                        out.write(childrenTemp.toString().getBytes());
                    } else {
                        if (!fStore.objectExists(path)) {
                            resp.sendError(HttpServletResponse.SC_NOT_FOUND, req.getRequestURI());
                        }
                    }
                }
            } finally {
                fResLocks.unlock(path, lockOwner);
            }
        } else {
            resp.sendError(WebdavStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }
