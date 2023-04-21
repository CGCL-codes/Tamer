    public void loadFromURL(URL urlToOpen) throws IOException {
        InputStream isConn = urlToOpen.openStream();
        StringBuilder sbBuffer = new StringBuilder();
        int iNextByte = isConn.read();
        while (iNextByte != -1) {
            sbBuffer.append((char) iNextByte);
            iNextByte = isConn.read();
        }
        isConn.close();
        m_sOriginalDocument = sbBuffer.toString();
        m_sDocumentLC = m_sOriginalDocument.toLowerCase();
        m_iCursor = 0;
        m_iSelector = 0;
    }
