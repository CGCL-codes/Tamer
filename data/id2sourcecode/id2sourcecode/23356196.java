    public void dumpBuffer(final ByteBuffer byteBuffer, final String filename) {
        try {
            byte[] array = new byte[byteBuffer.capacity()];
            byteBuffer.get(array);
            byteBuffer.rewind();
            try {
                zipfile.putNextEntry(new ZipEntry("dump" + dateStr + "/" + filename));
                zipfile.write(array);
                zipfile.closeEntry();
            } catch (ZipException e) {
                e.printStackTrace();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
