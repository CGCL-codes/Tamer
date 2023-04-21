    public void read() throws IOException {
        FileChannel ch = m_pe.getChannel();
        ByteBuffer head = ByteBuffer.allocate(40);
        head.order(ByteOrder.LITTLE_ENDIAN);
        ch.position(m_baseoffset);
        ch.read(head);
        head.position(0);
        ANSI_Name = new byte[8];
        for (int i = 0; i < 8; i++) ANSI_Name[i] = head.get();
        VirtualSize = head.getInt();
        VirtualAddress = head.getInt();
        SizeOfRawData = head.getInt();
        PointerToRawData = head.getInt();
        PointerToRelocations = head.getInt();
        PointerToLinenumbers = head.getInt();
        NumberOfRelocations = head.getShort();
        NumberOfLinenumbers = head.getShort();
        Characteristics = head.getInt();
    }
