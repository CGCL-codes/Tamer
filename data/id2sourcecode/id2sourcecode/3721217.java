    public void dumpTo(File destination) throws IOException, CloneNotSupportedException {
        int outputcount = 0;
        FileOutputStream fos = new FileOutputStream(destination);
        FileChannel out = fos.getChannel();
        PEOldMSHeader oldmsheader = (PEOldMSHeader) this.m_oldmsheader.clone();
        PEHeader peheader = (PEHeader) m_header.clone();
        Vector sections = new Vector();
        for (int i = 0; i < m_sections.size(); i++) {
            PESection sect = (PESection) m_sections.get(i);
            PESection cs = (PESection) sect.clone();
            sections.add(cs);
        }
        long newexeoffset = oldmsheader.e_lfanew;
        ByteBuffer msheadbuffer = oldmsheader.get();
        outputcount = out.write(msheadbuffer);
        this.m_channel.position(64);
        out.transferFrom(this.m_channel, 64, newexeoffset - 64);
        ByteBuffer headbuffer = peheader.get();
        out.position(newexeoffset);
        outputcount = out.write(headbuffer);
        long offset = oldmsheader.e_lfanew + (m_header.NumberOfRvaAndSizes * 8) + 24 + 96;
        out.position(offset);
        for (int i = 0; i < sections.size(); i++) {
            PESection sect = (PESection) sections.get(i);
            ByteBuffer buf = sect.get();
            outputcount = out.write(buf);
        }
        offset = 1024;
        long virtualAddress = offset;
        if ((virtualAddress % peheader.SectionAlignment) > 0) virtualAddress += peheader.SectionAlignment - (virtualAddress % peheader.SectionAlignment);
        long resourceoffset = m_header.ResourceDirectory_VA;
        for (int i = 0; i < sections.size(); i++) {
            PESection sect = (PESection) sections.get(i);
            if (resourceoffset == sect.VirtualAddress) {
                out.position(offset);
                long sectoffset = offset;
                PEResourceDirectory prd = this.getResourceDirectory();
                ByteBuffer resbuf = prd.buildResource(sect.VirtualAddress);
                resbuf.position(0);
                out.write(resbuf);
                offset += resbuf.capacity();
                long rem = offset % this.m_header.FileAlignment;
                if (rem != 0) offset += this.m_header.FileAlignment - rem;
                if (out.size() + 1 < offset) {
                    ByteBuffer padder = ByteBuffer.allocate(1);
                    out.write(padder, offset - 1);
                }
                long virtualSize = resbuf.capacity();
                if ((virtualSize % peheader.FileAlignment) > 0) virtualSize += peheader.SectionAlignment - (virtualSize % peheader.SectionAlignment);
                sect.PointerToRawData = sectoffset;
                sect.SizeOfRawData = resbuf.capacity();
                if ((sect.SizeOfRawData % this.m_header.FileAlignment) > 0) sect.SizeOfRawData += (this.m_header.FileAlignment - (sect.SizeOfRawData % this.m_header.FileAlignment));
                sect.VirtualAddress = virtualAddress;
                sect.VirtualSize = virtualSize;
                virtualAddress += virtualSize;
            } else if (sect.PointerToRawData > 0) {
                out.position(offset);
                this.m_channel.position(sect.PointerToRawData);
                long sectoffset = offset;
                out.position(offset + sect.SizeOfRawData);
                ByteBuffer padder = ByteBuffer.allocate(1);
                out.write(padder, offset + sect.SizeOfRawData - 1);
                long outted = out.transferFrom(this.m_channel, offset, sect.SizeOfRawData);
                offset += sect.SizeOfRawData;
                long rem = offset % this.m_header.FileAlignment;
                if (rem != 0) {
                    offset += this.m_header.FileAlignment - rem;
                }
                sect.PointerToRawData = sectoffset;
                sect.VirtualAddress = virtualAddress;
                virtualAddress += sect.VirtualSize;
                if ((virtualAddress % peheader.SectionAlignment) > 0) virtualAddress += peheader.SectionAlignment - (virtualAddress % peheader.SectionAlignment);
            } else {
                long virtualSize = sect.VirtualSize;
                if ((virtualSize % peheader.SectionAlignment) > 0) virtualSize += peheader.SectionAlignment - (virtualSize % peheader.SectionAlignment);
                sect.VirtualAddress = virtualAddress;
                virtualAddress += virtualSize;
            }
        }
        peheader.updateVAAndSize(m_sections, sections);
        headbuffer = peheader.get();
        out.position(newexeoffset);
        outputcount = out.write(headbuffer);
        offset = oldmsheader.e_lfanew + (m_header.NumberOfRvaAndSizes * 8) + 24 + 96;
        out.position(offset);
        for (int i = 0; i < sections.size(); i++) {
            PESection sect = (PESection) sections.get(i);
            ByteBuffer buf = sect.get();
            outputcount = out.write(buf);
        }
        fos.flush();
        fos.close();
    }
