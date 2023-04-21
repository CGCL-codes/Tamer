    public static Buffer encodeEvent(EventHeaderTags tags, Event event) {
        Buffer buf = new Buffer(256);
        tags.encode(buf);
        Buffer content = new Buffer(256);
        event.encode(content);
        buf.write(content, content.readableBytes());
        return buf;
    }
