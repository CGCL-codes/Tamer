    public void save(File f) throws IOException {
        FileOutputStream fos = new FileOutputStream(f);
        FileChannel fc = fos.getChannel();
        buffer.flip();
        fc.write(buffer);
        buffer.limit(buffer.capacity());
        fc.close();
        fos.flush();
        fos.close();
    }
