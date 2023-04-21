    private void lockFile() throws IOException {
        switch(m_lockMode) {
            case EXCEPTION_IF_LOCKED:
                m_fileLock = m_file.getChannel().tryLock();
                if (m_fileLock == null) {
                    throw new IOException("Access denied. " + "File [" + getPath() + "] already locked");
                }
                break;
            case WAIT_IF_LOCKED:
                while (m_fileLock == null) {
                    m_fileLock = m_file.getChannel().tryLock();
                    if (m_fileLock == null) {
                        try {
                            Thread.sleep(LOCK_DELAY);
                        } catch (final InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                break;
            case NO_LOCKS:
                break;
        }
    }
