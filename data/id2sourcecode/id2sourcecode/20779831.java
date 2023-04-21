    void nonStandardResponse(Source source, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, RpcException {
        String mime = source.getDstMime();
        if (mime == null) throw new ServletException("unexpected missing dst mime type");
        resp.setHeader("Server", "Mercury-Content-Server/0.11");
        resp.setContentType(mime);
        ServletOutputStream out = null;
        try {
            out = resp.getOutputStream();
            ExpiringWriter writer = new ExpiringWriter(out, TIMEOUT);
            ClientContent content = content_cache.getContent(RioLauncher.getDriverId(), req.getRemoteAddr(), source.getSrcId(), source.getContentURL());
            final int MAX_BUF_SIZE = 65536;
            byte[] buf = new byte[MAX_BUF_SIZE];
            int offset = 0;
            while (true) {
                if (log.isDebugEnabled()) log.debug("sendMpeg: offset=" + offset);
                int read_count = content.getBytes(offset, buf, 0, MAX_BUF_SIZE);
                if (read_count == 0) {
                    if (log.isDebugEnabled()) log.debug("sendMpeg: read_count==0");
                    break;
                }
                offset += read_count;
                if (!writer.write(buf, 0, read_count)) break;
                if (read_count < MAX_BUF_SIZE) {
                    if (log.isDebugEnabled()) log.debug("sendMpeg: complete");
                    break;
                }
                if (log.isDebugEnabled()) log.debug("sendMpeg: waiting");
                HelperMisc.sleep(100);
            }
        } finally {
            log.debug("nonStandardResponse: complete");
            if (out != null) out.close();
        }
    }
