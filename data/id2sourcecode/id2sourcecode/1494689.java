    void unzipBlock(byte[] uncompressedBlock, byte[] compressedBlock, int compressedLength) throws IOException {
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(compressedBlock, 0, compressedLength);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            if (byteBuffer.get() != BlockCompressedStreamConstants.GZIP_ID1 || byteBuffer.get() != (byte) BlockCompressedStreamConstants.GZIP_ID2 || byteBuffer.get() != BlockCompressedStreamConstants.GZIP_CM_DEFLATE || byteBuffer.get() != BlockCompressedStreamConstants.GZIP_FLG) {
                throw new IOException("Invalid GZIP header");
            }
            byteBuffer.position(byteBuffer.position() + 6);
            if (byteBuffer.getShort() != BlockCompressedStreamConstants.GZIP_XLEN) {
                throw new IOException("Invalid GZIP header");
            }
            byteBuffer.position(byteBuffer.position() + 4);
            final int totalBlockSize = (byteBuffer.getShort() & 0xffff) + 1;
            if (totalBlockSize != compressedLength) {
                throw new IOException("GZIP blocksize disagreement");
            }
            final int deflatedSize = compressedLength - BlockCompressedStreamConstants.BLOCK_HEADER_LENGTH - BlockCompressedStreamConstants.BLOCK_FOOTER_LENGTH;
            byteBuffer.position(byteBuffer.position() + deflatedSize);
            int expectedCrc = byteBuffer.getInt();
            int uncompressedSize = byteBuffer.getInt();
            inflater.reset();
            inflater.setInput(compressedBlock, BlockCompressedStreamConstants.BLOCK_HEADER_LENGTH, deflatedSize);
            final int inflatedBytes = inflater.inflate(uncompressedBlock, 0, uncompressedSize);
            if (inflatedBytes != uncompressedSize) {
                throw new IOException("Did not inflate expected amount");
            }
            crc32.reset();
            crc32.update(uncompressedBlock, 0, uncompressedSize);
            final long crc = crc32.getValue();
            if ((int) crc != expectedCrc) {
                throw new IOException("CRC mismatch");
            }
        } catch (DataFormatException e) {
            throw new RuntimeException(e);
        }
    }
