    private int deflateBlock() {
        if (numUncompressedBytes == 0) {
            return 0;
        }
        int bytesToCompress = numUncompressedBytes;
        deflater.reset();
        deflater.setInput(uncompressedBuffer, 0, bytesToCompress);
        deflater.finish();
        int compressedSize = deflater.deflate(compressedBuffer, 0, compressedBuffer.length);
        if (!deflater.finished()) {
            noCompressionDeflater.reset();
            noCompressionDeflater.setInput(uncompressedBuffer, 0, bytesToCompress);
            noCompressionDeflater.finish();
            compressedSize = noCompressionDeflater.deflate(compressedBuffer, 0, compressedBuffer.length);
            if (!noCompressionDeflater.finished()) {
                throw new IllegalStateException("unpossible");
            }
        }
        crc32.reset();
        crc32.update(uncompressedBuffer, 0, bytesToCompress);
        final int totalBlockSize = writeGzipBlock(compressedSize, bytesToCompress, crc32.getValue());
        assert (bytesToCompress <= numUncompressedBytes);
        if (bytesToCompress == numUncompressedBytes) {
            numUncompressedBytes = 0;
        } else {
            System.arraycopy(uncompressedBuffer, bytesToCompress, uncompressedBuffer, 0, numUncompressedBytes - bytesToCompress);
            numUncompressedBytes -= bytesToCompress;
        }
        mBlockAddress += totalBlockSize;
        return totalBlockSize;
    }
