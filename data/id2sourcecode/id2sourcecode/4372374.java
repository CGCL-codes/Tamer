        int write() throws IOException {
            if (!m_headerSent) {
                m_headerSent = true;
                m_headerBuffer.clear();
                m_headerBuffer.putInt(getBuffer().remaining());
                m_headerBuffer.flip();
                do {
                    getChannel().write(m_headerBuffer);
                } while (m_headerBuffer.remaining() > 0);
            }
            m_bytesWritten += (getChannel().write(getBuffer()));
            return getBuffer().remaining();
        }
