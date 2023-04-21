    public ShapeHeader(String fileprefix) {
        try {
            FileInputStream fis = new FileInputStream(fileprefix + ".shp");
            FileChannel fc = fis.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            fc.read(buffer);
            buffer.flip();
            buffer.order(ByteOrder.BIG_ENDIAN);
            filecode = buffer.getInt();
            buffer.getInt();
            buffer.getInt();
            buffer.getInt();
            buffer.getInt();
            buffer.getInt();
            filelength = buffer.getInt();
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            version = buffer.getInt();
            shapetype = buffer.getInt();
            boundingbox = new double[8];
            for (int i = 0; i < 8; i++) {
                boundingbox[i] = buffer.getDouble();
            }
            fis.close();
            isvalid = true;
        } catch (Exception e) {
            System.out.println("loading header error: " + fileprefix + ": " + e.toString());
            e.printStackTrace();
        }
    }
