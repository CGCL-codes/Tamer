    void readInterlaceFrame(InputStream inputStream, int rowInterval, int columnInterval, int startRow, int startColumn, int frameCount) throws IOException {
        int width = headerChunk.getWidth();
        int alignedBytesPerRow = getAlignedBytesPerRow();
        int height = headerChunk.getHeight();
        if (startRow >= height || startColumn >= width) return;
        int pixelsPerRow = (width - startColumn + columnInterval - 1) / columnInterval;
        int bytesPerRow = getBytesPerRow(pixelsPerRow);
        byte[] row1 = new byte[bytesPerRow];
        byte[] row2 = new byte[bytesPerRow];
        byte[] currentRow = row1;
        byte[] lastRow = row2;
        for (int row = startRow; row < height; row += rowInterval) {
            byte filterType = (byte) inputStream.read();
            int read = 0;
            while (read != bytesPerRow) {
                read += inputStream.read(currentRow, read, bytesPerRow - read);
            }
            filterRow(currentRow, lastRow, filterType);
            if (headerChunk.getBitDepth() >= 8) {
                int bytesPerPixel = getBytesPerPixel();
                int dataOffset = (row * alignedBytesPerRow) + (startColumn * bytesPerPixel);
                for (int rowOffset = 0; rowOffset < currentRow.length; rowOffset += bytesPerPixel) {
                    for (int byteOffset = 0; byteOffset < bytesPerPixel; byteOffset++) {
                        data[dataOffset + byteOffset] = currentRow[rowOffset + byteOffset];
                    }
                    dataOffset += (columnInterval * bytesPerPixel);
                }
            } else {
                int bitsPerPixel = headerChunk.getBitDepth();
                int pixelsPerByte = 8 / bitsPerPixel;
                int column = startColumn;
                int rowBase = row * alignedBytesPerRow;
                int valueMask = 0;
                for (int i = 0; i < bitsPerPixel; i++) {
                    valueMask <<= 1;
                    valueMask |= 1;
                }
                int maxShift = 8 - bitsPerPixel;
                for (int byteOffset = 0; byteOffset < currentRow.length; byteOffset++) {
                    for (int bitOffset = maxShift; bitOffset >= 0; bitOffset -= bitsPerPixel) {
                        if (column < width) {
                            int dataOffset = rowBase + (column * bitsPerPixel / 8);
                            int value = (currentRow[byteOffset] >> bitOffset) & valueMask;
                            int dataShift = maxShift - (bitsPerPixel * (column % pixelsPerByte));
                            data[dataOffset] |= value << dataShift;
                        }
                        column += columnInterval;
                    }
                }
            }
            currentRow = (currentRow == row1) ? row2 : row1;
            lastRow = (lastRow == row1) ? row2 : row1;
        }
        setImageDataValues(data, imageData);
        fireInterlacedFrameEvent(frameCount);
    }
