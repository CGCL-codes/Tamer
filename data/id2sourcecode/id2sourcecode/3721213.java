    public void open() throws FileNotFoundException, IOException {
        m_in = new FileInputStream(m_file);
        m_channel = m_in.getChannel();
        m_oldmsheader = new PEOldMSHeader(this);
        m_oldmsheader.read();
        long headoffset = m_oldmsheader.e_lfanew;
        m_header = new PEHeader(this, headoffset);
        m_header.read();
        int seccount = m_header.NumberOfSections;
        long offset = headoffset + (m_header.NumberOfRvaAndSizes * 8) + 24 + 96;
        for (int i = 0; i < seccount; i++) {
            PESection sect = new PESection(this, offset);
            sect.read();
            m_sections.add(sect);
            offset += 40;
        }
        ByteBuffer resbuf = null;
        long resourceoffset = m_header.ResourceDirectory_VA;
        for (int i = 0; i < seccount; i++) {
            PESection sect = (PESection) m_sections.get(i);
            if (sect.VirtualAddress == resourceoffset) {
                PEResourceDirectory prd = new PEResourceDirectory(this, sect);
                resbuf = prd.buildResource(sect.VirtualAddress);
                break;
            }
        }
    }
