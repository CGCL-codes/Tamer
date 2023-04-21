    @Nullable
    public String getVersionDescription() {
        try {
            final byte[] bytes = _directory.getByteArray(PhotoshopDirectory.TAG_PHOTOSHOP_VERSION);
            if (bytes == null) return null;
            BufferReader reader = new BufferReader(bytes);
            int pos = 0;
            int ver = reader.getInt32(0);
            pos += 4;
            pos++;
            int readerLength = reader.getInt32(5);
            pos += 4;
            String readerStr = reader.getString(9, readerLength * 2, "UTF-16");
            pos += readerLength * 2;
            int writerLength = reader.getInt32(pos);
            pos += 4;
            String writerStr = reader.getString(pos, writerLength * 2, "UTF-16");
            pos += writerLength * 2;
            int fileVersion = reader.getInt32(pos);
            return String.format("%d (%s, %s) %d", ver, readerStr, writerStr, fileVersion);
        } catch (BufferBoundsException e) {
            return null;
        }
    }
