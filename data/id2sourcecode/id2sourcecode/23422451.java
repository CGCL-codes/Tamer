    @Override
    protected void doRender(Invocation inv) throws IOException, ServletException, Exception {
        InputStream inputStream = this.inputStream;
        if (inputStream == null) {
            return;
        }
        HttpServletResponse response = inv.getResponse();
        if (response.getContentType() == null) {
            response.setContentType("application/octet-stream");
            if (logger.isDebugEnabled()) {
                logger.debug("set response.contentType by default:" + response.getContentType());
            }
        }
        try {
            byte[] buffer = new byte[bufferSize];
            int read;
            OutputStream out = null;
            while ((read = inputStream.read(buffer)) != -1) {
                if (read == 0) {
                    continue;
                }
                if (out == null) {
                    out = inv.getResponse().getOutputStream();
                }
                out.write(buffer, 0, read);
            }
            out.flush();
        } finally {
            inputStream.close();
        }
    }
