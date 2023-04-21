    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        WikiContext context = m_engine.createContext(req, WikiContext.ATTACH);
        String version = req.getParameter(HDR_VERSION);
        String nextPage = req.getParameter("nextpage");
        String msg = "An error occurred. Ouch.";
        int ver = WikiProvider.LATEST_VERSION;
        AttachmentManager mgr = m_engine.getAttachmentManager();
        AuthorizationManager authmgr = m_engine.getAuthorizationManager();
        String page = context.getPage().getName();
        if (page == null) {
            log.log(Level.INFO, "Invalid attachment name.");
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        OutputStream out = null;
        InputStream in = null;
        try {
            log.log(Level.INFO, "Attempting to download att " + page + ", version " + version);
            if (version != null) {
                ver = Integer.parseInt(version);
            }
            Attachment att = mgr.getAttachmentInfo(page, ver);
            if (att != null) {
                Permission permission = PermissionFactory.getPagePermission(att, "view");
                if (!authmgr.checkPermission(context.getWikiSession(), permission)) {
                    log.log(Level.INFO, "User does not have permission for this");
                    res.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                if (HttpUtil.checkFor304(req, att)) {
                    log.log(Level.INFO, "Client has latest version already, sending 304...");
                    res.sendError(HttpServletResponse.SC_NOT_MODIFIED);
                    return;
                }
                String mimetype = getMimeType(context, att.getFileName());
                res.setContentType(mimetype);
                res.addHeader("Content-Disposition", "inline; filename=\"" + att.getFileName() + "\";");
                res.addDateHeader("Last-Modified", att.getLastModified().getTime());
                if (!att.isCacheable()) {
                    res.addHeader("Pragma", "no-cache");
                    res.addHeader("Cache-control", "no-cache");
                }
                if (att.getSize() >= 0) {
                    res.setContentLength((int) att.getSize());
                }
                out = res.getOutputStream();
                in = mgr.getAttachmentStream(context, att);
                int read = 0;
                byte[] buffer = new byte[BUFFER_SIZE];
                while ((read = in.read(buffer)) > -1) {
                    out.write(buffer, 0, read);
                }
                if (log.isLoggable(Level.INFO)) {
                    msg = "Attachment " + att.getFileName() + " sent to " + req.getRemoteUser() + " on " + req.getRemoteAddr();
                    log.log(Level.INFO, msg);
                }
                if (nextPage != null) res.sendRedirect(nextPage);
                return;
            }
            msg = "Attachment '" + page + "', version " + ver + " does not exist.";
            log.log(Level.INFO, msg);
            res.sendError(HttpServletResponse.SC_NOT_FOUND, msg);
            return;
        } catch (ProviderException pe) {
            msg = "Provider error: " + pe.getMessage();
            log.log(Level.INFO, "Provider failed while reading", pe);
            try {
                res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, msg);
            } catch (IllegalStateException e) {
            }
            return;
        } catch (NumberFormatException nfe) {
            msg = "Invalid version number (" + version + ")";
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
            return;
        } catch (SocketException se) {
            log.log(Level.INFO, "I/O exception during download", se);
            return;
        } catch (IOException ioe) {
            msg = "Error: " + ioe.getMessage();
            log.log(Level.INFO, "I/O exception during download", ioe);
            try {
                res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, msg);
            } catch (IllegalStateException e) {
            }
            return;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }
