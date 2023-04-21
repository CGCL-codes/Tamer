    public void write(Tag tag, RandomAccessFile raf, RandomAccessFile rafTemp) throws CannotWriteException, IOException {
        logger.info("Started writing tag data");
        ByteBuffer rawIlstData = tc.convert(tag);
        rawIlstData.rewind();
        FileChannel fileReadChannel = raf.getChannel();
        FileChannel fileWriteChannel = rafTemp.getChannel();
        Mp4BoxHeader boxHeader;
        int oldIlstSize = 0;
        int relativeIlstposition;
        int relativeIlstEndPosition;
        int startIlstWithinFile;
        int newIlstSize;
        int oldMetaLevelFreeAtomSize;
        long extraDataSize;
        int level1SearchPosition = 0;
        int topLevelFreePosition;
        int topLevelFreeSize;
        boolean topLevelFreeAtomComesBeforeMdatAtom;
        Mp4BoxHeader topLevelFreeHeader;
        Mp4AtomTree atomTree;
        try {
            atomTree = new Mp4AtomTree(raf, false);
        } catch (CannotReadException cre) {
            throw new CannotWriteException(cre.getMessage());
        }
        Mp4BoxHeader moovHeader = atomTree.getBoxHeader(atomTree.getMoovNode());
        long positionWithinFileAfterFindingMoovHeader = moovHeader.getFilePos() + Mp4BoxHeader.HEADER_LENGTH;
        Mp4StcoBox stco = atomTree.getStco();
        Mp4BoxHeader ilstHeader = atomTree.getBoxHeader(atomTree.getIlstNode());
        Mp4BoxHeader udtaHeader = atomTree.getBoxHeader(atomTree.getUdtaNode());
        Mp4BoxHeader metaHeader = atomTree.getBoxHeader(atomTree.getMetaNode());
        Mp4BoxHeader hdlrMetaHeader = atomTree.getBoxHeader(atomTree.getHdlrWithinMetaNode());
        Mp4BoxHeader hdlrMdiaHeader = atomTree.getBoxHeader(atomTree.getHdlrWithinMdiaNode());
        Mp4BoxHeader mdatHeader = atomTree.getBoxHeader(atomTree.getMdatNode());
        if (mdatHeader == null) {
            throw new CannotWriteException(ErrorMessage.MP4_CHANGES_TO_FILE_FAILED_CANNOT_FIND_AUDIO.getMsg());
        }
        Mp4BoxHeader trakHeader = (Mp4BoxHeader) atomTree.getTrakNodes().get(0).getUserObject();
        ByteBuffer moovBuffer = atomTree.getMoovBuffer();
        if (udtaHeader != null) {
            if (metaHeader != null) {
                if (ilstHeader != null) {
                    oldIlstSize = ilstHeader.getLength();
                    startIlstWithinFile = (int) ilstHeader.getFilePos();
                    relativeIlstposition = (int) (startIlstWithinFile - (moovHeader.getFilePos() + Mp4BoxHeader.HEADER_LENGTH));
                    relativeIlstEndPosition = relativeIlstposition + ilstHeader.getLength();
                } else {
                    if (hdlrMetaHeader != null) {
                        startIlstWithinFile = (int) hdlrMetaHeader.getFilePos() + hdlrMetaHeader.getLength();
                        relativeIlstposition = (int) (startIlstWithinFile - (moovHeader.getFilePos() + Mp4BoxHeader.HEADER_LENGTH));
                        relativeIlstEndPosition = relativeIlstposition;
                    } else {
                        startIlstWithinFile = (int) metaHeader.getFilePos() + Mp4BoxHeader.HEADER_LENGTH + Mp4MetaBox.FLAGS_LENGTH;
                        relativeIlstposition = (int) ((startIlstWithinFile) - (moovHeader.getFilePos() + Mp4BoxHeader.HEADER_LENGTH));
                        relativeIlstEndPosition = relativeIlstposition;
                    }
                }
            } else {
                relativeIlstposition = moovHeader.getLength() - Mp4BoxHeader.HEADER_LENGTH;
                relativeIlstEndPosition = relativeIlstposition;
                startIlstWithinFile = (int) (moovHeader.getFilePos() + moovHeader.getLength());
            }
        } else {
            if (metaHeader != null) {
                startIlstWithinFile = (int) trakHeader.getFilePos() + trakHeader.getLength();
                relativeIlstposition = (int) (startIlstWithinFile - (moovHeader.getFilePos() + Mp4BoxHeader.HEADER_LENGTH));
                relativeIlstEndPosition = relativeIlstposition + ilstHeader.getLength();
            } else {
                relativeIlstposition = moovHeader.getLength() - Mp4BoxHeader.HEADER_LENGTH;
                relativeIlstEndPosition = relativeIlstposition;
                startIlstWithinFile = (int) (moovHeader.getFilePos() + moovHeader.getLength());
            }
        }
        newIlstSize = rawIlstData.limit();
        oldMetaLevelFreeAtomSize = 0;
        extraDataSize = 0;
        for (DefaultMutableTreeNode freeNode : atomTree.getFreeNodes()) {
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) freeNode.getParent();
            DefaultMutableTreeNode brotherNode = freeNode.getPreviousSibling();
            if (!parentNode.isRoot()) {
                Mp4BoxHeader header = ((Mp4BoxHeader) parentNode.getUserObject());
                Mp4BoxHeader freeHeader = ((Mp4BoxHeader) freeNode.getUserObject());
                if (brotherNode != null) {
                    Mp4BoxHeader brotherHeader = ((Mp4BoxHeader) brotherNode.getUserObject());
                    if (header.getId().equals(Mp4NotMetaFieldKey.META.getFieldName()) && brotherHeader.getId().equals(Mp4NotMetaFieldKey.ILST.getFieldName())) {
                        oldMetaLevelFreeAtomSize = freeHeader.getLength();
                        extraDataSize = moovHeader.getFilePos() + moovHeader.getLength() - (freeHeader.getFilePos() + freeHeader.getLength());
                        break;
                    }
                }
            }
        }
        if (oldMetaLevelFreeAtomSize == 0) {
            extraDataSize = moovHeader.getDataLength() - relativeIlstEndPosition;
        }
        level1SearchPosition = 0;
        topLevelFreePosition = 0;
        topLevelFreeSize = 0;
        topLevelFreeAtomComesBeforeMdatAtom = true;
        for (DefaultMutableTreeNode freeNode : atomTree.getFreeNodes()) {
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) freeNode.getParent();
            if (parentNode.isRoot()) {
                topLevelFreeHeader = ((Mp4BoxHeader) freeNode.getUserObject());
                topLevelFreeSize = topLevelFreeHeader.getLength();
                topLevelFreePosition = (int) topLevelFreeHeader.getFilePos();
                level1SearchPosition = topLevelFreePosition;
                break;
            }
        }
        if (topLevelFreeSize > 0) {
            if (topLevelFreePosition > mdatHeader.getFilePos()) {
                topLevelFreeAtomComesBeforeMdatAtom = false;
                level1SearchPosition = (int) mdatHeader.getFilePos();
            }
        } else {
            topLevelFreePosition = (int) mdatHeader.getFilePos();
            level1SearchPosition = topLevelFreePosition;
        }
        logger.info("Read header successfully ready for writing");
        if (oldIlstSize == newIlstSize) {
            logger.info("Writing:Option 1:Same Size");
            writeMetadataSameSize(rawIlstData, oldIlstSize, startIlstWithinFile, fileReadChannel, fileWriteChannel);
        } else if (oldIlstSize > newIlstSize) {
            if (oldMetaLevelFreeAtomSize > 0) {
                logger.info("Writing:Option 2:Smaller Size have free atom:" + oldIlstSize + ":" + newIlstSize);
                fileReadChannel.position(0);
                fileWriteChannel.transferFrom(fileReadChannel, 0, startIlstWithinFile);
                fileWriteChannel.position(startIlstWithinFile);
                fileWriteChannel.write(rawIlstData);
                fileReadChannel.position(startIlstWithinFile + oldIlstSize);
                int newFreeSize = oldMetaLevelFreeAtomSize + (oldIlstSize - newIlstSize);
                Mp4FreeBox newFreeBox = new Mp4FreeBox(newFreeSize - Mp4BoxHeader.HEADER_LENGTH);
                fileWriteChannel.write(newFreeBox.getHeader().getHeaderData());
                fileWriteChannel.write(newFreeBox.getData());
                fileReadChannel.position(fileReadChannel.position() + oldMetaLevelFreeAtomSize);
                fileWriteChannel.transferFrom(fileReadChannel, fileWriteChannel.position(), fileReadChannel.size() - fileReadChannel.position());
            } else {
                int newFreeSize = (oldIlstSize - newIlstSize) - Mp4BoxHeader.HEADER_LENGTH;
                if (newFreeSize > 0) {
                    logger.info("Writing:Option 3:Smaller Size can create free atom");
                    fileReadChannel.position(0);
                    fileWriteChannel.transferFrom(fileReadChannel, 0, startIlstWithinFile);
                    fileWriteChannel.position(startIlstWithinFile);
                    fileWriteChannel.write(rawIlstData);
                    fileReadChannel.position(startIlstWithinFile + oldIlstSize);
                    Mp4FreeBox newFreeBox = new Mp4FreeBox(newFreeSize);
                    fileWriteChannel.write(newFreeBox.getHeader().getHeaderData());
                    fileWriteChannel.write(newFreeBox.getData());
                    fileWriteChannel.transferFrom(fileReadChannel, fileWriteChannel.position(), fileReadChannel.size() - fileReadChannel.position());
                } else {
                    logger.info("Writing:Option 4:Smaller Size <=8 cannot create free atoms");
                    int sizeReducedBy = oldIlstSize - newIlstSize;
                    fileReadChannel.position(0);
                    fileWriteChannel.transferFrom(fileReadChannel, 0, moovHeader.getFilePos());
                    fileWriteChannel.position(moovHeader.getFilePos());
                    if (mdatHeader.getFilePos() > moovHeader.getFilePos()) {
                        stco.adjustOffsets(-sizeReducedBy);
                    }
                    adjustSizeOfMoovHeader(moovHeader, moovBuffer, -sizeReducedBy, udtaHeader, metaHeader);
                    fileWriteChannel.write(moovHeader.getHeaderData());
                    moovBuffer.rewind();
                    moovBuffer.limit(relativeIlstposition);
                    fileWriteChannel.write(moovBuffer);
                    fileWriteChannel.write(rawIlstData);
                    fileReadChannel.position(startIlstWithinFile + oldIlstSize);
                    if (extraDataSize > 0) {
                        fileWriteChannel.transferFrom(fileReadChannel, fileWriteChannel.position(), extraDataSize);
                        fileWriteChannel.position(fileWriteChannel.position() + extraDataSize);
                    }
                    fileWriteChannel.transferFrom(fileReadChannel, fileWriteChannel.position(), fileReadChannel.size() - fileReadChannel.position());
                }
            }
        } else {
            int additionalSpaceRequiredForMetadata = newIlstSize - oldIlstSize;
            if (additionalSpaceRequiredForMetadata <= (oldMetaLevelFreeAtomSize - Mp4BoxHeader.HEADER_LENGTH)) {
                int newFreeSize = oldMetaLevelFreeAtomSize - (additionalSpaceRequiredForMetadata);
                logger.info("Writing:Option 5;Larger Size can use meta free atom need extra:" + newFreeSize + "bytes");
                fileReadChannel.position(0);
                fileWriteChannel.transferFrom(fileReadChannel, 0, startIlstWithinFile);
                fileWriteChannel.position(startIlstWithinFile);
                fileWriteChannel.write(rawIlstData);
                fileReadChannel.position(startIlstWithinFile + oldIlstSize);
                Mp4FreeBox newFreeBox = new Mp4FreeBox(newFreeSize - Mp4BoxHeader.HEADER_LENGTH);
                fileWriteChannel.write(newFreeBox.getHeader().getHeaderData());
                fileWriteChannel.write(newFreeBox.getData());
                fileReadChannel.position(fileReadChannel.position() + oldMetaLevelFreeAtomSize);
                fileWriteChannel.transferFrom(fileReadChannel, fileWriteChannel.position(), fileReadChannel.size() - fileReadChannel.position());
            } else {
                int additionalMetaSizeThatWontFitWithinMetaAtom = additionalSpaceRequiredForMetadata - (oldMetaLevelFreeAtomSize);
                fileReadChannel.position(0);
                fileWriteChannel.transferFrom(fileReadChannel, 0, positionWithinFileAfterFindingMoovHeader - Mp4BoxHeader.HEADER_LENGTH);
                fileWriteChannel.position(positionWithinFileAfterFindingMoovHeader - Mp4BoxHeader.HEADER_LENGTH);
                if (udtaHeader == null) {
                    logger.info("Writing:Option 5.1;No udta atom");
                    Mp4HdlrBox hdlrBox = Mp4HdlrBox.createiTunesStyleHdlrBox();
                    Mp4MetaBox metaBox = Mp4MetaBox.createiTunesStyleMetaBox(hdlrBox.getHeader().getLength() + rawIlstData.limit());
                    udtaHeader = new Mp4BoxHeader(Mp4NotMetaFieldKey.UDTA.getFieldName());
                    udtaHeader.setLength(Mp4BoxHeader.HEADER_LENGTH + metaBox.getHeader().getLength());
                    additionalMetaSizeThatWontFitWithinMetaAtom = additionalMetaSizeThatWontFitWithinMetaAtom + (udtaHeader.getLength() - rawIlstData.limit());
                    if ((!topLevelFreeAtomComesBeforeMdatAtom) || ((topLevelFreeSize - Mp4BoxHeader.HEADER_LENGTH < additionalMetaSizeThatWontFitWithinMetaAtom) && (topLevelFreeSize != additionalMetaSizeThatWontFitWithinMetaAtom))) {
                        if (mdatHeader.getFilePos() > moovHeader.getFilePos()) {
                            stco.adjustOffsets(additionalMetaSizeThatWontFitWithinMetaAtom);
                        }
                    }
                    moovHeader.setLength(moovHeader.getLength() + additionalMetaSizeThatWontFitWithinMetaAtom);
                    fileWriteChannel.write(moovHeader.getHeaderData());
                    moovBuffer.rewind();
                    moovBuffer.limit(relativeIlstposition);
                    fileWriteChannel.write(moovBuffer);
                    fileWriteChannel.write(udtaHeader.getHeaderData());
                    fileWriteChannel.write(metaBox.getHeader().getHeaderData());
                    fileWriteChannel.write(metaBox.getData());
                    fileWriteChannel.write(hdlrBox.getHeader().getHeaderData());
                    fileWriteChannel.write(hdlrBox.getData());
                } else {
                    logger.info("Writing:Option 5.2;udta atom exists");
                    if ((!topLevelFreeAtomComesBeforeMdatAtom) || ((topLevelFreeSize - Mp4BoxHeader.HEADER_LENGTH < additionalMetaSizeThatWontFitWithinMetaAtom) && (topLevelFreeSize != additionalMetaSizeThatWontFitWithinMetaAtom))) {
                        if (mdatHeader.getFilePos() > moovHeader.getFilePos()) {
                            stco.adjustOffsets(additionalMetaSizeThatWontFitWithinMetaAtom);
                        }
                    }
                    adjustSizeOfMoovHeader(moovHeader, moovBuffer, additionalMetaSizeThatWontFitWithinMetaAtom, udtaHeader, metaHeader);
                    fileWriteChannel.write(moovHeader.getHeaderData());
                    moovBuffer.rewind();
                    moovBuffer.limit(relativeIlstposition);
                    fileWriteChannel.write(moovBuffer);
                }
                fileWriteChannel.write(rawIlstData);
                fileReadChannel.position(startIlstWithinFile + oldIlstSize);
                fileReadChannel.position(fileReadChannel.position() + oldMetaLevelFreeAtomSize);
                if (extraDataSize > 0) {
                    fileWriteChannel.transferFrom(fileReadChannel, fileWriteChannel.position(), extraDataSize);
                    fileWriteChannel.position(fileWriteChannel.position() + extraDataSize);
                }
                if (topLevelFreeAtomComesBeforeMdatAtom && (topLevelFreePosition > startIlstWithinFile)) {
                    if (topLevelFreeSize - Mp4BoxHeader.HEADER_LENGTH >= additionalMetaSizeThatWontFitWithinMetaAtom) {
                        logger.info("Writing:Option 6;Larger Size can use top free atom");
                        Mp4FreeBox freeBox = new Mp4FreeBox((topLevelFreeSize - Mp4BoxHeader.HEADER_LENGTH) - additionalMetaSizeThatWontFitWithinMetaAtom);
                        fileWriteChannel.write(freeBox.getHeader().getHeaderData());
                        fileWriteChannel.write(freeBox.getData());
                        fileReadChannel.position(fileReadChannel.position() + topLevelFreeSize);
                        fileWriteChannel.transferFrom(fileReadChannel, fileWriteChannel.position(), fileReadChannel.size() - fileReadChannel.position());
                    } else if (topLevelFreeSize == additionalMetaSizeThatWontFitWithinMetaAtom) {
                        logger.info("Writing:Option 7;Larger Size uses top free atom including header");
                        fileReadChannel.position(fileReadChannel.position() + topLevelFreeSize);
                        fileWriteChannel.transferFrom(fileReadChannel, fileWriteChannel.position(), fileReadChannel.size() - fileReadChannel.position());
                    } else {
                        logger.info("Writing:Option 8;Larger Size cannot use top free atom");
                        fileWriteChannel.transferFrom(fileReadChannel, fileWriteChannel.position(), fileReadChannel.size() - fileReadChannel.position());
                    }
                } else {
                    logger.info("Writing:Option 9;Top Level Free comes after Mdat or before Metadata so cant use it");
                    fileWriteChannel.transferFrom(fileReadChannel, fileWriteChannel.position(), fileReadChannel.size() - fileReadChannel.position());
                }
            }
        }
        fileReadChannel.close();
        raf.close();
        checkFileWrittenCorrectly(rafTemp, mdatHeader, fileWriteChannel, stco);
    }
