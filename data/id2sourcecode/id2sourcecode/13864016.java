    public DBFRecords(String filename, DBFHeader header) {
        records = new ArrayList();
        isvalid = false;
        try {
            FileInputStream fis = new FileInputStream(filename);
            FileChannel fc = fis.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate((int) fc.size() - header.getRecordsOffset());
            fc.read(buffer, header.getRecordsOffset());
            buffer.flip();
            int i = 0;
            ArrayList<DBFField> fields = header.getFields();
            while (i < header.getNumberOfRecords() && buffer.hasRemaining()) {
                records.add(new DBFRecord(buffer, fields));
                i++;
            }
            fis.close();
            isvalid = true;
        } catch (Exception e) {
            System.out.println("loading records error: " + filename + ": " + e.toString());
            e.printStackTrace();
        }
    }
