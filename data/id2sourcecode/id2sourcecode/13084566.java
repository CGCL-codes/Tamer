    public final MemoryMappedBitBuffer save(String path, String file) {
        setReadOnly();
        try {
            FileChannel channel = new FileOutputStream(new File(path, file)).getChannel();
            for (ByteBuffer buffer : buffers) {
                buffer.rewind();
                channel.write(buffer);
            }
            channel.close();
        } catch (FileNotFoundException e) {
            Log.getInstance().severe("File " + file + " not found: " + e.getMessage());
        } catch (IOException e) {
            Log.getInstance().severe("IO Exception on " + file + ": " + e.getMessage());
        }
        return new MemoryMappedBitBuffer(buffers, length, file);
    }
