    protected final synchronized long writePhysical(ByteBuffer[] buffersToWrite) throws ClosedConnectionException, IOException {
        if (isOpen()) {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine("[" + getId() + "] sending: " + printByteBuffersForDebug(buffersToWrite));
            }
            long numberOfSendData = getAssignedSocketChannel().write(buffersToWrite);
            bytesSend += numberOfSendData;
            return numberOfSendData;
        } else {
            ClosedConnectionException cce = new ClosedConnectionException("[" + getId() + "] connection " + toCompactString() + " ist already closed. Couldn't write " + printData(buffersToWrite));
            LOG.throwing(this.getClass().getName(), "send(ByteBuffer[])", cce);
            throw cce;
        }
    }
