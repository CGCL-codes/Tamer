        public void sendRespond(HttpServletResponse response) throws IOException {
            response.setContentType(getMimeType());
            response.setContentLength(getContentLength());
            if (getVersionId() != null) response.setHeader(HEADER_JNLP_VERSION, getVersionId());
            if (getLastModified() != 0) response.setDateHeader(HEADER_LASTMOD, getLastModified());
            if (_fileName != null) {
                if (_fileName.endsWith(".pack.gz")) {
                    response.setHeader(CONTENT_ENCODING, PACK200_GZIP_ENCODING);
                } else if (_fileName.endsWith(".gz")) {
                    response.setHeader(CONTENT_ENCODING, GZIP_ENCODING);
                } else {
                    response.setHeader(CONTENT_ENCODING, null);
                }
            }
            InputStream in = getContent();
            OutputStream out = response.getOutputStream();
            try {
                byte[] bytes = new byte[32 * 1024];
                int read;
                while ((read = in.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
            } finally {
                if (in != null) in.close();
            }
        }
