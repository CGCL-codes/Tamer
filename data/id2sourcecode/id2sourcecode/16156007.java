    private void writeSocket() throws IOException {
        if (isOpen()) {
            IWriteTask writeTask = null;
            if (pendingWriteTask != null) {
                writeTask = pendingWriteTask;
                pendingWriteTask = null;
            } else {
                try {
                    writeTask = TaskFactory.newTask(sendQueue, soSendBufferSize);
                } catch (Throwable t) {
                    throw ConnectionUtils.toIOException(t);
                }
            }
            IWriteResult result = writeTask.write(this);
            if (result.isAllWritten()) {
                sendQueue.removeLeased();
                writeTask.release();
                result.notifyWriteCallback();
            } else {
                pendingWriteTask = writeTask;
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
    }
