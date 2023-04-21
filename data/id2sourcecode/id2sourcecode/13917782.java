    void getBlockChecksum(DataInputStream in) throws IOException {
        final Block block = new Block(in.readLong(), 0, in.readLong());
        DataOutputStream out = null;
        final MetaDataInputStream metadataIn = datanode.data.getMetaDataInputStream(block);
        final DataInputStream checksumIn = new DataInputStream(new BufferedInputStream(metadataIn, BUFFER_SIZE));
        try {
            final BlockMetadataHeader header = BlockMetadataHeader.readHeader(checksumIn);
            final DataChecksum checksum = header.getChecksum();
            final int bytesPerCRC = checksum.getBytesPerChecksum();
            final long crcPerBlock = (metadataIn.getLength() - BlockMetadataHeader.getHeaderSize()) / checksum.getChecksumSize();
            final MD5Hash md5 = MD5Hash.digest(checksumIn);
            if (LOG.isDebugEnabled()) {
                LOG.debug("block=" + block + ", bytesPerCRC=" + bytesPerCRC + ", crcPerBlock=" + crcPerBlock + ", md5=" + md5);
            }
            out = new DataOutputStream(NetUtils.getOutputStream(s, datanode.socketWriteTimeout));
            out.writeShort(DataTransferProtocol.OP_STATUS_SUCCESS);
            out.writeInt(bytesPerCRC);
            out.writeLong(crcPerBlock);
            md5.write(out);
            out.flush();
        } finally {
            IOUtils.closeStream(out);
            IOUtils.closeStream(checksumIn);
            IOUtils.closeStream(metadataIn);
        }
    }
