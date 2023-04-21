    private void setup(String id, String name) throws IOException {
        file = new File(name);
        file.createNewFile();
        this.name = name;
        try {
            raf = new RandomAccessFile(file, "rw");
            chan = raf.getChannel();
        } catch (FileNotFoundException fnfe) {
        }
        System.setProperty(propid, this.name);
    }
