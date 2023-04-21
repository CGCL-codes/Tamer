    private WritableByteChannel getWriteChannel(String path) throws FileNotFoundDriverException {
        WritableByteChannel channel = null;
        try {
            File f = new File(path);
            if (!f.exists()) {
                System.out.println("Creando fichero " + f.getAbsolutePath());
                if (!f.createNewFile()) {
                    throw new FileNotFoundDriverException(getName(), null, path);
                }
            }
            RandomAccessFile raf = new RandomAccessFile(f, "rw");
            channel = raf.getChannel();
        } catch (IOException e) {
            throw new FileNotFoundDriverException(getName(), e, path);
        }
        return channel;
    }
