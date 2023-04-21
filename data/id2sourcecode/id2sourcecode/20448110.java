    @Override
    public void init() throws InitialisationException {
        try {
            raf = new RandomAccessFile(file, "r");
            size = raf.length() > Integer.MAX_VALUE ? Integer.MAX_VALUE : Long.valueOf(raf.length()).intValue();
            System.out.println("file size =" + size);
            buffer = raf.getChannel().map(MapMode.READ_ONLY, 0, size);
            buffer.load();
            Runtime.getRuntime().addShutdownHook(new Thread() {

                public void run() {
                    closed = true;
                    MappedByteBufferUtil.unmap(buffer);
                    try {
                        raf.close();
                    } catch (IOException e) {
                    }
                }
            });
        } catch (IOException e) {
            throw new InitialisationException(e);
        }
    }
