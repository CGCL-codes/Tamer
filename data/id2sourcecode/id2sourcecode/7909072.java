    private MappedByteBuffer mapFile(String filename, long offset) {
        MappedByteBuffer buffer = null;
        try {
            FileInputStream dataFile = new FileInputStream(filename);
            File file = new File(filename);
            FileChannel fc = dataFile.getChannel();
            buffer = fc.map(FileChannel.MapMode.READ_ONLY, offset, file.length() - offset);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
        } catch (FileNotFoundException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        return buffer;
    }
