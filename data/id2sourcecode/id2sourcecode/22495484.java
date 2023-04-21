    public void reload() throws IOException {
        String strEncoding = m_strSourceEncoding;
        if (m_bEncodingDetected) {
            strEncoding = null;
        }
        if (m_file != null) {
            init(new FileInputStream(m_file), strEncoding, m_file.lastModified());
        } else if (m_url != null) {
            URLConnection ucon = m_url.openConnection();
            long lLastModified = ucon.getLastModified();
            InputStream is = ucon.getInputStream();
            init(is, strEncoding, lLastModified);
        }
    }
