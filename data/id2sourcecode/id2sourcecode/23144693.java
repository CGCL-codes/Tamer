    private void readFixedLengthContent(ChannelBuffer buffer) {
        long length = message.getLiteralLength();
        if (content == null) {
            content = buffer.readBytes((int) length);
        } else {
            content.writeBytes(buffer.readBytes((int) length));
        }
    }
