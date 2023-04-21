        VLexer(String fname) {
            super(m_reader);
            try {
                m_writer = new PipedWriter();
                m_reader.connect(m_writer);
                m_lexerpp = new Lexerpp(fname, m_writer);
                setFilename(fname);
                m_lexerpp.start();
            } catch (Exception ex) {
                abnormalExit(ex);
            }
        }
