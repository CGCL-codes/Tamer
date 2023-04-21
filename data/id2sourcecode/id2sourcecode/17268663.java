    public void writeHeader(boolean startFileLockingThread, boolean shuttingDown) {
        if (shuttingDown) {
            _freespaceManager.write(this);
            _freespaceManager = null;
        }
        StatefulBuffer writer = createStatefulBuffer(systemTransaction(), 0, _fileHeader.length());
        _fileHeader.writeFixedPart(this, startFileLockingThread, shuttingDown, writer, blockSize());
        if (shuttingDown) {
            ensureLastSlotWritten();
        }
        syncFiles();
    }
