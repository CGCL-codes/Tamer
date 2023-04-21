    private void deliver(Resource res, HttpServletResponse response) throws IOException {
        if ("test".equals(EnvironmentProperties.getProperties().get("mode"))) {
            if (res.exists()) {
                OutputStream out = response.getOutputStream();
                String type = servletContext.getMimeType(res.getFilename());
                if (type == null) {
                    type = "application/octet-stream";
                }
                response.setContentType(type);
                response.setHeader("Content-Disposition", "inline;filename=" + res.getFilename());
                long contentLength = res.length();
                if (contentLength > -1 && contentLength < Integer.MAX_VALUE) {
                    response.setContentLength((int) contentLength);
                }
                long lastModified = res.lastModified();
                if (lastModified > -1) {
                    response.setDateHeader("Last-Modified", lastModified);
                }
                InputStream in = res.getInputStream();
                byte[] buffer = new byte[4096];
                int no = 0;
                try {
                    while ((no = in.read(buffer)) != -1) out.write(buffer, 0, no);
                } finally {
                    in.close();
                    out.close();
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, res.toString());
            }
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, res.toString());
        }
    }
