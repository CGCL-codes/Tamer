    @Override
    public void messageReceived(final ChannelHandlerContext channelHandlerContext, final MessageEvent messageEvent) throws Exception {
        final ResponseMessage command = (ResponseMessage) messageEvent.getMessage();
        final MemcachedBinaryCommandDecoder.BinaryOp bcmd = MemcachedBinaryCommandDecoder.BinaryOp.forCommandMessage(command.cmd);
        ChannelBuffer extrasBuffer = null;
        ChannelBuffer keyBuffer = null;
        if (bcmd.addKeyToResponse && command.cmd.keys != null && command.cmd.keys.size() != 0) {
            keyBuffer = ChannelBuffers.wrappedBuffer(command.cmd.keys.get(0).bytes);
        }
        ChannelBuffer valueBuffer = null;
        if (command.elements != null) {
            extrasBuffer = ChannelBuffers.buffer(ByteOrder.BIG_ENDIAN, 4);
            final CacheElement element = command.elements[0];
            extrasBuffer.writeShort((short) (element != null ? element.getExpire() : 0));
            extrasBuffer.writeShort((short) (element != null ? element.getFlags() : 0));
            if ((command.cmd.op == Op.GET || command.cmd.op == Op.GETS)) {
                if (element != null) {
                    valueBuffer = ChannelBuffers.wrappedBuffer(element.getData());
                } else {
                    valueBuffer = ChannelBuffers.buffer(0);
                }
            } else if (command.cmd.op == Op.INCR || command.cmd.op == Op.DECR) {
                valueBuffer = ChannelBuffers.buffer(ByteOrder.BIG_ENDIAN, 8);
                valueBuffer.writeLong(command.incrDecrResponse);
            }
        } else if (command.cmd.op == Op.INCR || command.cmd.op == Op.DECR) {
            valueBuffer = ChannelBuffers.buffer(ByteOrder.BIG_ENDIAN, 8);
            valueBuffer.writeLong(command.incrDecrResponse);
        }
        long casUnique = 0;
        if (command.elements != null && command.elements.length != 0 && command.elements[0] != null) {
            casUnique = command.elements[0].getCasUnique();
        }
        if (command.cmd.op == Op.STATS) {
            if (corkedBuffers.containsKey(command.cmd.opaque)) {
                uncork(command.cmd.opaque, messageEvent.getChannel());
            }
            for (final Map.Entry<String, Set<String>> statsEntries : command.stats.entrySet()) {
                for (final String stat : statsEntries.getValue()) {
                    keyBuffer = ChannelBuffers.wrappedBuffer(ByteOrder.BIG_ENDIAN, statsEntries.getKey().getBytes(MemcachedBinaryCommandDecoder.USASCII));
                    valueBuffer = ChannelBuffers.wrappedBuffer(ByteOrder.BIG_ENDIAN, stat.getBytes(MemcachedBinaryCommandDecoder.USASCII));
                    final ChannelBuffer headerBuffer = constructHeader(bcmd, extrasBuffer, keyBuffer, valueBuffer, getStatusCode(command).code, command.cmd.opaque, casUnique);
                    writePayload(messageEvent, extrasBuffer, keyBuffer, valueBuffer, headerBuffer);
                }
            }
            keyBuffer = null;
            valueBuffer = null;
            final ChannelBuffer headerBuffer = constructHeader(bcmd, extrasBuffer, keyBuffer, valueBuffer, getStatusCode(command).code, command.cmd.opaque, casUnique);
            writePayload(messageEvent, extrasBuffer, keyBuffer, valueBuffer, headerBuffer);
        } else {
            final ChannelBuffer headerBuffer = constructHeader(bcmd, extrasBuffer, keyBuffer, valueBuffer, getStatusCode(command).code, command.cmd.opaque, casUnique);
            if (bcmd.noreply) {
                final int totalCapacity = headerBuffer.capacity() + (extrasBuffer != null ? extrasBuffer.capacity() : 0) + (keyBuffer != null ? keyBuffer.capacity() : 0) + (valueBuffer != null ? valueBuffer.capacity() : 0);
                final ChannelBuffer corkedResponse = cork(command.cmd.opaque, totalCapacity);
                corkedResponse.writeBytes(headerBuffer);
                if (extrasBuffer != null) {
                    corkedResponse.writeBytes(extrasBuffer);
                }
                if (keyBuffer != null) {
                    corkedResponse.writeBytes(keyBuffer);
                }
                if (valueBuffer != null) {
                    corkedResponse.writeBytes(valueBuffer);
                }
            } else {
                if (corkedBuffers.containsKey(command.cmd.opaque)) {
                    uncork(command.cmd.opaque, messageEvent.getChannel());
                }
                writePayload(messageEvent, extrasBuffer, keyBuffer, valueBuffer, headerBuffer);
            }
        }
    }
