    @Override
    protected void writeImage(final BufferedImage image, final File destFile) {
        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    PngEncoder encoder = new PngEncoder(image, encodeAlpha, PngEncoder.FILTER_NONE, compressionLevel);
                    monitor.setProgress(55);
                    byte[] encodedBytes = encoder.pngEncode();
                    monitor.setProgress(90);
                    if (monitor.isCancelled()) {
                        monitor.stop();
                        return;
                    }
                    FileChannel outChannel = new FileOutputStream(destFile).getChannel();
                    ByteBuffer buffer = ByteBuffer.allocate(encodedBytes.length);
                    buffer.put(encodedBytes);
                    buffer.clear();
                    outChannel.write(buffer);
                    outChannel.close();
                    monitor.stop();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    handleExportFailure();
                }
            }
        };
        thread.start();
    }
