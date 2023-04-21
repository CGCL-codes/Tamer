    protected final void load(File file) {
        try {
            final FileChannel channel = new FileInputStream(file).getChannel();
            final long bytes = channel.size();
            int size = (int) (1 + (bytes - 1) / (BUFFER_SIZE + 7));
            if (Bits.CFG_BITS_MAP.getValue()) {
                buffers = new ByteBuffer[size--];
                buffers[size] = channel.map(FileChannel.MapMode.READ_ONLY, size * (BUFFER_SIZE + 7L), bytes % (BUFFER_SIZE + 7L));
                while (size-- > 0) buffers[size] = channel.map(FileChannel.MapMode.READ_ONLY, size * (BUFFER_SIZE + 7L), BUFFER_SIZE + 7L);
                for (ByteBuffer buffer : buffers) buffer.order(ByteOrder.LITTLE_ENDIAN);
            } else {
                allocate(bytes - 7l * size);
                channel.read(buffers);
            }
            channel.close();
        } catch (FileNotFoundException e) {
            Log.getInstance().severe("File " + file + " not found: " + e.getMessage());
        } catch (IOException e) {
            Log.getInstance().severe("IO Exception on " + file + ": " + e.getMessage());
        }
    }
