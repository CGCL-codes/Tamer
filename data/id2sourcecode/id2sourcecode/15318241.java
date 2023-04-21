    public void read(DwgFile dwgFile) throws IOException {
        System.out.println("DwgFileV15Reader.read() executed ...");
        this.dwgFile = dwgFile;
        File f = new File(dwgFile.getFileName());
        FileInputStream fis = new FileInputStream(f);
        FileChannel fc = fis.getChannel();
        long s = fc.size();
        ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, s);
        readDwgSectionOffsets(bb);
        try {
            readDwgObjectOffsets(bb);
        } catch (Exception e) {
            System.out.println("Error leyendo offsets y classes. Posible corrupci�n en" + "el DWG file ...");
        }
        long t1 = System.currentTimeMillis();
        readDwgObjects(bb);
        long t2 = System.currentTimeMillis();
        System.out.println("Tiempo empleado por readDwgObjects() = " + (t2 - t1));
    }
