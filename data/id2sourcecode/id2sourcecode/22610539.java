    @Override
    public void messageReceived(ChannelHandlerContext channelHandlerContext, MessageEvent messageEvent) throws Exception {
        ResponseMessage<CACHE_ELEMENT> command = (ResponseMessage<CACHE_ELEMENT>) messageEvent.getMessage();
        Command cmd = command.cmd.cmd;
        Channel channel = messageEvent.getChannel();
        if (cmd == Command.GET || cmd == Command.GETS) {
            CacheElement[] results = command.elements;
            int totalBytes = 0;
            for (CacheElement result : results) {
                if (result != null) {
                    totalBytes += result.size() + 256;
                }
            }
            ChannelBuffer writeBuffer = ChannelBuffers.dynamicBuffer(totalBytes);
            for (CacheElement result : results) {
                if (result != null) {
                    writeBuffer.writeBytes(VALUE.duplicate());
                    writeBuffer.writeBytes(ChannelBuffers.copiedBuffer(result.getKeystring(), USASCII));
                    writeBuffer.writeByte((byte) ' ');
                    writeBuffer.writeBytes(ChannelBuffers.copiedBuffer(String.valueOf(result.getFlags()), USASCII));
                    writeBuffer.writeByte((byte) ' ');
                    writeBuffer.writeBytes(ChannelBuffers.copiedBuffer(String.valueOf(result.getData().length), USASCII));
                    if (cmd == Command.GETS) {
                        writeBuffer.writeByte((byte) ' ');
                        writeBuffer.writeBytes(ChannelBuffers.copiedBuffer(String.valueOf(result.getCasUnique()), USASCII));
                    }
                    writeBuffer.writeByte((byte) '\r');
                    writeBuffer.writeByte((byte) '\n');
                    writeBuffer.writeBytes(result.getData());
                    writeBuffer.writeByte((byte) '\r');
                    writeBuffer.writeByte((byte) '\n');
                }
            }
            writeBuffer.writeBytes(END.duplicate());
            StatsCounter.bytes_read.addAndGet(writeBuffer.writerIndex());
            Channels.write(channel, writeBuffer);
        } else if (cmd == Command.SET || cmd == Command.CAS || cmd == Command.ADD || cmd == Command.REPLACE || cmd == Command.APPEND || cmd == Command.PREPEND) {
            if (!command.cmd.noreply) Channels.write(channel, storeResponse(command.response));
        } else if (cmd == Command.INCR || cmd == Command.DECR) {
            if (!command.cmd.noreply) Channels.write(channel, incrDecrResponseString(command.incrDecrResponse));
        } else if (cmd == Command.DELETE) {
            if (!command.cmd.noreply) Channels.write(channel, deleteResponseString(command.deleteResponse));
        } else if (cmd == Command.STATS) {
            for (Map.Entry<String, Set<String>> stat : command.stats.entrySet()) {
                for (String statVal : stat.getValue()) {
                    StringBuilder builder = new StringBuilder();
                    builder.append("STAT ");
                    builder.append(stat.getKey());
                    builder.append(" ");
                    builder.append(String.valueOf(statVal));
                    builder.append("\r\n");
                    Channels.write(channel, ChannelBuffers.copiedBuffer(builder.toString(), USASCII));
                }
            }
            Channels.write(channel, END.duplicate());
        } else if (cmd == Command.VERSION) {
            Channels.write(channel, ChannelBuffers.copiedBuffer("VERSION " + command.version + "\r\n", USASCII));
        } else if (cmd == Command.QUIT) {
            Channels.disconnect(channel);
        } else if (cmd == Command.FLUSH_ALL) {
            if (!command.cmd.noreply) {
                ChannelBuffer ret = command.flushSuccess ? OK.duplicate() : ERROR.duplicate();
                Channels.write(channel, ret);
            }
        } else {
            Channels.write(channel, ERROR.duplicate());
            logger.error("error; unrecognized command: " + cmd);
        }
    }
