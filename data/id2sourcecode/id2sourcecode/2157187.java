    protected synchronized ByteBuffer[] writePhysical(ByteBuffer[] buffers) throws ClosedConnectionException, IOException {
        if (isOpen()) {
            if (buffers.length > 0) {
                int sizeToSend = 0;
                for (ByteBuffer buffer : buffers) {
                    sizeToSend += buffer.remaining();
                }
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.fine("[" + id + "] sending (" + sizeToSend + " bytes): " + TextUtils.toTextOrHexString(buffers, "UTF-8", DEBUG_MAX_OUTPUT_SIZE));
                }
                long numberOfSendData = channel.write(buffers);
                bytesWritten += numberOfSendData;
                if (numberOfSendData < sizeToSend) {
                    List<ByteBuffer> unwritten = new ArrayList<ByteBuffer>();
                    for (ByteBuffer buffer : buffers) {
                        if (buffer.remaining() > 0) {
                            unwritten.add(buffer);
                        }
                    }
                    return unwritten.toArray(new ByteBuffer[unwritten.size()]);
                }
            }
            return null;
        } else {
            ClosedConnectionException cce = new ClosedConnectionException("[" + id + "] connection " + id + " ist already closed. Couldn't write");
            LOG.throwing(this.getClass().getName(), "send(ByteBuffer[])", cce);
            throw cce;
        }
    }
