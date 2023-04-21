    private NRTFile(File f, String mode) throws IOException {
        raf = new RandomAccessFile(f, mode);
        fch = raf.getChannel();
        b = ByteBuffer.allocate(OSCChannel.DEFAULTBUFSIZE);
        time = 0.0;
        pending = new OSCBundle(time);
    }
