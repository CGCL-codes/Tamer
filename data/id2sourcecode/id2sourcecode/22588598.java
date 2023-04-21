    public void writeTo(OutputStream os) throws IOException {
        if (m_head == null) {
            throw new EOFException("Buffer is empty");
        }
        boolean interrupt = Thread.interrupted();
        Trace.isGIOPHeaderOK(m_head.m_fBuffer, 0);
        if (m_read_write_mode) {
            while (m_head != m_tail) {
                if (m_head.m_fMode == Scrap.SCRAP_MODE_READONLY) {
                    byte[] tmp = new byte[m_head.m_fLength];
                    System.arraycopy(m_head.m_fBuffer, m_head.m_fOffset, tmp, 0, m_head.m_fLength);
                    m_head.m_fBuffer = tmp;
                    m_head.m_fOffset = 0;
                    m_head.m_fMode = Scrap.SCRAP_MODE_NORMAL;
                }
                int d = 0;
                while (d < m_head.m_fLength) {
                    try {
                        os.write(m_head.m_fBuffer, m_head.m_fOffset + d, m_head.m_fLength - d);
                        break;
                    } catch (InterruptedIOException ex) {
                        interrupt = true;
                        d += ex.bytesTransferred;
                    }
                }
                m_head = m_head.m_fNext;
            }
        } else {
            while (m_head != m_tail) {
                int d = 0;
                while (d < m_head.m_fLength) {
                    try {
                        os.write(m_head.m_fBuffer, m_head.m_fOffset + d, m_head.m_fLength - d);
                        break;
                    } catch (InterruptedIOException ex) {
                        interrupt = true;
                        d += ex.bytesTransferred;
                    }
                }
                m_head = m_head.m_fNext;
            }
        }
        m_head = null;
        m_avail = 0;
        if (interrupt) {
            Thread.currentThread().interrupt();
        }
    }
