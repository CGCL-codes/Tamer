    public int save(File file) {
        int nBytesWritten = 0;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(m_baBackground);
            nBytesWritten = m_baBackground.length;
            nBytesWritten += m_FileHeader.save(fos);
            nBytesWritten += m_Objects.save(fos);
            nBytesWritten += m_DataHeader.save(fos);
            nBytesWritten += m_TrackSegments.save(fos);
            nBytesWritten += m_CCLine.save(fos);
            nBytesWritten += m_CCSetup.save(fos);
            nBytesWritten += m_PitlaneSegments.save(fos);
            m_Footer.setChecksum(0);
            nBytesWritten += m_Footer.save(fos);
            FileChannel fc = fos.getChannel();
            long lPos = fc.position();
            m_FileHeader.setChecksumOffset(lPos - 4);
            fc.position(4104);
            m_FileHeader.saveChecksumOffset(fos);
            fos.close();
            calculateChecksum(file);
        } catch (IOException ioe) {
        }
        return nBytesWritten;
    }
