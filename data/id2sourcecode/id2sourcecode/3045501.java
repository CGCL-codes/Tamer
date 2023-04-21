    @Override
    @SuppressWarnings("unchecked")
    public void messageReceived(ChannelHandlerContext channelHandlerContext, MessageEvent messageEvent) throws Exception {
        ResponseMessage<CACHE_ELEMENT> command = (ResponseMessage<CACHE_ELEMENT>) messageEvent.getMessage();
        Object additional = messageEvent.getMessage();
        MemcachedBinaryCommandDecoder.BinaryCommand bcmd = MemcachedBinaryCommandDecoder.BinaryCommand.forCommandMessage(command.cmd);
        ChannelBuffer extrasBuffer = null;
        ChannelBuffer keyBuffer = null;
        if (bcmd.addKeyToResponse && command.cmd.keys != null && command.cmd.keys.size() != 0) {
            keyBuffer = ChannelBuffers.wrappedBuffer(ByteOrder.BIG_ENDIAN, command.cmd.keys.get(0).getBytes());
        }
        ChannelBuffer valueBuffer = null;
        if (command.elements != null) {
            extrasBuffer = ChannelBuffers.buffer(ByteOrder.BIG_ENDIAN, 4);
            CacheElement element = command.elements[0];
            extrasBuffer.writeShort((short) (element != null ? element.getExpire() : 0));
            extrasBuffer.writeShort((short) (element != null ? element.getFlags() : 0));
            if ((command.cmd.cmd == Command.GET || command.cmd.cmd == Command.GETS)) {
                if (element != null) {
                    valueBuffer = ChannelBuffers.wrappedBuffer(ByteOrder.BIG_ENDIAN, element.getData());
                } else {
                    valueBuffer = ChannelBuffers.buffer(0);
                }
            } else if (command.cmd.cmd == Command.INCR || command.cmd.cmd == Command.DECR) {
                valueBuffer = ChannelBuffers.buffer(ByteOrder.BIG_ENDIAN, 8);
                valueBuffer.writeLong(command.incrDecrResponse);
            }
        } else if (command.cmd.cmd == Command.INCR || command.cmd.cmd == Command.DECR) {
            valueBuffer = ChannelBuffers.buffer(ByteOrder.BIG_ENDIAN, 8);
            valueBuffer.writeLong(command.incrDecrResponse);
        }
        long casUnique = 0;
        if (command.elements != null && command.elements.length != 0 && command.elements[0] != null) {
            casUnique = command.elements[0].getCasUnique();
        }
        if (command.cmd.cmd == Command.STATS) {
            if (corkedBuffers.containsKey(command.cmd.opaque)) uncork(command.cmd.opaque, messageEvent.getChannel());
            for (Map.Entry<String, Set<String>> statsEntries : command.stats.entrySet()) {
                for (String stat : statsEntries.getValue()) {
                    keyBuffer = ChannelBuffers.wrappedBuffer(ByteOrder.BIG_ENDIAN, statsEntries.getKey().getBytes(MemcachedBinaryCommandDecoder.USASCII));
                    valueBuffer = ChannelBuffers.wrappedBuffer(ByteOrder.BIG_ENDIAN, stat.getBytes(MemcachedBinaryCommandDecoder.USASCII));
                    ChannelBuffer headerBuffer = constructHeader(bcmd, extrasBuffer, keyBuffer, valueBuffer, getStatusCode(command).code, command.cmd.opaque, casUnique);
                    writePayload(messageEvent, extrasBuffer, keyBuffer, valueBuffer, headerBuffer);
                }
            }
            keyBuffer = null;
            valueBuffer = null;
            ChannelBuffer headerBuffer = constructHeader(bcmd, extrasBuffer, keyBuffer, valueBuffer, getStatusCode(command).code, command.cmd.opaque, casUnique);
            writePayload(messageEvent, extrasBuffer, keyBuffer, valueBuffer, headerBuffer);
        } else {
            ChannelBuffer headerBuffer = constructHeader(bcmd, extrasBuffer, keyBuffer, valueBuffer, getStatusCode(command).code, command.cmd.opaque, casUnique);
            if (bcmd.noreply) {
                int totalCapacity = headerBuffer.capacity() + (extrasBuffer != null ? extrasBuffer.capacity() : 0) + (keyBuffer != null ? keyBuffer.capacity() : 0) + (valueBuffer != null ? valueBuffer.capacity() : 0);
                ChannelBuffer corkedResponse = cork(command.cmd.opaque, totalCapacity);
                corkedResponse.writeBytes(headerBuffer);
                if (extrasBuffer != null) corkedResponse.writeBytes(extrasBuffer);
                if (keyBuffer != null) corkedResponse.writeBytes(keyBuffer);
                if (valueBuffer != null) corkedResponse.writeBytes(valueBuffer);
            } else {
                if (corkedBuffers.containsKey(command.cmd.opaque)) uncork(command.cmd.opaque, messageEvent.getChannel());
                writePayload(messageEvent, extrasBuffer, keyBuffer, valueBuffer, headerBuffer);
            }
        }
    }
