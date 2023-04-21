    public IsoFileSystem(String name) {
        try {
            pointer = new RandomAccessFile(name, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            pvd = pointer.getChannel().map(MapMode.READ_ONLY, 16 << 11, 2048);
            PathTableSize = pvd.getInt(PathTableSizeInfo);
            System.out.println("PathTableSize " + PathTableSize);
            LocationOfPathTable = pvd.getInt(PathTableLocationInfo);
            System.out.println(LocationOfPathTable);
            pointer.seek((LocationOfPathTable << 11));
            System.out.println(pointer.readUnsignedByte());
            System.out.print(pvd.getShort(SectorSizeInfo));
        } catch (IOException e) {
        }
    }
