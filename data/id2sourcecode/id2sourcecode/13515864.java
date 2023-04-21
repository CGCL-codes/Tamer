    public ShapeRecords(String fileprefix, int shapetype) {
        isvalid = false;
        try {
            FileInputStream fis = new FileInputStream(fileprefix + ".shp");
            FileChannel fc = fis.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate((int) fc.size() - 100);
            fc.read(buffer, 100);
            buffer.flip();
            buffer.order(ByteOrder.BIG_ENDIAN);
            records = new ArrayList();
            while (buffer.hasRemaining()) {
                records.add(new ShapeRecord(buffer, shapetype));
            }
            fis.close();
            isvalid = true;
        } catch (Exception e) {
            System.out.println("loading shape records error: " + fileprefix + ": " + e.toString());
            e.printStackTrace();
        }
    }
