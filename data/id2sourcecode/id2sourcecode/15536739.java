    @SuppressWarnings("unchecked")
    private int writeSocket() throws IOException {
        assert (IoSocketDispatcher.isDispatcherThread());
        int sent = 0;
        if (isOpen()) {
            ByteBuffer[] buffers = sendQueue.drain();
            if (buffers == null) {
                return 0;
            }
            boolean hasUnwrittenBuffers = false;
            try {
                for (int i = 0; i < buffers.length; i++) {
                    if (buffers[i] != null) {
                        int writeSize = buffers[i].remaining();
                        if (writeSize > 0) {
                            if (LOG.isLoggable(Level.FINE)) {
                                if (LOG.isLoggable(Level.FINE)) {
                                    LOG.fine("[" + id + "] sending (" + writeSize + " bytes): " + DataConverter.toTextOrHexString(buffers[i].duplicate(), "UTF-8", 500));
                                }
                            }
                            try {
                                int written = channel.write(buffers[i]);
                                sent += written;
                                sendBytes += written;
                                if (written == writeSize) {
                                    try {
                                        getPreviousCallback().onWritten(buffers[i]);
                                    } catch (Exception e) {
                                        if (LOG.isLoggable(Level.FINE)) {
                                            LOG.fine("error occured by notifying that buffer has been written " + e.toString());
                                        }
                                    }
                                    buffers[i] = null;
                                } else {
                                    hasUnwrittenBuffers = true;
                                    if (LOG.isLoggable(Level.FINE)) {
                                        LOG.fine("[" + id + "] " + written + " of " + (writeSize - written) + " bytes has been sent (" + DataConverter.toFormatedBytesSize((writeSize - written)) + ")");
                                    }
                                    break;
                                }
                            } catch (IOException ioe) {
                                if (LOG.isLoggable(Level.FINE)) {
                                    LOG.fine("error " + ioe.toString() + " occured by writing " + DataConverter.toTextOrHexString(buffers[i].duplicate(), "US-ASCII", 500));
                                }
                                try {
                                    getPreviousCallback().onWriteException(ioe, buffers[i]);
                                } catch (Exception e) {
                                    if (LOG.isLoggable(Level.FINE)) {
                                        LOG.fine("error occured by notifying that write exception (" + e.toString() + ") has been occured " + e.toString());
                                    }
                                }
                                buffers[i] = null;
                                return sent;
                            }
                        }
                    }
                }
            } finally {
                if (hasUnwrittenBuffers) {
                    sendQueue.addFirst(buffers);
                }
            }
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
        return sent;
    }
