    @Override
    protected boolean onEncode(Buffer outbuf) {
        BufferHelper.writeVarInt(outbuf, type.getValue());
        Buffer content = new Buffer(256);
        ev.encode(content);
        byte[] raw = content.getRawBuffer();
        switch(type) {
            case NONE:
                {
                    BufferHelper.writeVarInt(outbuf, content.readableBytes());
                    outbuf.write(raw, content.getReadIndex(), content.readableBytes());
                    break;
                }
            case QUICKLZ:
                {
                    try {
                        byte[] newbuf = QuickLZ.compress(raw, content.getReadIndex(), content.readableBytes(), 1);
                        BufferHelper.writeVarInt(outbuf, newbuf.length);
                        outbuf.write(newbuf);
                    } catch (Exception e) {
                        return false;
                    }
                    break;
                }
            case FASTLZ:
                {
                    byte[] newbuf = new byte[raw.length];
                    JFastLZ fastlz = new JFastLZ();
                    int afterCompress;
                    try {
                        afterCompress = fastlz.fastlzCompress(raw, content.getReadIndex(), content.readableBytes(), newbuf, 0, newbuf.length);
                        BufferHelper.writeVarInt(outbuf, afterCompress);
                        outbuf.write(newbuf, 0, afterCompress);
                    } catch (IOException e) {
                        return false;
                    }
                    break;
                }
            case SNAPPY:
                {
                    try {
                        SnappyBuffer newbuf = SnappyCompressor.compress(raw, content.getReadIndex(), content.readableBytes());
                        BufferHelper.writeVarInt(outbuf, newbuf.getLength());
                        outbuf.write(newbuf.getData(), 0, newbuf.getLength());
                    } catch (Exception e) {
                        return false;
                    }
                    break;
                }
            case LZF:
                {
                    try {
                        byte[] newbuf = LZFEncoder.encode(raw, content.readableBytes());
                        BufferHelper.writeVarInt(outbuf, newbuf.length);
                        outbuf.write(newbuf);
                    } catch (Exception e) {
                        return false;
                    }
                    break;
                }
            default:
                {
                    return false;
                }
        }
        return true;
    }
