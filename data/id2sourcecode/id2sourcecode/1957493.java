    @SuppressWarnings("unchecked")
    private void writeSendQueueDataToSocket() throws IOException {
        assert (IoSocketDispatcher.isDispatcherThread());
        if (isOpen() && !sendQueue.isEmpty()) {
            ByteBuffer buffer = null;
            do {
                buffer = sendQueue.removeFirst();
                if (buffer != null) {
                    int writeSize = buffer.remaining();
                    if (writeSize > 0) {
                        if (LOG.isLoggable(Level.FINE)) {
                            if (LOG.isLoggable(Level.FINE)) {
                                LOG.fine("[" + id + "] sending (" + writeSize + " bytes): " + DataConverter.toTextOrHexString(buffer.duplicate(), "UTF-8", 500));
                            }
                        }
                        int written = channel.write(buffer);
                        sendBytes += written;
                        if (written != writeSize) {
                            if (LOG.isLoggable(Level.FINE)) {
                                LOG.fine("[" + id + "] " + written + " of " + (writeSize - written) + " bytes has been sent. initiate sending of the remaining (total sent " + sendBytes + " bytes)");
                            }
                            sendQueue.addFirst(buffer);
                            updateInterestedSetWrite();
                            break;
                        }
                    }
                }
            } while (buffer != null);
        } else {
            if (LOG.isLoggable(Level.FINEST)) {
                if (!isOpen()) {
                    LOG.finest("[" + getId() + "] couldn't write send queue to socket because socket is already closed (sendQueuesize=" + DataConverter.toFormatedBytesSize(sendQueue.getSize()) + ")");
                }
                if (sendQueue.isEmpty()) {
                    LOG.finest("[" + getId() + "] nothing to write, because send queue is empty ");
                }
            }
        }
    }
