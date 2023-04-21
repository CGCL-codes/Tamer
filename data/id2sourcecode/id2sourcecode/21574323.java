    private void writeFinalC14nScene(String finalScene) {
        try {
            long length = finalScene.length();
            raf = new RandomAccessFile(x3dFileNameCanonical, "rwd");
            fc = raf.getChannel();
            bb = ByteBuffer.allocate((int) length);
            bb = ByteBuffer.wrap(finalScene.getBytes());
            fc.truncate(length);
            fc.position(0);
            fc.write(bb);
        } catch (IOException ioe) {
            log.fatal(ioe);
        }
    }
