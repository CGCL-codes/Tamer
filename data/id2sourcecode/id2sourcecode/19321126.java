    public int render() throws Exception {
        WebInteractionContext request = WebLocal.getWebInteractionContext();
        InputStream in = null;
        OutputStream out = null;
        try {
            WikiEngine we = WikiLocal.getWikiEngine();
            WikiCategoryEngine wce = WikiLocal.getWikiCategoryEngine();
            WikiLinkHolder wlh = WikiLocal.getWikiLinkHolder();
            WikiAttachmentProvider prov = wce.getAttachmentProvider();
            String attname = wlh.getExtrainfo();
            if (StringUtils.isBlank(attname)) {
                throw new WikiException("No attachment name is provided");
            }
            WikiProvidedObject att = new WikiProvidedObject(attname);
            att.setVersion(wlh.getVersion());
            String mimetype = request.getMimeType(att.getName());
            if (mimetype == null) {
                mimetype = "application/binary";
            }
            request.setContentType(mimetype);
            request.setHeader("Content-Disposition", "inline; filename=\"" + att.getName() + "\";");
            in = prov.getAttachmentInputStream(wlh.getWikiPage(), att);
            out = request.getOutputStream();
            int read = 0;
            byte buffer[] = new byte[8192];
            while ((read = in.read(buffer)) > -1) {
                out.write(buffer, 0, read);
            }
            return RENDER_COMPLETED;
        } finally {
            CloseUtils.close(in);
            CloseUtils.close(out);
        }
    }
