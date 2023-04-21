    private void writeData(ChannelBuffer buffer, List<String> data) {
        for (String string : data) {
            ChannelBuffer buff = ChannelBuffers.copiedBuffer(string, Charset.forName("UTF-8"));
            buffer.writeShort(buff.readableBytes());
            buffer.writeBytes(buff);
        }
    }
