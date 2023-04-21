    public ShapeRecords(String fileprefix, int shapetype) {
        isvalid = false;
        try {
            FileInputStream fis = new FileInputStream(fileprefix + ".shp");
            FileChannel fc = fis.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate((int) fc.size() - 100);
            fc.read(buffer, 100);
            buffer.flip();
            buffer.order(ByteOrder.BIG_ENDIAN);
            region = new ArrayList<ComplexRegion>();
            while (buffer.hasRemaining()) {
                ShapeRecord shr = new ShapeRecord(buffer, shapetype);
                ComplexRegion sr = new ComplexRegion();
                ArrayList<SimpleRegion> regions = new ArrayList();
                for (int j = 0; j < shr.getNumberOfParts(); j++) {
                    SimpleRegion s = new SimpleRegion();
                    s.setPolygon(shr.getPoints(j));
                    regions.add(s);
                }
                sr.addSet(regions);
                region.add(sr);
            }
            fis.close();
            isvalid = true;
        } catch (Exception e) {
            System.out.println("loading shape records error: " + fileprefix + ": " + e.toString());
            e.printStackTrace();
        }
    }
