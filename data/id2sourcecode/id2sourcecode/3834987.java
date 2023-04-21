    private void readZipEntry(final ZipInputStream in, final ZipEntry entry) throws IOException {
        final int length = (int) entry.getSize();
        if (length >= 0) {
            ensureReadCapacity(length);
            int totalread = 0;
            for (int read; (totalread < length) && (read = in.read(m_readbuf, totalread, length - totalread)) >= 0; totalread += read) ;
            m_readpos = totalread;
        } else {
            ensureReadCapacity(BUF_SIZE);
            m_baos.reset();
            for (int read; (read = in.read(m_readbuf)) >= 0; m_baos.write(m_readbuf, 0, read)) ;
            m_readbuf = m_baos.copyByteArray();
            m_readpos = m_readbuf.length;
        }
    }
