    public PRCPreViewer(String filename) throws Exception {
        File file = new File(filename);
        FileInputStream fin = new FileInputStream(file);
        channel = fin.getChannel();
        extensions = new Hashtable();
        recordMap = new Hashtable();
        huffmanTable = new Vector<HuffmanSection>();
        int numRecords = readPRCHeader();
        readRecords(numRecords);
        readMOBIHeader();
        readEXTHeader();
        channel.close();
        fin.close();
        if (fullNameOffset != -1) {
            title = getFullname(file, (Integer) recordMap.get(0), fullNameOffset, fullNameLength, m_encoding);
            System.out.println(title);
        }
    }
