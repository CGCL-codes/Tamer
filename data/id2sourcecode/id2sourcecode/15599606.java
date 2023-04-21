    public void serialize(DigitalObject obj, OutputStream out, String encoding, int transContext) throws ObjectIntegrityException, StreamIOException, UnsupportedEncodingException {
        m_obj = obj;
        m_encoding = (encoding == null || encoding == "") ? "UTF-8" : encoding;
        m_transContext = transContext;
        m_pid = PID.getInstance(m_obj.getPid());
        m_feed = abdera.newFeed();
        if (m_format.equals(ATOM_ZIP1_1)) {
            m_zout = new ZipOutputStream(out);
        }
        addObjectProperties();
        m_feed.setIcon("http://www.fedora-commons.org/images/logo_vertical_transparent_200_251.png");
        addDatastreams();
        if (m_format.equals(ATOM_ZIP1_1)) {
            try {
                m_zout.putNextEntry(new ZipEntry("atommanifest.xml"));
                m_feed.writeTo("prettyxml", m_zout);
                m_zout.closeEntry();
                m_zout.close();
            } catch (IOException e) {
                throw new StreamIOException(e.getMessage(), e);
            }
        } else {
            try {
                m_feed.writeTo("prettyxml", out);
            } catch (IOException e) {
                throw new StreamIOException(e.getMessage(), e);
            }
        }
    }
