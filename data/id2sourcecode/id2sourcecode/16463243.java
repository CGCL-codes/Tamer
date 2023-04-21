    private void send(HttpServletResponse resp, Buffer buf) throws Exception {
        resp.setStatus(200);
        resp.setContentType("image/jpeg");
        resp.setContentLength(buf.readableBytes());
        writeBytes(resp, buf.getRawBuffer(), buf.getReadIndex(), buf.readableBytes());
    }
