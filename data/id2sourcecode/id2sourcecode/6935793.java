    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (this.log.isDebugEnabled()) this.log.debug("processing download request");
        try {
            IFile item = getData(req);
            if (item != null) {
                if (this.log.isDebugEnabled()) this.log.debug("sending file '" + item.getName() + "' to browser");
                if (StringUtil.isValid(item.getContentType())) resp.setContentType(item.getContentType());
                resp.setHeader("Content-Disposition", makeContentDisposition(item.getName()));
                if (isDisableCache()) {
                    resp.setHeader("Cache-Control", "no-store");
                    resp.setHeader("Pragma", "no-cache");
                    resp.setDateHeader("Expires", 0);
                }
                ServletOutputStream out = resp.getOutputStream();
                if (item instanceof IFileStream) {
                    IFileStream stream = (IFileStream) item;
                    stream.write(out);
                } else {
                    InputStream is = item.getInputStream();
                    if (is != null) {
                        int read;
                        byte[] buff = new byte[1024];
                        while ((read = is.read(buff)) != -1) {
                            out.write(buff, 0, read);
                        }
                    } else {
                        byte[] data = item.getData();
                        if (data != null) out.write(data); else {
                            resp.sendError(HttpServletResponse.SC_NO_CONTENT, "Data not available");
                            return;
                        }
                    }
                }
                out.close();
                if (this.log.isDebugEnabled()) this.log.debug("sent file done.");
            } else resp.sendError(HttpServletResponse.SC_NO_CONTENT, "Data not available");
        } catch (InvalidSessionException e) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
            this.log.error("download failed", e);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            this.log.error("download failed", e);
        }
    }
