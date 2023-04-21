    public void write(DirectByteBuffer buffer, int buffer_offset, long file_offset, int length) throws IOException {
        if (access_mode == MODE_READ_ONLY) {
            throw new IOException("cannot write to a read-only file");
        }
        if (buffer.limit(DirectByteBuffer.SS_OTHER) - buffer_offset < length) {
            throw new IOException("not enough buffer remaining to write given length");
        }
        file.createNewFile();
        int key_pos = new Long(file_offset / BLOCK_SIZE).intValue();
        int map_offset = new Long(file_offset % BLOCK_SIZE).intValue();
        int written = 0;
        while (written < length) {
            MappedByteBuffer mbb = null;
            long f_offset = file_offset + written;
            int length_to_write = BLOCK_SIZE - map_offset;
            if (length - written < length_to_write) length_to_write = length - written;
            if (mapKeys.length > key_pos) {
                Object key = mapKeys[key_pos];
                if (key != null) {
                    mbb = MemoryMapPool.getBuffer(key);
                    if (mbb != null && mbb.capacity() < (map_offset + length_to_write)) {
                        MemoryMapPool.clean(mbb);
                        mbb = null;
                    }
                }
            } else {
                mapKeys = new Object[key_pos * 2];
            }
            if (mbb == null) {
                int size = BLOCK_SIZE;
                if (f_offset + length_to_write > file.length()) {
                    size = map_offset + length_to_write;
                }
                mbb = createMappedBuffer(f_offset - map_offset, size);
                cache_misses++;
            } else cache_hits++;
            buffer.position(DirectByteBuffer.SS_OTHER, buffer_offset + written);
            buffer.limit(DirectByteBuffer.SS_OTHER, buffer.position(DirectByteBuffer.SS_OTHER) + length_to_write);
            mbb.position(map_offset);
            mbb.put(buffer.getBuffer(DirectByteBuffer.SS_OTHER));
            written += length_to_write;
            mapKeys[key_pos] = MemoryMapPool.addBuffer(mbb);
            key_pos++;
            map_offset = 0;
        }
    }
