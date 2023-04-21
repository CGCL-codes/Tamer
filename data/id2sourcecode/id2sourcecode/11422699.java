    public ZipWriter(String filename, int version) throws IOException {
        images = new LinkedList<ZipSerializerImage>();
        files = new LinkedList<ZipSerializerFile>();
        zos = new ZipOutputStream(new FileOutputStream(filename));
        ZipEntry mainEntry = new ZipEntry("maindata.xml");
        zos.putNextEntry(mainEntry);
        this.version = version;
    }
