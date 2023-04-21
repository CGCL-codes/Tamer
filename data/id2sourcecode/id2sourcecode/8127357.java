    public ByteArrayDataSource(InputStream is, String sType) {
        this.m_sType = sType;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int nCh;
            while ((nCh = is.read()) != -1) os.write(nCh);
            m_abyteData = os.toByteArray();
        } catch (IOException ioex) {
            m_log.error("IOException caught in reading input stream with type=" + sType, ioex);
        }
    }
