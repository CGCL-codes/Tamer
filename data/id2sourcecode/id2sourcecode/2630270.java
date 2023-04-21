    public static PSF read(String filename) throws IOException {
        PSF psf = new PSF();
        RandomAccessFile raf = new RandomAccessFile(filename, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int) channel.size());
        psf.read(buffer);
        channel.close();
        raf.close();
        System.out.println(psf);
        return psf;
    }
