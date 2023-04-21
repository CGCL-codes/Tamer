    @Override
    protected boolean onEncode(Buffer buffer) {
        BufferHelper.writeVarInt(buffer, type.getValue());
        Buffer content = new Buffer(256);
        ev.encode(content);
        switch(type) {
            case SE1:
                {
                    SimpleEncrypt se1 = new SimpleEncrypt();
                    se1.encrypt(content);
                    break;
                }
            default:
                {
                    break;
                }
        }
        buffer.write(content, content.readableBytes());
        return true;
    }
