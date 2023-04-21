    public TideDB(String fileName) throws FileNotFoundException, IOException, TideDBException {
        FileInputStream input = null;
        this.fileName = fileName;
        input = new FileInputStream(fileName);
        FileChannel channel = input.getChannel();
        int fileLength = (int) channel.size();
        buffer = new XByteBuffer((channel.map(FileChannel.MapMode.READ_ONLY, 0, fileLength)), ByteOrder.LITTLE_ENDIAN);
//        buffer.order(ByteOrder.LITTLE_ENDIAN);
//        buffer.order(ByteOrder.BIG_ENDIAN);
        // create index
        header = new TideHeaderData(buffer, tindex);
        
        for (int i = 0; i < header.getPub().getNumberOfRecords(); i++) {
            records.add(new TideRecord(this, buffer, tindex.get(i), i));
        }
        
    }
