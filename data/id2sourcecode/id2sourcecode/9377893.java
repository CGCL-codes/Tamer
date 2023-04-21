    protected void doWriteInputStream(InputStream inputStream, HttpServletResponse response, String contentType, int contentLength, String contentDisposition) throws IOException {
        ServletOutputStream op = response.getOutputStream();
        response.setContentType(contentType);
        response.setContentLength(contentLength);
        response.setHeader("Content-Disposition", contentDisposition);
        byte[] bbuf = new byte[1024];
        DataInputStream in = new DataInputStream(inputStream);
        int length = 0;
        while ((length = in.read(bbuf)) != -1) op.write(bbuf, 0, length);
        in.close();
        op.flush();
        op.close();
    }
