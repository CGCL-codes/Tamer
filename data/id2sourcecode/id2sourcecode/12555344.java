    public static int getVersion(String filename) {
        int version = 0;
        try {
            int peStart;
            int peSignature;
            short numberOfSections;
            int ptrOptionalHeader;
            MappedByteBuffer file = new FileInputStream(filename).getChannel().map(FileChannel.MapMode.READ_ONLY, 0, new File(filename).length());
            file.order(ByteOrder.LITTLE_ENDIAN);
            peStart = file.getInt(PE_START);
            peSignature = file.getInt(peStart + 0);
            if (peSignature != 0x00004550) throw new IOException("Invalid PE file!");
            numberOfSections = file.getShort(peStart + 6);
            ptrOptionalHeader = peStart + 24;
            version = processOptionalHeader(file, ptrOptionalHeader, numberOfSections);
        } catch (IOException e) {
            System.err.println(e);
        }
        return version;
    }
