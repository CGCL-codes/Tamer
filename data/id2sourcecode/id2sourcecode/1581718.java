    protected static Buffer buildRequestBuffer(HTTPRequestEvent ev) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(ev.method).append(" ").append(ev.url).append(" ").append("HTTP/1.1\r\n");
        for (KeyValuePair<String, String> header : ev.headers) {
            buffer.append(header.getName()).append(":").append(header.getValue()).append("\r\n");
        }
        buffer.append("\r\n");
        Buffer msg = new Buffer(buffer.length() + ev.content.readableBytes());
        msg.write(buffer.toString().getBytes());
        if (ev.content.readable()) {
            msg.write(ev.content.getRawBuffer(), ev.content.getReadIndex(), ev.content.readableBytes());
            return msg;
        } else {
            return msg;
        }
    }
