    public IsoFS(String name) {
        try {
            pointer = new RandomAccessFile(name, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            pvd = pointer.getChannel().map(MapMode.READ_ONLY, 16 << 11, 2048);
            PathTableSize = pvd.getInt(136);
            System.out.println("PathTableSize " + PathTableSize);
        } catch (IOException e) {
        }
    }
