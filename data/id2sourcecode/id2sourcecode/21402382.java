    protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        JavaliController.debug(JavaliController.LG_VERBOSE, "JavaliDownloader called");
        String attachmentId = req.getParameter(FormConstants.DWLD_ATTACHMENT_ID);
        HttpSession sess = req.getSession(false);
        if (sess == null) {
            JavaliController.debug(JavaliController.LG_VERBOSE, "Downloads accessed without a session from: " + req.getRemoteAddr());
            res.sendError(res.SC_NOT_FOUND);
            return;
        }
        JavaliSession js = (JavaliSession) sess.getAttribute(FormConstants.SESSION_BINDING);
        if (js == null || !js.isConnected()) {
            JavaliController.debug(JavaliController.LG_VERBOSE, "Unallowed access to downloads from: " + req.getRemoteAddr());
            res.sendError(res.SC_NOT_FOUND);
            return;
        }
        JavaliAttachment att = URLUtils.IDToAttachment(attachmentId, js.getMailStore());
        if (att == null) {
            JavaliController.debug(JavaliController.LG_VERBOSE, "Could not find attachment by id: " + attachmentId);
            res.sendError(res.SC_NOT_FOUND);
            return;
        }
        InputStream in = null;
        try {
            in = att.getInputStream();
        } catch (Exception e) {
            JavaliController.debug(JavaliController.LG_VERBOSE, "Exception getting part.getInputStream", e);
            res.sendError(res.SC_NOT_FOUND);
            return;
        }
        byte buf[] = new byte[2048];
        int read = -1;
        res.setContentType(att.getContentType());
        OutputStream out = res.getOutputStream();
        while ((read = in.read(buf)) != -1) out.write(buf, 0, read);
        out.close();
    }
