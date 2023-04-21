    public static synchronized Dimension getImageDimensionsFromJPEGHeader(URL url, int kB) throws IOException {
        Dimension dimension = new Dimension(0, 0);
        InputStream stream = url.openConnection().getInputStream();
        offset = 0;
        offset = readBytes(stream, 10);
        if (offset < 0) return dimension;
        if (!(buffer[0] == 0xFF && buffer[1] == 0xD8 && buffer[2] == 0xFF && buffer[3] == 0xE0)) return dimension;
        if (!(buffer[6] == 'J' && buffer[7] == 'F' && buffer[8] == 'I' && buffer[9] == 'F' && buffer[10] == 0x00)) return dimension;
        for (int position = 2; position + 4 < BUF_SIZE; ) {
            if (offset < position + 4) {
                int bytesRead = readBytes(stream, position + 4 - offset);
                if (bytesRead < 0) break;
                offset += bytesRead;
            }
            int readBlockSize = readBlock(position, dimension);
            if (readBlockSize <= 0) break;
            position += readBlockSize;
        }
        stream.close();
        return dimension;
    }
