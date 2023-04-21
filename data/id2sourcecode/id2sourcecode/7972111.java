    public synchronized boolean write(byte[] buf, int off, int len) throws IOException {
        if (m_thread != null) throw new IllegalStateException("cannot reuse active writer");
        m_buf = buf;
        m_off = off;
        m_len = len;
        m_is_done = false;
        m_exception_message = null;
        m_thread = new Thread(m_target);
        if (log.isDebugEnabled()) log.debug("write: starting thread, m_thread=" + m_thread.getName());
        m_thread.start();
        try {
            try {
                m_thread.join(m_timeout);
                if (log.isDebugEnabled()) log.debug("write: joined, m_thread=" + m_thread.getName());
            } catch (InterruptedException e) {
                if (log.isDebugEnabled()) log.debug("write: interrupted, reason=" + e.getMessage());
            }
            if (m_is_done == false) {
                if (log.isDebugEnabled()) log.debug("write: not done, probably due to timeout, which is okay");
                return false;
            }
            if (m_exception_message != null) {
                log.warn("write: exception occurred during write, reason=" + m_exception_message);
                return false;
            }
            return true;
        } finally {
            synchronized (this) {
                if (m_thread != null) m_thread.interrupt();
                m_thread = null;
            }
        }
    }
