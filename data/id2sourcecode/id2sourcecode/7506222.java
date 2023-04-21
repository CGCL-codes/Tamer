    private int deflateBlock() {
        if (numUncompressedBytes == 0) {
            return 0;
        }
        int bytesToCompress = numUncompressedBytes;
        while (true) {
            deflater.reset();
            deflater.setInput(uncompressedBuffer, 0, bytesToCompress);
            deflater.finish();
            final int compressedSize = deflater.deflate(compressedBuffer, 0, compressedBuffer.length);
            if (!deflater.finished()) {
                bytesToCompress -= BlockCompressedStreamConstants.UNCOMPRESSED_THROTTLE_AMOUNT;
                ++numberOfThrottleBacks;
                assert (bytesToCompress > 0);
                continue;
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
            return totalBlockSize;
        }
    }
