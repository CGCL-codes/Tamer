    public void open(File file) throws FileNotFoundDriverException {
        try {
            fin = new FileInputStream(file);
            channel = fin.getChannel();
            buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
            myHeader = new DbaseFileHeader();
            myHeader.readHeader(buffer);
        } catch (IOException e) {
            throw new FileNotFoundDriverException(file.getName(), e, file.getAbsolutePath());
        }
    }
