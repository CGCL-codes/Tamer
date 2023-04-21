    public long getSourceModifiedTime() {
        if (m_file != null) {
            return m_file.lastModified();
        } else if (m_url != null) {
            try {
                URLConnection ucon = m_url.openConnection();
                return ucon.getLastModified();
            } catch (IOException ex) {
                return 0;
            }
        } else {
            return 0;
        }
    }
