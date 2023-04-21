    public void write(Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotWriteException, IOException {
        logger.info("Writing tag");
        streamInfoBlock = null;
        metadataBlockPadding.clear();
        metadataBlockApplication.clear();
        metadataBlockSeekTable.clear();
        metadataBlockCueSheet.clear();
        FlacStreamReader flacStream = new FlacStreamReader(raf);
        try {
            flacStream.findStream();
        } catch (CannotReadException cre) {
            throw new CannotWriteException(cre.getMessage());
        }
        boolean isLastBlock = false;
        while (!isLastBlock) {
            MetadataBlockHeader mbh = MetadataBlockHeader.readHeader(raf);
            switch(mbh.getBlockType()) {
                case STREAMINFO:
                    {
                        streamInfoBlock = new MetadataBlock(mbh, new MetadataBlockDataStreamInfo(mbh, raf));
                        break;
                    }
                case VORBIS_COMMENT:
                case PADDING:
                case PICTURE:
                    {
                        raf.seek(raf.getFilePointer() + mbh.getDataLength());
                        MetadataBlockData mbd = new MetadataBlockDataPadding(mbh.getDataLength());
                        metadataBlockPadding.add(new MetadataBlock(mbh, mbd));
                        break;
                    }
                case APPLICATION:
                    {
                        MetadataBlockData mbd = new MetadataBlockDataApplication(mbh, raf);
                        metadataBlockApplication.add(new MetadataBlock(mbh, mbd));
                        break;
                    }
                case SEEKTABLE:
                    {
                        MetadataBlockData mbd = new MetadataBlockDataSeekTable(mbh, raf);
                        metadataBlockSeekTable.add(new MetadataBlock(mbh, mbd));
                        break;
                    }
                case CUESHEET:
                    {
                        MetadataBlockData mbd = new MetadataBlockDataCueSheet(mbh, raf);
                        metadataBlockCueSheet.add(new MetadataBlock(mbh, mbd));
                        break;
                    }
                default:
                    {
                        raf.seek(raf.getFilePointer() + mbh.getDataLength());
                        break;
                    }
            }
            isLastBlock = mbh.isLastBlock();
        }
        int availableRoom = computeAvailableRoom();
        int newTagSize = tc.convert(tag).limit();
        int neededRoom = newTagSize + computeNeededRoom();
        raf.seek(flacStream.getStartOfFlacInFile());
        logger.info("Writing tag available bytes:" + availableRoom + ":needed bytes:" + neededRoom);
        if ((availableRoom == neededRoom) || (availableRoom > neededRoom + MetadataBlockHeader.HEADER_LENGTH)) {
            raf.seek(flacStream.getStartOfFlacInFile() + FlacStreamReader.FLAC_STREAM_IDENTIFIER_LENGTH);
            raf.write(streamInfoBlock.getHeader().getBytesWithoutIsLastBlockFlag());
            raf.write(streamInfoBlock.getData().getBytes());
            for (MetadataBlock aMetadataBlockApplication : metadataBlockApplication) {
                raf.write(aMetadataBlockApplication.getHeader().getBytesWithoutIsLastBlockFlag());
                raf.write(aMetadataBlockApplication.getData().getBytes());
            }
            for (MetadataBlock aMetadataBlockSeekTable : metadataBlockSeekTable) {
                raf.write(aMetadataBlockSeekTable.getHeader().getBytesWithoutIsLastBlockFlag());
                raf.write(aMetadataBlockSeekTable.getData().getBytes());
            }
            for (MetadataBlock aMetadataBlockCueSheet : metadataBlockCueSheet) {
                raf.write(aMetadataBlockCueSheet.getHeader().getBytesWithoutIsLastBlockFlag());
                raf.write(aMetadataBlockCueSheet.getData().getBytes());
            }
            raf.getChannel().write(tc.convert(tag, availableRoom - neededRoom));
        } else {
            int dataStartSize = flacStream.getStartOfFlacInFile() + FlacStreamReader.FLAC_STREAM_IDENTIFIER_LENGTH + MetadataBlockHeader.HEADER_LENGTH + MetadataBlockDataStreamInfo.STREAM_INFO_DATA_LENGTH;
            raf.seek(0);
            rafTemp.getChannel().transferFrom(raf.getChannel(), 0, dataStartSize);
            rafTemp.seek(dataStartSize);
            for (MetadataBlock aMetadataBlockApplication : metadataBlockApplication) {
                rafTemp.write(aMetadataBlockApplication.getHeader().getBytesWithoutIsLastBlockFlag());
                rafTemp.write(aMetadataBlockApplication.getData().getBytes());
            }
            for (MetadataBlock aMetadataBlockSeekTable : metadataBlockSeekTable) {
                rafTemp.write(aMetadataBlockSeekTable.getHeader().getBytesWithoutIsLastBlockFlag());
                rafTemp.write(aMetadataBlockSeekTable.getData().getBytes());
            }
            for (MetadataBlock aMetadataBlockCueSheet : metadataBlockCueSheet) {
                rafTemp.write(aMetadataBlockCueSheet.getHeader().getBytesWithoutIsLastBlockFlag());
                rafTemp.write(aMetadataBlockCueSheet.getData().getBytes());
            }
            rafTemp.write(tc.convert(tag, FlacTagCreator.DEFAULT_PADDING).array());
            raf.seek(dataStartSize + availableRoom);
            rafTemp.getChannel().transferFrom(raf.getChannel(), rafTemp.getChannel().position(), raf.getChannel().size());
        }
    }
