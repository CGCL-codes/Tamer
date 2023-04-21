    public boolean load(File file) {
        boolean loadSuccess = true;
        long lPos;
        FileChannel fc;
        try {
            m_File = file;
            FileInputStream fis = new FileInputStream(file);
            fc = fis.getChannel();
            fis.read(m_baBackground);
            m_FileHeader.load(fis);
            m_Objects.load(fis, m_FileHeader.m_nTrackDataOffset);
            m_DataHeader.load(fis);
            m_TrackSegments.load(fis);
            m_CCLine.load(fis);
            m_CCSetup.load(fis);
            m_PitlaneSegments.load(fis);
            m_PitlaneSegments.remove(m_PitlaneSegments.size() - 1);
            lPos = m_PitlaneSegments.size();
            lPos = fc.position();
            int nCount = m_Footer.load(fis);
            m_nLapNumIndex = nCount - 10;
            fis.close();
        } catch (Exception exceptionError) {
            loadSuccess = false;
        }
        ;
        if (loadSuccess == false) {
            return false;
        } else {
            calculateTrackLayout();
            calculateCCLine();
            return true;
        }
    }
