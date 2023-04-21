    public MobiDecoder(String filename) throws Exception {
        file = new File(filename);
        filesize = file.length();
        FileInputStream fin = new FileInputStream(file);
        channel = fin.getChannel();
        prcHeader = PRCHeader.read(channel);
        palmDocHeader = PalmDocHeader.read(channel);
        mobiHeader = MOBIHeader.read(channel);
        String encoding = mobiHeader.getEncoding();
        extHeader = EXTHeader.read(channel, encoding);
        channel.close();
        fin.close();
        if (palmDocHeader.getCompression() == 17480) {
            int recordOffset = mobiHeader.getHuffmanRecordOffset();
            int recordCount = mobiHeader.getHuffmanRecordCount();
            List<Record> list = new ArrayList<Record>();
            for (int i = 0; i < recordCount; i++) {
                Record record = prcHeader.getRecordList().get(recordOffset + i);
                list.add(record);
            }
            dec = new HuffmanDecoder(file, list, mobiHeader.getEncoding());
            dec.process();
        }
        imageSections = getImageSections();
        if (mobiHeader.getFullNameOffset() != -1) {
            int initialOffset = prcHeader.getRecordList().get(0).getRecordOffset();
            title = NamingUtils.getFullname(file, initialOffset, mobiHeader.getFullNameOffset(), mobiHeader.getFullNameLength(), encoding);
        }
    }
