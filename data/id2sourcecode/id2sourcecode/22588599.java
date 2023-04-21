    public int next(OctetSeqHolder buf, IntHolder off, IntHolder len) {
        if (m_head == null) {
            buf.value = null;
            off.value = 0;
            return -1;
        }
        if (len.value < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (m_read_write_mode && m_head.m_fMode == Scrap.SCRAP_MODE_READONLY) {
            byte[] copy = new byte[m_head.m_fLength];
            System.arraycopy(m_head.m_fBuffer, m_head.m_fOffset, copy, 0, m_head.m_fLength);
            m_head.m_fBuffer = copy;
            m_head.m_fOffset = 0;
            m_head.m_fMode = Scrap.SCRAP_MODE_NORMAL;
        }
        buf.value = m_head.m_fBuffer;
        off.value = m_head.m_fOffset;
        if (len.value < m_head.m_fLength) {
            int olen = len.value;
            len.value = 0;
            if ((m_mark == null && m_tail == null) || m_head == m_temp_head) {
                m_head.m_fOffset += olen;
                m_head.m_fLength -= olen;
            } else {
                m_temp_head.m_fBuffer = m_head.m_fBuffer;
                m_temp_head.m_fOffset = m_head.m_fOffset + olen;
                m_temp_head.m_fLength = m_head.m_fLength - olen;
                m_temp_head.m_fMode = m_head.m_fMode | Scrap.SCRAP_MODE_SHARED;
                m_temp_head.m_fPosition = m_head.m_fPosition;
                m_temp_head.m_fNext = m_head.m_fNext;
                m_head = m_temp_head;
            }
            m_avail -= olen;
            return olen;
        } else {
            int olen = m_head.m_fLength;
            len.value -= olen;
            if ((m_head = m_head.m_fNext) == m_tail) {
                m_head = null;
            }
            m_avail -= olen;
            return olen;
        }
    }
