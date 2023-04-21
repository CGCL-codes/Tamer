    public MetadataBlockDataPicture(MetadataBlockHeader header, RandomAccessFile raf) throws IOException, InvalidFrameException {
        ByteBuffer rawdata = ByteBuffer.allocate(header.getDataLength());
        int bytesRead = raf.getChannel().read(rawdata);
        if (bytesRead < header.getDataLength()) {
            throw new IOException("Unable to read required number of databytes read:" + bytesRead + ":required:" + header.getDataLength());
        }
        rawdata.rewind();
        pictureType = rawdata.getInt();
        if (pictureType >= PictureTypes.getInstanceOf().getSize()) {
            throw new InvalidFrameException("PictureType was:" + pictureType + "but the maximum allowed is " + (PictureTypes.getInstanceOf().getSize() - 1));
        }
        int mimeTypeSize = rawdata.getInt();
        mimeType = getString(rawdata, mimeTypeSize, "ISO-8859-1");
        int descriptionSize = rawdata.getInt();
        description = getString(rawdata, descriptionSize, "UTF-8");
        width = rawdata.getInt();
        height = rawdata.getInt();
        colourDepth = rawdata.getInt();
        indexedColouredCount = rawdata.getInt();
        int rawdataSize = rawdata.getInt();
        imageData = new byte[rawdataSize];
        rawdata.get(imageData);
        logger.info("Read image:" + this.toString());
    }
