    protected void processDownloadRequest(HttpServletRequest req, HttpServletResponse res, boolean redirectToLogin) throws ServletException, IOException {
        Log logger = getLogger();
        String uri = req.getRequestURI();
        if (logger.isDebugEnabled()) {
            String queryString = req.getQueryString();
            logger.debug("Processing URL: " + uri + ((queryString != null && queryString.length() > 0) ? ("?" + queryString) : ""));
        }
        uri = uri.substring(req.getContextPath().length());
        StringTokenizer t = new StringTokenizer(uri, "/");
        int tokenCount = t.countTokens();
        t.nextToken();
        String attachToken = t.nextToken();
        boolean attachment = URL_ATTACH.equals(attachToken) || URL_ATTACH_LONG.equals(attachToken);
        ServiceRegistry serviceRegistry = getServiceRegistry(getServletContext());
        NodeRef nodeRef;
        String filename;
        String path = req.getParameter(ARG_PATH);
        String language = req.getParameter("language");
        if (path != null && path.length() != 0) {
            PathRefInfo pathInfo = null;
            if (language != null && language.length() != 0) {
                try {
                    pathInfo = resolveNamePath(getServletContext(), path + "_" + language);
                } catch (Throwable e) {
                }
            }
            if (pathInfo == null) {
                pathInfo = resolveNamePath(getServletContext(), path);
            }
            nodeRef = pathInfo.NodeRef;
            filename = pathInfo.Filename;
        } else {
            if (tokenCount < 6) {
                throw new IllegalArgumentException("Download URL did not contain all required args: " + uri);
            }
            StoreRef storeRef = new StoreRef(t.nextToken(), t.nextToken());
            String id = URLDecoder.decode(t.nextToken(), "UTF-8");
            nodeRef = new NodeRef(storeRef, id);
            if (tokenCount > 6) {
                List<String> paths = new ArrayList<String>(tokenCount - 5);
                while (t.hasMoreTokens()) {
                    paths.add(URLDecoder.decode(t.nextToken()));
                }
                filename = paths.get(paths.size() - 1);
                try {
                    NodeRef parentRef = serviceRegistry.getNodeService().getPrimaryParent(nodeRef).getParentRef();
                    FileInfo fileInfo = serviceRegistry.getFileFolderService().resolveNamePath(parentRef, paths);
                    nodeRef = fileInfo.getNodeRef();
                } catch (FileNotFoundException e) {
                    throw new AlfrescoRuntimeException("Unable to find node reference by relative path:" + uri);
                }
            } else {
                filename = t.nextToken();
            }
        }
        QName propertyQName = ContentModel.PROP_CONTENT;
        String property = req.getParameter(ARG_PROPERTY);
        if (property != null && property.length() != 0) {
            propertyQName = QName.createQName(property);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Found NodeRef: " + nodeRef);
            logger.debug("Will use filename: " + filename);
            logger.debug("For property: " + propertyQName);
            logger.debug("With attachment mode: " + attachment);
        }
        NodeService nodeService = serviceRegistry.getNodeService();
        ContentService contentService = serviceRegistry.getContentService();
        PermissionService permissionService = serviceRegistry.getPermissionService();
        try {
            if (permissionService.hasPermission(nodeRef, PermissionService.READ_CONTENT) == AccessStatus.DENIED) {
                if (logger.isDebugEnabled()) logger.debug("User does not have permissions to read content for NodeRef: " + nodeRef.toString());
                if (redirectToLogin) {
                    if (logger.isDebugEnabled()) logger.debug("Redirecting to login page...");
                    redirectToLoginPage(req, res, getServletContext());
                } else {
                    if (logger.isDebugEnabled()) logger.debug("Returning 403 Forbidden error...");
                    res.sendError(HttpServletResponse.SC_FORBIDDEN);
                }
                return;
            }
            Date modified = (Date) nodeService.getProperty(nodeRef, ContentModel.PROP_MODIFIED);
            long modifiedSince = req.getDateHeader("If-Modified-Since");
            if (modifiedSince > 0L) {
                long modDate = (modified.getTime() / 1000L) * 1000L;
                if (modDate <= modifiedSince) {
                    if (logger.isDebugEnabled()) logger.debug("Returning 304 Not Modified.");
                    res.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    return;
                }
            }
            res.setDateHeader("Last-Modified", modified.getTime());
            if (attachment == true) {
                res.setHeader("Content-Disposition", "attachment");
            }
            ContentReader reader = contentService.getReader(nodeRef, propertyQName);
            reader = FileContentReader.getSafeContentReader(reader, Application.getMessage(req.getSession(), MSG_ERROR_CONTENT_MISSING), nodeRef, reader);
            String mimetype = reader.getMimetype();
            if (mimetype == null || mimetype.length() == 0) {
                MimetypeService mimetypeMap = serviceRegistry.getMimetypeService();
                mimetype = MIMETYPE_OCTET_STREAM;
                int extIndex = filename.lastIndexOf('.');
                if (extIndex != -1) {
                    String ext = filename.substring(extIndex + 1);
                    String mt = mimetypeMap.getMimetypesByExtension().get(ext);
                    if (mt != null) {
                        mimetype = mt;
                    }
                }
            }
            res.setContentType(mimetype);
            res.setCharacterEncoding(reader.getEncoding());
            res.setHeader("Accept-Ranges", "bytes");
            try {
                boolean processedRange = false;
                String range = req.getHeader("Content-Range");
                if (range == null) {
                    range = req.getHeader("Range");
                }
                if (range != null) {
                    if (logger.isDebugEnabled()) logger.debug("Found content range header: " + range);
                    try {
                        if (range.length() > 6) {
                            StringTokenizer r = new StringTokenizer(range.substring(6), "-/");
                            if (r.countTokens() >= 2) {
                                long start = Long.parseLong(r.nextToken());
                                long end = Long.parseLong(r.nextToken());
                                res.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                                res.setHeader("Content-Range", range);
                                res.setHeader("Content-Length", Long.toString(((end - start) + 1L)));
                                InputStream is = null;
                                try {
                                    is = reader.getContentInputStream();
                                    if (start != 0) is.skip(start);
                                    long span = (end - start) + 1;
                                    long total = 0;
                                    int read = 0;
                                    byte[] buf = new byte[((int) span) < 8192 ? (int) span : 8192];
                                    while ((read = is.read(buf)) != 0 && total < span) {
                                        total += (long) read;
                                        res.getOutputStream().write(buf, 0, (int) read);
                                    }
                                    res.getOutputStream().close();
                                    processedRange = true;
                                } finally {
                                    if (is != null) is.close();
                                }
                            }
                        }
                    } catch (NumberFormatException nerr) {
                    }
                }
                if (processedRange == false) {
                    long size = reader.getSize();
                    res.setHeader("Content-Range", "bytes 0-" + Long.toString(size - 1L) + "/" + Long.toString(size));
                    res.setHeader("Content-Length", Long.toString(size));
                    reader.getContent(res.getOutputStream());
                }
            } catch (SocketException e1) {
                if (logger.isInfoEnabled()) logger.info("Client aborted stream read:\n\tnode: " + nodeRef + "\n\tcontent: " + reader);
            } catch (ContentIOException e2) {
                if (logger.isInfoEnabled()) logger.info("Client aborted stream read:\n\tnode: " + nodeRef + "\n\tcontent: " + reader);
            }
        } catch (Throwable err) {
            throw new AlfrescoRuntimeException("Error during download content servlet processing: " + err.getMessage(), err);
        }
    }
